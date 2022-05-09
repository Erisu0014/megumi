create table battle_boss
(
    id varchar(32) not null
        primary key,
    name varchar(200) not null,
    hp_max int not null,
    boss_order int not null
);

create table battle_boss_order
(
    id int auto_increment
        primary key,
    group_id varchar(200) not null,
    qq_id varchar(200) not null,
    boss_order int not null
);

create table battle_damage
(
    id int auto_increment
        primary key,
    group_id varchar(20) not null,
    qq_id varchar(20) not null,
    boss_id varchar(32) not null,
    damage int not null,
    now_boss_id int not null,
    lost double not null,
    damage_date timestamp default CURRENT_TIMESTAMP not null
);

create table battle_group
(
    group_id varchar(20) not null
        primary key,
    group_name varchar(200) not null
);

create table battle_stage
(
    id int not null
        primary key,
    boss1_id varchar(32) not null,
    boss2_id varchar(32) not null,
    boss3_id varchar(32) not null,
    boss4_id varchar(32) not null,
    boss5_id varchar(32) not null
);

create table battle_user
(
    qq_id varchar(20) not null,
    nickname varchar(20) not null,
    group_id varchar(200) not null,
    damage_times double not null,
    primary key (qq_id, group_id)
);

create table dev_remind
(
    id int auto_increment
        primary key,
    group_id varchar(20) not null,
    qq_id varchar(20) not null,
    remind_json varchar(2000) not null
);

create table group_plugin
(
    group_id varchar(20) not null,
    plugin_id varchar(20) not null,
    enabled int default 0 null,
    primary key (group_id, plugin_id)
);

create table jap_words
(
    id int auto_increment
        primary key,
    word varchar(20) not null,
    pseudonym varchar(100) not null,
    chinese varchar(100) not null,
    type varchar(100) not null,
    insertTag int default 0 not null
);

create table now_boss
(
    now_id int auto_increment
        primary key,
    boss_id varchar(32) not null,
    boss_name varchar(200) not null,
    group_id varchar(20) not null,
    hp_now int not null,
    boss_order int not null,
    boss_rounds int not null,
    now_stage int not null
);

create table pcr_avatar
(
    avatar_id varchar(6) not null
        primary key,
    princess_id varchar(4) not null,
    type int not null,
    avatar_url varchar(200) not null
);

create table pcr_emoji
(
    id int auto_increment
        primary key,
    name varchar(200) not null,
    path varchar(200) not null
);

create table pcr_princess
(
    princess_id varchar(4) not null
        primary key,
    name varchar(20) not null,
    guild varchar(100) not null,
    birthday varchar(20) not null,
    age varchar(20) not null,
    height varchar(20) not null,
    weight varchar(20) not null,
    bloodType varchar(20) not null,
    race varchar(100) not null,
    princess_like varchar(100) not null,
    voice varchar(20) not null
);

create table plugin
(
    id varchar(20) not null
        primary key,
    name varchar(20) not null,
    des varchar(100) null
);

create table qq_group
(
    id varchar(20) not null
        primary key,
    name varchar(20) not null
);

create table rss_subscription
(
    id int auto_increment
        primary key,
    group_id varchar(32) not null,
    rss_url varchar(200) not null,
    nickname varchar(200) null,
    type varchar(100) null
);

