INSERT INTO users (id, login, pwd, first_name, last_name, email, registration_time) values
(1, 'admin', '$2a$10$Cj4nAygXM0WSJa1SjMsChOPRh5t2VXc5r57S37T0EftV/.W8.gKRS', 'admin', 'admin', 'admin@admin.net', now());

INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);