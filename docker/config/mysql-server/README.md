# How to Configure MySQL Cluster

[TOC]

## File Description

These files below are MySQL initialization scripts:

```
script
  mysql
    init_replication_user.sql
    muscle_and_fitness.sql
```

## Useful MySQL command

### On Slave MySQL Server

1. Connect to MySQL

   ```mysql
   mysql -u root -pjm@mysql
   ```

2. Change master server configuration

   ```mysql
   CHANGE MASTER TO 
   MASTER_HOST='maf-mysql-server-master-development-docker',
   MASTER_PORT=3306,
   MASTER_USER='replication_user',
   MASTER_PASSWORD='replication_password',
   GET_MASTER_PUBLIC_KEY=1,
   MASTER_AUTO_POSITION=1;
   ```

3. Start slave

   ```mysql
   START SLAVE;
   ```

   

