# Chat application 🖥️⌨️💬
This application consists of a **Spring Boot** backend and an **Angular** frontend. The communication between the two is secured via **HTTP**S using a **self-signed SSL certificate**.

## How to start:
**1. Run Docker Desktop**

**2. Start MySQL Docker container:**
Use the following command:
```
docker run --name mysql-container  -e MYSQL_ROOT_PASSWORD=rootpassword  -e MYSQL_DATABASE=basics  -e MYSQL_USER=admin  -e MYSQL_PASSWORD=s3cret  -p 3306:3306  -d mysql:8.0
```

## SSL Certification Process
**1. Generate the Keystore**

```
 keytool -genkeypair -v -keystore my-keystore.p12 -keyalg RSA -keysize 2048 -storetype PKCS12 -validity 3650 -alias myalias
```

**2. Configure Spring Boot to Use SSL**
```
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=s3cret
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=selfsigned
```

**3. Extract the Certificate**
```
openssl pkcs12 -in keystore.p12 -out cert.pem -clcerts -nokeys
```

**4. Extract the private key**
```
openssl pkcs12 -in keystore.p12 -out key.pem -nocerts -nodes
```

**5. Run the Angular Frontend with SSL**
```
ng serve --ssl true --ssl-key "C:\projects\Angular\GitHub\my-chat-app\chat-backend-spring\src\main\resources\key.pem" --ssl-cert "C:\projects\Angular\GitHub\my-chat-app\chat-backend-spring\src\main\resources\cert.pem"
```
