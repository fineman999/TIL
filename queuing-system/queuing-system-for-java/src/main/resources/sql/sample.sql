-- 1. Insert into performances
INSERT INTO performances (
    performance_name,
    performance_place,
    performance_showtime,
    created_at,
    updated_at
) VALUES
    ('Spring Concert 2025', 'Main Concert Hall', '2025-05-15 19:00:00', NOW(), NOW());

-- 2. Insert into seat_grades (referencing performance_id = 1)
INSERT INTO seat_grades (
    grade_name,
    price,
    performance_id,
    created_at,
    updated_at
) VALUES
      ('VIP', 150000, 1, NOW(), NOW()),
      ('Premium', 100000, 1, NOW(), NOW()),
      ('Standard', 70000, 1, NOW(), NOW());

-- 3. Insert into zones (referencing performance_id = 1)
INSERT INTO zones (
    zone_name,
    performance_id,
    created_at,
    updated_at
) VALUES
      ('Zone A - Front', 1, NOW(), NOW()),
      ('Zone B - Middle', 1, NOW(), NOW()),
      ('Zone C - Back', 1, NOW(), NOW());

-- 4. Insert into seats (referencing seat_grade_id and zone_id)
INSERT INTO seats (
    seat_code,
    seat_status,
    version,
    seat_grade_id,
    zone_id,
    created_at,
    updated_at
) VALUES
      ('A-101', 'SELECTABLE', 0, 1, 1, NOW(), NOW()),  -- VIP seat in Zone A
      ('A-102', 'SELECTABLE', 0, 1, 1, NOW(), NOW()),  -- VIP seat in Zone A
      ('B-201', 'SELECTABLE', 0, 2, 2, NOW(), NOW()),  -- Premium seat in Zone B
      ('C-301', 'SELECTABLE', 0, 3, 3, NOW(), NOW());  -- Standard seat in Zone C