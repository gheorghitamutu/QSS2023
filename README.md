- [QSS2023](#qss2023)
- [SchoolTimetable](#schooltimetable)
  - [Project Requirements](#project-requirements)
    - [Description](#description)
    - [Phase 1 - Application Development](#phase-1---application-development)
    - [Phase 2 - Unit Testing](#phase-2---unit-testing)
    - [Phase 3 - Use of Assertions](#phase-3---use-of-assertions)
    - [Phase 4 - Documentation](#phase-4---documentation)
  - [Design \& Development (Phase 1)](#design--development-phase-1)
    - [Use Cases (based on the project requirements)](#use-cases-based-on-the-project-requirements)
    - [Programming language (Java)](#programming-language-java)
    - [Database interaction (the choice of an ORM)](#database-interaction-the-choice-of-an-orm)
    - [Database type (Apache Derby - embedded SQL)](#database-type-apache-derby---embedded-sql)
    - [GUI Framework (TODO: Roberta)](#gui-framework-todo-roberta)
    - [HTML Generator (TODO: Radu)](#html-generator-todo-radu)
    - [Architecture (separation of concerns \& modularization)](#architecture-separation-of-concerns--modularization)
    - [Putting all together (writing models in Java for Hibernate \& Apache Derby)](#putting-all-together-writing-models-in-java-for-hibernate--apache-derby)
  - [Unit Testing - design \& implementation (Phase 2: libraries \& validators)](#unit-testing---design--implementation-phase-2-libraries--validators)


# QSS2023
Quality of Software Systems

# SchoolTimetable

[![Java CI](https://github.com/gheorghitamutu/QSS2023/actions/workflows/project.yaml/badge.svg)](https://github.com/gheorghitamutu/QSS2023/actions/workflows/project.yaml)
![Coverage Branches](.github/badges/branches.svg)
![Coverage Overall](.github/badges/jacoco.svg)


## Project Requirements

### Description

The goal of this project is to develop an application that assists users in creating and managing the timetable of a faculty. The application will provide support to users by offering a graphical interface that simplifies the manipulation of various entities involved in the timetable creation process.

### Phase 1 - Application Development

The program should support users in creating the timetable without actually generating it.
Consider the following elements:

- Students, organized by study years and groups
- Teachers
- Disciplines
- Class types (course/seminar/laboratory)
- Rooms
- Time slots
- There are certain restrictions to be applied:


Classes should only be scheduled on weekdays (Monday to Friday) between 8 AM and 8 PM.

Course classes should be scheduled only in course rooms due to the large number of students, while laboratory classes should be scheduled in laboratory rooms. There are no restrictions for seminar classes.

Course classes are taught to entire study years, while seminar/laboratory classes are taught to specific groups.
The program should assist the user by:

Providing a graphical interface (web-based or otherwise) that simplifies the manipulation of the entities involved.
Notifying the user of any issues such as broken restrictions, overlapping classes (e.g., two classes scheduled simultaneously in the same room or two classes taught by the same teacher at the same time), etc.

Generating HTML files for publishing the timetable, similar to the current faculty timetable.

Implementation should not rely on library functions, meaning the code must be written by the programmers. However, if a database server is used, it does not have to be implemented by the programmers.

Continuous communication with the beneficiary is necessary, allowing for clarification of requirements. Failure to understand the requirements and deliver a non-functional program will result in penalties.

Any programming language can be used, as long as it supports unit testing and mocking tools, as well as assertions specific to the language. These will be required in subsequent phases.

It is recommended to keep the program structure as simple as possible, without incorporating additional features beyond those mentioned above. The main goal is to create a working version of the program, even if it is not fully stable or error-free, to facilitate subsequent testing techniques.


### Phase 2 - Unit Testing

Requirements:

- Use unit testing tools to test the code developed during Phase 1.

Note that unit testing aims to determine if the tested module can handle incorrect input data from other modules or application I/O. Fixing errors is not required at this stage.

Strive for comprehensive code coverage during testing. For guidance on the conditions to be tested, refer to the provided courses.

Each module should be tested independently, and mocking will be used where necessary to simulate interactions with other modules.

### Phase 3 - Use of Assertions

Requirements:

- Insert assertions into the application code to verify the preconditions, postconditions, and invariants of the operations implemented in Phase 1. These assertions should be directly inserted into the application code and are separate from unit testing.
- If the programming language used lacks built-in assertions, a function or method must be written to achieve similar functionality.

### Phase 4 - Documentation

Requirements:

- Write documentation for Phases 1-3.
- There are no specific size requirements for the documentation, but it should clearly describe all the work done, including program design and implementation, a concise user manual, testing, and the use of assertions.
- Explicitly state the contributions of each team member.

## Design & Development (Phase 1)

### Use Cases (based on the project requirements)

TODO: Roberta & Radu

### Programming language (Java)
The following arguments made Java a suitable choice for developing an application to assist in creating and managing a faculty timetable:

1. Platform Independence: Java is a platform-independent language, which means that the application developed in Java can run on different operating systems without requiring significant modifications. This makes it easier to deploy the application on various platforms commonly used in educational institutions.

2. Rich Ecosystem and Libraries: Java has a vast ecosystem with a wide range of libraries and frameworks that can facilitate the development process. There are numerous libraries available for building graphical user interfaces (GUIs), handling web-based interfaces, managing databases, and implementing unit testing, which aligns with the project requirements.

3. Object-Oriented Programming (OOP) Paradigm: Java follows the OOP paradigm, which promotes modular and organized code structure. With the entities involved in the faculty timetable management system, such as students, teachers, disciplines, and rooms, an object-oriented approach can provide a clear and structured representation of these entities and their relationships, making it easier to manage and manipulate the data.

4. Robustness and Reliability: Java is known for its robustness and reliability. It has a strong type system, extensive error handling mechanisms, and automatic memory management through garbage collection. These features contribute to the stability and error-free operation of the application, which is essential for a project that involves managing critical information like a faculty timetable.

5. Community Support and Documentation: Java has a large and active community of developers, which means that there are ample resources available for learning, troubleshooting, and seeking assistance. The availability of comprehensive documentation, tutorials, and online forums can be beneficial for the development team, especially when encountering challenges or seeking best practices.

6. Integration Capabilities: Java offers excellent support for integrating with various systems and technologies. This can be advantageous when integrating the application with existing systems used by the faculty, such as databases, authentication systems, or other internal tools.

### Database interaction (the choice of an ORM)
Hibernate is a widely used Object-Relational Mapping (ORM) framework in the Java ecosystem, and it can provide several benefits for the project of creating and managing a faculty timetable. Here are some reasons why Hibernate might be a suitable choice:

1. Simplified Database Access: Hibernate simplifies database access by abstracting the low-level JDBC (Java Database Connectivity) code. It provides a high-level object-oriented approach to interact with the database, allowing developers to work with Java objects rather than writing complex SQL queries manually. This abstraction can significantly reduce the amount of boilerplate code required for database operations.

2. Object-Relational Mapping: The project involves managing various entities, such as students, teachers, disciplines, and rooms. Hibernate's core functionality lies in mapping Java objects to database tables, providing a seamless integration between the application's object model and the relational database schema. It simplifies the process of storing and retrieving objects from the database, handling relationships between entities, and managing database transactions.

3. Automatic Query Generation: Hibernate provides a Query Language called HQL (Hibernate Query Language), which is similar to SQL but operates on objects and their properties rather than database tables. It allows developers to write high-level, object-oriented queries, which Hibernate translates into appropriate SQL statements. This automatic query generation can save development time and reduce the chances of writing incorrect or inefficient SQL queries manually.

4. Caching and Performance Optimization: Hibernate incorporates various caching mechanisms to improve performance. It offers a first-level cache (session cache) and a second-level cache (optional shared cache), which can reduce the number of database round-trips and enhance application responsiveness. Hibernate also provides optimization techniques like lazy loading and batch fetching to minimize unnecessary database queries.

5. Transaction Management: Hibernate integrates well with transaction management frameworks like Java Transaction API (JTA) and Java Persistence API (JPA). It supports declarative transaction demarcation, ensuring ACID (Atomicity, Consistency, Isolation, Durability) properties for database operations. This simplifies the management of database transactions and helps maintain data integrity.

6. Portability and Database Independence: Hibernate abstracts the underlying database-specific details, allowing developers to write database-agnostic code. It provides a consistent API that works across different database systems, reducing the effort required to switch databases or adapt the application to different environments. This portability can be beneficial if there is a need to support multiple database platforms or if the database technology used by the faculty changes in the future.

### Database type (Apache Derby - embedded SQL)

We have chosen Apache Derby as the database system to be used in conjunction with Hibernate because of the following:

1. Lightweight and Embedded Database: Apache Derby is a lightweight, open-source relational database management system (RDBMS) that can be embedded within the application itself. This means that the database can run within the same Java process as the application, eliminating the need for a separate database server installation and configuration. This simplicity of deployment can be advantageous for small-scale applications or scenarios where the database needs to be self-contained.

2. Java-Based: Apache Derby is implemented in Java and fully supports the Java Database Connectivity (JDBC) API. Since the project is being developed in Java, using Apache Derby ensures a seamless integration between the application and the database. It allows for native communication between Hibernate and Apache Derby, making it straightforward to leverage Hibernate's ORM capabilities.

3. Ease of Setup and Configuration: Apache Derby is known for its ease of setup and configuration. It provides a simple installation process, and the database can be started with minimal configuration effort. This can save time and reduce the complexity of the development environment setup.

4. Portability: Apache Derby is designed to be highly portable, compatible with various operating systems and Java platforms. This portability allows the application to be deployed on different environments without significant modifications, providing flexibility for potential deployment scenarios.

5. Good Performance: Apache Derby offers decent performance for small to medium-sized datasets. As an embedded database, it operates within the same Java process as the application, resulting in reduced network latency and improved data access speed. While it may not be as performant as larger-scale database systems in certain scenarios, it can still provide satisfactory performance for a faculty timetable management application.

6. Seamless Integration with Hibernate: Apache Derby has excellent compatibility with Hibernate. Hibernate provides a comprehensive set of features for mapping Java objects to Apache Derby tables, generating SQL queries, and managing transactions. By combining Apache Derby with Hibernate, developers can leverage the power of Hibernate's ORM capabilities and take advantage of its robust features, such as object-relational mapping, caching, and query optimization.

### GUI Framework (TODO: Roberta)

### HTML Generator (TODO: Radu)

### Architecture (separation of concerns & modularization)

TODO: Cosmin (placeholder below for now)

The architecture of this project is designed using a layered architecture pattern, with several distinct layers responsible for different functionalities. Here's an overview of the proposed architecture:

**Presentation Layer**: This layer handles the user interface and interaction. It includes graphical interfaces (GUI) that allows users to interact with the application. The presentation layer communicates with the business layer to retrieve and display data, as well as to capture user input.

**Business Layer**: The business layer contains the core logic of the application. It is responsible for processing and manipulating the data, enforcing business rules, and coordinating interactions between different components. In this project, the business layer would handle tasks such as managing the timetable entities, validating inputs, applying restrictions, and generating notifications for issues or conflicts.

**Persistence Layer**: The persistence layer is responsible for interacting with the database and handling data storage and retrieval. It utilizes Hibernate as the Object-Relational Mapping (ORM) framework to map Java objects to the underlying Apache Derby database. This layer handles tasks like persisting entity objects, executing database queries, and managing transactions.

**Domain/Model Layer**: The domain or model layer represents the core entities and data structures of the application. It encapsulates the business logic and defines the relationships between different entities, such as students, teachers, disciplines, and rooms. This layer provides the foundation for the application's functionality and serves as the bridge between the business and persistence layers.

**Utilities/Supporting Components**: Apart from the main layers, there are additional supporting components and utilities. We have logging modules for capturing and logging application events, exception handling components for managing errors and exceptions, and configuration components for managing application settings.

The proposed architecture promotes separation of concerns and modularization, enabling easier maintenance, testing, and scalability. It allows for the independent development and testing of different layers, promoting code reusability and flexibility. Additionally, the use of Hibernate with Apache Derby provides a seamless integration between the business and persistence layers, simplifying database operations and ensuring data consistency.

### Putting all together (writing models in Java for Hibernate & Apache Derby)

Writing classes for Hibernate and Apache Derby using Java is relatively straightforward and follows standard object-oriented programming principles. Here's an overview of the process:

1. Entity Classes: To map Java objects to database tables, you'll need to create entity classes. These classes represent the entities in your application's domain model, such as students, teachers, disciplines, and rooms. Each entity class typically corresponds to a table in the database.

2. Annotations or XML Mapping: Hibernate supports two approaches for mapping entity classes to database tables: annotations and XML mapping files. Annotations provide a more concise and intuitive way to define mappings directly within the entity classes using annotations like @Entity, @Table, @Column, and others. Alternatively, XML mapping files can be used to define the mappings externally.

3. Define Relationships: If your entities have relationships, such as one-to-many, many-to-one, or many-to-many, you'll need to establish these relationships in your entity classes using Hibernate annotations or XML mappings. This involves defining fields or properties representing the relationships and annotating them appropriately.

4. Configuration: Hibernate requires configuration to connect to the database and specify other settings. You'll need to create a Hibernate configuration file (usually named hibernate.cfg.xml) that provides details like the database connection URL, driver class, username, password, and other configuration options.

5. Session Factory: The Session Factory is a central component in Hibernate that manages database connections and provides sessions for performing database operations. You'll need to create and configure a Session Factory instance using the Hibernate configuration.

6. CRUD Operations: With the entity classes and Hibernate set up, you can perform Create, Read, Update, and Delete (CRUD) operations using Hibernate APIs. Hibernate provides methods like save, get, update, and delete to interact with the database. These operations allow you to persist, retrieve, update, and delete entities.

7. Transactions: Hibernate supports transaction management for ensuring data integrity and consistency. You can use Java Transaction API (JTA) or Hibernate's built-in transaction management mechanisms to demarcate transaction boundaries and perform atomic operations.

8. Querying: Hibernate provides various options for querying the database. You can use Hibernate Query Language (HQL), a powerful object-oriented query language similar to SQL, or Criteria API, which allows you to build queries programmatically using a type-safe API.

9. Testing and Integration: It's crucial to test your classes and database interactions. You can write unit tests using frameworks like JUnit to verify the correctness of your Hibernate and Apache Derby code. Integration testing can be performed by running the application against a test database to ensure seamless interaction between Hibernate and Apache Derby.
    
TODO: Roberta & Radu

Presentation of GUI (technical) & HTML table generator (technical)

## Unit Testing - design & implementation (Phase 2: libraries & validators)

WIP

....
