## Seed code - Boilerplate for step 4 - Keep Note Assignment

### Assignment Step Description

In this Case study: Keep Note Step 4, we will create a RESTful application with Logger and we will add .gitlab-ci.yml file for CI.

Representational State Transfer (REST) is an architectural style that specifies constraints. 
In the REST architectural style, data and functionality are considered resources and are accessed using Uniform Resource Identifiers (URIs), typically links on the Web.

Resources are manipulated using a fixed set of four create, read, update, delete operations: PUT, GET, POST, and DELETE. 
 - POST creates a new resource, which can be then deleted by using DELETE. 
 - GET retrieves the current state of a resource in some representation. 
 - PUT transfers a new state onto a resource. 

### Problem Statement

In this case study, we will develop a RESTful application with which we will register a user, create a note and delete a note, add a note into the different category, set a reminder to the note. Also, we will perform authentication like login and log out. Check the performance of the operations with the help of Postman API.

### Solution Step

        Step 1: Configure Postman in your Google Chrome
        Step 2: Use URI's mentioned in the controller to check all the expected operations using Postman.

### Following are the broad tasks:

 - Create a new user, update the user, retrieve a single user, delete the user.
 - Login using userId and password, log out using userID.
 - Create a Note, update a note,  delete a note, get all notes of a specific userId.
 - Create a Category, update a Category,  delete a Category, get all Categories of a specific userId.
 - Create a Reminder, update a Reminder,  delete a Reminder, get all Reminders of a specific userId.
 - Add logger for all the controller methods.
 - Add .gitlab-ci.yml file for CI
 

### Steps to be followed:

    Step 1: Clone the boilerplate in a specific folder on your local machine and import the same in your eclipse STS.
    Step 2: Add relevant dependencies in pom.xml file. 
        Note: Read the comments mentioned in pom.xml file for identifying the relevant dependencies. 
    Step 3: Implement ApplicationContextConfig.java 
    Step 4: Specify Root config class in WebApplicationInitializer.java file.
    Step 5: Define the data model classes (User, Reminder, Note, Category)
    Step 6: Test each and every model with appropriate test cases.
    Step 7: See the methods mentioned in the DAO interface.
    Step 8: Implement all the methods of DAO interface in DaoImpl.
    Step 9: Test each and every DaoImpl with appropriate test cases.
    Step 10: See the methods mentioned in the service interface.
    Step 11: Implement all the methods of service interface in ServiceImpl.
    Step 12: Test each and every serviceImpl with appropriate test cases.
    Step 13: Write controllers to work with RESTful web services. 
    Step 14: Test each and every controller with appropriate test cases.
    Step 15: Go through logback.xml in resources. 
    Step 16: Write loggers for each of the methods of controller.
    Step 17: Test LoggingAspect with LoggerTest cases.
    Step 18: Check all the functionalities using URI's mentioned in the controllers with the help of Postman for final output.
    

### .gitlab-ci.yml
    Add valid .gitlab-ci.yml file in root directory for CI.
    


### Project structure

