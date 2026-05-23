FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=senha-dimdim
ENV MYSQL_DATABASE=db-dimdim
ENV MYSQL_USER=user-dimdim
ENV MYSQL_PASSWORD=senha-dimdim

COPY docker-entrypoint-initdb.d/init.sql /docker-entrypoint-initdb.d/init.sql

EXPOSE 3306
