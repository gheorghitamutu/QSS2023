package org.application.teachers;

import com.google.inject.Inject;
import org.dataaccess.teacher.ITeacherRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Teacher;

import java.text.MessageFormat;
import java.util.List;

public class TeachersService implements ITeachersService {

    private final ITeacherRepository teacherRepository;

    @Inject
    public TeachersService(ITeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher addTeacher(String name, Teacher.Type type) throws TeacherAdditionException {

        if (name == null || name.isEmpty()) {
            throw new TeacherAdditionException("[Teachers Service] Teacher name is invalid");
        }

        if (type == null) {
            throw new TeacherAdditionException("[Teachers Service] Teacher type is invalid");
        }

        Teacher teacher = null;

        try {
            teacher = teacherRepository.getByName(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (teacher == null) {
            try {
                teacher = teacherRepository.createNewTeacher(name, type);
            } catch (RepositoryOperationException e) {
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

    @Override
    public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException, TeacherDeletionFailed {

        if (teacherId < 0) {
            throw new IllegalArgumentException("[TeacherService] Teacher id is invalid");
        }

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

    @Override
    public boolean deleteTeachers(String name) throws TeacherDeletionFailed {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("[TeacherService] Teacher name is invalid");
        }

        var teachers = teacherRepository.readAll().stream().filter(t -> t.getName().equals(name)).toList();

        try {
            teacherRepository.deleteMany(teachers);
        } catch (Exception e) {
            throw new TeacherDeletionFailed(" [TeacherService] Failed to delete teachers.", e);
        }

        return true;
    }

    @Override
    public boolean deleteAll() throws TeacherDeletionFailed {
        try {
            teacherRepository.deleteMany(teacherRepository.readAll());
        } catch (Exception e) {
            throw new TeacherDeletionFailed(" [TeacherService] Failed to delete teachers.", e);
        }

        return true;
    }

    @Override
    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException {

        if (teacherId < 0) {
            throw new IllegalArgumentException("[TeacherService] Teacher id is invalid");
        }

        var teacher = teacherRepository.getById(teacherId);
        if (teacher == null) {
            throw new TeacherNotFoundException(MessageFormat.format("[TeacherService] Teacher with id {0} not found.", teacherId));
        }
        return teacher;
    }

    @Override
    public List<Teacher> getTeachers() {
        return teacherRepository.readAll();
    }
}
