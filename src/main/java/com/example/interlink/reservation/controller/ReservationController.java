package com.example.interlink.reservation.controller;

import com.example.interlink.jwt.util.JwtUtil;
import com.example.interlink.reservation.service.ReservationService;
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

    @PostMapping("/{seatId}/reserve")
    public ResponseEntity<?> reserveTicket(HttpServletRequest request, @PathVariable Long seatId) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        Long reservationId = reservationService.reserveTicket(userId, seatId);
        return ResponseEntity.ok(reservationId);
    }
}
