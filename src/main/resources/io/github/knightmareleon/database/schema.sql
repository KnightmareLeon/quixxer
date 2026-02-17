CREATE TABLE IF NOT EXISTS study_set (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    subject TEXT NOT NULL,
    imgpath TEXT,
    created_on TEXT NOT NULL,
    last_taken_on TEXT,
    total_takes INTEGER NOT NULL DEFAULT 0,
    UNIQUE(title, subject)
);