### STAGE 1: Build ###
FROM openjdk:8-jre-alpine as builder
RUN apk add openjdk8
RUN apk add maven
RUN mkdir -p ./src/
COPY src ./src/
COPY ./pom.xml .
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

### STAGE 2: Setup ###
FROM openjdk:8
RUN groupadd -r plm && useradd -r -g plm -m -d /home/plm -c "Docker Image User" plm
USER plm
WORKDIR /home/plm
RUN mkdir -p /home/plm/plm-server/
RUN mkdir -p /home/plm/template
RUN mkdir -p /home/plm/logs
COPY /template/PDF.ftl /home/plm/template/
COPY /template/PDFAll.ftl /home/plm/template/
COPY --from=builder /app/target/plm-server-1.0.0.jar /home/plm/plm-server/
EXPOSE 9000
ENTRYPOINT ["java","-jar","/home/plm/plm-server/plm-server-1.0.0.jar"]