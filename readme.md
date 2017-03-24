Experiment pulling KMS encrypted spring boot config from EC2 parameter store.

## Instructions

Start a transient mysql instance:

    docker run --rm -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD=insecure mysql
    docker exec mysql mysql -uroot -pinsecure --execute 'create database demo_db;'