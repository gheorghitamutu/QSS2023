package org.application.teachers;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.teacher.ITeacherRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;

import java.text.MessageFormat;
import java.util.List;

/**
 * This class is the implementation of the ITeachersService interface.
 */
public class TeachersService implements ITeachersService {

    /**
     * The teacher repository.
     */
    private final ITeacherRepository teacherRepository;

    /**
     * This method initializes the teacher repository.
     * @param teacherRepository The teacher repository.
     */
    @Inject
    public TeachersService(ITeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * This method adds a teacher.
     * @param name The name of the teacher.
     * @param type The type of the teacher.
     * @return The added teacher.
     * @throws TeacherAdditionException Thrown if the teacher could not be added.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public Teacher addTeacher(String name, Teacher.Type type) throws TeacherAdditionException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[Teachers Service] Teacher name is invalid", null);
        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "[Teachers Service] Teacher type is invalid", null);

        Teacher teacher = null;

        try {
            teacher = teacherRepository.getByName(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (teacher == null) {
            try {
                teacher = teacherRepository.createNewTeacher(name, type);
            } catch (RepositoryOperationException | ValidationException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            teacherRepository.save(teacher);
        } catch (Exception e) {
            throw new TeacherAdditionException("[TeacherService] Failed adding teacher!", e);
        }

        return teacher;
    }

    /**
     * This method gets a teacher by id.
     * @param teacherId The id of the teacher.
     * @return The teacher.
     * @throws TeacherNotFoundException Thrown if the teacher could not be found.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException, TeacherDeletionFailed, ValidationException {

        ValidationHelpers.requirePositiveOrZero(teacherId, IllegalArgumentException.class, "[TeacherService] Teacher id is invalid", null);

        var teacher = teacherRepository.getById(teacherId);
        if (teacher == null) {
            throw new TeacherNotFoundException(MessageFormat.format("[TeacherService] Discipline with id {0} not found.", teacherId));
        }

        try {
            teacherRepository.delete(teacher);
        } catch (Exception e) {
            throw new TeacherDeletionFailed(" [TeacherService] Failed to delete teacher.", e);
        }

        return true;
    }

    /**
     * Deletes all teachers with the given name.
     * @param name The name of the Teachers to delete.
     * @return True if the teachers were deleted, false otherwise.
     * @throws TeacherDeletionFailed Thrown if the teachers could not be deleted.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public boolean deleteTeachers(String name) throws TeacherDeletionFailed, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[TeacherService] Teacher name is invalid", null);

        var teachers = teacherRepository.readAll().stream().filter(t -> t.getName().equals(name)).toList();

        try {
            teacherRepository.deleteMany(teachers);
        } catch (Exception e) {
            throw new TeacherDeletionFailed(" [TeacherService] Failed to delete teachers.", e);
        }

        return true;
    }

    /**
     * Deletes all teachers.
     * @return True if the teachers were deleted, false otherwise.
     * @throws TeacherDeletionFailed Thrown if the teachers could not be deleted.
     */
    @Override
    public boolean deleteAll() throws TeacherDeletionFailed {
        try {
            teacherRepository.deleteMany(teacherRepository.readAll());
        } catch (Exception e) {
            throw new TeacherDeletionFailed(" [TeacherService] Failed to delete teachers.", e);
        }

        return true;
    }

    /**
     * This method gets a teacher by id.
     * @param teacherId The id of the teacher.
     * @return The teacher.
     * @throws TeacherNotFoundException Thrown if the teacher could not be found.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException, ValidationException {

        ValidationHelpers.requirePositiveOrZero(teacherId, IllegalArgumentException.class, "[TeacherService] Teacher id is invalid", null);

        var teacher = teacherRepository.getById(teacherId);
        if (teacher == null) {
            throw new TeacherNotFoundException(MessageFormat.format("[TeacherService] Teacher with id {0} not found.", teacherId));
        }
        return teacher;
    }

    /**
     * Retrieves all teachers.
     * @return The list of teachers.
     */
    @Override
    public List<Teacher> getTeachers() {
        return teacherRepository.readAll();
    }
}
