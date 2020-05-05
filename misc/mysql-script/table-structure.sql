CREATE TABLE body_part (
    id   bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of body part.'
        PRIMARY KEY,
    name varchar(20) NOT NULL COMMENT 'The name of body part.',
    CONSTRAINT body_part_name_uindex
        UNIQUE (name)
)
    COMMENT 'Human Body Part.';

CREATE TABLE equipment (
    id   bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of exercise equipment.'
        PRIMARY KEY,
    name varchar(50) NOT NULL COMMENT 'The name of equipment.',
    CONSTRAINT equipment_name_uindex
        UNIQUE (name)
)
    COMMENT 'Exercise Equipment.';

CREATE TABLE exercise (
    id                bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of exercise.'
        PRIMARY KEY,
    name              varchar(100) NOT NULL COMMENT 'The name of exercise.',
    preparation       varchar(800) NOT NULL COMMENT 'Exercise preparation description.',
    execution         varchar(800) NOT NULL COMMENT 'Exercise execution description.',
    exercise_gif_path varchar(100) NULL COMMENT 'Exercise GIF image path.',
    CONSTRAINT exercise_name_uindex
        UNIQUE (name)
)
    COMMENT 'Exercise.

https://exrx.net/Lists/Directory';

CREATE TABLE exercise_classification (
    id          bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of exercise classification.'
        PRIMARY KEY,
    name        varchar(20)   NOT NULL COMMENT 'The name of exercise classification.',
    description varchar(1000) NOT NULL COMMENT 'The description of exercise classification.',
    CONSTRAINT exercise_classification_name_uindex
        UNIQUE (name)
)
    COMMENT 'Exercise Classification.

https://exrx.net/WeightTraining/Glossary';

CREATE TABLE exercise_comment (
    id          bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of exercise comment.'
        PRIMARY KEY,
    exercise_id bigint UNSIGNED NOT NULL COMMENT 'The ID of exercise.',
    comment     varchar(2500)   NOT NULL COMMENT 'The comment.',
    CONSTRAINT exercise_comment_exercise_id_uindex
        UNIQUE (exercise_id)
)
    COMMENT 'Exercise Comment.

Relationship:
One exercise to one comment.

If the exercise doen''t have comment,
then this table will still store the comment record for it,
like { id: 1, exercise_id: 1, comment: ''NONE'' }.';

CREATE TABLE exercise_related_classification (
    id                          bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of exercise related classification.'
        PRIMARY KEY,
    exercise_id                 bigint UNSIGNED NOT NULL COMMENT 'The ID of exercise.',
    exercise_classification_id  bigint UNSIGNED NOT NULL COMMENT 'The exercise classification ID.',
    related_classification_type tinyint         NOT NULL COMMENT '1 - Utility, 2 - Mechanics, 3 - Force'
)
    COMMENT 'Exercise Related Classification.

Relationship:
One exercise usually mapping to 3 types of exercise classification.
And one exercise can have more than one specific type of classification.

For example:
One exercise can have 2 records whose types are both Utility.';

CREATE TABLE exercise_related_muscle (
    id                  bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of exercise related muscle.'
        PRIMARY KEY,
    exercise_id         bigint UNSIGNED NOT NULL COMMENT 'The ID of exercise.',
    muscle_id           bigint UNSIGNED NOT NULL COMMENT 'The ID of muscle.',
    related_muscle_type tinyint         NOT NULL COMMENT 'Related muscle type. Muscle movement classification.

1 - Agonist
2 - Antagonist
3 - Target
4 - Synergist
5 - Stabilizer
6 - Dynamic Stabilizer
7 - Antagonist Stabilizer

https://exrx.net/Kinesiology/Glossary#MuscleMovClass'
)
    COMMENT 'Exercise Related Muscle.

Relationship:
One exercise can have 3 (or more) different types of related muscle.
And one exercise can have more than one specific type of related muscles.';

CREATE TABLE kinesiology_glossary (
    id          bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of kinesiology glossary.'
        PRIMARY KEY,
    name        varchar(30)     NOT NULL COMMENT 'The name kinesiology glossary.',
    description varchar(2000)   NULL COMMENT 'The description of kinesiology glossary.',
    parent_id   bigint UNSIGNED NULL COMMENT 'The parent ID of kinesiology glossary.',
    CONSTRAINT kinesiology_glossary_name_uindex
        UNIQUE (name)
)
    COMMENT 'Kinesiology Glossary.

https://exrx.net/Kinesiology/Glossary';

CREATE TABLE muscle (
    id           bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of muscle.'
        PRIMARY KEY,
    name         varchar(50)     NOT NULL COMMENT 'The name of muscle.',
    other_names  varchar(200)    NULL COMMENT 'The muslce''s other names.',
    parent_id    bigint UNSIGNED NULL COMMENT 'The parent ID of muscle.',
    body_part_id bigint UNSIGNED NOT NULL COMMENT 'Related body part ID.',
    CONSTRAINT muscle_name_uindex
        UNIQUE (name)
)
    COMMENT 'Muscle.

https://exrx.net/Lists/Muscle';

CREATE TABLE muscle_image (
    id               bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of muscle image.'
        PRIMARY KEY,
    muscle_id        bigint UNSIGNED NOT NULL COMMENT 'The ID of muslce.',
    image_path       varchar(100)    NOT NULL COMMENT 'The image path of muscle image.',
    alternative_text varchar(100)    NOT NULL COMMENT 'A textual description of the image.',
    CONSTRAINT muscle_image_image_path_uindex
        UNIQUE (image_path)
)
    COMMENT 'Muscle Image.

The relationship:
One muscle to one or more muscle image.';

