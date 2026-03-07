ALTER TABLE tasks
    ALTER COLUMN status SET DEFAULT 'pending';

ALTER TABLE tasks
    ALTER COLUMN priority SET DEFAULT 'medium';  