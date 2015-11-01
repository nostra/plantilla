FROM relateiq/oracle-java8

WORKDIR /code

COPY build/libs/plantilla-*-all.jar /code/plantilla.jar
COPY dropwizard.yml /code/

EXPOSE 8080
CMD ["java", "-jar", "/code/plantilla.jar"]
