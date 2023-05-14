package org.application.teachers;

import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Teacher;

import java.util.List;

public interface ITeachersService {

    public Teacher addTeacher(String name, Teacher.Type type) throws TeacherAdditionException;

    public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException, TeacherDeletionFailed;

    public boolean deleteTeachers(String name) throws TeacherDeletionFailed;

    public boolean deleteAll() throws TeacherDeletionFailed;

    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException;

    public List<Teacher> getTeachers();
}