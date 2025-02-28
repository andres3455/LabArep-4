package edu.eci.arep.microspring;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpServer {
    private static final int PORT = 35000;
    private static final int THREAD_POOL_SIZE = 10;
    private static boolean running = true;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        String Package = "edu.eci.arep.microspring.Server";
        WebFrameWork.scanControllers(Package);

        // Configuración de los archivos estáticos

        String staticPath;
        if (new File("/usrapp/bin/www").exists()) {
            staticPath = "/usrapp/bin/www"; // Para Docker
        } else {
            staticPath = "src/main/resources/www"; // Para local
        }

        WebFrameWork.StaticFiles(staticPath);
        System.out.println("📂 Sirviendo archivos desde: " + staticPath);
        WebFrameWork.loadController("edu.eci.arep.microspring.Server");

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("✅ Server running on port " + PORT);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> {
                    try {
                        RequestHandler.handle(clientSocket);
                        clientSocket.close();
                    } catch (IOException e) {
                        System.err.println("⚠️ Client handling error: " + e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

}
