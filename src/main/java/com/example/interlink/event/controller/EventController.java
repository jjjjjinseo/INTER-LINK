package com.example.interlink.event.controller;

import com.example.interlink.event.dto.EventReqDto;
import com.example.interlink.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody  EventReqDto eventReqDto){
        return ResponseEntity.ok(eventService.create(eventReqDto));
    }

    @GetMapping("")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok(eventService.list());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> read(@PathVariable Long eventId){
        return ResponseEntity.ok(eventService.read(eventId));
    }

}
