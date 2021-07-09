#####################################
# Dockerfile per l'applicazione hello 
#####################################

FROM openjdk:11-jdk 

ADD hello.jar hello.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx128m", "-Xms128m", "-jar", "hello.jar"]
