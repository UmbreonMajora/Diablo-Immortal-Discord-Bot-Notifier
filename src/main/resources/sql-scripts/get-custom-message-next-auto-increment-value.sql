SELECT AUTO_INCREMENT
FROM information_schema.tables
WHERE table_name = 'custom_messages'
  AND table_schema = DATABASE();