# Escuela Colombiana de Ingeniería
# Arquitecturas Empresariales

## Taller 3 Taller de Arquitecturas de Servidores de Aplicaciones, Meta protocolos de objetos, Patrón IoC, Reflexión
Se trabajó implementando las clases vistas en clase, junto con el servidor HTTP que venimos desarrollando en anteriores laboratorios

Para este taller desarrolle un prototipo mínimo que demuestre las capacidades reflexivas de JAVA y permita por lo menos cargar un bean (POJO) y derivar una aplicación Web a partir de él. 

---
### Prerrequisitos

* [Maven](https://maven.apache.org/): Es una herramienta de comprensión y gestión de proyectos de software. Basado en el concepto de modelo de objetos de proyecto (POM), Maven puede gestionar la construcción, los informes y la documentación de un proyecto desde una pieza de información central.
* [Git](https://learn.microsoft.com/es-es/devops/develop/git/what-is-git): Es un sistema de control de versiones distribuido, lo que significa que un clon local del proyecto es un repositorio de control de versiones completo. Estos repositorios locales plenamente funcionales permiten trabajar sin conexión o de forma remota con facilidad.

### Arquitectura representada por Capas

```

```




### Instalación

1) Debemos clonar el repositorio
```
https://github.com/andres3455/ArepLab3.git
```
2) Una vez clonamos, accedemos al directorio
```
cd ArepLab3
```
3) Construimos el proyecto
```
mvn package
```
---

## Ejecución

### Primera Forma
En la terminal de comando, utilizamos la sentencia:
```
mvn exec:java -"Dexec.mainClass"="edu.eci.arep.http.httpServer"  
```

### Segunda Forma
1) En la barra de navegación de nuestro IDE, buscamos la opción "Ejecutar".
   
2) Luego, elegimos la opción "iniciar depuración" o "Ejecutar sin depuración"


## Casos de uso

Una vez ejecutado, accedemos a la URL en un navegador

```
http://localhost:35000
```


Aquí podemos observar formato de nuestro servidor HTTP.

![Imagen1](img/1.png)

1) Primera Version, cargue el POJO desde la linea de comando

Utilizamos el siguiente comando para cumplir dicho objetivo

```
java -cp "target/classes" edu.eci.arep.microspring.Server.MicroServer edu.eci.arep.microspring.Server.GreetingController                                                      

```
Con esto logramos invocar el framework

![Imagen1](img/2.png)

como podemos ver, hemos puesto unas salidas de texto, para controlar el registro de rutas

2) Atiende la anotación @GetMapping 

Esta anotación,la habiamos creado en clase, por lo tanto, solamente debemos cambiar lo que queremos que nos regrese el servicio


![image](img/3.png)

![image](img/4.png)

3) Version final, busqueda de clases con una anotación

se crea un metodo dentro de la clase WebFrameWork, el cual nos va a permitir escanear un paquete dado para clases con la anotación @RestController y registrar sus metodos que tenga la anotación @ GetMapping



2) Mecanismo de extracción de valores de consulta:

![](/img/5.png)

Para esta parte, se implementó un mecanismo el cual extrae los valores de una consulta, en este caso una querystring, de donde tenemos el parámetro "name" con valor "Andrés" para ello se utiliza el método .get pasándole la ruta que queremos que responda y me retornara un mensaje de "hola" identado con el valor del parámetro que extrajimos

3) Especificación de ubicación de archivo estático

![Imagen](img/6.png)

Antes lo que hacíamos, era asignar a una variable, la ruta donde estaban nuestros archivos, ahora debemos implementar un método, lo que se realizó fue definir una ruta por defecto como una variable, luego se creó el método que cambia esa variable que definimos, por el nuevo argumento que especifiquemos al llamar al método.

## Pruebas

para ejecutar las pruebas, puede utilizar el siguiente comando 

```
mvn clean test
```

![Imagen](img/7.png)

## Cobertura de las pruebas con JaCoCo

![Imagen](img/8.png)

### Construido con

* [Maven](https://maven.apache.org/): Es una herramienta de comprensión y gestión de proyectos de software. Basado en el concepto de modelo de objetos de proyecto (POM), Maven puede gestionar la construcción, los informes y la documentación de un proyecto desde una pieza de información central.

* [Git](https://learn.microsoft.com/es-es/devops/develop/git/what-is-git): Es un sistema de control de versiones distribuido, lo que significa que un clon local del proyecto es un repositorio de control de versiones completo. Estos repositorios locales plenamente funcionales permiten trabajar sin conexión o de forma remota con facilidad.

* [GitHub](https://platzi.com/blog/que-es-github-como-funciona/): Es una plataforma de alojamiento, propiedad de Microsoft, que ofrece a los desarrolladores la posibilidad de crear repositorios de código y guardarlos en la nube de forma segura, usando un sistema de control de versiones llamado Git.

* [Java -17](https://www.cursosaula21.com/que-es-java/): Es un lenguaje de programación y una plataforma informática que nos permite desarrollar aplicaciones de escritorio, servidores, sistemas operativos y aplicaciones para dispositivos móviles, plataformas IoT basadas en la nube, televisores inteligentes, sistemas empresariales, software industrial, etc.

* [JavaScript](https://universidadeuropea.com/blog/que-es-javascript/): Es un lenguaje de programación de scripts que se utiliza fundamentalmente para añadir funcionalidades interactivas y otros contenidos dinámicos a las páginas web.

* [HTML](https://aulacm.com/que-es/html-significado-definicion/): Es un lenguaje de marcado de etiquetas que se utiliza para crear y estructurar contenido en la web. Este lenguaje permite definir la estructura y el contenido de una página web mediante etiquetas y atributos que indican al navegador cómo mostrar la información.

* [CSS](https://www.hostinger.co/tutoriales/que-es-css): Es un lenguaje que se usa para estilizar elementos escritos en un lenguaje de marcado como HTML.

* [Visual Studio Code](https://openwebinars.net/blog/que-es-visual-studio-code-y-que-ventajas-ofrece/): Es un editor de código fuente desarrollado por Microsoft. Es software libre y multiplataforma, está disponible para Windows, GNU/Linux y macOS.

## Autor

* **[Andrés Felipe Rodríguez Chaparro](https://www.linkedin.com/in/andres-felipe-rodriguez-chaparro-816ab527a/)** - [20042000](https://github.com/20042000)

## Licencia
**©** Andrés Felipe Rodríguez Chaparro. Estudiante de Ingeniería de Sistemas de la Escuela Colombiana de Ingeniería Julio Garavito
