package com.example.interlink.event.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private String venue;

    @Column
    private String description;

    @Column(nullable = false)
    private int maxTickets;

    @Column(nullable = false)
    private int availableTickets;

    @Builder
    public Event(String name, LocalDateTime eventDate, String venue, String description, int maxTickets, int availableTickets) {
        this.name = name;
        this.eventDate = eventDate;
        this.venue = venue;
        this.description = description;
        this.maxTickets = maxTickets;
        this.availableTickets = availableTickets;
    }


    public void decreaseAvailableTickets() {
        if (availableTickets <= 0) {
            throw new IllegalStateException("티켓이 매진되었습니다.");
        }
        this.availableTickets--;
    }

}
