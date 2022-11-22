## 运维部署
### 环境
+ 阿里云esc：https://ecs.console.aliyun.com/home?spm=5176.12818093.ProductAndResource--ali--widget-product-recent.dre2.3be916d0dUh1gt#/
+ 登录实例：https://ecs-workbench.aliyun.com/?accounttraceid=d7250b81d6d3456583df6108d774a68fwwrw


### 打包
本地
```sh
    mvn clean package -U -DskipTests
    scp mydream-java.tar.gz root@120.26.126.100:/root/app/
```
远程实例
```sh
    cd /root/app
    [先关闭原来服务: jps kill-9/ sh xxx.sh stop]
    mv mydream-java mydream-java-tmp
    mkdir -p mydream-java
    tar -zxvf mydream-java.tar.gz -C mydream-java/
    ./mydream-java/bin/mydream.sh start
```

### Mysql
+ 启动：`service mysqld start`
+ 关闭：`service mysqld stop`
+ 登录：`mysql -u root -p` 密码：[xxxxxx]
+ 初始化：
```sh
    mysql> source /root/app/mydream-java/sql/schema_v1.sql
    mysql> source /root/app/mydream-java/sql/insert_v1.sql
```
