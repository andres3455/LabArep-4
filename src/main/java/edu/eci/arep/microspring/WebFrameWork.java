package edu.eci.arep.microspring;

import edu.eci.arep.microspring.Server.GetMapping;
import edu.eci.arep.microspring.Server.RestController;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class WebFrameWork {
    public static final Map<String, Method> routes = new HashMap<>();
    public static final Map<String, Object> controllers = new HashMap<>();
    private static final Set<Class<?>> loadedControllers = new HashSet<>();
    private static String staticFilesPath = "/usrapp/bin/www";


    public static void loadController(String packageName) {
        try {
            List<Class<?>> controllerClasses = scanControllers(packageName);
            for (Class<?> controllerClass : controllerClasses) {
                if (!loadedControllers.contains(controllerClass)) { // Evita duplicados
                    System.out.println("📌 Cargando controlador: " + controllerClass.getName());

                    Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                    controllers.put(controllerClass.getName(), controllerInstance);
                    loadedControllers.add(controllerClass); // Marcar como cargado

                    // Registrar métodos con @GetMapping
                    for (Method method : controllerClass.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(GetMapping.class) && method.getReturnType() == String.class) {
                            String route = method.getAnnotation(GetMapping.class).value();
                            routes.put(route, method);
                            System.out.println("✅ Ruta registrada: " + route);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error cargando controladores: " + e.getMessage(), e);
        }
    }

    public static List<Class<?>> scanControllers(String packageName) {
        List<Class<?>> controllerClasses = new ArrayList<>();
        try {
            String path = packageName.replace(".", "/");
            URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

            if (resource == null) {
                throw new RuntimeException("No se encontró el paquete base: " + packageName);
            }

            File directory = new File(resource.toURI());

            File[] classFiles = directory.listFiles((dir, name) -> name.endsWith(".class"));
            if (classFiles == null) {
                throw new RuntimeException("No se encontraron clases en el paquete: " + packageName);
            }

            for (File classFile : classFiles) {
                String className = packageName + "." + classFile.getName().replace(".class", "");
                Class<?> controllerClass = Class.forName(className);

                if (controllerClass.isAnnotationPresent(RestController.class)) {
                    controllerClasses.add(controllerClass);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al escanear los controladores: " + e.getMessage(), e);
        }
        return controllerClasses;
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
                    response = (String) method.invoke(loadedControllers, req, res);
                } else if (method.getParameterCount() == 0) {
                    response = (String) method.invoke(loadedControllers);
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

    public static Set<Class<?>> getLoadedController() {
        return loadedControllers;
    }

    public static void StaticFiles(String path) {
        staticFilesPath = path;
    }
}
