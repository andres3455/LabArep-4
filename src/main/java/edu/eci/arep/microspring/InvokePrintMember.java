
package edu.eci.arep.microspring;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class InvokePrintMember {

    public static void main(String[] args) {
        try {

            Class<?> c = Class.forName(args[0]);

            for (Method method : c.getDeclaredMethods()) {
                System.out.println("Método encontrado: " + method);
            }

            Class<?>[] argTypes = {Member[].class, String.class};
            Method m = c.getDeclaredMethod("printMembers", argTypes);

            Class<?> a = LinkedList.class;

            System.out.format("Invocando %s.printMembers()%n", c.getName());

                Object instance = c.getDeclaredConstructor().newInstance();
                m.invoke(instance, a.getDeclaredFields(), "Fields");
            
                m.invoke(null, a.getDeclaredFields(), "Fields");
            
        } catch (Exception x) {
            x.printStackTrace(); // Imprime el error para depuración
        }
    }
}

