# MVP - Tech challenge

### Lucas Fernando das Neves Carvalho dos Santos
- Email: lfneves.dg@gmail.com
- RM: 350505
- Grupo 53


---

#### This is a [Spring Boot WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) application using [Kotlin](https://kotlinlang.org/).

Spring WebFlux utilizes the [Reactor](https://projectreactor.io/) library, which is an implementation of Reactive Streams specs for building non-blocking applications.

This project:
- Uses [Reactor Netty as the default implementation](https://github.com/reactor/reactor-netty) for testing purposes. To change to Apache Tomcat as the default Web container for Spring WebFlux, follow these steps.
- Utilizes functional endpoints.
- Employs the [PostgreSQL](https://www.postgresql.org/) database.


## 💡 Requirements

- Java 17 or later - [SDKMAN - Recommendation](https://sdkman.io/install)
- Gradle 7.6.1 or later - [Gradle build tool Installation](https://gradle.org/install/)
- Docker 24.0.2 or later - [How to install Docker](https://docs.docker.com/engine/install/)
- Docker Compose 1.29.2 or later - [Reference guide](https://docs.docker.com/compose/install/)
- Minikube v1.31.2 or later - [Get Started with Minikube](https://minikube.sigs.k8s.io/docs/start/)
- Helm v3.10.1 or later - [Installing Helm](https://helm.sh/docs/intro/install/)
- The project runs on port 8099 (http://localhost:8099).

<!-- GETTING STARTED -->
## Getting Started

```sh
# Get the latest version

git clone https://github.com/lfneves/mvp.git
```

## Project Structure


```
main
├── kotlin
|  └── com
|     └── mvp
|        └── delivery
|           ├── DeliveryApplication.kt
|           ├── application
|           ├── domain
|           ├── infrastruture
|           └── utils
└── resources
   ├── application.yml
   └── database
      ├── 1_create_tables.sql
      └── 2_inserts_category.sql
```

### Prerequisites
Check versions:
* Java 17+
  ```sh
  java --version
  ```

* Docker
  ```sh
  docker -v
  ```

* Docker Compose
  ```sh
  docker-compose --version
  ```

## Installation
This is an example of how to use the software and how to install it.


### Docker

In the main project directory:

  
  Docker build and start applications:
  ```sh 
  $ docker-compose up --build
  ```
  
   Or use:
   
  ```
  $ docker-compose up -d --build
  ```
  

  To recreate the application in case of problems, use the command:
    
  ```
  $ docker-compose down
  ```
  
---


### Kubernetes (k8s)

#### To initiate Kubernetes applications, execute the commands found within the "k8s" folder.

```
$ kubectl apply -f delivery/k8s/postgres/.
```

```
$ kubectl apply -f delivery/k8s/application/.
```


#### o access the application URL, use the following command:

```
$ minikube service delivery --url
```

#### Example output:
```
http://192.168.49.2:32000

```

Inside the "k8s" folder, you will discover ".yaml" files utilized to deploy databases and applications within Kubernetes.

```
/delivery/k8s
├── application
|  ├── 1-deployment.yaml
|  ├── 2-service-load-balancer.yaml
|  ├── 3-hpa.yaml
|  ├── 4-ingress.yaml
|
└── postgres
   ├── 1-db-persistent-volume.yaml
   ├── 2-db-volume-claim.yaml
   ├── 3-db-configmap.yaml
   ├── 4-db-secret.yaml
   ├── 5-db-deployment.yaml
   └── 6-db-service.yaml
```

#### Metric Server 

```
$ minikube addons enable metrics-server
```

To monitor the Horizontal Pod Autoscaler, employ the following command:
```
$ kubectl get hpa
```
![Alt text](images/hpa_example_img.png "Horizontal Pod Autoscaler")

---

###  Kubernetes (k8s) - Install with Helm 

[BETA] Because this hasn't been implemented following best practices.

```
$ helm install deliveryhelm deliveryhelm/
```

#### Helm uninstall

```
$ helm uninstall deliveryhelm deliveryhelm/
```

---
###  Integration Mercado Pago 

For the webhook checkout process, generate a QR code.

For testing full process with Mercado Pago webhook, use hookdeck.com with CLI to change the order status in the localhost application.

Apllication path **/api/v1/mp-order/qr-code-checkout** creates a checkout with Mercado Pago.


Example:

```json
{
    "in_store_order_id": "75ca8fe9-3b1a-4053-8f3e-49a62e91f8e8",
    "qr_data": "00020101021243650016COM.MERCADOLIBRE02013063675ca8fe9-3b1a-4053-8f3e-49a62e91f8e85204000053039865802BR5908delivery6009SAO PAULO62070503***63042BFA"
}
```

---
### This project uses [CommandLineRunner](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/CommandLineRunner.html)
- CommandLineRunner is used to create a default user, products and categories on start application startup.
- Default login :

**/api/auth/login-token**
```json
{
  "username": "99999999999",
  "password": "123"
}
```
---

## The best way to use it as a suggestion is by using [Postman](https://www.postman.com/downloads/)
#### A collection is available preconfigured in the project root
[MVP - Pos tech delivery application.postman_collection.json]()

- This project uses user and session control for access
- Endpoints without control access  _**"/api/auth/*"**_, **_"/api/v1/users/signup"_**

#### Create new user example:
http://localhost:8099/api/v1/users/signup

Body:
```json
{
    "name": "Admin",
    "email": "admin@email.com",
    "cpf": "99999999999",
    "password": "admin",
    "address": {
        "street": "rua 1",
        "city": "sp",
        "state": "sp",
        "postalCode": "1234"
    }
}
```

<br/>

#### Login - Use the username (cpf) and password, then copy the token and use it in authenticated endpoints.

http://localhost:8099/api/auth/login-token
```json
{
  "username": "99999999999",
  "password": "admin"
}
```
Response:
```json
{
"token": "eyJhbGciOiJIUzI1NiJ9.eyJpZENsaWVudCI6IjAiLCJ1c2VybmFtZSI6IjEyMzQ1Njc4OTEyIiwic3ViIjoiMTIzNDU2Nzg5MTIiLCJpYXQiOjE2ODgwOTI1NTAsImF1ZCI6Im5vLWFwcGxpY2F0aW9uLW5hbWUiLCJleHAiOjE2ODgwOTQwMDB9.HagYPqukwOML3OYad8sRjlnE0Gsy-5tGUSC72S-xyfU"
}
```

### 💡To make it easier use environment variables 
#### Place the command in the test tab on /api/auth/login-token
```sh
pm.environment.set("token", pm.response.json().token);
```

#### Example:

![Alt text](images/postman_01.png "Postman token environment")
<br>
![Alt text](images/postman_02.png "Postman token using")
---

### This project also uses OpenAPI Specification [(Swagger)](https://swagger.io/docs/specification/about/).

#### To access swagger use the URL:
http://localhost:8099/swagger-ui.html
or
http://localhost:8099/webjars/swagger-ui/index.html

![Alt text](images/swagger_01.png "Swagger home")

![Alt text](images/swagger_02.png "Swagger Endpoints")

---

<!-- ROADMAP -->
## Roadmap

- [x] Improve README.md
- [X] Update order add paid status and adjusting service 
- [x] Implementation Helm 
- ### Improvements
- [x] Refactor admin services and repository to new package
- [x] Fix create order exceptions
- [x] Mercado Pago Qr code checkout
- [x] Refactor scripts database

---
<!-- LICENSE -->
## License

Distributed under the MIT License. See LICENSE.txt for more information.
