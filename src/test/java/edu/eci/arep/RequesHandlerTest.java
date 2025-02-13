package edu.eci.arep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.arep.microspring.RequestHandler;
import edu.eci.arep.microspring.WebFrameWork;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;
    
    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream, true);
    }


    @Test
    void testHandleDynamicRoute_withInvalidRoute() {
        Map<String, String> queryParams = new HashMap<>();

        String response = RequestHandler.handleDynamicRoute("/App/invalid", queryParams);
        assertEquals("404 Not Found", response, "Debe devolver 404 para rutas no registradas.");
    }

    @Test
    void testParseQueryString_withValidParams() {
        String queryString = "name=Andres&age=25";
        Map<String, String> queryParams = RequestHandler.parseQueryString(queryString);

        assertEquals(2, queryParams.size(), "Debe haber dos parámetros.");
        assertEquals("Andres", queryParams.get("name"), "El valor del parámetro 'name' debe ser 'Andres'.");
        assertEquals("25", queryParams.get("age"), "El valor del parámetro 'age' debe ser '25'.");
    }

    @Test
    void testParseQueryString_withEmptyValue() {
        String queryString = "name=Andres&age=";
        Map<String, String> queryParams = RequestHandler.parseQueryString(queryString);

        assertEquals(2, queryParams.size(), "Debe haber dos parámetros.");
        assertEquals("Andres", queryParams.get("name"), "El valor del parámetro 'name' debe ser 'Andres'.");
        assertEquals("", queryParams.get("age"), "El valor del parámetro 'age' debe ser una cadena vacía.");
    }

}
