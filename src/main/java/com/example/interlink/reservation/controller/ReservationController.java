package com.example.interlink.reservation.controller;

import com.example.interlink.queue.service.QueueService;
import com.example.interlink.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class ReservationController {
    private final ReservationService reservationService;
    private final QueueService queueService;

    @PostMapping("/{ticketId}")
    @Operation(summary = "티켓 예매", description = "티켓번호에 해당하는 티켓을 예매합니다.")
    public ResponseEntity<?> reserveTicket(@RequestParam Long userId, @PathVariable Long ticketId) {
        try {
            // 티켓 예약 처리
            reservationService.reserveTicket(userId, ticketId);

            // 대기열에서 처리 완료 후 다음 사용자 처리
            queueService.removeFromQueue();

            // 대기열 상태 업데이트 메시지 브로드캐스트
            int queueSize = queueService.getQueueState("ticket:queue").size();
            queueService.broadcastQueueState(queueSize);

            return ResponseEntity.ok("예매가 완료되었습니다. 감사합니다!");
        } catch (Exception e) {
            // 예외 발생 시 오류 메시지 반환
            return ResponseEntity.badRequest().body("예매 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
