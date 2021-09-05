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
RUN groupadd -r dynamike && useradd -r -g dynamike -m -d /home/dynamike -c "Docker Image User" dynamike
USER dynamike
WORKDIR /home/dynamike
RUN mkdir -p /home/dynamike/dynamike-server/
RUN mkdir -p /home/dynamike/template
RUN mkdir -p /home/dynamike/logs
COPY /template/PDF.ftl /home/dynamike/template/
COPY /template/PDFAll.ftl /home/dynamike/template/
COPY --from=builder /app/target/dynamike-server-1.0.0.jar /home/dynamike/dynamike-server/
EXPOSE 9000
ENTRYPOINT ["java","-jar","/home/dynamike/dynamike-server/dynamike-server-1.0.0.jar"]