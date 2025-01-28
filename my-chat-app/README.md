# Chat application 🖥️⌨️💬

This application consists of a **Spring Boot** backend and an **Angular** frontend. The communication between the two is secured via **HTTP**S using a **self-signed SSL certificate**.

## How to start:

**1. Run Docker Desktop**

**2. Start MySQL Docker container:**
Use the following command:
```
docker run --name mysql-container  -e MYSQL_ROOT_PASSWORD=rootpassword  -e MYSQL_DATABASE=basics  -e MYSQL_USER=admin  -e MYSQL_PASSWORD=s3cret  -p 3306:3306  -d mysql:8.0
```
**3. Run Spring Boot BE and Angular FE**

## Security

1. JWT Token: After a successful login, the backend generates a JWT token containing the user's username. The token is used to identify which user is logged in and expires after a certain period.

2. Cookie: The JWT token is sent back to the frontend in an HTTP-only, Secure cookie. The cookie is stored by the browser and automatically included in subsequent requests to the backend.

3. Backend Security: For each request, the backend reads the JWT token from the cookie, validates it, and authenticates the user based on the information within the token (e.g., username).If the token expires in fixed amount of time - new token is attached to the reponse. If the token is invalid or missing, the backend rejects the request.

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


## Application Front-end

**Login**

![image](https://github.com/user-attachments/assets/3181dba6-3794-4bd7-83e8-83926bfc9417)

**Home**

![image](https://github.com/user-attachments/assets/69ad0317-204b-4fc1-a977-3a7471a5bc55)

**Add Contacts**

![image](https://github.com/user-attachments/assets/a332979f-4f1e-4d6f-a8ff-79bbd79d8f68)

**Edit Channel**

![image](https://github.com/user-attachments/assets/f0d0754f-57dc-48e4-9335-38ffac5574e9)

**Chat**

![image](https://github.com/user-attachments/assets/405e6fcc-9c38-4616-a294-341ccc1b37fd)


