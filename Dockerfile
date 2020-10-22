FROM openjdk:8-jdk-alpine

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8095
ENTRYPOINT ["java","-cp","app:app/lib/*", "tr.com.ogedik.timetracker.TimeTrackerApplication"]

MAINTAINER m.eneserciyes@gmail.com