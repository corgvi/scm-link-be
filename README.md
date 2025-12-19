# SCM Link - Backend Service
**Supply Chain Management**
This service is responsible for:
* Roles and permissions
* Authentication
* Managing warehouses, suppliers, products, and warehouse locations
* Managing inventory and batches
* Managing orders, receives, deliveries and tracking schedule
* Managing users and profiles
* Reporting and statistics

## Tech stack
* Build tool: maven >= 3.9.11
* Java: 21
* Framework: Spring boot 3.5.x
* DBMS: MySQL
* Security: Spring Security + JWT
* Mapping: MapStruct
* Validation: Hibernate Validator
* Container: Docker

## Prerequisites
* Java SDK 21
* A MySQL server

## Project Structure
src/
├── main/
│   ├── java/com/cvv/scm_link/
│   │   ├── configuration/      
│   │   ├── constant/           
│   │   ├── controller/        
│   │   ├── dto/               
│   │   ├── entity/            
│   │   ├── enums/              
│   │   ├── exception/         
│   │   ├── mapper/            
│   │   ├── repository/
│   │   ├── service/            
│   │   ├── validator/          
│   │   └── ScmLinkApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-prod.properties
└── test/
└── java/...                

## Production configuration
The application uses the following environment variables for configuration:
* `MYSQLDATABASE`: The JDBC connection string for the MySQL database (e.g., `jdbc:mysql://localhost:3306/scm_link`).
* `MYSQLUSER`: The username for the MySQL database.
* `MYSQLPASSWORD`: The password for the MySQL database
* `MYSQLPORT`: The port for the MySQL database
* `MYSQLHOST`: The host for the MySQL database
* `VM options`: -Dspring.profiles.active=prod

## Local development configuration
You can set the following environment variables for local development:
* `DBMS_CONNECTION`: The JDBC connection string for the MySQL database (e.g., `jdbc:mysql://localhost:3306/scm_link`).
* `DBMS_USERNAME`: The username for the MySQL database.
* `DBMS_PASSWORD`: The password for the MySQL database
* `VM options`: -Dspring.profiles.active=default

Deploy

Frontend: https://scm-link-fe-production.up.railway.app/
Backend API: https://seal-app-9lhfz.ondigitalocean.app/

Demo account:

Username: admin | user
Password: admin | user

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Docker guideline
### Build docker image
`docker build -t cuongvv205/scm-link:0.0.1 .`
### Check list image
`docker image ls`
### Push docker image to Docker Hub
`docker image push cuongvv205/scm-link:0.0.1`
### Create network:
`docker network create scm-link-network`
### Check network:
`docker network ls`
### Start MySQL in demo-network
`docker run --network scm-link-network --name mysql-scm-link -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.36-debian`
### Run your application in demo-network
`docker run --name scm-link --network scm-link-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql-scm-link:3306/scm_link -d scm-link:0.0.1`
## Install Docker on ubuntu

# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo docker run hello-world