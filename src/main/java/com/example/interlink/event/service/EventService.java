package com.example.interlink.event.service;

import com.example.interlink.event.domain.Event;
import com.example.interlink.event.dto.EventDto;
import com.example.interlink.event.dto.EventReqDto;
import com.example.interlink.event.repository.EventRepository;
import com.example.interlink.ticket.domain.Ticket;
import com.example.interlink.ticket.domain.TicketStatus;
import com.example.interlink.ticket.dto.TicketDto;
import com.example.interlink.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        for (int i = 0; i < 249; i++) {
            Ticket ticket = Ticket.builder()
                    .price(132000)
                    .event(event)
                    .status(TicketStatus.AVAILABLE)
                    .build();
            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);
    }

    public List<EventDto> list(){
        return eventRepository.findAll().stream()
                .map(EventDto::fromEntity)
                .collect(Collectors.toList());
    }
    public EventDto read(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        return EventDto.fromEntity(event);
    }
}
