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

### Save curls output into a file
```bash
$ curl -o mygettext.html http://www.gnu.org/software/gettext/manual/gettext.html
```
This will save the server response to a file called mygettext.html in your current directory.

### Download a file in current folder
```bash
curl -O http://www.stockvault.net/data/2010/02/04/113039/small.jpg
```
This will save the file small.jpg to the directory you are in.

### Download multiple files
```bash
$ curl -0 http://www.domain.com/{one,two,three}.txt
```
This will save the server responses from the extensions http://www.domain.com/one, http://www.domain.com/two and http://www.domain.com/three

### Check in the dictionary for word
```bash
curl dict://dict.org/d:your_word
```
Some websites are configured to provide a server response from a curl request that looks good in your terminal. Trying copying the command above into the terminal and updating your_word to your favourite word for a definition from dict.org.

## Using cURL to test your API query

One useful way to use curls is to test your API query. Type curl, then -X, then the request type and the result will be returned in the terminal.
```bash
curl -X GET http://www.google.it
curl -X POST http://www.google.it
```
If you dont specify anything as in the first example curl defaults to GET

## TUTORIAL - make a post request on a dummy server

Let's try a POST request:

* Server that accepts post and put requests

http://posttestserver.com

* The endpoint to make a post request is

http://posttestserver.com/post.php

* Pass the variable dir to save in a specific folder. The beginning of the parameters is marked by a question mark.

http://posttestserver.com/post.php?dir=myfolderName

* To pass the values that you want to save, use option -d of curl. Try to write the curl command by your self. The solution is just few lines below:

```bash
curl -i -X POST http://www.posttestserver.com/post.php?dir=myDir -d '{"value":"my message"}'
```
Now you content is saved in the folder. To access it use the link provided in the response message

## TUTORIAL - hack facebook with curl

Lets update our facebook page using cURL with help from the Chrome Inspector.

* Open up facebook and enter a new status but don't press 'post'. Open up the chrome inspector and go to the 'Network' tab. Network keeps a log of all the calls to other files; XHR (the one we're interested in) but also JS and CSS files.

![About to post](about-to-post.png)

* Press the clear button (the one next to the red circle) which will remove all of the calls which have happened up to that point. Now press 'post' to post your status to your facebook wall.

![Update status](update-status.png)

* On the righthand side, a new call should have appeared called updatestatus.php

* If you tried to POST directly (like we did in our first tutorial), the request would fail because you do not have an access key. But we can use the Chrome Inspector to send a cURL request with the right permissions.

* Right-click on the XHR request in the Network tab in the Chrome Inspector and select 'Copy as cURL'. Delete your status update on facebook.

* Open your terminal and paste in the cURL request you have just copied to the clipboard. Hit enter in the terminal to send the API POST request and refresh your browser.

![Paste in cURL](paste-in-cURL.png)

* Congratulations, you just updated Facebook from the command line.

* If you're feeling bold, you can even change the message that you post by going to the part of the cURL request with the message tag and changing the message text.