package com.example.interlink.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventReqDto {
    private String name;
    private LocalDateTime eventDate; //"2024-12-01T10:30:00"
    private String venue;
    private String description;
    private int maxTickets;
}
