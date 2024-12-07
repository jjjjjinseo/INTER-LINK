package com.example.interlink.reservation.service;

import com.example.interlink.event.domain.Event;
import com.example.interlink.reservation.domain.Reservation;
import com.example.interlink.reservation.repository.ReservationRepository;
import com.example.interlink.ticket.domain.Ticket;
import com.example.interlink.ticket.domain.TicketStatus;
import com.example.interlink.ticket.repository.TicketRepository;
import com.example.interlink.user.domain.User;
import com.example.interlink.user.dto.UserDto;
import com.example.interlink.user.repository.UserRepository;
import com.example.interlink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long reserveTicket(Long userId, Long ticketId) {
        //lock 걸기
        Ticket ticket = ticketRepository.findByIdWithLock(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (ticket.getStatus() != TicketStatus.AVAILABLE) {
            throw new IllegalStateException("해당 티켓은 예매할 수 없습니다.");
        }

        Event event = ticket.getEvent();
        //티켓 감소
        event.decreaseAvailableTickets();

        Reservation reservation = Reservation.builder()
                .user(user)
                .ticket(ticket)
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);

        ticket.updateStatus(TicketStatus.SOLD);

        return reservation.getId();
    }
}
