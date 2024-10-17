package io.hhplus.concert.app.api.waitingqueue;

import io.hhplus.concert.app.application.waitingqueue.WaitingQueueService;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waiting-queue")
@RequiredArgsConstructor
@Tag(name = "WaitingQueue", description = "대기열 관리")
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    // 대기열 토큰 발급
    @PostMapping("/issue-token")
    @Operation(summary = "대기열 토큰 발급", description = "주어진 콘서트 ID에 대한 대기열 토큰을 발급합니다.")
    public ResponseEntity<WaitingQueue> issueToken(@RequestParam Long concertId) {
        WaitingQueue token = waitingQueueService.issueToken(concertId);
        return ResponseEntity.ok(token);
    }

    // 토큰 조회
    @GetMapping("/token")
    @Operation(summary = "토큰 조회", description = "주어진 토큰을 기반으로 대기열 정보를 조회합니다.")
    public ResponseEntity<WaitingQueue> getToken(@RequestParam String token) {
        WaitingQueue waitingQueue = waitingQueueService.getToken(token);
        return ResponseEntity.ok(waitingQueue);
    }

    // 토큰 활성화
    @PostMapping("/active-token")
    @Operation(summary = "토큰 활성화", description = "대기열의 토큰을 활성화합니다.")
    public ResponseEntity<Void> activateToken() {
        waitingQueueService.activeToken();
        return ResponseEntity.noContent().build();
    }

    // 토큰 만료
    @PostMapping("/expire-token")
    @Operation(summary = "토큰 만료", description = "대기열의 토큰을 만료시킵니다.")
    public ResponseEntity<Void> expireToken() {
        waitingQueueService.expireToken();
        return ResponseEntity.noContent().build();
    }
}