DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS board;

-- user 테이블 생성
CREATE TABLE "user"
(
    idx      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  VARCHAR(255),
    password VARCHAR(255),
    name     VARCHAR(30),
    reg_date DATETIME,
    mod_date DATETIME
);

-- user_role 테이블 생성
CREATE TABLE user_role
(
    idx       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_idx  BIGINT,
    role_type VARCHAR(10)
);

-- board 테이블 생성
CREATE TABLE board
(
    idx       BIGINT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(255),
    content   TEXT,
    user_idx  BIGINT,
    view_count INT DEFAULT 0,
    status    varchar(5) DEFAULT 'Y',
    reg_date  DATETIME,
    mod_date  DATETIME
);