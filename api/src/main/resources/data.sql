INSERT INTO board(board_name)
VALUES ('board1'),
       ('board2'),
       ('board3');

INSERT INTO board_user(username, password, email, status, created_at)
VALUES ('userA', '{bcrypt}$2a$10$6u/uBlfp5hw8cKLLL/pbsu2a3/DQYePmD05Q4IZl0WltKPO59kYMy',
        'userA@google.com', 'VERIFY', NOW()),
       ('userB', '{bcrypt}$2a$10$6u/uBlfp5hw8cKLLL/pbsu2a3/DQYePmD05Q4IZl0WltKPO59kYMy',
        'userB@google.com', 'VERIFY', NOW()),
       ('userC', '{bcrypt}$2a$10$6u/uBlfp5hw8cKLLL/pbsu2a3/DQYePmD05Q4IZl0WltKPO59kYMy',
        'userC@google.com', 'VERIFY', NOW()),
       ('userD', '{bcrypt}$2a$10$6u/uBlfp5hw8cKLLL/pbsu2a3/DQYePmD05Q4IZl0WltKPO59kYMy',
        'userD@google.com', 'VERIFY', NOW()),
       ('userE', '{bcrypt}$2a$10$6u/uBlfp5hw8cKLLL/pbsu2a3/DQYePmD05Q4IZl0WltKPO59kYMy',
        'userE@google.com', 'VERIFY', NOW())
;

INSERT INTO article(board_id, user_id, title, content, created_at)
VALUES (1, 1, 'title1', 'content1', '2024-07-20 00:00:00'),
       (1, 1, 'title2', 'content2', '2024-07-20 00:00:00'),
       (1, 2, 'title3', 'content3', '2024-07-20 00:00:00'),
       (1, 3, 'title4', 'content4', '2024-07-20 00:00:00'),
       (1, 2, 'title5', 'content5', '2024-07-20 00:00:00'),
       (1, 3, 'title6', 'content6', '2024-07-20 00:00:00')
;
