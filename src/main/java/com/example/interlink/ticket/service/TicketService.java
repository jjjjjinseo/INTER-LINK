package com.example.interlink.ticket.service;

import com.example.interlink.ticket.domain.Ticket;
import com.example.interlink.ticket.domain.TicketStatus;
import com.example.interlink.ticket.dto.TicketDto;
import com.example.interlink.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public List<TicketDto> list(){
        return ticketRepository.findAll().stream()
                .map(TicketDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TicketDto read(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        return TicketDto.fromEntity(ticket);
    }

    public boolean isAvailable(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        return ticket.getStatus().equals(TicketStatus.AVAILABLE);
    }
}
