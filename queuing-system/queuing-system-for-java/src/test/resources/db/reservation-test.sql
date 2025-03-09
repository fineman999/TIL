DELETE
FROM ticket;
DELETE
FROM seats;
DELETE
FROM seat_grades;
DELETE
FROM zones;
DELETE
FROM performances;
DELETE
FROM members;


-- Member 테이블에 데이터 삽입
INSERT INTO members (member_id, email, password, member_role, created_at, updated_at, deleted_at)
VALUES (1, 'test@gmail.com', 'testpassword', 'USER', NOW(), NOW(), NOW());

-- Performance 테이블에 데이터 삽입
INSERT INTO performances (performance_id, performance_name, performance_place, performance_showtime, created_at,
                         updated_at, deleted_at)
VALUES (1, '공연', '장소', '2024-08-23 14:30:00', NOW(), NOW(), NOW());

-- Zone 테이블에 데이터 삽입
INSERT INTO zones (zone_id, zone_name, performance_id, created_at, updated_at, deleted_at)
VALUES (1, 'VIP', 1, NOW(), NOW(), NOW());

-- SeatGrade 테이블에 데이터 삽입
INSERT INTO seat_grades (seat_grade_id, grade_name, price, performance_id, created_at, updated_at, deleted_at)
VALUES (1, 'Grade1', 10000, 1, NOW(), NOW(), NOW());

-- Seat 테이블에 데이터 삽입
INSERT INTO seats (seat_id, seat_code, seat_status, member_id, zone_id, seat_grade_id, version, created_at, updated_at, deleted_at)
VALUES (1, 'A01', 'SELECTED', 1, 1, 1, 1, NOW(), NOW(), NOW());