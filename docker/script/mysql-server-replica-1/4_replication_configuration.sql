# CHANGE MASTER TO
#     MASTER_HOST = 'maf-mysql-server-source',
#     MASTER_PORT = 3306,
#     MASTER_USER = 'replication_user',
#     MASTER_PASSWORD = 'replication_password',
#     GET_MASTER_PUBLIC_KEY = 1,
#     MASTER_AUTO_POSITION = 1;
#
# START SLAVE;

# https://dev.mysql.com/doc/refman/8.0/en/change-replication-source-to.html
CHANGE REPLICATION SOURCE TO
    SOURCE_HOST = 'maf-mysql-server-source',
    SOURCE_PORT = 3306,
    SOURCE_USER = 'replication_user',
    SOURCE_PASSWORD = 'replication_password',
    GET_SOURCE_PUBLIC_KEY = 1,
    SOURCE_AUTO_POSITION = 1;

START REPLICA;
