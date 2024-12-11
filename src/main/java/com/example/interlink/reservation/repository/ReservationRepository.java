package com.example.interlink.reservation.repository;

import com.example.interlink.reservation.domain.Reservation;
import com.example.interlink.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByTicket(Ticket ticket);
}