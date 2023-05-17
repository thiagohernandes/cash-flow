# API cash-flow
Merchant's Cash Flow

### Goals
    - Control of financial entries and cash balance

### Instructions

Before install:

`Java: https://docs.oracle.com/en/java/javase/17/install/installation-jdk-microsoft-windows-platforms.html`

`Docker: https://docs.docker.com/`

`Kubernetes and Minikube (only deploy k8s cluster): https://kubernetes.io/docs/setup/`

Running local:

`git clone https://github.com/thiagohernandes/cash-flow.git`

`cd cash-flow`

`git checkout dev`

`mvn clean install`

`docker build -t api-cash-flow:1 .`

`docker-compose up -d`

### Swagger

`http://localhost:8088/swagger-ui/index.html`

### Calls

Return of entries and exits in the period and updated balance

`GET http://localhost:8088/cash-flow/daily-condensed/{initialDate}/{finalDate}`

`Example: http://localhost:8088/cash-flow/daily-condensed/2023-05-15/2023-05-17`

Save an incoming (CREDIT) or outgoing (DEBIT) financial entry

`POST http://localhost:8088/cash-flow/save`

`Example: http://localhost:8088/cash-flow/save`

Body

`{
"date": "2023-05-15",
"type": "DEBIT",
"value": 30
}`

### Deploy Kubernetes

Init Kubernetes Tool

`minikube start`

`minikube dashboard`

API Cash flow

`cd cash-flow`

`git checkout prod`

`cd k8s`

`kubectl apply -f .`

`kubectl get svc`

`minikube service api-cash-flow`

After last command, do:

`http://<ip-generated-local>:31000/swagger-ui/index.html`

`http://<ip-generated-local>:31000/cash-flow/daily-condensed/{initialDate}/{finalDate}`

`http://<ip-generated-local>:31000/cash-flow/save`

