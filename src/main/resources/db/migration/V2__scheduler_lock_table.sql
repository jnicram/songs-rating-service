CREATE TABLE shedLock
(
    name       VARCHAR(64) PRIMARY KEY,
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255)
)
