package com.example.interlink.ticket.controller;

import com.example.interlink.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("")
    private ResponseEntity<?> list(){
        return ResponseEntity.ok(ticketService.list());
    }

    @GetMapping("/{ticketId}")
    private ResponseEntity<?> read(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketService.read(ticketId));
    }

}
