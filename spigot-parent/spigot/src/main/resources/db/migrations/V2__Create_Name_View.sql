-- A view of name_cache for looking up players by name.
-- Players changing their name can cause duplicates to
-- appear in the database(although we generally resolve
-- them quickly by checking for duplicates on login), so
-- this view only contains the player_id, player_name, and
-- last_seen of the player who most recently used the name
-- on our server.
-- This is mostly useful for other plugins to join on

-- Not all versions of MySQL support a subquery in the FROM clause so split
-- this into two views.  While views are generally scary from a performance
-- perspective, these views hit only indexed columns, and are the fastest way
-- to solve the group-wise maximum value of a field problem.
CREATE VIEW `name_last_seen` (player_name, last_seen) AS
  SELECT player_name, MAX(last_seen) AS last_seen FROM name_cache GROUP BY player_name;
CREATE VIEW `names`(player_id, player_name, last_seen) AS
  SELECT player_id, player_name, last_seen FROM name_cache
  INNER JOIN `name_last_seen`
  USING (player_name, last_seen);