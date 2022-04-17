CREATE USER 'replication_user'@'%' IDENTIFIED BY 'replication_password';
GRANT REPLICATION SLAVE ON *.* TO 'replication_user'@'%';

CREATE USER 'maf_mysql_rw'@'%' IDENTIFIED BY 'maf@mysql';
GRANT SELECT, INSERT, UPDATE, DELETE ON *.* TO 'maf_mysql_rw'@'%';

CREATE USER 'maf_mysql_r'@'%' IDENTIFIED BY 'maf@mysql';
GRANT SELECT ON *.* TO 'maf_mysql_r'@'%';
