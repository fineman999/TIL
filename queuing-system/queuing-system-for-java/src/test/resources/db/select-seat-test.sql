DELETE
FROM tickets
WHERE ticket_id > 0;
DELETE
FROM seats
WHERE seat_id > 0;
DELETE
FROM seat_grades
WHERE seat_grade_id > 0;
DELETE
FROM zones
WHERE zone_id > 0;
DELETE
FROM performances
WHERE performance_id > 0;
DELETE
FROM members
WHERE member_id > 0;

-- Member 테이블에 데이터 삽입
INSERT INTO members (member_id, email, password, member_role, created_at, updated_at)
VALUES (1, 'test@gmail.com', 'testpassword', 'USER', NOW(), NOW());

-- Performance 테이블에 데이터 삽입
INSERT INTO performances (performance_id, performance_name, performance_place, performance_showtime, created_at,
                         updated_at)
VALUES (1, '공연', '장소', '2024-08-23 14:30:00', NOW(), NOW());

-- Zone 테이블에 데이터 삽입
INSERT INTO zones (zone_id, zone_name, performance_id, created_at, updated_at)
VALUES (1, 'VIP', 1, NOW(), NOW());

-- SeatGrade 테이블에 데이터 삽입
INSERT INTO seat_grades (seat_grade_id, grade_name, price, performance_id, created_at, updated_at)
VALUES (1, 'Grade1', 10000, 1, NOW(), NOW());

-- Seat 테이블에 데이터 삽입
INSERT INTO seats (seat_id, seat_code, seat_status, zone_id, seat_grade_id, version, created_at, updated_at)
VALUES (1, 'A01', 'SELECTABLE', 1, 1, 1, NOW(), NOW());