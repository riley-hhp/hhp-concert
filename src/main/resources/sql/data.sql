-- concert 테이블에 더미 데이터 삽입
INSERT INTO concert (created_at, end_at, start_at, updated_at, title)
VALUES
    (CURRENT_TIMESTAMP, '2024-12-31 23:59:59', '2024-12-01 19:00:00', null, 'New Year Concert'),
    (CURRENT_TIMESTAMP, '2024-11-15 21:00:00', '2024-11-15 18:00:00', null, 'Rock Festival'),
    (CURRENT_TIMESTAMP, '2024-10-30 22:00:00', '2024-10-30 20:00:00', null, 'Classical Evening');

-- concert_item 테이블에 더미 데이터 삽입
INSERT INTO concert_item (capacity, concert_id, created_at, session_at, updated_at, location, session_title)
VALUES
    (500, 1, CURRENT_TIMESTAMP, '2024-12-01 19:30:00', null, 'Main Hall', 'Opening Act'),
    (300, 1, CURRENT_TIMESTAMP, '2024-12-01 20:30:00', null, 'Main Hall', 'Main Event'),
    (1000, 2, CURRENT_TIMESTAMP, '2024-11-15 18:30:00', null, 'Outdoor Stage', 'Rock Fest Opening'),
    (800, 3, CURRENT_TIMESTAMP, '2024-10-30 20:30:00', null, 'Grand Theatre', 'Classical Overture');

-- seat 테이블에 더미 데이터 삽입
INSERT INTO seat (price, concert_item_id, created_at, seat_number, updated_at, status)
VALUES
    (100.00, 1, CURRENT_TIMESTAMP, 1, null, 'AVAILABLE'),
    (100.00, 1, CURRENT_TIMESTAMP, 2, null, 'AVAILABLE'),
    (150.00, 2, CURRENT_TIMESTAMP, 1, null, 'AVAILABLE'),
    (200.00, 2, CURRENT_TIMESTAMP, 2, null, 'AVAILABLE'),
    (50.00, 3, CURRENT_TIMESTAMP, 1, null, 'AVAILABLE'),
    (50.00, 3, CURRENT_TIMESTAMP, 2, null, 'AVAILABLE');

-- point 테이블에 더미 데이터 삽입
INSERT INTO point (balance, created_at, updated_at, user_id)
VALUES
    (10000000.00, CURRENT_TIMESTAMP, null, 1),
    (5000000.00, CURRENT_TIMESTAMP, null, 2),
    (3000000.00, CURRENT_TIMESTAMP, null, 3);

-- users 테이블에 더미 데이터 삽입
INSERT INTO users (name, password)
VALUES
    ('Alice', 'password123'),
    ('Bob', 'password456'),
    ('Charlie', 'password789');