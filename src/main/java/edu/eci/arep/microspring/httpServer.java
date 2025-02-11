package edu.eci.arep.microspring;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpServer {
    private static final int PORT = 35000;
    private static final int THREAD_POOL_SIZE = 10; // Permite hasta 10 clientes simultáneamente

    public static void main(String[] args) {

        String Package = "edu.eci.arep.microspring.Server";
        WebFrameWork.scanControllers(Package);

        String controllerClassName = args[0];

        // Configuración de los archivos estáticos
        WebFrameWork.StaticFiles("src/main/resources/static");

        try {
            WebFrameWork.loadController("edu.eci.arep.microspring.Server.GreetingController");
            System.out.println("Cargando controlador" + controllerClassName);
        } catch (Exception e) {
            System.out.println("Error al cargar el controlador" + e.getMessage());
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> {
                    try {
                        RequestHandler.handle(clientSocket);
                        clientSocket.close();
                    } catch (IOException e) {
                        System.err.println("Client handling error: " + e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}