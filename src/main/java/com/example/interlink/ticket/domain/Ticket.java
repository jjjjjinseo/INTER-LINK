package com.example.interlink.ticket.domain;

import com.example.interlink.event.domain.Event;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int price;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Builder
    public Ticket(int price, TicketStatus status, Event event) {
        this.price = price;
        this.status = status;
        this.event = event;
    }

    public void updateStatus(TicketStatus newStatue){
        this.status = newStatue;
    }
}
