ALTER TABLE notification
    ADD COLUMN IF NOT EXISTS title_key varchar(255),
    ADD COLUMN IF NOT EXISTS message_key varchar(255),
    ADD COLUMN IF NOT EXISTS params_json text NOT NULL DEFAULT '{}';

UPDATE notification
SET title_key = CASE source_entity_type
        WHEN 'GAME' THEN 'notifications.inventory.gameDeleted.title'
        WHEN 'PLATFORM' THEN 'notifications.inventory.platformDeleted.title'
        WHEN 'STUDIO' THEN 'notifications.inventory.studioDeleted.title'
        ELSE 'notifications.inventory.itemDeleted.title'
    END,
    message_key = CASE source_entity_type
        WHEN 'GAME' THEN 'notifications.inventory.gameDeleted.message'
        WHEN 'PLATFORM' THEN 'notifications.inventory.platformDeleted.message'
        WHEN 'STUDIO' THEN 'notifications.inventory.studioDeleted.message'
        ELSE 'notifications.inventory.itemDeleted.message'
    END,
    params_json = jsonb_build_object('name', COALESCE(source_entity_name, ''))::text
WHERE title_key IS NULL
   OR message_key IS NULL;

ALTER TABLE notification
    ALTER COLUMN title_key SET NOT NULL,
    ALTER COLUMN message_key SET NOT NULL;
