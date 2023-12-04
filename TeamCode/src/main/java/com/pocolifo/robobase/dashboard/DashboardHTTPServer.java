package com.pocolifo.robobase.dashboard;

import android.webkit.MimeTypeMap;
import com.pocolifo.robobase.dashboard.input.InputsManager;
import com.pocolifo.robobase.dashboard.output.OutputManager;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.HashMap;

public class DashboardHTTPServer extends NanoHTTPD {
    public DashboardHTTPServer() {
        super(2713);
    }

    @Override
    public Response serve(IHTTPSession session) {
        switch (session.getUri()) {
            case "/inputs":
                return newFixedLengthResponse(
                        Response.Status.OK,
                        "application/json",
                        InputsManager.getValuesAsJSON()
                );

            case "/telemetry":
                try {
                    return newFixedLengthResponse(
                            Response.Status.OK,
                            "application/json",
                            OutputManager.getValuesAsJSON()
                    );
                } catch (Exception e) {
                    return respondWithException(e);
                }

            case "/set":
                HashMap<String, String> convertedMap = new HashMap<>();

                // most foul one liner
                // converts Map<String, List<String>> to Map<String, String> where the value of the resulting map is the first element of the List<String>
                session.getParameters().entrySet().stream().map(stringListEntry -> new AbstractMap.SimpleEntry<>(stringListEntry.getKey(), stringListEntry.getValue().get(0))).forEach(stringStringSimpleEntry -> convertedMap.put(stringStringSimpleEntry.getKey(), stringStringSimpleEntry.getValue()));

                InputsManager.setValues(convertedMap);

                return newFixedLengthResponse(
                        Response.Status.OK,
                        "text/plain",
                        "OK"
                );

            default:
                return serveFileSystem(session); // This will also return a 404 if the file is not found.
        }
    }

    private Response serveFileSystem(IHTTPSession session) {
        // TODO: Yes. This does create a security hole. Yes, this does allow for directory traversal. But, yes, this can be fixed... LATER!
        try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/public/" + session.getUri())){
            if (resourceAsStream == null) {
                return newFixedLengthResponse(
                        Response.Status.NOT_FOUND,
                        "text/plain",
                        "Not found"
                );
            }

            // Infer mime type from file extension
            String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(session.getUri());
            String mimeType = null;

            if (fileExtensionFromUrl != null) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl); // This may return null.
            }

            // Default to text/html otherwise.
            if (mimeType == null) {
                mimeType = "text/html";
            }

            return newChunkedResponse(
                    Response.Status.OK,
                    mimeType,
                    resourceAsStream
            );
        } catch (IOException e) {
            return respondWithException(e);
        }
    }

    private static Response respondWithException(Exception e) {
        // Return the stack trace if there's a server error.
        String stackTrace = "500 ISR\n\n";

        try (PrintWriter writer = new PrintWriter(new StringWriter())) {
            e.printStackTrace(writer);
            stackTrace += writer.toString();
        }

        return newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                "text/plain",
                stackTrace
        );
    }
}