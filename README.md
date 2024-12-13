# Restaurant Reservation Service API

This is a Spring Boot-based RESTful API designed for managing restaurant reservations. It allows customers to create, update, view, and cancel their reservations, as well as manage their profiles. The API provides functionality for customer management, reservation management, and sending notifications.

## Table of Contents

[//]: # (- [API Endpoints]&#40;#api-endpoints&#41;)

[//]: # (    - [Reservation Endpoints]&#40;#reservation-endpoints&#41;)

[//]: # (    - [Customer Endpoints]&#40;#customer-endpoints&#41;)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Building Javadoc](#building-javadoc)

[//]: # (## API Endpoints)

[//]: # ()
[//]: # (### Reservation Endpoints)

[//]: # (These are the available endpoints to manage reservations in the system.)

[//]: # ()

[//]: # (### Customer Endpoints)

## Running the Application
1. **Clone the Repository**  
   First, clone the project repository to your local machine:
   #### `git clone https://github.com/airissejoygonzales/restaurant-reservation-service.git`

2. **Navigate to Project Directory**  
   Change into the project directory:
   #### `cd restaurant-reservation-service`

3. **Build the Application**  
   Use Gradle to build the application. If you don’t have Gradle installed, you can use the Gradle wrapper included with the project:
   #### `./gradlew build`

4. **Run the Application**  
   After building the application, run it using Gradle:
   #### `./gradlew bootRun`
   The application will start running on [http://localhost:8080](http://localhost:8080). You can access the REST API at this address.

## Testing

To run the unit tests for the application, use Gradle. This will execute all tests and provide feedback about the correctness of the application.

1. **Run Unit Tests**  
   Use the following command to run the unit tests:
   #### `./gradlew test`
   This command will run all the unit tests present in the project and display the results in the terminal or command prompt.

2. **View Test Results**  
   After running the tests, you will see the test results in the console. If any test fails, the output will indicate the test that failed along with the reason for the failure.

3. **Test Report**  
   For more detailed information on the test results, you can view the test report generated by Gradle. By default, the test report is saved in the `build/reports/tests/test/index.html` file. Open this file in your browser to see a comprehensive breakdown of each test and its results.

   To view the test report, open the following in your browser:
   ```
   build/reports/tests/test/index.html
   ```


## Javadoc

You can generate the Javadoc documentation for the project using Gradle. This will create an HTML representation of the code documentation, which is useful for developers to understand the functionality of the classes, methods, and their parameters.

### Steps to Build Javadoc:

1. **Generate Javadoc**  
   To generate the Javadoc for this project, use the following Gradle command:
   #### `./gradlew javadoc`
   This command will process the Javadoc comments in the source code and generate documentation.

2. **Find the Javadoc Output**  
   After the command completes, the generated Javadoc will be available in the `build/docs/javadoc` directory.

3. **View the Javadoc**  
   Open the `index.html` file located in the `build/docs/javadoc` directory in your browser to view the Javadoc documentation. You can navigate through the API classes, methods, and other details here.

   To view the generated Javadoc, open:
   ```
   build/docs/javadoc/index.html
   ```
