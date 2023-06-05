# Source Code for the Synchronous Implementation of the API Gateway

This repository contains the source code for the synchronous implementation of the API gateway.
The service exposes a single endpoint: `http://localhost:8080/order/:orderId` that returns the aggregated details of an
order.

## Branches of Repository

Each branch of this repository contains a variant of this implementation.

## Build Project

Maven is the chosen build tool for this project.

The project can be built with following command:
```
mvn clean install spring-boot:repackage
```

## Start Service

The address of the backend service to be called is configured via Spring properties.
The following properties define the addresses:
* ``service.address.auth`` - the address of the authentication service
* ``service.address.bill`` - the address of the billing service
* ``service.address.delivery`` - the address of the delivery service

The following command shows how to start service with all three backend services running on the same address:
```
java -jar target/api_gateway_reactive-1.0-SNAPSHOT-spring-boot.jar --service.address.auth=10.1.0.5:3000 --service.address.bill=10.1.0.5:3000 --service.address.delivery=10.1.0.5:3000
```

