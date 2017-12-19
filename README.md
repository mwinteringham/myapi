# Wirebridge
Wirebridge is a configurable API that helps teams wrap complex data creation takes around programmable HTTP requests. 

Currently, Wirebridge supports:
* MySQL
* Postgres

If you would like Wirebridge to support another database or activity then please raise [an issue](https://github.com/mwinteringham/wirebridge/issues/new) or check out details below on how to get setup with developing Wirebridge further.

## Using Wirebridge

Wirebridge is configured by JSON files to allow you to tie HTTP requests to database queries. To get started you will need to create two JSON files within a folder named ```mappings``` on the same level as the JAR.

### config.json

Config JSON takes a list of your different database types:

```json
{
  "databases" : [{
    "name" : "mysql",
    "host" : "127.0.0.1:3306",
    "database" : "talks",
    "username" : "root",
    "password" : "password",
    "driver" : "jdbc:mysql"
  },{
    "name" : "postgres",
    "host" : "127.0.0.1:5432",
    "database" : "talks",
    "username" : "postgres",
    "password" : "password",
    "driver" : "jdbc:postgresql"
  }]
}
```

You can provide as many different databases as you like as long as they each have a unique name. Naturally, you will need to ensure your database is up and running already and that you have the necessary credentials and host details.

### Request configs 

Once you have your ```config.json``` file ready, you can create your HTTP config files:

```json
{
  "request" : {
    "method" : "POST",
    "path" : "/talk",
    "parameters" : ["title"]
  },
  "sql" : {
    "database" : "mysql",
    "query" : "INSERT INTO talks (title) VALUES (${title});"
  }
}
```

The SQL object lets you set which uniquely named databases from ```config.json``` you would like to use followed by the query you want to execute against that database. 

Notice the ```${title}``` within the query. Wirebridge allows you to send data in your HTTP request that will then be mapped to your query. You set the key of your parameter in the request section and where you would like to use it in your query by entering ```${parameterName}```.   

The request object is responsible for setting out the details of the HTTP request you would like to call to execute your query. In this example, we are setting a POST endpoint that takes a urlencoded payload with the parameter ```title``` so we can use the value in our SQL query. This would result in an HTTP request like so:

```
POST /talk HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=A+talk+created+via+API
```

Once configured, simply send an HTTP request with your favourite HTTP library to execute the query. Wirebridge will then return the query results back in JSON format. This works for both inserting data and querying data.

## Development

To develop Wirebridge further you will need to go through a couple of initial steps:
1. Add the ApprovalTest jar file to your local maven repo using the following command in the root project folder:

```mvn install:install-file -Dfile=./lib/approvaltests.jar -DgroupId=org.approvaltests -DartifactId=approvaltests -Dversion=0.0.19 -Dpackaging=jar``` 

Approval test is responsible for the assertions in the automated checks as well as the reading of JSON files within Wirebridge.  
2. Test databases are managed by Docker. Therefore before running any checks, start up the test databases using ```docker-compose up```.