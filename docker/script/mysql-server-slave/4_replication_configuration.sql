CHANGE MASTER TO
    MASTER_HOST = 'maf-mysql-server-master',
    MASTER_PORT = 3306,
    MASTER_USER = 'replication_user',
    MASTER_PASSWORD = 'replication_password',
    GET_MASTER_PUBLIC_KEY = 1,
    MASTER_AUTO_POSITION = 1;

START SLAVE;
