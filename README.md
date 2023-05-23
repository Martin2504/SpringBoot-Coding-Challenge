# Martin Petrov SpringBoot Coding Challenge README

## Installation
1. Make sure you have Java 17 on your computer
2. Load the project in your favorite Java IDE (preferably IntelliJ Idea Ultimate
3. build and run the project via the IDE or via maven

## Usage and Testing

### Unit Testing
You may run the entire unit testing suite, in the `com/example/claytoncodingassessment` package
with your IDE. 

### Project Context:
* Task had one-to-one relationship with taskReport.

* To create a TaskExecutionReport a Task has to exist. One task can have max one report.

* To create a TaskStepExecutionReport a taskReport must exist.

### Recommended tool for User Testing - Insomnia:
You can test functionality through Insomnia, the _Insomnia HTTP requests collection_ is 
included in the root folder of the project.
Alternatively other programs like _Postman_ are also a viable option.

### Recommended steps in Insomnia:
1. Run the Create Task HTTP request to create a task. 
2. Run the Create TaskReport request
3. Run the Create a StepReport request
4. You may run the remaining reports as you see fit for testing purposes.

## Documentation

### Javadoc
Javadoc is written for every method in the services of the API. I consider a good rule of thumb to add javadoc 
only to services methods, as if the application design is good, no method, worth documenting in any class should 
exist without being declared in a service. 
### CRUD operations endpoints:
For the CRUD operation endpoints documentation, please start the application and 
go to the following URL: http://localhost:8080/swagger-ui/index.html#/

