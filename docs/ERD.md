# ERD

```mermaid
erDiagram
    USER {
        bigint id
        string name
        string password
        datetime createdAt
        datetime updatedAt
    }

    QUEUE {
        bigint id
        bigint userId
        string token
        string status
        datetime createdAt
        datetime updatedAt
    }

    CONCERT {
        bigint id
        string title
        datetime startDate
        datetime endDate
        datetime createdAt
        datetime updatedAt
    }

    CONCERT_ITEM {
        bigint id
        bigint concertId       
        string sessionTitle
        datetime sessionDate
        string location
        datetime createdAt
        datetime updatedAt
    }

    SEAT {
        bigint id
        bigint concertItemId
        bigint seatNumber
        string status
        decimal price
        datetime createdAt
        datetime updatedAt
    }

    PAYMENT {
        bigint id
        bigint userId    
        bigint reservationId    
        decimal totalAmount
        datetime paymentDate
        string status
        datetime createdAt
        datetime updatedAt
    }

    RESERVATION {
        bigint id
        bigint userId
        bigint paymentId
        bigint concertItemId
        bigint seatId
        datetime reservationDate
        string status
        datetime createdAt
        datetime updatedAt
    }

    POINT {
        bigint id
        bigint userId     
        decimal balance
        datetime createdAt
        datetime updatedAt
    }

    %% Relationships
    QUEUE ||--o| CONCERT : "related to"
    CONCERT ||--o{ CONCERT_ITEM : "contains"
    CONCERT_ITEM ||--o{ SEAT : "contains"

    USER ||--o{ QUEUE : "enter into"
    USER ||--o{ PAYMENT : "makes"
    USER ||--o{ POINT : "has"
    PAYMENT ||--o| RESERVATION : "is associated with"


  RESERVATION ||--o| USER : "is made by"
  RESERVATION ||--o| SEAT : "reserves"
```