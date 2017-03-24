## What?

Pull some configuration for a spring boot app out of AWS' EC2 parameter store. The config is a database password encrypted using KMS.

## Background

AWS [blog](https://aws.amazon.com/blogs/compute/managing-secrets-for-amazon-ecs-applications-using-parameter-store-and-iam-roles-for-tasks/) describes the Parameter Store component of the Systems Manager:

> you can also use it as a generic secret management store

Another [post](https://stelligent.com/2017/03/09/using-parameter-store-with-aws-codepipeline/) describes populating the secrets (or other config) from a build pipeline.

## Approach

Use [PropertySourceLocator](https://github.com/spring-cloud/spring-cloud-commons/blob/master/spring-cloud-context/src/main/java/org/springframework/cloud/bootstrap/config/PropertySourceLocator.java) from spring-cloud-commons.
Approach copied from spring-cloud-vault's [VaultBootstrapConfiguration](https://github.com/spring-cloud/spring-cloud-vault-config/blob/master/spring-cloud-vault-config/src/main/java/org/springframework/cloud/vault/config/VaultBootstrapConfiguration.java).

## Instructions

You will need:

* An AWS account with your credentials sitting in ~/.aws/credentials. Or anywhere else the java-aws-sdk can find them.
* docker or just plain old mysql running on port 3306

Start a transient mysql instance:

    docker run --rm -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD=insecure mysql
    docker exec mysql mysql -uroot -pinsecure --execute 'create database demo_db;'
    
Store an encrypted password in EC2 parameter store:

    aws ssm put-parameter --name "db.password" --type "SecureString" --value "insecure"
    
You should be able to run the app (AwsSsmDemoApplication)
Break it by changing the password:

    aws ssm put-parameter --name "db.password" --type "SecureString" --value "letmein" --overwrite