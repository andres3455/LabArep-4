package edu.eci.arep.microspring;


import java.util.Arrays;
import java.lang.reflect.Method;


public class InvokeMain {
        
    public static void main(String[] args) {
        try {

            Class<?> c = Class.forName(args[0]);
            @SuppressWarnings("rawtypes")
            Class[] argTypes = new Class []{String[].class};
            Method main = c.getDeclaredMethod("main", argTypes);
            String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
            System.out.format("invoking %s.main()%n", c.getName());
            main.invoke(null,(Object) mainArgs); 
            
        } catch (Exception x) {
            x.getStackTrace();
        }
    }
}
