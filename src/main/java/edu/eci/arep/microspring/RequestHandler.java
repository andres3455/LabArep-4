package edu.eci.arep.microspring;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestHandler {

    public static void handle(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream outputStream = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);

        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return;
        }

        System.out.println("Request: " + requestLine);

        StringTokenizer tokens = new StringTokenizer(requestLine);
        String method = tokens.nextToken(); // GET, POST, etc.
        String fullPath = tokens.nextToken(); // /, /hello?name=Andrés

        if (!method.equals("GET")) {
            sendResponse(writer, 405, "Method Not Allowed", "Solo se permite GET.");
            return;
        }

        // Separar ruta y parámetros de consulta
        String path;
        String queryString = null;
        if (fullPath.contains("?")) {
            String[] parts = fullPath.split("\\?", 2);
            path = parts[0];
            queryString = parts[1];
        } else {
            path = fullPath;
        }

        if (path.equals("/")) {
            path = "/index.html";
        }

        // Servir archivos estáticos (HTML, imágenes PNG)
        if (serveStaticFile(writer, outputStream, path)) {
            return;

        }

        Map<String, String> queryParams = parseQueryString(queryString);

        if (WebFrameWork.routes.containsKey(path)) {
            Method controllerMethod = WebFrameWork.routes.get(path);
            Object controllerInstance = WebFrameWork.controllers.get(controllerMethod.getDeclaringClass().getName());
            

            try {
                String response;
                if (controllerMethod.getParameterCount() == 1) {
                    String paramName = controllerMethod.getParameters()[0].getName(); // Obtener el nombre del parámetro
                    String paramValue = queryParams.getOrDefault("name", "world"); // Obtener el valor del parámetro
                    response = (String) controllerMethod.invoke(controllerInstance, paramValue);
                    System.out.println("parametro recibido: " + paramValue);
                } else {
                    response = (String) controllerMethod.invoke(controllerInstance);
                }
                sendResponse(writer, 200, "OK", response);
            } catch (Exception e) {
                sendResponse(writer, 500, "Internal Server Error",
                        "Error ejecutando el controlador: " + e.getMessage());
            }
        }
        String responseBody = handleDynamicRoute(path, queryParams);

        // Si la ruta dinámica no existe, devolver 404
        if (responseBody.equals("404 Not Found")) {
            sendResponse(writer, 404, "Not Found", "Ruta no encontrada.");
        } else {
            sendResponse(writer, 200, "OK", responseBody);
        }
    }

    // Buscar rutas dinámicas en los controladores registrados en WebFramework
    public static String handleDynamicRoute(String path, Map<String, String> queryParams) {
        Method method = WebFrameWork.routes.get(path);
        if (method != null) {
            try {
                System.out.println("✅ Ejecutando ruta dinámica: " + path);
                System.out.println("🔹 Método encontrado: " + (method != null ? method.getName() : "null"));
                System.out.println("📌 Número de parámetros: " + method.getParameterCount());

                Object controllerInstance = WebFrameWork.controllers.get(method.getDeclaringClass().getName());
                if (controllerInstance == null) {
                    return "Error: Controlador no encontrado.";
                }

                // Si el método espera 1 parámetro
                if (method.getParameterCount() == 1) {
                    String nameParam = queryParams.getOrDefault("name", "World");
                    return (String) method.invoke(controllerInstance, nameParam);
                }
                // Si el método NO tiene parámetros
                else if (method.getParameterCount() == 0) {
                    return (String) method.invoke(controllerInstance);
                }
                // Si el método tiene más parámetros de los esperados
                else {
                    return "⚠️ Método con parámetros no manejados";
                }

            } catch (Exception e) {
                return "Error ejecutando controlador: " + e.getMessage();
            }
        }
        return "404 Not Found";
    }

    private static boolean serveStaticFile(PrintWriter writer, OutputStream outputStream, String path)
            throws IOException {
        String staticPath = WebFrameWork.getStaticFilesPath();
        File file = new File(staticPath + path);

        if (file.exists() && !file.isDirectory()) {
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));

            writer.printf("HTTP/1.1 200 OK\r\n");
            writer.println("Content-Type: " + (contentType != null ? contentType : "text/plain") + "; charset=UTF-8");
            writer.println("Content-Length: " + file.length());
            writer.println();
            writer.flush();

            if (contentType != null && contentType.startsWith("image")) {
                Files.copy(file.toPath(), outputStream);
            } else {
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    writer.println(line);
                }
                fileReader.close();
            }
            return true;
        }
        return false;
    }

    private static void sendResponse(PrintWriter writer, int statusCode, String statusMessage, String body) {
        writer.printf("HTTP/1.1 %d %s\r\n", statusCode, statusMessage);
        writer.println("Content-Type: text/plain; charset=UTF-8");
        writer.println("Content-Length: " + body.length());
        writer.println();
        writer.println(body);
    }

    private static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        if (queryString != null) {
            for (String param : queryString.split("&")) {
                String[] keyValue = param.split("=", 2); // <- Asegurarse de dividir solo en el primer "="
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]); // Guardar clave y valor
                } else {
                    queryParams.put(keyValue[0], ""); // Parámetros sin valor
                }
            }
        }
        return queryParams;
    }

}
