FROM openjdk:8-jdk-alpine
COPY build/libs/Todo-1.0.jar Todo.jar
EXPOSE 8080
CMD ["java", "-jar", "Todo.jar"]