# 3LineServices

Multimodule repository consisting of 3 microservices:

-User Service

-Transaction Service

-Notification Service

The user Service is preloaded with admin and subadmin roles, takes in customers and content creators through the registration api:

url: localhost:8080/api/v1/user

method: Post

data params: {
"firstName":"",
"lastName":"",
"email":"",
"password":"",
"pin":""
}

url: localhost:8080/api/v1/user/create_content_creator

method: Post

data params: {
"firstName":"",
"lastName":"",
"email":"",
"password":"",
"pin":""
}
Email is published to the rabbitMQ upon registration

The admin has authority to create a content creator.

An authenticated content creator can create content.

url:localhost:8080/api/v1/content

method: Post

data params:
{
"title":"",
"body":"",
"price":""
}

An authenticated user can fund account and buy content.

url: localhost:8082/api/v1/transaction/fund

method: Post

data params: {
"amount":"",
"accountNum":"4906027518",
"pin":"1234"
}
url: localhost:8082/api/v1/transaction/buy

method:Post

data params: {"title":"",
"accountNumber":"",
"pin":""}

User can see content bought

url: localhost:8080/api/v1/content/catalogue

method: Get
