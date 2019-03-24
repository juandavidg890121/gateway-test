# Gateway REST API

_Gateway_Test_ is a Spring Boot project for managing Gateways and Peripheral Devices.

### Description
This sample project is managing gateways - master devices that control multiple peripheral devices. 
Is a REST service (JSON/HTTP) for storing information about these gateways and their associated devices. 
This information must be stored in the database.

When storing a gateway, any field marked as “to be validated” must be validated and an error returned if it is invalid. 
Also, no more that 10 peripheral devices are allowed for a gateway.

The service provide operations for displaying information about all stored gateways (and their devices) and an operation for displaying details for a single gateway. 
Finally, it must be possible to add and remove a device from a gateway

Each gateway has:
* a unique serial number (string)
* human-readable name (string)
* IPv4 address (to be validated)

Each peripheral device has:
* a UID (number)
* vendor (string)
* date created
* status - online/offline

### Installation
1. Clone or download the project in GitHub.
2. Go to project folder.
3. Run the next command to build the project.
```bash
mvn package
```

### Run application
```bash
java -jar application.jar
```

### Access to API REST Documentation
```bash
http://localhost:8080/swagger-ui.html
```