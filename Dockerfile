FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY . /app

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-dimdim:3306/db-dimdim
ENV SPRING_DATASOURCE_USERNAME=user-dimdim
ENV SPRING_DATASOURCE_PASSWORD=senha-dimdim

RUN adduser -h /home/appuser -s /bin/bash -D appuser

USER appuser

EXPOSE 8080

CMD ["bash", "-c", "mvn clean package -DskipTests && java -jar target/*.jar"]
