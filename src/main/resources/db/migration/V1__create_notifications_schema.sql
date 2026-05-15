CREATE TABLE known_user (
    id uuid PRIMARY KEY,
    email varchar(255) NOT NULL,
    first_name varchar(120),
    last_name varchar(120),
    role varchar(60),
    active boolean NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE TABLE notification (
    id uuid PRIMARY KEY,
    type varchar(80) NOT NULL,
    title varchar(255) NOT NULL,
    message text NOT NULL,
    source_service varchar(80) NOT NULL,
    source_entity_type varchar(80) NOT NULL,
    source_entity_id uuid NOT NULL,
    source_entity_name varchar(255),
    occurred_at timestamptz NOT NULL,
    created_at timestamptz NOT NULL
);

CREATE TABLE user_notification (
    id uuid PRIMARY KEY,
    notification_id uuid NOT NULL REFERENCES notification(id) ON DELETE CASCADE,
    user_id uuid NOT NULL,
    read_flag boolean NOT NULL DEFAULT false,
    created_at timestamptz NOT NULL,
    read_at timestamptz,
    CONSTRAINT uk_user_notification UNIQUE (notification_id, user_id)
);

CREATE INDEX idx_known_user_active ON known_user(active);
CREATE INDEX idx_notification_source ON notification(source_service, source_entity_type, source_entity_id);
CREATE INDEX idx_user_notification_user_created ON user_notification(user_id, created_at DESC);
CREATE INDEX idx_user_notification_user_read ON user_notification(user_id, read_flag);
