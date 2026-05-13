package org.example.service;

import org.example.model.Activity;
import org.example.model.Reservation;
import org.example.model.User;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(User user, Activity activity) {
        // Verificar si ya existe reserva
        if (reservationRepository.findByUserAndActivity(user, activity).isPresent()) {
            throw new IllegalArgumentException("User already has a reservation for this activity");
        }

        // Verificar disponibilidad
        if (!activity.isAvailable()) {
            throw new IllegalArgumentException("Activity is not available");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .activity(activity)
                .confirmed(false)
                .createdAt(LocalDateTime.now())
                .build();

        return reservationRepository.save(reservation);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public Optional<Reservation> findByUserAndActivity(User user, Activity activity) {
        return reservationRepository.findByUserAndActivity(user, activity);
    }

    public List<Reservation> findByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> findByActivity(Activity activity) {
        return reservationRepository.findByActivity(activity);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setConfirmed(true);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Integer getTotalReservations() {
        return (int) reservationRepository.count();
    }

    public Integer getTotalReservationsByActivity(Activity activity) {
        return reservationRepository.countByActivity(activity);
    }

    public Integer getConfirmedReservationsByActivity(Activity activity) {
        return reservationRepository.countByActivityAndConfirmed(activity, true);
    }
}

