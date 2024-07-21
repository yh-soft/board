create table article
(
    article_id bigint not null auto_increment,
    board_id   bigint,
    created_at timestamp(6),
    updated_at timestamp(6),
    user_id    bigint,
    content    varchar(255),
    title      varchar(255),
    primary key (article_id)
);
create table board
(
    board_id   bigint       not null auto_increment,
    board_name varchar(255) not null,
    primary key (board_id)
);
create table board_user
(
    user_id    bigint       not null auto_increment,
    email      varchar(255) not null,
    password   varchar(255) not null,
    username   varchar(255) not null unique,
    status     enum ('LOCKED','REGISTER','VERIFY') not null,
    created_at timestamp(6),
    primary key (user_id)
);
alter table if exists article add constraint FK2y7w132xb5xp1aiouig87aqjo foreign key (board_id) references board;
alter table if exists article add constraint FK2rv7lek3nu7u2qij9fm5x275r foreign key (user_id) references board_user;
