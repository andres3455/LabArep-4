package edu.eci.arep.microspring.Server;

@RestController
public class GreetingController {


    @GetMapping("/App/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/App/pi")
    public String pi() {
        System.out.println("Metod pi ejecutado");
        return Double.toString(Math.PI);
    }

    @GetMapping("/App/test")
    public String test() {
        return "✅ Ruta /App/test funcionando correctamente!";
    }

    @GetMapping("/App/weather")
    public String weather(){
        return "Este servicio fue dado de baja";
    }


}
