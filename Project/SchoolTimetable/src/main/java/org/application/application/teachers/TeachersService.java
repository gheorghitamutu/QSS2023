package org.application.application.teachers;

import com.google.inject.Inject;
import org.application.dataaccess.teacher.ITeacherRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.teacher.TeacherAdditionException;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Teacher;

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
            throw new TeacherAdditionException("[TeacherService] Failed adding room!", e);
        }

        return teacher;
    }

    @Override
    public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException, TeacherDeletionFailed {
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
    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException {
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
