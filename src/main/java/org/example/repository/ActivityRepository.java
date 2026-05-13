package org.example.repository;

import org.example.model.Activity;
import org.example.model.Course;
import org.example.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCourse(Course course);

    List<Activity> findByLocation(Location location);

    List<Activity> findByStartDateAfter(LocalDateTime dateTime);

    List<Activity> findByEndDateBefore(LocalDateTime dateTime);

    @Query("SELECT a FROM Activity a WHERE a.startDate > :now ORDER BY a.startDate ASC")
    List<Activity> findUpcomingActivities(LocalDateTime now);

    Integer countByCourse(Course course);
}

