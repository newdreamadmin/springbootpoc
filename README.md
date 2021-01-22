# Azure Spring Boot POC

This is a sample Java11 / Maven /Tomcat 9/ Spring Boot (version 2.4.2)/ Spring Data JPA application that can be used as a starter for creating a service complete.

## About the Service
Spring Boot POC:
* Load CSV into Azure File Storage
* HTTP Post to trigger the flow
* Read data from Azure File Storage and store in Azure SQL using JDBC/JPA

## IDE Used
 Spring Tool Suite 4

## Explore Rest API
The app defines following POST API.
```
POST /api/v1/post 
Content-Type: application/json

{
    "CSV_FILE_URL":"http://cloud.newdreamsystems.com:8081/base/poc_file_3.csv"
}

RESPONSE: HTTP 200 (Created)
API URL: http://*.*.*.*:8080/api/v1/post
```