The folders and files you see in this repositories is how it is expected to be in projects, which are submitted for automated evaluation by Hobbes

    Project
    ├─src/main/java
            |
            ├── com.stackroute.keepnote.aspect 
            |        └── LoggingAspect.java  		   // This class will contain logger for all the controller methods.
            ├── com.stackroute.keepnote.config               
            |        └── ApplicationContextConfig.java         // This class will contain the application-context for the application.
            |        └── WebApplicationInitializer.java        // This class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer class.
            ├── com.stackroute.keepnote.controller
            |        └── CategoryController.java           // This class is responsible for processing all requests related to Category and builds an appropriate model and passes it to the view for rendering.
            |        └── NoteController.java               // This class is responsible for processing all requests related to Note and builds an appropriate model and passes it to the view for rendering.
            |        └── ReminderController.java           // This class is responsible for processing all requests related to Reminder and builds an appropriate model and passes it to the view for rendering.
            |        └── UserAuthenticationController.java // This class is responsible for processing all requests related to UserAuthentication and builds an appropriate model and passes it to the view for rendering.
            |        └── UserController.java               // This class is responsible for processing all requests related to User and builds an appropriate model and passes it to the view for rendering.
            ├── com.stackroute.keepnote.dao
            |        └── CategoryDAO.java                  // This interface contains all the behaviors of Category Model
            |        └── NoteDAO.java                      // This interface contains all the behaviors of Note Model    
            |        └── ReminderDAO.java                  // This interface contains all the behaviors of Reminder Model
            |        └── UserDAO.java                      // This interface contains all the behaviors of User Model
            |        └── CategoryDAOImpl.java              // This class implements the CategoryDAO interface. This class has to be annotated with @Repository annotation.
            |        └── NoteDAOImpl.java                  // This class implements the NoteDAO interface. This class has to be annotated with @Repository annotation.
            |        └── ReminderDAOImpl.java              // This class implements the ReminderDAO interface. This class has to be annotated with @Repository annotation.
            |        └── UserDAOImpl.java                  // This class implements the UserDAO interface. This class has to be annotated with @Repository annotation.
            ├── com.stackroute.keepnote.exception
            |        └──CategoryNotFoundException.java     // This class extends Exception and used for a custom exception. 
            |        └──NoteNotFoundException.java         // This class extends Exception and used for a custom exception. 
            |        └──ReminderNotFoundException.java     // This class extends Exception and used for a custom exception. 
            |        └──UserAlreadyExistException.java     // This class extends Exception and used for a custom exception. 
            |        └──UserNotFoundException.java         // This class extends Exception and used for a custom exception. 
            ├── com.stackroute.keepnote.model
            |        └── Category.java                     // This class will be acting as the data model for the Category Table in the database.
            |        └── Note.java                         // This class will be acting as the data model for the Note Table in the database.
            |        └── Reminder.java                     // This class will be acting as the data model for the Reminder Table in the database.
            |        └── User.java                         // This class will be acting as the data model for the User Table in the database.
            ├── com.stackroute.keepnote.service
            |        └──CategoryService             // This interface contains all the behaviors of Category Model
            |        └──CategoryServiceImpl             // This class implements the CategoryService interface. This class has to be annotated with @Service annotation.
            |        └──NoteService                // This interface contains all the behaviors of Note Model
            |        └──NoteServiceImpl            // This class implements the NoteService interface. This class has to be annotated with @Service annotation.
            |        └──ReminderService            // This interface contains all the behaviors of Reminder Model
            |        └──ReminderServiceImpl            // This class implements the ReminderService interface. This class has to be annotated with @Service annotation.
            |        └──UserService                // This interface contains all the behaviors of User Model
            |        └──UserServiceImpl            // This class implements the UserService interface. This class has to be annotated with @Service annotation.
    ├─src/main/resources
            |        └── logback.xml
    ├─src/test/java                            // All the test case classes are made available here
            ├── com.stackroute.keepnote.test.controller                  
            |        └── CategoryControllerTest.java
            |        └── NoteControllerTest.java
            |        └── ReminderControllerTest.java
            |        └── UserAuthenticationControllerTest.java
            |        └── UserControllerTest.java    
            ├── com.stackroute.keepnote.test.dao
            |        └── CategoryDAOImplTest
            |        └── NoteDAOImplTest
            |        └── ReminderDAOImplTest
            |        └── UserDAOImplTest
            ├── com.stackroute.keepnote.test.log
            |        └── LoggerTest.java
            ├── com.stackroute.keepnote.test.model
            |        └── CategoryTest.java
            |        └── NoteTest.java
            |        └── ReminderTest.java
            |        └── UserTest.java
            ├── com.stackroute.keepnote.test.service
            |        └── CategoryServiceImplTest.java
            |        └── NoteServiceImplTest.java
            |        └── ReminderServiceImplTest.java
            |        └── UserServiceImplTest.java
            ├── .classpath                                    // This file is generated automatically while creating the project in eclipse
            ├── .gitlab-ci.yml                                // CI specific config (participants need to add this file in root directory for CI)
            ├── .hobbes                                       // Hobbes specific config options, such as type of evaluation schema, type of tech stack etc., Have saved a default values for convenience
            ├── .project                                    // This is automatically generated by eclipse, if this file is removed your eclipse will not recognize this as your eclipse project. 
            ├── pom.xml                                     // This is a default file generated by maven, if this file is removed your project will not get recognized in hobbes.
            └── PROBLEM.md                                  // This files describes the problem of the assignment/project, you can provide as much as information and clarification you want about the project in this file

> PS: All lint rule files are by default copied during the evaluation process, however, if need to be customized, you should copy from this repo and modify in your project repo


#### To use this as a boilerplate for your new project, you can follow these steps

1. Clone the base boilerplate in the folder **assignment-solution-step3** of your local machine
     
    `git clone https://gitlab-cts.stackroute.in/stack_java_keep/KeepNote-Step4-Boilerplate.git assignment-solution-step4`

2. Navigate to assignment-solution-step3 folder

    `cd assignment-solution-step4`

3. Remove its remote or original reference

     `git remote rm origin`

4. Create a new repo in gitlab named `assignment-solution-step4` as private repo

5. Add your new repository reference as remote

     `git remote add origin https://gitlab.cts.com/{{yourUserName}}/assignment-solution-step4.git`

     **Note: {{yourUserName}} should be replaced by your userName from gitlab**

5. Check the status of your repo 
     
     `git status`

6. Use the following command to update the index using the current content found in the working tree, to prepare the content staged for the next commit.

     `git add .`
 
7. Commit and Push the project to git

     `git commit -a -m "Initial commit | or place your comments according to your need"`

     `git push -u origin master`

8. Check on the git repo online, if the files have been pushed

### Important instructions for Participants
> - We expect you to write the assignment on your own by following through the guidelines, learning plan, and the practice exercises
> - The code must not be plagirized, the mentors will randomly pick the submissions and may ask you to explain the solution
> - The code must be properly indented, code structure maintained as per the boilerplate and properly commented
> - Follow through the problem statement shared with you

### MENTORS TO BEGIN REVIEW YOUR WORK ONLY AFTER ->
> - You add the respective Mentor as a Reporter/Master into your Assignment Repository
> - You have checked your Assignment on the Automated Evaluation Tool - Hobbes (Check for necessary steps in your Boilerplate - README.md file. ) and got the required score - Check with your mentor about the Score you must achieve before it is accepted for Manual Submission.
> - Intimate your Mentor on Slack and/or Send an Email to learner.support@stackroute.in - with your Git URL - Once you done working and is ready for final submission.


### Further Instructions on Release

*** Release 0.1.0 ***

- Right click on the Assignment select Run As -> Java Application to run your Assignment.
- Right click on the Assignment select Run As -> JUnit Test to run your Assignment.