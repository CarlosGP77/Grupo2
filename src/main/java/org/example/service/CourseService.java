package org.example.service;

import org.example.model.Course;
import org.example.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(String code, String name, String description, Integer duration, Double price) {
        if (courseRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Course with code '" + code + "' already exists");
        }

        Course course = Course.builder()
                .code(code)
                .name(name)
                .description(description)
                .duration(duration)
                .price(price)
                .createdAt(LocalDateTime.now())
                .build();

        return courseRepository.save(course);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    public Optional<Course> findByName(String name) {
        return courseRepository.findByName(name);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Integer getTotalCourses() {
        return (int) courseRepository.count();
    }
}

