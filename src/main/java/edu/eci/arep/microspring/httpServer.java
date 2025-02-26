package edu.eci.arep.microspring;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        WebFrameWork.StaticFiles("src/main/resources/static");
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

    public static void handleShutdown(OutputStream out) throws IOException {
        System.out.println("🛑 Apagando el servidor...");

        running = false;  // 🔹 Detener el bucle principal
        stop();

        // Intentar cerrar el socket del servidor
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }

        // Mostrar la página de apagado
        String htmlPath = "src/main/resources/static/shutdown.html";
        byte[] content = Files.readAllBytes(Paths.get(htmlPath));
        sendResponse(out, "200 OK", "text/html", content);
    }

    public static void stop() {
        running = false;
        System.out.println("✅ Servidor detenido correctamente.");
    }

    private static void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        out.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(content);
        out.flush();
    }
}
