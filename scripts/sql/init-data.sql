-- decoded password is '123'
INSERT INTO `user` (`id`, `email`, `first_name`, `last_name`, `password`, `is_active`) VALUES
(1, 'admin@mail.com', 'John', 'Doe', '$2a$10$KUT/nlSrR9UxbQqLFSu//eLULd1OdJRHeY.7On5ePczb1DnK03fge', '1');

INSERT INTO `user_role` (`user_id`, `role`) VALUES (1, 'ROLE_USER'),(1, 'ROLE_MANAGER'),(1, 'ROLE_ADMIN');
