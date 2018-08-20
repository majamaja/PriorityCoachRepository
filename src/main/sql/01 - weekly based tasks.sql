
-- no more default actions
ALTER TABLE life_upgrade_actions DROP COLUMN is_custom;

 -- make user required
DELETE FROM life_upgrade_actions WHERE user_id IS NULL;
ALTER TABLE life_upgrade_actions MODIFY user_id CHAR(36) NOT NULL;

-- add timesPerWeek
ALTER TABLE life_upgrade_actions ADD COLUMN times_per_week INT NOT NULL CHECK (timesPerWeek > 0);



-- Upgrade Actions will track the user commitment
ALTER TABLE user_actions_log ADD COLUMN life_upgrade_action_id CHAR(36);

UPDATE user_actions_log l
SET life_upgrade_action_id = (SELECT i.action_id FROM user_action_items i
WHERE i.id = l.user_action_item_id);

ALTER TABLE user_actions_log MODIFY life_upgrade_action_id CHAR(36) NOT NULL;
ALTER TABLE user_actions_log ADD CONSTRAINT user_actions_log_life_upgrade_action_id_fk FOREIGN KEY (life_upgrade_action_id) REFERENCES life_upgrade_actions(id);

ALTER TABLE user_actions_log DROP FOREIGN KEY user_actions_log_user_action_item_id_fk;
ALTER TABLE user_actions_log DROP COLUMN user_action_item_id;



DROP TABLE user_action_items;
