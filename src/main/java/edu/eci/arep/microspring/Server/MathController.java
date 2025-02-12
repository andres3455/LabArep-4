package edu.eci.arep.microspring.Server;



@RestController
public class MathController {
    @GetMapping("/App/e")
    public static String e (String nousada){
        return String.valueOf(Math.E);

    }
}
