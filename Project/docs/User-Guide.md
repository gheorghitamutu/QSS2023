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
![Main Menu](./reportImages/mainMenu.png)
### Database Operations

The Timetable Generator allows you to perform various database operations to manage the core entities of the application. These operations include inserting new records and deleting existing records for students, teachers, rooms, sessions, and timeslots.

## Using the Timetable Generator

Let's explore the key functionalities of the Timetable Generator in more detail:

### Student Management

The Student Management section allows you to add and delete student records. You can enter details such as student names, registration ID, year of study, and other relevant information. This section helps you maintain accurate and up-to-date student data.
![Student Section](./reportImages/studentPanel.png)
#### Add Student Operation:

1. Complete the corresponding information: student name, registration ID (unique constraint), year of study,group of study.
2. Click on "SUBMIT" button under the first bulk of fields.

Once the user has properly entered the data, a connection is established to the backend. A call is made to the appropriate service for inserting a student into the database, using the information collected on the frontend. At the implementation level, validation checks are performed on the entered data, and the actual operation is carried out.
#### Delete Student Operation:
1. The user has to introduce the corresponding registration ID for a student.
2. Click on "SUBMIT" button under the second bulk of fields.

Once the user initiates the delete student operation, the system establishes a connection to the backend. It then invokes the corresponding service responsible for deleting a student from the database. The service uses the provided registration ID to locate the student's record.

During the implementation of the delete operation, the system performs necessary checks and validations to ensure the correctness and integrity of the data. Once the student is successfully located and identified by the registration ID, the actual deletion operation is performed, removing the student's record from the database.
### Teacher Management

The Teacher Management section enables you to manage teacher information. You can add new teachers, remove existing one. This section is particularly useful for maintaining a comprehensive record of teachers within the institution.
![Teacher Section](./reportImages/teacherPanel.png)
#### Add Teacher Operation:
1. Complete the corresponding information: teacher name, role of the teacher in the current institution.
2. Click on "SUBMIT" button under the first bulk of fields.

To add a teacher, the user enters the teacher's name and selects their role.The system utilizes the provided data from the user interface and invokes the appropriate service responsible for handling teacher insertion. The service performs necessary validations to ensure the accuracy and completeness of the entered information.

#### Delete Teacher Operation:
1. The user selects the corresponding teacher name for which wants to perform the deletion.
2. Click on "SUBMIT" button under the second bulk of fields.

To delete a teacher, the user is presented with a list of existing teachers in the user interface. They can navigate through the list and select the specific teacher they wish to delete. The list typically displays the names of the teachers, providing an intuitive way for users to identify the teacher they want to remove.
### Room Availability

The Room Availability section provides a way to manage the creation/deletion of available rooms. This functionality ensures efficient utilization of the available space.
![Room Section](./reportImages/roomPanel.png)
#### Add Room Operation:
1. Complete the corresponding information: room name, type of activities that can be sustained into the current room, capacity, floor.
2. Click on "SUBMIT" button under the first bulk of fields.

The room name should be unique and identifiable, while the room type should accurately represent the purpose or function of the room.
Upon clicking the "SUBMIT" button, the system will validate the entered data to ensure its completeness and correctness. If any required field is left blank or contains invalid information, an error message will be displayed, prompting the user to correct the input accordingly.

#### Delete Room Operation:
1. The user selects the corresponding room name for which wants to perform the deletion.
2. Click on "SUBMIT" button under the second bulk of fields.

From the available list of rooms, select the room name that you wish to delete.
Ensure that you have identified the correct room for deletion, as this action cannot be undone.Upon successful deletion, the selected room will be removed from the database, and its details will no longer be accessible in the system.
### Discipline Management

The Discipline Management section enables you to manage discipline information. You can add new disciplines, remove existing one, and add associate teachers to the existing ones. This section is particularly useful for maintaining a comprehensive record of disciplines and their corresponding teachers.
![Teacher Section](./reportImages/disciplinePanel.png)
#### Add Discipline Operation:
1. Complete the corresponding information: discipline name, number of credits associated with disciplines.
2. Click on "SUBMIT" button under the first bulk of fields.

To add a discipline, the user enters the discipline's name and selects their credits.The system utilizes the provided data from the user interface and invokes the appropriate service responsible for handling discipline insertion. The service performs necessary validations to ensure the accuracy and completeness of the entered information.

#### Add Teacher To Discipline Operation:
1. The user selects the corresponding discipline name for which wants to add a teacher.
2. The user selects the corresponding teacher name for which wants to add a discipline.
3. Click on "SUBMIT" button under the second bulk of fields.

To add a teacher to a discipline, the user is presented with a list of existing disciplines and a list of existing teachers in the user interface. The lists typically displays the names of the disciplines and teachers.

#### Delete Discipline Operation:
1. The user selects the corresponding discipline name for which wants to perform the deletion.
2. Click on "SUBMIT" button under the third bulk of fields.

To delete a discipline, the user is presented with a list of existing disciplines in the user interface. They can navigate through the list and select the specific discipline they wish to delete. The list typically displays the names of the disciplines, providing an intuitive way for users to identify the discipline they want to remove.
### Session Details

The Session Details section allows you to define and manage various sessions within the Timetable Generator. You can create sessions for different subjects, assign teachers to sessions, assing a group to  session. This feature helps in organizing and structuring the overall timetable.
![Session Section](./reportImages/sessionPanel.png)
#### Add Session Operation:
1. Complete the corresponding information: the discipline you want to assign, type of the discipline and select the specific half-year.
2. Click on "SUMBIT" button under the first bulk of fields.

