# E-commerce Project Using Spring Boot, MyBatis & PostGres
- JDBC
- Postgres
- MyBatis
- Spring Boot
- Flyway 

## Contains the following REST APIs:
- Insert Order: Endpoint to insert a new order and its details.
- Update Order: Endpoint to update an existing order and its details.
- Delete Order: Endpoint that deletes an order and its associated details.
- Query Order: Endpoint that retrieves order within a specified data range (start_date and end_date), returning only the order information and the count of related order_detail records.
- Query Order Detail: Endpoint to fetch detailed information about a specific order, including all its details from the order_detail table.

## Tables
- order_table
  - contains columns:
      - id
      - total_amount
      - total_discount_amount
      - created_at
      - updated_at

- order_detail_table
  - contains columns:
      - id
      - order_id
      - product_name
      - product_quantity
      - product_price
      - product_discount
      - created_at
      - updated_at


## Set up/installation:

1. Clone this project locally using this command
      - git clone `https://github.com/wwon0039/e-commerce-project.git`

2. Install Postgres via Docker. click [this link](https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/) for further info on how to install it

3. Open the project, go into the terminal and call this command, which is used to start docker's postgres
```
$ docker-compose build (used to build or rebuild images in the docker-compose. yml file. This file contains all the necessary configurations for all the services that make up the application)
$ docker-compose up (used to start postgres in docker)
```
4. Navigate to src/main/java and click on `ECommerceApplication.java` file and run it (this file is run to enable REST API)

5. Enjoy!

Note: Other Important commands 
```
$ docker ps (used to check if your current local postgres is running in docker)
$ docker compose down (stop services running to docker)

// For interacting with postgres thru terminal
$ docker exec -it postgres psql -U myuser -d mydatabase (used to access the local postgres DB by passing username and local DB name)
# \dt (check for current tables in the DB)
# \c {DB_NAME} (used to connect to a DB)
```

## Execution Instructions: (all this can be done on Postman)
- Insert Order
   1. add a GET request with url http://localhost:8080/orders/add 
   2. add the JSON structure shown below
   3. should return 200 response with text `created order count: 1, created order details count: 2`

Example JSON structure:
```
    {
      "total_amount": 20,
      "total_discount_amount": 5,
      "detail": [
          {
              "order_id": 3,
              "product_name": "Pencil",
              "product_price": 5,
              "product_quantity": 2,
              "product_discount": 1
          },
          {
              "order_id": 3,
              "product_name": "Chocolate",
              "product_price": 10,
              "product_quantity": 1,
              "product_discount": 3
          }
      ],
      "created_at": "2024-05-02",
      "updated_at": "2024-05-02"
  }
```



- Update Order
   1. add a PATCH request with url http://localhost:8080/orders/update/{id}
   2. replace {id} in url with the id of the order you wish to update
   3. add the JSON structure shown below
   4. should return an empty 200 response

Example JSON structure:
```
{
    "total_amount": 22,
    "total_discount_amount": 22,
    "detail": [
        {
            "id": 4,
            "product_name": "Pencil22",
            "product_price": 22,
            "product_quantity": 22,
            "product_discount": 22
        },
        {
            "id": 5,
            "product_name": "Chocolate22",
            "product_price": 22,
            "product_quantity": 22,
            "product_discount": 22
        }
    ]
}
```


  
- Delete Order
   1. add a GET request with url http://localhost:8080/orders/delete/{id}
   2. replace {id} in url with the id of the order you wish to delete
   3. should return an empty 200 response


  
- Query Order
   1. add a DELETE request with url http://localhost:8080/orders/query
   2. add the request JSON structure shown below
   3. should return an 200 response with a response JSON

Example request JSON structure:
```
{
    "start_date": "2024-05-06",
    "end_date": "2024-05-07"
}
```

Example response JSON structure:
```
{
    "total_amount": 22.0,
    "total_discount_amount": 22.0,
    "id": 3,
    "order_detail_count": 2
}
```


- Query Order Detail
   1. add a GET request with url http://localhost:8080/orders/detail/{id}
   2. replace {id} in url with the id of the order you wish to retrieve
   3. add the JSON structure shown below
   4. should return an 200 response with a response JSON
  
Example response JSON structure:
```
{
    "total_amount": 22.0,
    "total_discount_amount": 22.0,
    "id": 3,
    "detail": [
        {
            "product_discount": 22.0,
            "id": 4,
            "product_price": 22.0,
            "order_id": 3,
            "product_name": "Pencil22",
            "product_quantity": 22.0
        },
        {
            "product_discount": 22.0,
            "id": 5,
            "product_price": 22.0,
            "order_id": 3,
            "product_name": "Chocolate22",
            "product_quantity": 22.0
        }
    ]
}
```
