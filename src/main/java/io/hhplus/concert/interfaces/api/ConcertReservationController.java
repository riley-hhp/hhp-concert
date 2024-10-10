package io.hhplus.concert.interfaces.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ConcertReservationController {

    // 유저 대기열 토큰 발급 API
    @PostMapping("/queue/token")
    public ResponseEntity<Map<String, Object>> generateQueueToken() {

        String token = UUID.randomUUID().toString();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "대기열 토큰이 생성되었습니다.");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // 예약 가능 날짜 조회 API
    @GetMapping("/reservations/dates")
    public ResponseEntity<Map<String, Object>> getAvailableDates() {

        List<String> availableDates = Arrays.asList("2024-10-01", "2024-10-02", "2024-10-03");

        Map<String, Object> response = new HashMap<>();
        response.put("availableDates", availableDates);

        return ResponseEntity.ok(response);
    }

    // 좌석 조회 API
    @GetMapping("/reservations/seats")
    public ResponseEntity<Map<String, Object>> getAvailableSeats(@RequestParam String date) {

        List<Map<String, Object>> availableSeats = Arrays.asList(
                Map.of("seatNumber", 1, "status", "available"),
                Map.of("seatNumber", 2, "status", "available"),
                Map.of("seatNumber", 3, "status", "reserved")
        );

        Map<String, Object> response = new HashMap<>();
        response.put("date", date);
        response.put("availableSeats", availableSeats);

        return ResponseEntity.ok(response);
    }

    // 좌석 예약 요청 API
    @PostMapping("/reservations")
    public ResponseEntity<Map<String, Object>> reserveSeat( @RequestHeader("Authorization") String token, @RequestBody Map<String, Object> requestBody) {

        String date = (String) requestBody.get("date");
        int seatNumber = (int) requestBody.get("seatNumber");

        long reservationId = 12345;

        Map<String, Object> response = new HashMap<>();
        response.put("message", "좌석이 예약되었습니다.");
        response.put("reservationId", reservationId);

        return ResponseEntity.ok(response);
    }

    // 잔액 충전 API
    @PostMapping("/wallet/recharge")
    public ResponseEntity<Map<String, Object>> rechargeWallet(@RequestBody Map<String, Object> requestBody) {

        int amount = (int) requestBody.get("amount");

        int balance = 15000;

        Map<String, Object> response = new HashMap<>();
        response.put("message", "잔액이 충전되었습니다.");
        response.put("balance", balance);

        return ResponseEntity.ok(response);
    }

    // 잔액 조회 API
    @GetMapping("/wallet/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestHeader("Authorization") String token) {

        String userId = "user1";
        int balance = 15000;

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("balance", balance);

        return ResponseEntity.ok(response);
    }

    // 결제 API
    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> requestBody) {

        Long reservationId = ((Integer) requestBody.get("reservationId")).longValue();
        int amount = (int) requestBody.get("amount");

        String paymentId = "abcd1234";

        Map<String, Object> response = new HashMap<>();
        response.put("message", "결제가 완료되었습니다.");
        response.put("paymentId", paymentId);

        return ResponseEntity.ok(response);
    }
}