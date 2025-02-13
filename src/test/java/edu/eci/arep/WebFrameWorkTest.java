package edu.eci.arep;

import edu.eci.arep.microspring.Server.GreetingController;
import edu.eci.arep.microspring.WebFrameWork;
import edu.eci.arep.microspring.Request;
import edu.eci.arep.microspring.RequestHandler;
import edu.eci.arep.microspring.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WebFrameWorkTest {

    @BeforeEach
    void setUp() {
        // Limpiar datos antes de cada prueba
        WebFrameWork.routes.clear();
        WebFrameWork.controllers.clear();
        WebFrameWork.getLoadedController().clear();
    }

    @Test
    void testLoadController() {
        WebFrameWork.loadController("edu.eci.arep.microspring.Server");

        // Verificar que se haya cargado el controlador
        assertFalse(WebFrameWork.getLoadedController().isEmpty(), "No se han cargado controladores.");
        assertTrue(WebFrameWork.controllers.containsKey(GreetingController.class.getName()),
                "El controlador no se ha registrado correctamente.");
    }

    @Test
    void testRouteRegistration() {
        WebFrameWork.loadController("edu.eci.arep.microspring.Server");

        // Verificar que la ruta "/App/hello" esté registrada
        Map<String, Method> routes = WebFrameWork.routes;
        assertTrue(routes.containsKey("/App/hello"), "La ruta /App/hello está registrada.");
    }

    @Test
    void testHandleRequest() {
        WebFrameWork.loadController("edu.eci.arep.microspring.Server");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "andres");

        String result = RequestHandler.handleDynamicRoute("/App/hello", queryParams);

        assertNotNull(result, "La respuesta no debe ser nula.");
        assertTrue(result.contains("Hello, andres!"), "La respuesta debe contener el saludo.");
    }

    @Test
    void testHandleUnknownRoute() {
        String result = WebFrameWork.handleRequest("/ruta-inexistente", new Request("GET", "/ruta-inexistente"),
                new Response());
        assertEquals("404 Not Found", result, "Debe devolver un error 404 para rutas desconocidas.");
    }

    
}
