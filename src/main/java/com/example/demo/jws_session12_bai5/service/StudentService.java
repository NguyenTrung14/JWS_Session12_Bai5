package com.example.demo.jws_session12_bai5.service;

import com.example.demo.jws_session12_bai5.exception.StudentNotFoundException;
import com.example.demo.jws_session12_bai5.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StudentService {
    private final Map<Long, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    public Student findById(Long id) {
        Student student = students.get(id);
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        return student;
    }

    public Student create(Student student) {
        Long id = nextId.getAndIncrement();
        Student createdStudent = new Student(
                id,
                student.getStudentCode(),
                student.getFullName(),
                student.getMajor(),
                student.getGpa()
        );
        students.put(id, createdStudent);
        return createdStudent;
    }

    public Student update(Long id, Student student) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }

        Student updatedStudent = new Student(
                id,
                student.getStudentCode(),
                student.getFullName(),
                student.getMajor(),
                student.getGpa()
        );
        students.put(id, updatedStudent);
        return updatedStudent;
    }

    public void delete(Long id) {
        Student removedStudent = students.remove(id);
        if (removedStudent == null) {
            throw new StudentNotFoundException(id);
        }
    }
}
