FROM openjdk:22
WORKDIR /usrapp/bin

ENV PORT 35000

#Se copian las clases y las dependencias
COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

COPY src/main/resources/www /usrapp/bin/www

CMD ["java","-cp","./classes:./dependency/*","edu.eci.arep.microspring.httpServer", "edu.eci.arep.microspring.Server.GreetingController"]