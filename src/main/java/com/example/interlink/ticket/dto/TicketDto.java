package com.example.interlink.ticket.dto;

import com.example.interlink.ticket.domain.Ticket;
import com.example.interlink.ticket.domain.TicketStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class TicketDto {
    private Long id;
    private int price;
    private TicketStatus status;

    @Builder
    public TicketDto(Long id, int price, TicketStatus status) {
        this.id = id;
        this.price = price;
        this.status = status;
    }

    public static TicketDto fromEntity(Ticket ticket){
        return TicketDto.builder()
                .id(ticket.getId())
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .build();
    }
}
