## GRPC Sample Project
This project is a sample project to demonstrate how to use GRPC in a Java Spring Boot project. The project is a simple a store that can manage products, and increment stock.
The project is divided into three parts: the server, the client, and the proto module. 
The server is a Spring Boot application that exposes a GRPC service. 

The client is a Postman application that consumes the GRPC service. 

The proto module is a Maven module that contains the proto files and is used by the server and the client to generate the GRPC code.

### Configuration
The server is configured to use the port 8980.

### Running the project
To run the project, you need to start the server and use the gRPC Postman collection to interact with the server or use your own client.

### Commands
To generate the GRPC code, run the following command in the proto module:

```mvn clean install```

This command will generate the GRPC code in the target/generated-sources/protobuf/grpc-java directory.

To run the server, run the following command in the server module:

```mvn spring-boot:run```

To run the client, use the following Postman collection and run the requests.

[Postman Collection](https://www.postman.com/lunar-shuttle-200815/workspace/public-achaghirc/collection/669c1a89b4c9791b5a25b63e?action=share&creator=11026541)


#### Source
Generated following the tutorial de [Paradigma Digital](https://www.paradigmadigital.com) 

[gRPC: Un ejemplo con Java Spring Boot y Postman](https://www.paradigmadigital.com/dev/grpc-ejemplo-java-springboot-postman/)
