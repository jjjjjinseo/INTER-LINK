package com.example.interlink.event.service;

import com.example.interlink.event.domain.Event;
import com.example.interlink.event.dto.EventDto;
import com.example.interlink.event.dto.EventReqDto;
import com.example.interlink.event.repository.EventRepository;
import com.example.interlink.ticket.domain.Ticket;
import com.example.interlink.ticket.domain.TicketStatus;
import com.example.interlink.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public EventDto create(EventReqDto eventReqDto){
        Event event = Event.builder()
                .name(eventReqDto.getName())
                .eventDate(eventReqDto.getEventDate())
                .venue(eventReqDto.getVenue())
                .description(eventReqDto.getDescription())
                .maxTickets(eventReqDto.getMaxTickets())
                .availableTickets(eventReqDto.getMaxTickets())
                .build();
        eventRepository.save(event);
        //티켓 생성
        createTicketsForEvent(event);
        return EventDto.fromEntity(event);
    }

    private void createTicketsForEvent(Event event) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < event.getMaxTickets(); i++) {
            Ticket ticket = Ticket.builder()
                    .price(10000)
                    .event(event)
                    .status(TicketStatus.AVAILABLE)
                    .build();
            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
    }
}
