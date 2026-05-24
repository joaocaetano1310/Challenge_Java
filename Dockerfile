FROM maven:3.9-eclipse-temurin-17
 
WORKDIR /app
 
COPY . /app
 
ENV SPRING_PROFILES_ACTIVE=default
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:futurevetdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver
ENV SPRING_DATASOURCE_USERNAME=sa
ENV SPRING_DATASOURCE_PASSWORD=
ENV SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.H2Dialect
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
 
RUN adduser --disabled-password --gecos "" --home /home/appuser --shell /bin/bash appuser \
    && chown -R appuser:appuser /app /home/appuser
 
USER appuser
 
EXPOSE 8080
 
CMD ["bash", "-c", "mvn clean package -DskipTests && java -jar target/*.jar"]