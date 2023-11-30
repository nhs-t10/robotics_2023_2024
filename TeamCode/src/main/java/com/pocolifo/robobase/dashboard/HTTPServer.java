package com.pocolifo.robobase.dashboard;

import com.pocolifo.robobase.dashboard.output.OutputManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServer {
    private static final int CONNECTION_HANDLE_THREADS = 10; // Number of concurrent requests that can be handled at the same time
    private final ExecutorService executorService;
    private final ServerSocket serverSocket;


    public HTTPServer() throws IOException {
        this.serverSocket = new ServerSocket(2713);

        System.out.println("[dashboard] Server started on port 3724");

        // Use a thread pool for handling multiple connections concurrently
        this.executorService = Executors.newFixedThreadPool(HTTPServer.CONNECTION_HANDLE_THREADS);

        OutputManager.startUpdateScheduler();
    }

    public void startOnSeparateThread() {
        new Thread(this::run).start();
    }

    public void run() {
        while (true) {
            // Wait for a client to connect
            try (Socket clientSocket = serverSocket.accept()) {
                executorService.execute(() -> {
                    try {
                        new HTTPRequest(clientSocket).handleRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                        executorService.shutdown();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}