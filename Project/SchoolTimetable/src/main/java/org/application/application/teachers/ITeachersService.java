package org.application.application.teachers;

import org.application.domain.exceptions.teacher.TeacherAdditionException;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.Room;
import org.application.domain.models.Teacher;

import java.util.List;

public interface ITeachersService {

    public Teacher addTeacher(String name, Teacher.Type type) throws TeacherAdditionException;

    public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException, TeacherDeletionFailed;

    public boolean deleteAll() throws TeacherDeletionFailed;

    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException;

    public List<Teacher> getTeachers();
}
