- [QSS2023](#qss2023)
- [SchoolTimetable](#schooltimetable)
  - [Objective](#objective)
    - [Phase 1 - Application Development](#phase-1---application-development)
    - [Phase 2 - Unit Testing](#phase-2---unit-testing)
    - [Phase 3 - Use of Assertions](#phase-3---use-of-assertions)
    - [Phase 4 - Documentation](#phase-4---documentation)


# QSS2023
Quality of Software Systems

# SchoolTimetable

[![Java CI](https://github.com/gheorghitamutu/QSS2023/actions/workflows/project.yaml/badge.svg)](https://github.com/gheorghitamutu/QSS2023/actions/workflows/project.yaml)
![Coverage Branches](.github/badges/branches.svg)
![Coverage Overall](.github/badges/jacoco.svg)


## Objective

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

