# Messaging Application

simple peer-to-peer messaging server where a user can be
registered with a unique human readable username and they can start message threads with
other users.

## General usage

### User Signup
```bash
curl --location 'http://localhost:8080/user' \
--header 'Content-Type: application/json' \
--header 'Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMTExIiwiaWF0IjoxNzAzNzczMjIzLCJleHAiOjE3MDM3NzQ2NjN9.JtiyC-Ocv1FIb1DRcdmL1J9gutkyhAVOhBWRu7nv7b0' \
--data '{
    "username":"nasruddin1111",
    "passcode":"passcode1"
}'

```
 Response if username is not registered.
```bash
{
    "status": "success",
    "message": "User registered Successfully",
    "httpStatus": null
}
```
Response if username is registered.
```bash
{
    "status": "failure",
    "message": "User already exist",
    "httpStatus": null
}
```
### Login
```bash
curl --location --request GET 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMSIsImlhdCI6MTcwMzc3MzA4MywiZXhwIjoxNzAzNzc0NTIzfQ.8-R_nkzXNJ_ex9X4YanbUMDQ5wUGrZ4g9hgagUN8oxg' \
--data '{
    "username":"nasruddin11",
    "passcode":"passcode1"
}'
```

#### Response if the credentials are correct
```bash
{
    "headers": {},
    "body": {
        "status": "success",
        "message": "logged in successfully",
        "httpStatus": null,
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMSIsImlhdCI6MTcwMzc3MzA4MywiZXhwIjoxNzAzNzc0NTIzfQ.8-R_nkzXNJ_ex9X4YanbUMDQ5wUGrZ4g9hgagUN8oxg"
    },
    "statusCode": "OK",
    "statusCodeValue": 200
}
```
#### Response if the credentials are incorrect
```bash
{
    "headers": {},
    "body": {
        "httpStatus": "BAD_REQUEST",
        "message": "Invalid username or password"
    },
    "statusCode": "BAD_REQUEST",
    "statusCodeValue": 400
}

### Login
```bash
curl --location --request GET 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMSIsImlhdCI6MTcwMzc3MzA4MywiZXhwIjoxNzAzNzc0NTIzfQ.8-R_nkzXNJ_ex9X4YanbUMDQ5wUGrZ4g9hgagUN8oxg' \
--data '{
    "username":"nasruddin11",
    "passcode":"passcode1"
}'
```

#### Response if the credentials are correct
```bash
{
    "headers": {},
    "body": {
        "status": "success",
        "message": "logged in successfully",
        "httpStatus": null,
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMSIsImlhdCI6MTcwMzc3MzA4MywiZXhwIjoxNzAzNzc0NTIzfQ.8-R_nkzXNJ_ex9X4YanbUMDQ5wUGrZ4g9hgagUN8oxg"
    },
    "statusCode": "OK",
    "statusCodeValue": 200
}
```
### Get All User on Server
##### This api will take cookie for browser
#### Request
```bash
curl --location 'http://localhost:8080/user' \
--header 'Cookie: chat-app-token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMSIsImlhdCI6MTcwMzc2Njc4MiwiZXhwIjoxNzAzNzY4MjIyfQ.7uzqANw-rabzw_ZWzz07Wh6Q546yEu7NvlZXxZNZJlk; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMSIsImlhdCI6MTcwMzc2Njg4NCwiZXhwIjoxNzAzNzY4MzI0fQ.RY1vY2MIBcExaaAzsDy0PA43CmM8wO3x7_F2kCxuToA'
```
#### response of authorised
```bash
{
    "status": "success",
    "users": [
        "munns",
        "munna",
        "munna1",
        "nasruddin",
        "nasruddin11",
        "nasruddin111"
    ]
}
```
### Send Message to a User
##### Curl Request
```bash 
curl --location 'http://localhost:8080/message/send' \
--header 'Content-Type: application/json' \
--header 'Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXNydWRkaW4xMTExIiwiaWF0IjoxNzAzNzczMjIzLCJleHAiOjE3MDM3NzQ2NjN9.JtiyC-Ocv1FIb1DRcdmL1J9gutkyhAVOhBWRu7nv7b0' \
--data '{
    "sender":"nasruddin11",
    "receiver":"munna",
    "text":"this is a test message"
}'
```
#### Response if he is logged in before and JWT Token is stored in cookie.
```bash
{
  Success
}
```
#### Response if user is not logged in
```bash
{
  Failure
}
```