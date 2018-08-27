-- Authentication and users
CREATE TABLE native_users (
  id                        CHAR(36) PRIMARY KEY,
  email                     VARCHAR(255) NOT NULL UNIQUE,
  password                  CHAR(64)
) engine = InnoDB;


CREATE TABLE facebook_users (
  id                        CHAR(36) PRIMARY KEY,
  fb_id                     VARCHAR(255) UNIQUE,
  fb_token                  VARCHAR(255) UNIQUE
) engine = InnoDB;


CREATE TABLE gplus_users (
  id                        CHAR(36) PRIMARY KEY,
  gplus_id                  VARCHAR(255) UNIQUE,
  gplus_token               VARCHAR(255) UNIQUE
) engine = InnoDB;


CREATE TABLE users (
  id                        CHAR(36) PRIMARY KEY,
  name                      VARCHAR(255) NOT NULL,
  email                     VARCHAR(255) NOT NULL UNIQUE,
  phone                     VARCHAR(16),
  native_user_id            CHAR(36),
  facebook_user_id          CHAR(36),
  gplus_user_id             CHAR(36),
  registration_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login                TIMESTAMP NULL,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT users_native_user_id_fk FOREIGN KEY (native_user_id) REFERENCES native_users(id),
  CONSTRAINT users_facebook_user_id_fk FOREIGN KEY (facebook_user_id) REFERENCES facebook_users(id),
  CONSTRAINT users_gplus_user_id_fk FOREIGN KEY (gplus_user_id) REFERENCES gplus_users(id),
  CONSTRAINT users_auth_type_ck CHECK(native_user_id IS NOT NULL OR facebook_user_id IS NOT NULL OR gplus_user_id IS NOT NULL)

) engine = InnoDB;


CREATE TABLE user_devices (
  user_id                   CHAR(36),
  device_id                 VARCHAR(255),
  type                      VARCHAR(100) NOT NULL CHECK(type IN ('apple', 'android')),
  apn_token                 VARCHAR(255),

  CONSTRAINT user_devices_pk PRIMARY KEY (user_id, device_id),
  CONSTRAINT user_devices_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
) engine = InnoDB;


CREATE TABLE life_upgrade_categories (
  id                        CHAR(36) PRIMARY KEY,
  name                      VARCHAR(255),
  icon                      BLOB,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted                BOOL NOT NULL DEFAULT false
) engine = InnoDB;


CREATE TABLE life_upgrade_actions (
  id                        CHAR(36) PRIMARY KEY,
  life_upgrade_category_id  CHAR(36) NOT NULL,
  name                      VARCHAR(255),
  user_id                   CHAR(36) NOT NULL,
  times_per_week            INT NOT NULL CHECK (timesPerWeek > 0),
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted                BOOL NOT NULL DEFAULT false,

  CONSTRAINT life_upgrade_actions_life_upgrade_category_id_fk FOREIGN KEY (life_upgrade_category_id) REFERENCES life_upgrade_categories(id),
  CONSTRAINT life_upgrade_actions_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
) engine = InnoDB;


CREATE TABLE user_actions_log (
  id                        CHAR(36) PRIMARY KEY,
  user_id                   CHAR(36),
  life_upgrade_action_id    CHAR(36) NOT NULL,
  action_date               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status                    VARCHAR(32) DEFAULT 'done',
  times_done                INT DEFAULT 1 CHECK (times_done >= 0),
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted                BOOL NOT NULL DEFAULT false,

  CONSTRAINT user_actions_log_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT user_actions_log_life_upgrade_action_id_fk FOREIGN KEY (life_upgrade_action_id) REFERENCES life_upgrade_actions(id)
) engine = InnoDB;


CREATE TABLE happiness_level_checkins (
  id                        CHAR(36) PRIMARY KEY,
  user_id                   CHAR(36),
  level                     INT NOT NULL CHECK (1 <= level AND level <= 10),
  checkin_date              DATE NOT NULL,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT happiness_level_checkins_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
) engine = InnoDB;


