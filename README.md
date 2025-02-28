# Escuela Colombiana de Ingeniería
# Arquitecturas Empresariales

## Taller 4 Modularización con virtualización y introduccion a Docker

## Descripcion del laboratorio

En este laboratorio, se trabajó con el framework desarrollado previamente, con el objetivo de implementar concurrencia para mejorar su rendimiento. Además, se buscó optimizar la salida del programa para que fuera más clara y elegante. Finalmente, se realizó el despliegue de la aplicación utilizando instancias EC2 en AWS y contenedores Docker.


---
### Prerrequisitos

* [Maven](https://maven.apache.org/): Es una herramienta de comprensión y gestión de proyectos de software. Basado en el concepto de modelo de objetos de proyecto (POM), Maven puede gestionar la construcción, los informes y la documentación de un proyecto desde una pieza de información central.
* [Git](https://learn.microsoft.com/es-es/devops/develop/git/what-is-git): Es un sistema de control de versiones distribuido, lo que significa que un clon local del proyecto es un repositorio de control de versiones completo. Estos repositorios locales plenamente funcionales permiten trabajar sin conexión o de forma remota con facilidad.

* [Docker](https://www.docker.com/): Es una plataforma para desarrollar, enviar y ejecutar aplicaciones en contenedores. Permite empaquetar una aplicación y sus dependencias en un contenedor ligero y portátil, garantizando la consistencia en diferentes entornos.

---

## Arquitectura representada por Capas

```
┌────────────────────────────────────────────────┐
│           Capa de Presentación (Frontend)      │
│        ( HTML, JavaScript,CSS)                 │           
└────────────────────────────────────────────────┘
                        │
                        ▼
┌────────────────────────────────────────────────┐
│          Capa de Controladores (Server)        │
│  - @GreetingController                         │
│  - @MathController                             │
│  - Métodos con @GetMapping("/ruta")            │
└────────────────────────────────────────────────┘
                        │
                        ▼
┌────────────────────────────────────────────────┐
│        Capa del Framework Web (MicroSpring)    │
│  - WebFrameWork → Registra rutas y métodos     │
│  - RequestHandler → Ejecuta métodos dinámicos
|  - Response -> Maneja la respuesta
    - Request -> Maneja la petición 
└────────────────────────────────────────────────┘
                        

```

## Diseño de Clases

1. Clase HttpServer: Es la clase principal que arranca el servidor y gestiona las peticiones HTTP
   
2. Clase WebFramework: Clase que maneja el registro, la ejecucion y la correcta lectura de los controladores REST
   
3. Clase RequestHandler: Maneja las peticiones que vengan del cliente y retorna la respuesta correspondiente
   
4. Clases MathController y GreetingController: Manejan y los recursos que queremos exponer 

### Instalación y instrucciones de despliegue

1) Debemos clonar el repositorio
```
https://github.com/andres3455/LabArep-4.git
```
2) Una vez clonamos, accedemos al directorio
```
cd LabArep-4
```
3) Construimos la imagen de docker
```
docker build --tag microspringdocker .
```
-- Video de la contrucción de la imagen

https://github.com/user-attachments/assets/96ac6a6b-bb51-4823-a697-e72bba3cda27


4) Creamos la imagen de un container para poder ejecutarlo

Recomendacion: Se sugiere crear 3 imagenes diferentes para poder acceder desde varios puertos

```
docker run -d -p 34000:35000 --name firstdockercontainer microspringdocker
```

## Imagenes de referencia

![Imagen1](img/1.png)

En docker, se debe ver algo asi:

![Imagen1](img/2.png)

## Comandos que te pueden ayudar para esta parte

```
docker logs [imagen del contenedor]
docker exec -it [imagen del contenedor] sh
docker ps
docker images 
```

## Antes de configurar AWS EC2

Debemos crear un repositorio en dockerHub:

![Imagen1](img/3.png)

Luego lo que haremos es crear una referencia del repositorio a nuestra imagen que creamos anteriomente,

```
docker tag microspringdocker andres3455/dockerandres
```
![Imagen1](img/4.png)

Nos logeamos desde la consola 
```
docker login
```
Empujamos la imagen al repositorio en DockerHub

```
docker push andres3455/dockerandres:latest
```
![Imagen1](img/5.png)

![Imagen1](img/3.png)

## Despliegue en AWS

Creamos una instancia en AWS EC2 con un sistema operativo basado en Linux , y accedemos a la consola de la instancia.

![Imagen1](img/6.png)

Instalamos docker en la instancia con el siguiente comando:

```
sudo apt install docker
sudo service docker start
```
Se configura el usuario en el grupo de docker para no tener que usar sudo cada vez que invoquemos un comando

```
sudo usermod -aG docker ubuntu
```
Nos desconectamos de la maquina para guardar los cambios y volvemos a ingresar

### Ultimo paso

A partir de la imagen del repo que creamos en dockerhub , creamos una instancia a una nuevo contenedor independiente de la consola con el puerto 80, enlazada al puerto donde manejamos nuestro servidor de manera local (35000)

```
docker run -d -p 80:35000 --name firstdockeraws andres3455/dockerandres
```
![Imagen1](img/7.png)

-- Video

https://github.com/user-attachments/assets/3bcb667f-f794-48a0-a6c9-f783d4b4af7f

Si todo sale bien , podremos acceder a nuestro servidor sede la Ip publica de la instancia como se muestra en el siguiente video


https://github.com/user-attachments/assets/0b8c6281-517f-472e-bf43-a625b47a4f52




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
