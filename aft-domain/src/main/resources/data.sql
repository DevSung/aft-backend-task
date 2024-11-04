-- user 테이블에 기초 데이터 추가
INSERT INTO "user" (user_id, password, name, reg_date)
SELECT 'admin',
       '$2a$10$zeymu6LZY70/MzTVMa067OrNu5QDAuCV9IyM85oym8tO1D7Ke5xGW',
       '관리자',
       NOW() WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE user_id = 'admin');

-- user_role 테이블에 기초 데이터 추가
INSERT INTO user_role (user_idx, role_type)
SELECT 1,
       'ROLE_ADMIN' WHERE EXISTS (SELECT 1 FROM "user" WHERE user_id = 'admin');

-- board 테이블에 기초 데이터 추가
INSERT INTO board (title, content, user_idx, status, reg_date)
SELECT title, content, 1, status, NOW()
FROM (SELECT '첫 번째 게시글' AS title, '이것은 첫 번째 게시글의 내용입니다.' AS content, 'Y' AS status
      UNION ALL
      SELECT '두 번째 게시글', '이것은 두 번째 게시글의 내용입니다.', 'Y'
      UNION ALL
      SELECT '세 번째 게시글', '이것은 세 번째 게시글의 내용입니다.', 'Y'
      UNION ALL
      SELECT '네 번째 게시글', '이것은 네 번째 게시글의 내용입니다 (비공개).', 'N'
      UNION ALL
      SELECT '다섯 번째 게시글', '이것은 세 번째 게시글의 내용입니다 (삭제).', 'D') AS tmp
WHERE EXISTS (SELECT 1 FROM "user" WHERE user_id = 'admin');