package org.example.repository;

import org.example.model.Activity;
import org.example.model.Reservation;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);

    List<Reservation> findByActivity(Activity activity);

    Optional<Reservation> findByUserAndActivity(User user, Activity activity);

    Integer countByActivity(Activity activity);

    Integer countByActivityAndConfirmed(Activity activity, Boolean confirmed);

    List<Reservation> findByActivityAndConfirmed(Activity activity, Boolean confirmed);
}

