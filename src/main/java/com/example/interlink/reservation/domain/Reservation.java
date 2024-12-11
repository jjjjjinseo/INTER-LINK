package com.example.interlink.reservation.domain;

import com.example.interlink.event.domain.Event;
import com.example.interlink.ticket.domain.Ticket;
import com.example.interlink.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Reservation(User user, Ticket ticket, LocalDateTime createdAt) {
        this.user = user;
        this.ticket = ticket;
        this.createdAt = createdAt;
    }
}
