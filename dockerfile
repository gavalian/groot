# Base image to build and publsih the artifact
FROM maven3.9.8-eclipse-temurin-11

# sets up working directory

WORKDIR /app

# copy the all maven dependencies 

COPY . .

# build the actual application once its copies the entire code inot container

RUN mvn clean package
