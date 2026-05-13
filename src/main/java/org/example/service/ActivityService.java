package org.example.service;

import org.example.model.Activity;
import org.example.model.Course;
import org.example.model.Location;
import org.example.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity createActivity(String name, String description, LocalDateTime startDate,
                                  LocalDateTime endDate, Integer capacity, Course course, Location location) {
        Activity activity = Activity.builder()
                .name(name)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .capacity(capacity)
                .course(course)
                .location(location)
                .createdAt(LocalDateTime.now())
                .build();

        return activityRepository.save(activity);
    }

    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    public List<Activity> findByCourse(Course course) {
        return activityRepository.findByCourse(course);
    }

    public List<Activity> findByLocation(Location location) {
        return activityRepository.findByLocation(location);
    }

    public List<Activity> findUpcomingActivities() {
        return activityRepository.findUpcomingActivities(LocalDateTime.now());
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    public Integer getTotalActivities() {
        return (int) activityRepository.count();
    }

    public Integer getTotalActivitiesByCourse(Course course) {
        return activityRepository.countByCourse(course);
    }
}

