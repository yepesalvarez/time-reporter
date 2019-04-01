INSERT INTO `role`(`description`, `name`)
SELECT * FROM (SELECT 'USER ROLE','USER') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM `role` WHERE name = 'USER'
) LIMIT 1;
INSERT INTO `role`(`description`, `name`)
SELECT * FROM (SELECT 'ADMIN ROLE','ADMIN') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM `role` WHERE name = 'ADMIN'
) LIMIT 1;