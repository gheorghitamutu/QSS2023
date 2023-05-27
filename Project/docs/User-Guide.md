# User Guide Manual - Timetable Generator

## Introduction

This manual will provide you with detailed instructions on how to use the Timetable Generator GUI application effectively. The Timetable Generator allows you to manage student, teacher, room, session, discipline and timeslot information, and generate timetables based on your requirements. Whether you are a teacher, administrator, or student, this guide will help you navigate through the various features and functionalities of the application.

## Getting Started

Let's familiarize ourselves with the main components and functionalities of the Timetable Generator GUI:

### GUI Interface

The GUI (Graphical User Interface) provides an intuitive and user-friendly environment for interacting with the Timetable Generator. It consists of a  vertical menu, buttons, and panels that allow you to perform different operations and access various sections of the application.

### Main Menu

The main menu acts as the primary navigation hub of the application. It contains options for managing student records, teacher information, room availability, session details, and timeslots. 
Each menu option leads to a specific section where you can perform related tasks.
![Main Menu](/docs/reportImages/mainMenu.png)
### Database Operations

The Timetable Generator allows you to perform various database operations to manage the core entities of the application. These operations include inserting new records and deleting existing records for students, teachers, rooms, sessions, and timeslots.

## Using the Timetable Generator

Let's explore the key functionalities of the Timetable Generator in more detail:

### Student Management

The Student Management section allows you to add and delete student records. You can enter details such as student names, registration ID, year of study, and other relevant information. This section helps you maintain accurate and up-to-date student data.
![Student Section](/docs/reportImages/studentPanel.png)
#### Add Student Operation:

1. Complete the corresponding information: student name, registration ID (unique constraint), year of study,group of study.
2. Click on "SUMBIT" button under the first bulk of fields.

Once the user has properly entered the data, a connection is established to the backend. A call is made to the appropriate service for inserting a student into the database, using the information collected on the frontend. At the implementation level, validation checks are performed on the entered data, and the actual operation is carried out.
#### Delete Student Operation:
1. The user has to introduce the corresponding registration ID for a student.
2. Click on "SUBMIT" button under the second bulk of fields.

Once the user initiates the delete student operation, the system establishes a connection to the backend. It then invokes the corresponding service responsible for deleting a student from the database. The service uses the provided registration ID to locate the student's record.

During the implementation of the delete operation, the system performs necessary checks and validations to ensure the correctness and integrity of the data. Once the student is successfully located and identified by the registration ID, the actual deletion operation is performed, removing the student's record from the database.
### Teacher Management

The Teacher Management section enables you to manage teacher information. You can add new teachers, remove existing one. This section is particularly useful for maintaining a comprehensive record of teachers within the institution.
#### Add Teacher Operation:
1. Complete the corresponding information: teacher name, role of the teacher in the current institution.
2. Click on "SUMBIT" button under the first bulk of fields.

To add a teacher, the user enters the teacher's name and selects their role.The system utilizes the provided data from the user interface and invokes the appropriate service responsible for handling teacher insertion. The service performs necessary validations to ensure the accuracy and completeness of the entered information.

#### Delete Teacher Operation:
1. The user selects the corresponding teacher name for which wants to perform the deletion.
2. Click on "SUBMIT" button under the second bulk of fields.

To delete a teacher, the user is presented with a list of existing teachers in the user interface. They can navigate through the list and select the specific teacher they wish to delete. The list typically displays the names of the teachers, providing an intuitive way for users to identify the teacher they want to remove.
### Room Availability

The Room Availability section provides a way to manage the creation/deletion of available rooms. This functionality ensures efficient utilization of the available space.
#### Add Room Operation:
1. Complete the corresponding information: room name, type of activities that can be sustained into the current room, capacity, floor.
2. Click on "SUMBIT" button under the first bulk of fields.

The room name should be unique and identifiable, while the room type should accurately represent the purpose or function of the room.
Upon clicking the "SUBMIT" button, the system will validate the entered data to ensure its completeness and correctness. If any required field is left blank or contains invalid information, an error message will be displayed, prompting the user to correct the input accordingly.

#### Delete Room Operation:
1. The user selects the corresponding room name for which wants to perform the deletion.
2. Click on "SUBMIT" button under the second bulk of fields.

From the available list of rooms, select the room name that you wish to delete.
Ensure that you have identified the correct room for deletion, as this action cannot be undone.Upon successful deletion, the selected room will be removed from the database, and its details will no longer be accessible in the system.
