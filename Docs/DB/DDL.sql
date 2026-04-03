-- 기존 테이블 전체 삭제
DROP TABLE IF EXISTS tbAILog;
DROP TABLE IF EXISTS tbHighlight;
DROP TABLE IF EXISTS tbDiary;
DROP TABLE IF EXISTS tbBook;
DROP TABLE IF EXISTS tbUser;

-- 1. tbUser
CREATE TABLE tbUser (
    Idx_User    BIGINT          NOT NULL AUTO_INCREMENT,
    ID          VARCHAR(100)    NOT NULL,
    Pass        VARCHAR(255)    NOT NULL,
    Email       VARCHAR(100)    NULL,
    Name        VARCHAR(50)     NOT NULL,
    Role        ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',
    Status      ENUM('ACTIVE','SUSPENDED')     NOT NULL DEFAULT 'ACTIVE',
    CreateDate  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UpdateDate  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (Idx_User),
    UNIQUE KEY UK_ID    (ID),
    UNIQUE KEY UK_Email (Email)
);

-- 2. tbBook
CREATE TABLE tbBook (
    Idx_Book    BIGINT          NOT NULL AUTO_INCREMENT,
    Isbn        VARCHAR(20)     NOT NULL,
    Title       VARCHAR(255)    NOT NULL,
    Author      VARCHAR(100)    NULL,
    Publisher   VARCHAR(100)    NULL,
    ImageURL    VARCHAR(500)    NULL,
    PublishDate DATE            NULL,
    PRIMARY KEY (Idx_Book),
    UNIQUE KEY UK_Isbn (Isbn)
);

-- 3. tbDiary
CREATE TABLE tbDiary (
    Idx_Diary   BIGINT          NOT NULL AUTO_INCREMENT,
    Idx_User    BIGINT          NOT NULL,
    Idx_Book    BIGINT          NOT NULL,
    StartDate   DATE            NULL,
    EndDate     DATE            NULL,
    Rating      DECIMAL(2,1)    NULL,
    Favorite    BOOLEAN         NOT NULL DEFAULT FALSE,
    MemoTitle   VARCHAR(100)    NULL,
    MemoContent TEXT            NULL,
    Status      ENUM('READING','DONE','WANT') NOT NULL DEFAULT 'WANT',
    CreateDate  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (Idx_Diary),
    UNIQUE KEY UK_User_Book (Idx_User, Idx_Book),
    FOREIGN KEY (Idx_User) REFERENCES tbUser(Idx_User) ON DELETE CASCADE,
    FOREIGN KEY (Idx_Book) REFERENCES tbBook(Idx_Book) ON DELETE CASCADE
);

-- 4. tbHighlight
CREATE TABLE tbHighlight (
    Idx_Highlight   BIGINT      NOT NULL AUTO_INCREMENT,
    Idx_Diary       BIGINT      NOT NULL,
    PageNumber      INT         NULL,
    HighlightText   TEXT        NOT NULL,
    CreateDate      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UpdateDate      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (Idx_Highlight),
    FOREIGN KEY (Idx_Diary) REFERENCES tbDiary(Idx_Diary) ON DELETE CASCADE
);

-- 5. tbAILog
CREATE TABLE tbAILog (
    Idx_AiLog        BIGINT      NOT NULL AUTO_INCREMENT,
    Idx_User         BIGINT      NOT NULL,
    Idx_Book         BIGINT      NULL,
    PromptSummary    TEXT        NULL,
    Result           JSON        NULL,
    PromptTokens     INT         NULL,
    CompletionTokens INT         NULL,
    TotalTokens      INT         NULL,
    CreateDate       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (Idx_AiLog),
    FOREIGN KEY (Idx_User) REFERENCES tbUser(Idx_User) ON DELETE CASCADE,
    FOREIGN KEY (Idx_Book) REFERENCES tbBook(Idx_Book) ON DELETE CASCADE
);