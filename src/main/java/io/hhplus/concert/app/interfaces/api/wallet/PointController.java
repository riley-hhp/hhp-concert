package io.hhplus.concert.app.interfaces.api.wallet;

import io.hhplus.concert.app.application.point.PointUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "포인트 지갑 관리")
public class PointController {

    private final PointUsecase pointUsecase;

    // 사용자 포인트 조회
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 포인트 조회", description = "주어진 사용자 ID에 대한 포인트 잔액을 조회합니다.")
    public ResponseEntity<Integer> getUserPoints(@PathVariable long userId) {
        int points = pointUsecase.getUserPoints(userId);
        return ResponseEntity.ok(points);
    }

    // 포인트 충전
    @PostMapping("/charge")
    @Operation(summary = "포인트 충전", description = "주어진 사용자 ID에 대한 포인트를 충전합니다.")
    public ResponseEntity<Void> chargePoints(@RequestParam long userId, @RequestParam int amount) {
        pointUsecase.chargePoints(userId, amount);
        return ResponseEntity.noContent().build();
    }

    // 포인트 사용
    @PostMapping("/use")
    @Operation(summary = "포인트 사용", description = "주어진 사용자 ID에 대한 포인트를 사용합니다.")
    public ResponseEntity<Void> usePoints(@RequestParam long userId, @RequestParam double amount) {
        pointUsecase.usePoints(userId, amount);
        return ResponseEntity.noContent().build();
    }
}