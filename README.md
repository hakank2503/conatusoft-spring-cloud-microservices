## Spring Microservices

# Introduction
1.  A Spring Cloud Config server that is deployed as Docker container and can manage a services configuration information using a file system/ classpath or GitHub-based repository.
2.  A licensing service that will manage licensing data used within Ostock.
3.  A Postgres SQL database used to hold the data.

## Initial Configuration
1.	Apache Maven (http://maven.apache.org)  All of the code examples in this project have been compiled with Java version 11.
2.	Git Client (http://git-scm.com)
3.  Docker(https://www.docker.com/products/docker-desktop)

## How To Use

To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). From your command line:

```bash
$ mvn clean package dockerfile:build

# Now we are going to use docker-compose to start the actual image. 
$ docker-compose -f docker/docker-compose.yml up
```

# The build command

Will execute the [Spotify dockerfile plugin](https://github.com/spotify/dockerfile-maven) defined in the pom.xml file.  

# The Run command

This command will run our services using the docker-compose.yml file located in the /docker directory. 

# Database
You can find the database script as well in the docker directory.

## Contact

I'd like you to send me an email on <hakank2503@gmail.com> about anything you'd want to say about this software.

### Contributing
Feel free to file an issue if it doesn't work for your code sample. Thanks.