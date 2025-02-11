package edu.eci.arep.microspring;

import edu.eci.arep.microspring.Server.GetMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import edu.eci.arep.microspring.Server.RestController;


public class WebFrameWork {

    static final Map<String, Method> routes = new HashMap<>();
    static final Map<String, Object> controllers = new HashMap<>();
    static Object controllerInstance;
    private static String staticFilesPath = "src/main";
    private static Class<?> loadedControllerClass;

    public static void loadController(String className) {
        try {
            loadedControllerClass = Class.forName(className);
            controllerInstance = loadedControllerClass.getDeclaredConstructor().newInstance();

            for (Method method : loadedControllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class) && method.getReturnType() == String.class) {
                    GetMapping annotation = method.getAnnotation(GetMapping.class);
                    routes.put(annotation.value(), method);
                    System.out.println("Ruta Registrada: " + annotation.value());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error cargando controlador: " + e.getMessage());
        }
    }


    public static void scanControllers (String Package){
        try {
            String path = Package.replace(".","/");
            URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

            if (resource == null){
                throw new RuntimeException("no se encontro el paquete base");
            }

            File file = new File(resource.toURI());
            for (File classFile : file.listFiles()) {
                if (classFile.getName().endsWith(".class")) {
                    String className = Package + "." + classFile.getName().replace(".class", "");
                    Class<?> class1 = Class.forName(className);
                    
                    if (class1.isAnnotationPresent(RestController.class)) {
                        System.out.println("Cargando controlador: " + className);
                        Object instance = class1.getDeclaredConstructor().newInstance();
                        controllers.put(class1.getName(),instance);
                        controllerInstance = instance;
                        
                        for (Method method : class1.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(GetMapping.class)) {
                                String route = method.getAnnotation(GetMapping.class).value();
                                routes.put(route, method);
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al escanear los controladores" + e.getMessage());
        }
    }

    public static String handleRequest(String path, Request req, Response res) {
        System.out.println("🔹 Buscando ruta: " + path); 
    
        Method method = routes.get(path);
        if (method != null) {
            try {
                System.out.println("✅ Método encontrado: " + method.getName());
                System.out.println("📌 Número de parámetros esperados: " + method.getParameterCount());
    
                String response;
                if (method.getParameterCount() == 2) {
                    response = (String) method.invoke(controllerInstance, req, res);
                } else if (method.getParameterCount() == 0) {
                    response = (String) method.invoke(controllerInstance);
                } else {
                    response = "⚠️ Método con parámetros no manejados";
                }
    
                System.out.println("🔹 Respuesta generada: " + response);
                return response;
    
            } catch (Exception e) {
                System.out.println("❌ Error ejecutando controlador: " + e.getMessage());
                return "Error ejecutando controlador: " + e.getMessage();
            }
        }
    
        System.out.println("⚠️ Ruta no encontrada: " + path);
        return "404 Not Found";
    }
    
    

    public static String getStaticFilesPath() {
        return staticFilesPath;
    }

    public static Class<?> getLoadedController() {
        return loadedControllerClass;
    }

    public static void StaticFiles(String path) {
        staticFilesPath = path;
    }
}
