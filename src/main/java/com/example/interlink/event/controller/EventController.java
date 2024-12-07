package com.example.interlink.event.controller;

import com.example.interlink.event.dto.EventReqDto;
import com.example.interlink.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody  EventReqDto eventReqDto){
        return ResponseEntity.ok(eventService.create(eventReqDto));
    }
}
