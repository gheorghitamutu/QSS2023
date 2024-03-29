package org;

import com.google.inject.Inject;
import org.application.disciplines.IDisciplinesService;
import org.application.rooms.IRoomsService;
import org.application.sessions.ISessionsService;
import org.application.studentgroups.IStudentGroupsService;
import org.application.students.IStudentsService;
import org.application.teachers.ITeachersService;
import org.application.timeslots.ITimeslotsService;
import org.dataaccess.database.MainDatabaseHibernateProvider;
import org.dataaccess.discipline.DisciplineRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;

/**
 * Represents the main application class.
 * This class is responsible for initializing the application and running it.
 */
public class Application {

    /**
     * The service used for accessing Discipline entities.
     */
    public final IDisciplinesService disciplinesService;

    /**
     * The service used for accessing Room entities.
     */
    public final IRoomsService roomsService;

    /**
     * The service used for accessing Session entities.
     */
    public final ISessionsService sessionsService;

    /**
     * The service used for accessing StudentGroup entities.
     */
    public final IStudentGroupsService studentGroupsService;

    /**
     * The service used for accessing Student entities.
     */
    public final IStudentsService studentsService;

    /**
     * The service used for accessing Teacher entities.
     */
    public final ITeachersService teachersService;

    /**
     * The service used for accessing Timeslot entities.
     */
    public final ITimeslotsService timeslotsService;

    /**
     * Creates a new instance of the {@link Application} class.
     * @param disciplinesService The service used for accessing Discipline entities.
     * @param roomsService The service used for accessing Room entities.
     * @param sessionsService The service used for accessing Session entities.
     * @param studentGroupsService The service used for accessing StudentGroup entities.
     * @param studentsService The service used for accessing Student entities.
     * @param teachersService The service used for accessing Teacher entities.
     * @param timeslotsService The service used for accessing Timeslot entities.
     */
    @Inject
    public Application(IDisciplinesService disciplinesService,
                       IRoomsService roomsService,
                       ISessionsService sessionsService,
                       IStudentGroupsService studentGroupsService,
                       IStudentsService studentsService,
                       ITeachersService teachersService, ITimeslotsService timeslotsService) {
        this.disciplinesService = disciplinesService;
        this.roomsService = roomsService;
        this.sessionsService = sessionsService;
        this.studentGroupsService = studentGroupsService;
        this.studentsService = studentsService;
        this.teachersService = teachersService;
        this.timeslotsService = timeslotsService;
    }

