INSERT INTO board(board_name)
VALUES ('board1'),
       ('board2'),
       ('board3');

INSERT INTO board_user(username, password, email, status, created_at)
VALUES ('userA', '1234', 'userA@google.com', 'VERIFY', NOW()),
       ('userB', '1234', 'userB@google.com', 'VERIFY', NOW()),
       ('userC', '1234', 'userC@google.com', 'VERIFY', NOW()),
       ('userD', '1234', 'userD@google.com', 'VERIFY', NOW()),
       ('userE', '1234', 'userE@google.com', 'VERIFY', NOW())
;

INSERT INTO article(board_id, user_id, title, content, created_at)
VALUES (1, 1, 'title1', 'content1', '2024-07-20 00:00:00'),
       (1, 1, 'title2', 'content2', '2024-07-20 00:00:00'),
       (1, 2, 'title3', 'content3', '2024-07-20 00:00:00'),
       (1, 3, 'title4', 'content4', '2024-07-20 00:00:00'),
       (1, 2, 'title5', 'content5', '2024-07-20 00:00:00'),
       (1, 3, 'title6', 'content6', '2024-07-20 00:00:00')
;