CREATE TABLE user_notes (
  id                        CHAR(36) PRIMARY KEY,
  user_id                   CHAR(36) NOT NULL,
  note_date                 TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  title                     VARCHAR(512) NOT NULL,
  content                   TEXT NOT NULL,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted                BOOL NOT NULL DEFAULT false,

  CONSTRAINT user_notes_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
) engine = InnoDB;


CREATE TABLE invitations (
  id                        CHAR(36) PRIMARY KEY,
  user_id                   CHAR(36) NOT NULL,
  invitee_id                CHAR(36),
  email                     VARCHAR(256),
  phone                     VARCHAR(256),
  token                     VARCHAR(512) NOT NULL,
  invitation_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT invitations_email_phone_ck CHECK(email IS NOT NULL OR phone IS NOT NULL),
  CONSTRAINT invitations_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT invitations_invitee_id_fk FOREIGN KEY (invitee_id) REFERENCES users(id)
) engine = InnoDB;


CREATE TABLE user_friends (
  id                        CHAR(36) PRIMARY KEY,
  user_id                   CHAR(36) NOT NULL,
  friend_id                 CHAR(36),
  friend_email              VARCHAR(255),
  friend_phone              VARCHAR(32),
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted                BOOL NOT NULL DEFAULT false,

  CONSTRAINT user_friends_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT user_friends_friend_id_fk FOREIGN KEY (friend_id) REFERENCES users(id)
) engine = InnoDB;
-- status:
--     if friend_id is empty && email/phone not empty -> invited
--     if friend_id not empty -> accepted
--     if friend_id && email && phone are empty -> invalid


CREATE TABLE user_friend_permissions (
  id                        CHAR(36) PRIMARY KEY,
  user_id                   CHAR(36) NOT NULL,
  friendship_id             CHAR(36) NOT NULL,
  life_upgrade_action_id    CHAR(36) NOT NULL,
  visible                   BOOL NOT NULL DEFAULT true,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT user_friend_permissions_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT user_friend_permissions_friendship_id_fk FOREIGN KEY (friendship_id) REFERENCES user_friends(id),
  CONSTRAINT user_friend_permissions_life_upgrade_action_id_fk FOREIGN KEY (life_upgrade_action_id) REFERENCES life_upgrade_actions(id)
) engine = InnoDB;
-- NOTES:
-- 1. No need for is_deleted, as you just need to set “visible” to false
-- 2. If an action_item record does not exists, this means the friend doesn’t have permission to see it.


CREATE TABLE user_messages (
  id                        CHAR(36) PRIMARY KEY,
  user_id_from              CHAR(36) NOT NULL,
  user_id_to                CHAR(36) NOT NULL,
  message                   VARCHAR(4096) NOT NULL,
  send_at                   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_modified             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted                BOOL NOT NULL DEFAULT false,

  CONSTRAINT user_messages_user_id_from_fk FOREIGN KEY (user_id_from) REFERENCES users(id),
  CONSTRAINT user_messages_user_id_to_fk FOREIGN KEY (user_id_to) REFERENCES users(id)
) engine = InnoDB;


CREATE TABLE system_parameters
(
  parameter_key               VARCHAR(128) PRIMARY KEY,
  parameter_value             VARCHAR(4096),
  parameter_blob              BLOB
) engine = InnoDB;

INSERT INTO system_parameters (parameter_key, parameter_value, parameter_blob) VALUES
  ('APN Is Production', 'False', NULL),
  ('APN Certificate', NULL, NULL),
  ('APN Password', '', NULL);


CREATE VIEW user_permissions AS (
    SELECT
        ufp.user_id as user_id,
        uf.friend_id as access_to,
        ufp.life_upgrade_action_id,
        ufp.visible
    FROM user_friend_permissions ufp
    JOIN user_friends uf ON ufp.friendship_id = uf.id AND ufp.user_id = uf.user_id
    WHERE uf.is_deleted = false
    UNION ALL
    SELECT
        ufp.user_id as user_id,
        uf.user_id as access_to,
        ufp.life_upgrade_action_id,
        ufp.visible
    FROM user_friend_permissions ufp
    JOIN user_friends uf ON ufp.friendship_id = uf.id AND ufp.user_id = uf.friend_id
    WHERE uf.is_deleted = false
);