    /**
     * Runs the application.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the provided name or credits are invalid.
     */
    public void run() throws RepositoryOperationException, ValidationException {

        var dr = new DisciplineRepository(new MainDatabaseHibernateProvider());

        dr.createNewDiscipline("test", -1);

//        showcaseStudentsService();
//
//        showcaseSampleCodeForRepo();
    }
//
//
//    private void showcaseStudentsService() {
//        var allStudents = studentsService.getStudents();
//
//        System.out.println("All students:");
//        for (var student : allStudents) {
//            System.out.println(student.getName());
//        }
//
//        int newStudentId = 0;
//        Student specificStudent = null;
//
//        StudentGroup studentGroup = new StudentGroup();
//        studentGroup.setName("A6");
//        studentGroup.setYear(1);
//        studentGroup.setType(StudentGroup.Type.BACHELOR);
//        studentGroup.setInsertTime(new Date());
//        Student newStudent = new Student();
//
//        newStudent.setGroup(studentGroup);
//        newStudent.setYear(1);
//        newStudent.setRegistrationNumber("310910204006SM000000");
//        newStudent.setName("test" + UUID.randomUUID());
//
//        System.out.println("Creating Student...");
//
//        try {
//            newStudent = studentsService.addStudent(newStudent.getName(), newStudent.getRegistrationNumber(), newStudent.getYear(), newStudent.getGroup().getName());
//
//            newStudentId = newStudent.getId();
//
//            System.out.println("Student with id " + newStudentId + " created.");
//
//        } catch (StudentAdditionException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be created.");
//
//            throw new RuntimeException(e);
//        }
//
//        try {
//            specificStudent = studentsService.getStudentById(newStudentId);
//        } catch (StudentNotFoundException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be created.");
//
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Student with id " + newStudentId + " is in group " + specificStudent.getGroup().getName());
//        System.out.println("Student with id " + newStudentId + " is in year " + specificStudent.getYear());
//        System.out.println("Student with id " + newStudentId + " is named " + specificStudent.getName());
//
//
//        System.out.println("Reassigning student with id " + newStudentId + " to group A1");
//
//        try {
//            studentsService.reassignStudent(newStudentId, "A1");
//            System.out.println("Reassigned student with id " + newStudentId + " to group A1");
//
//        } catch (StudentGroupReassignException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be reassigned.");
//            throw new RuntimeException(e);
//        }
//
//        try {
//            specificStudent = studentsService.getStudentById(newStudentId);
//        } catch (StudentNotFoundException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be created.");
//
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Student with id " + newStudentId + " is in group " + specificStudent.getGroup().getName());
//        System.out.println("Student with id " + newStudentId + " is in year " + specificStudent.getYear());
//        System.out.println("Student with id " + newStudentId + " is named " + specificStudent.getName());
//
//
//        System.out.println("Updating student with id " + newStudentId + " (year to become 2)");
//
//        try {
//            studentsService.updateStudent(newStudentId, "test" + "newtestupdatename" + UUID.randomUUID(), 2);
//        } catch (StudentUpdateException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be updated.");
//            throw new RuntimeException(e);
//        }
//
//        try {
//            specificStudent = studentsService.getStudentById(newStudentId);
//        } catch (StudentNotFoundException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be created.");
//
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Student with id " + newStudentId + " is in group " + specificStudent.getGroup().getName());
//        System.out.println("Student with id " + newStudentId + " is in year " + specificStudent.getYear());
//        System.out.println("Student with id " + newStudentId + " is named " + specificStudent.getName());
//
//
//        System.out.println("Deleting student with id " + newStudentId);
//
//        try {
//            studentsService.deleteStudent(newStudentId);
//        } catch (StudentNotFoundException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be found for performing delete student.");
//            throw new RuntimeException(e);
//        } catch (StudentDeletionFailed e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be deleted.");
//            throw new RuntimeException(e);
//        }
//
//        try {
//            specificStudent = studentsService.getStudentById(newStudentId);
//        } catch (StudentNotFoundException e) {
//            System.out.println("Student with id " + newStudentId + " couldn't be created.");
//
//            throw new RuntimeException(e);
//        }
//
//        if (specificStudent == null) {
//            System.out.println("Student with id " + newStudentId + " doesn't exist.");
//        } else {
//            System.out.println("Student with id " + newStudentId + "  exists.");
//        }
//    }
//
//    private void showcaseSampleCodeForRepo() {
//        var students = this.studentsRepository.readAll();
//
//        if (students.size() <= 45) {
//            StudentGroup studentGroup = new StudentGroup();
//
//            // check group validator
//            // studentGroup.setName("1A1");
//            studentGroup.setName("A1");
//            studentGroup.setYear(1);
//            studentGroup.setType(StudentGroup.Type.BACHELOR);
//
//            studentGroup.setInsertTime(new Date());
//
//            Student newStudent = new Student();
//            newStudent.setGroup(studentGroup);
//            newStudent.setYear(1);
//            newStudent.setName("test" + UUID.randomUUID());
//            newStudent.setInsertTime(new Date());
//
//            try {
//                this.studentsRepository.save(newStudent);
//            } catch (Exception e) {
//                System.out.println("Couldn't insert student.");
//                e.printStackTrace();
//            }
//
//            students = this.studentsRepository.readAll();
//        }
//
//        System.out.println("************************************** STUDENTS LIST *************************");
//
//        for (var student : students) {
//            System.out.println(student.getName());
//        }
//
//        System.out.println("************************************** END STUDENTS LIST *************************");
//    }
}
