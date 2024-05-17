# nanda_betest

# REST API URL
https://nanda-betest.onrender.com

#Mongo Access
mongodb+srv://nanda_betest:Uwm3BkFRhqpQTLfe@mongo-nanda.ft9zi9y.mongodb.net/db_nanda_betest

#Redis Access
url : redis-18822.c302.asia-northeast1-1.gce.redns.redis-cloud.com
port : 18822
pass : JnWKbZpEs1QmLAmOfKNJLjT2VklbSdR0

#JSON of Data

1. Create User

Method : POST
https://nanda-betest.onrender.com/account/insert

{
    "accountId" : "test2",
    "userName" : "nanda2",
    "password" : "admin",
    "userId" : "111112",
    "fullName" : "nanda dua",
    "accountNumber" : "112",
    "emailAddress" : "nandac2@mail.com",
    "registrationNumber" : "0112"
}

2. Login
https://nanda-betest.onrender.com/account/login

{
    "username" : "nandac",
    "password" : "admin"
}

3. Get UserInfo by Account Number

Method GET
https://nanda-betest.onrender.com/account/accountNumber/123

Headers : Authorization = Bearer Token
Note : Get token from login Response

4. Get UserInfo by Registration Number

Method : GET
https://nanda-betest.onrender.com/account/registrationNumber/0112

Headers : Authorization = Bearer Token
Note : Get token from login Response

5. Get Account Login by lastLoginDateTime > 3 days

Method : GET
https://nanda-betest.onrender.com/account/lastLogin

Headers : Authorization = Bearer Token
Note : Get token from login Response