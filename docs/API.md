# API 명세

## 콘서트 예약 시스템 API

## 1️⃣ 유저 대기열 토큰 발급 API

- **요청**
    - **URL**: `/api/queue/token`
    - **메소드**: `POST`

- **응답 본문**:
    ```json
    {
      "message": "대기열 토큰이 생성되었습니다.",
      "token": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx", // 생성된 UUID 스타일의 토큰
    }
    ```

---

## 2️⃣ 예약 가능 날짜 조회 API

- **요청**
    - **URL**: `/api/reservations/dates`
    - **메소드**: `GET`

- **응답 본문**:
    ```json
    {
      "availableDates": [
        "2024-10-01",
        "2024-10-02",
        "2024-10-03"
      ]
    }
    ```


## 좌석 조회 API

- **요청**
    - **URL**: `/api/reservations/seats`
    - **메소드**: `GET`
    - **URL 파라미터**:
        - `date`: 예약 가능한 날짜 (YYYY-MM-DD 형식)

- **응답 본문**:
    ```json
    {
      "date": "2024-10-01",
      "availableSeats": [
        {"seatNumber": 1, "status": "available"},
        {"seatNumber": 2, "status": "available"},
        {"seatNumber": 3, "status": "reserved"}
      ]
    }
    ```

---

## 3️⃣ 좌석 예약 요청 API

- **요청**
    - **URL**: `/api/reservations`
    - **메소드**: `POST`
    - **헤더**:
        - `Authorization`: `Bearer {token}` (대기열 토큰)

- **요청 본문**:
  ```json
  {
    "date": "2024-10-01",
    "seatNumber": 1
  }
  ```

- **응답 본문**:
  ```json
  {
    "message": "좌석이 예약되었습니다.",
    "reservationId": 12345,
  }
  ```
---
## 4️⃣ 잔액 충전 API

- **요청**
    - **URL**: `/api/wallet/recharge`
    - **메소드**: `POST`

- **요청 본문**:
  ```json
  {
    "amount": 10000 // 충전할 금액
  }
  ```
- **응답 본문**:
  ```json
  {
    "message": "잔액이 충전되었습니다.",
    "balance": 15000 // 충전 후 새로운 잔액
  }
  ```

## 잔액 조회 API

- **요청**
    - **URL**: /api/wallet/balance
    - **메소드**: GET
    - **헤더**:
        - Authorization: Bearer {token} (대기열 토큰)
- **응답 본문**:
  ```json
  {
    "userId": "user1", // 사용자 식별자
    "balance": 15000 // 현재 잔액
  }
  ```
---
## 5️⃣ 결제 API

- **요청**
    - **URL**: `/api/payments`
    - **메소드**: `POST`
    - **헤더**:
        - `Authorization`: `Bearer {token}` (대기열 토큰)

- **요청 본문**:
  ```json
  {
    "reservationId": 12345, // 예약 ID
    "amount": 10000 // 결제 금액
  }
  ```
- **응답 본문**:
  ```json
  {
    "message": "결제가 완료되었습니다.",
    "paymentId": "abcd1234", // 결제 ID
  }
  ```
