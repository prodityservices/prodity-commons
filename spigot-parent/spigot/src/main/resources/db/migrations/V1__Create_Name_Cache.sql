CREATE TABLE `name_cache` (
  player_id BINARY(16) PRIMARY KEY,
  player_name VARCHAR(16) NOT NULL,
    player_last_seen TIMESTAMP,
  INDEX `name` (player_name)
);
