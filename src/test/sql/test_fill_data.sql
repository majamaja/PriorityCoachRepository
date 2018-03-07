BEGIN;

INSERT INTO system_properties (name, value) VALUES
    ('SMTP_HOST', 'smtp.mailtrap.io'),
    ('SMTP_PORT', '25'),
    ('SMTP_USERNAME', 'bbd973d0b163ae'),
    ('SMTP_PASSWORD', 'ace2e774f42250'),
    ('SMTP_FROM', 'noreply@futurist-labs.com'),
    ('SMTP_FROM_ALIAS', 'Futurist Labs'),
    ('SMTP_USE_SSL', 'true');

INSERT INTO static_pages (id, name, header, content)
VALUES
    ('9887ef76-e746-4a24-8659-e83087e46327', 'about_us', 'About Us', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.'),
    ('bb3d9b31-2789-49a7-88c8-0e0936cd747a', 'terms', 'Terms & Conditions', 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.');

INSERT INTO users (email, name, password)
    VALUES ('zhilkov@futurist-labs.com', 'Gencho Zhilkov', 'd404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db');

COMMIT;
