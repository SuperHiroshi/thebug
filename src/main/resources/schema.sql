-- バグ展示用テーブル（Bug知識庫）
CREATE TABLE IF NOT EXISTS bug (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(64) NOT NULL,
    bug_code VARCHAR(32) NOT NULL,
    title VARCHAR(512) NOT NULL,
    occurrence_condition CLOB,
    error_message CLOB,
    wrong_code CLOB NOT NULL,
    cause_analysis CLOB,
    correct_code CLOB,
    correct_comment CLOB,
    prevention CLOB,
    sort_order INT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_bug_category ON bug(category);
CREATE INDEX IF NOT EXISTS idx_bug_sort ON bug(category, sort_order);