Select the discipline you want to assign to the session from the list of available disciplines. This list contains disciplines stored in the database.
Choose the specific half-year for which the session is scheduled. By completing the required fields and clicking "SUBMIT," you are instructing the system to create a new session with the provided details.
nce submitted, the session will be created and become part of the system's database.

#### Assign Teacher To Session Operation:
1. Select the teacher you want to assign from a list of available teachers into the current database.
2. Select the session you want to book for the corresponding teacher.
3. Click on "SUBMIT" button under the second bulk of fields.

By performing this operation, you are associating a teacher with a particular session, indicating that the teacher will be responsible for conducting that session.
The backend receives the request, validates the data, and performs the corresponding operation.

#### Assign Group of Students To Session Operation:
1. Select the group of students you want to assign from a list of available groups into the current database.
2. Select the session you want to book for the corresponding group of students.
3. Click on "SUBMIT" button under the third bulk of fields.

By following these steps, the backend ensures that the selected group of students is properly assigned to the chosen session. This allows the Timetable Generator application to keep track of the student-group-session relationships, enabling efficient scheduling and management of the timetable.

#### Delete Session Operation:
1. The user selects the corresponding session for which wants to perform the deletion.
2. Click on "SUBMIT" button under the last bulk of fields.

Once the user clicks the "SUBMIT" button, the backend validates the request, searches for the session based on the provided discipline name, and performs the necessary operations to delete the session from the database.
### Timeslot Configuration

The Timeslot Configuration section enables you to set up and configure timeslots for the timetable. You can define specific time intervals, assign days and durations, periodicity, room and sessions involved. This functionality ensures accurate scheduling of sessions and effective time management.
![Timeslot Section](./reportImages/timeslotPanel.png)
#### Add Timeslot Operation:
1. Fill in the required information: start/end date for a session, periodicity, duration of course, room.
2. Click on "SUMMIT" button under the first bulk of fields.

Upon submission, the backend of the application processes the data provided and performs validation checks. It creates a new timeslot with the specified parameters and associates it with the corresponding room and session.This functionality ensures that the timetable is accurately populated with defined time intervals, taking into account the session's duration, start and end dates, and periodicity and room availability.
#### Delete Timeslot Operation:
1. The user selects the corresponding timeslot for which wants to perform the deletion.
2. Click on "SUBMIT" button under the second bulk of fields.

Browse through the list of displayed timeslots and choose the one you want to delete. Each timeslot is represented by its date, duration, assigned room, and corresponding discipline.After clicking "SUBMIT," the application's backend processes your request. It identifies the selected timeslot based on the provided information and performs the necessary actions to remove it from the timetable. This involves updating the underlying database and making sure that the deleted timeslot no longer appears in the system.

### Timetable HTML Generation
The Timetable Generation feature allows you to generate timetables based on the defined students, teachers, disciplines, sessions, timeslots, and rooms availability. By specifying filters and preferences, you can generate timetables that meet specific requirements. The generated timetables can be exported in HTML format for easy sharing and printing.

#### Generating Timetables:
The process of generating timetables in the application involves the utilization of user-defined data and a comprehensive validation system to ensure the accuracy and feasibility of associations between entities. It is about the following steps:
1. Input Data: You provide all the necessary information, such as student groups, teachers, classrooms, disciplines, and also all the associations between these entities. This data serves as the foundation for the timetables.
2. Data Validation and Processing: The application performs thorough validation checks on the manually entered data to ensure the correctness of associations between entities. It verifies that the specified associations, such as assigning teachers to sessions or allocating classrooms to specific time slots, are valid and feasible. If any invalid associations are detected, the application provides warning messages, preventing them from being stored in the database. This validation step guarantees that only accurate and appropriate data is used for generating the timetables.
3. Individual Timetables: The generator creates separate timetables for each entity in the database. For example, it generates specific timetables for student groups, teachers, and classrooms. These individual timetables reflect the schedule for each entity, taking into account their availability and assigned sessions.
4. Complete Timetable: In addition to individual timetables, the generator also generates a complete timetable. This comprehensive timetable combines all the sessions and resources, providing a holistic view of the schedule across different entities.
5. Room Availability: The application includes a dedicated page that displays the schedule of all the rooms according to days and time slots. This page shows which time slots are occupied and which ones are available, allowing you to easily identify free time slots for scheduling new sessions.
6. Instant Saving: The generated timetables are instantly saved in HTML format. You can access and share these timetables with others, or print them for reference. The saved timetables reflect the finalized schedule based on the input data and the generator's processing.

#### Customization and HTML Templates
Although there is no direct editing functionality for visual appearance, the application uses HTML templates to define the layout, styling, and content of the timetables. This allows you to modify the templates to suit your preferences and adapt the visual presentation of the timetables.

## Conclusion

This user guide manual has provided an overview of the application, including student and teacher management, room availability, session details, timeslot configuration, and timetable generation. Use this manual as a reference to efficiently navigate and utilize the Timetable Generator to create well-organized and optimized timetables for your institution.



#### Contributions:
Nechita Roberta - Use Case Diagram and UseCase Documentation, FrontEnd part, Functional testing on frontend part (adding both pre- and post-conditions), Backend integration on frontend,
Document the personal implementation with java doc comment in order to generate the tech report, User-Guide Manual.