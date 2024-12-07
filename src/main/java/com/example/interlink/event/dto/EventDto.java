package com.example.interlink.event.dto;

import com.example.interlink.event.domain.Event;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String name;
    private LocalDateTime eventDate;
    private String venue;
    private String description;
    private int maxTickets;
    private int availableTickets;

    @Builder
    public EventDto(Long id, String name, LocalDateTime eventDate, String venue, String description, int maxTickets, int availableTickets) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.venue = venue;
        this.description = description;
        this.maxTickets = maxTickets;
        this.availableTickets = availableTickets;
    }

    public static EventDto fromEntity(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .eventDate(event.getEventDate())
                .venue(event.getVenue())
                .description(event.getDescription())
                .maxTickets(event.getMaxTickets())
                .availableTickets(event.getAvailableTickets())
                .build();
    }
}
