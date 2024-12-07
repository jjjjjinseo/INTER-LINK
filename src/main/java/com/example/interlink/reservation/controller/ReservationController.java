package com.example.interlink.reservation.controller;

import com.example.interlink.jwt.util.JwtUtil;
import com.example.interlink.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class ReservationController {
    private final ReservationService reservationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{ticketId}/reserve")
    @Operation(summary = "티켓 예매", description = "티켓번호에 해당하는 티켓을 예매합니다.")
    private ResponseEntity<?> reserveTicket(HttpServletRequest request, @PathVariable Long ticketId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(reservationService.reserveTicket(userId, ticketId));
    }
}
