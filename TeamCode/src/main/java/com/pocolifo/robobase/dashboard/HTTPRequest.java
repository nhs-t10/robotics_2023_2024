package com.pocolifo.robobase.dashboard;

import com.pocolifo.robobase.dashboard.input.InputsManager;
import com.pocolifo.robobase.dashboard.output.OutputManager;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private Socket socket;
    private BufferedReader reader; // Use this instead of `socket.getInputStream()`
    private OutputStream out;
    private String requestUrl;
    private FileUtils files;
    
    public HTTPRequest(Socket socket) throws java.io.IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = socket.getOutputStream();
        this.files = new FileUtils();
    }

    public void handleRequest() throws IOException {
        initUrl();
        if (requestUrl == null) {
            return;
        }

        String path = getRequestPath();

        if ("/inputs".equals(path)) {
            writeOkResponseToOutputStream(InputsManager.getValuesAsJSON(), "text/json");
        } else if ("/telemetry".equals(path)) {
            writeOkResponseToOutputStream(OutputManager.getValuesAsJSON(), "text/json");
        } else if ("/set".equals(path)) {
            try {
                InputsManager.setValues(getRequestParams());
            } catch (ArrayIndexOutOfBoundsException e) {
                // User did not supply anything to set
                out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
            }
            writeOkResponseToOutputStream("Success", "text/plain");
        } else if ("/index.html".equals(path)) {
            writeOkResponseToOutputStream(this.files.fileContents("index.html"), "text/html");
        } else if ("/script.js".equals(path)) {
            writeOkResponseToOutputStream(this.files.fileContents("script.js"), "text/javascript");
        } else if ("/styles.css".equals(path)) {
            writeOkResponseToOutputStream(this.files.fileContents("styles.css"), "text/css");
        } else if ("/".equals(path)) {
            writeOkResponseToOutputStream(this.files.fileContents("index.html"), "text/html");
        } else {
            out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
        }

        socket.close();
    }

    private void writeOkResponseToOutputStream(String response, String responseType) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write(("Content-Type: " + responseType + "\r\n").getBytes());
        out.write(("Content-Length: " + response.length() + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(response.getBytes());
    }

    /**
     * Can only be run one.
     * @return none
     * @throws IOException
     */
    private void initUrl() throws IOException {
        String requestLine = reader.readLine();
        requestUrl = requestLine;
    }

    private String  getRequestPath() {
        String[] parts = requestUrl.split(" ");
        return parts.length > 1 ? parts[1].split("\\?")[0] : "/";
    }

    private Map<String, String> getRequestParams() throws IOException {
        String[] parts = requestUrl.split(" ");
        String paramsString = parts.length > 1 ? parts[1].split("\\?")[1] : "";
        Map<String, String> params = new HashMap<String, String>();
        for (String paramString : paramsString.split("\\&")) {
            params.put(paramString.split("=")[0], paramString.split("=")[1]);
        }

        return params;
    }
}
