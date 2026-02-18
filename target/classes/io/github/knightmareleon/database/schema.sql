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

CREATE TABLE IF NOT EXISTS standard_question (
    id INTEGER PRIMARY KEY,
    type INTEGER NOT NULL CHECK (type >= 0),
    description TEXT NOT NULL,
    set_id INTEGER NOT NULL,
    FOREIGN KEY (set_id)
        REFERENCES study_set(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_std_question_set_id_id
ON standard_question(set_id, id);

CREATE TABLE IF NOT EXISTS tof_question (
    id INTEGER PRIMARY KEY,
    description TEXT NOT NULL,
    is_true INTEGER NOT NULL CHECK (is_true IN (0,1)),
    set_id INTEGER NOT NULL,
    FOREIGN KEY (set_id) 
        REFERENCES study_set(id) 
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_tof_question_set_id_id
ON tof_question(set_id, id);

CREATE TABLE IF NOT EXISTS choice (
    id INTEGER PRIMARY KEY,
    description TEXT NOT NULL,
    answer INTEGER NOT NULL CHECK (answer IN (0,1)),
    q_id INTEGER NOT NULL,
    FOREIGN KEY (q_id)
        REFERENCES standard_question(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_choice_q_id_id
ON choice(q_id, id);