CREATE TABLE permission (
    id                    bigint UNSIGNED AUTO_INCREMENT COMMENT 'Primary key'
        PRIMARY KEY,
    url                   varchar(200)                                                                          NOT NULL COMMENT 'URL. If type of record is page (1), URL stands for route; if type of record is button (2), URL stands for API',
    description           varchar(100)                                                                          NOT NULL COMMENT 'Permission description',
    type                  tinyint                                                                               NOT NULL COMMENT 'Permission type. 1 - page; 2 - button',
    permission_expression varchar(50)                                                                           NULL COMMENT 'Permission expression',
    method                enum ('GET', 'HEAD', 'POST', 'PUT', 'DELETE', 'CONNECT', 'OPTIONS', 'TRACE', 'PATCH') NULL COMMENT 'HTTP method of API',
    sort                  int     DEFAULT 0                                                                     NOT NULL COMMENT 'Sort number',
    parent_id             bigint  DEFAULT 0                                                                     NULL COMMENT 'Primary key of parent',
    deleted               tinyint DEFAULT 0                                                                     NOT NULL COMMENT 'Deleted flag',
    created_time          datetime                                                                              NOT NULL COMMENT 'Created time',
    modified_time         datetime                                                                              NOT NULL COMMENT 'Modified time',
    CONSTRAINT permission_expression_UNIQUE
        UNIQUE (permission_expression)
)
    COMMENT 'Permission.';

CREATE TABLE related_muscle (
    id                bigint UNSIGNED AUTO_INCREMENT COMMENT 'The ID of related muscle.'
        PRIMARY KEY,
    muscle_id         bigint UNSIGNED NOT NULL COMMENT 'The ID of muscle.',
    related_muscle_id bigint UNSIGNED NOT NULL COMMENT 'Related muscle''s ID.'
)
    COMMENT 'Muscle''s related muscles.';

CREATE TABLE role (
    id            bigint UNSIGNED AUTO_INCREMENT COMMENT 'Primary key'
        PRIMARY KEY,
    name          varchar(50)  NOT NULL COMMENT 'Role name',
    description   varchar(100) NOT NULL COMMENT 'Role description',
    created_time  datetime     NOT NULL COMMENT 'Created time',
    modified_time datetime     NOT NULL COMMENT 'Modified time',
    CONSTRAINT name_UNIQUE
        UNIQUE (name)
)
    COMMENT 'Role.';

CREATE TABLE role_permission (
    role_id       bigint      NOT NULL COMMENT 'Primary key of role',
    permission_id varchar(45) NOT NULL COMMENT 'Primary key of permission',
    PRIMARY KEY (role_id, permission_id)
)
    COMMENT 'Role-permission Relation.';

CREATE TABLE test_table (
    id             bigint UNSIGNED AUTO_INCREMENT
        PRIMARY KEY,
    string_value   varchar(10) NOT NULL,
    int_value      int         NULL,
    double_value   double      NULL,
    datetime_value datetime    NULL
)
    COMMENT 'Test Table for ORM library.';

CREATE TABLE user (
    id            bigint UNSIGNED AUTO_INCREMENT COMMENT 'Primary key of user'
        PRIMARY KEY,
    username      varchar(50)                                                                                                                                                                                                                                                                                                                                                                                                                            NOT NULL COMMENT 'Username',
    email         varchar(100)                                                                                                                                                                                                                                                                                                                                                                                                                           NOT NULL COMMENT 'Email',
    cellphone     varchar(11)                                                                                                                                                                                                                                                                                                                                                                                                                            NULL COMMENT 'Cellphone number',
    password      varchar(60)                                                                                                                                                                                                                                                                                                                                                                                                                            NOT NULL COMMENT 'Password',
    full_name     varchar(255)                                                                                                                                                                                                                                                                                                                                                                                                                           NULL COMMENT 'Full name',
    birthday      date                                                                                                                                                                                                                                                                                                                                                                                                                                   NULL COMMENT 'Birthday',
    gender        enum ('Agender', 'Androgyne', 'Bigender', 'Cisgender', 'Cisgender Female', 'Cisgender Male', 'Female to Male', 'Gender Fluid', 'Gender Nonconforming', 'Gender Questioning', 'Gender Variant', 'Genderqueer', 'Intersex', 'Male to Female', 'Neither', 'Neutrois', 'Non-binary', 'Other', 'Pangender', 'Transfeminine', 'Transgender', 'Transgender Female', 'Transgender Male', 'Transgender Person', 'Transmasculine', 'Two-Spirit') NULL COMMENT '26 gender options',
    avatar        varchar(255)                                                                                                                                                                                                                                                                                                                                                                                                                           NULL COMMENT 'User avatar full path on SFTP server',
    status        tinyint DEFAULT 1                                                                                                                                                                                                                                                                                                                                                                                                                      NOT NULL COMMENT 'Status. 1 - enabled, 2 - disabled',
    created_time  datetime                                                                                                                                                                                                                                                                                                                                                                                                                               NOT NULL COMMENT 'Created time',
    modified_time datetime                                                                                                                                                                                                                                                                                                                                                                                                                               NULL COMMENT 'Modified time',
    CONSTRAINT cellphone_UNIQUE
        UNIQUE (cellphone),
    CONSTRAINT email_UNIQUE
        UNIQUE (email),
    CONSTRAINT username_UNIQUE
        UNIQUE (username)
)
    COMMENT 'User.';

CREATE TABLE user_role (
    user_id bigint NOT NULL COMMENT 'Primary key of user',
    role_id bigint NOT NULL COMMENT 'Primary key of role',
    PRIMARY KEY (user_id, role_id)
)
    COMMENT 'User-role Relation. Roles that users have.';

