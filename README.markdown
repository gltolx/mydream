## 开发
### 代码
+ HTTPS: https://github.com/gltolx/mydream.git
+ SSH: git@github.com:gltolx/mydream.git
+ 添加本地密钥
```sh
  # 1、用ed25519算法生成本地密钥
  ssh-keygen -t ed25519 -C "your_github_email@example.com"
  # 2、复制~/.ssh/id_ed25519.pub的内容到粘贴板
  cat ~/.ssh/id_ed25519.pub
  # 3、粘贴到github的SSH keys中，https://github.com/gltolx/mydream/settings/keys/new
```
### 本地mysql
brew services start mysql

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
+ CentOS安装：
```sh
#    yum install mysql-server -y
    sudo yum update -y
    sudo yum install -y mariadb mariadb-server
    sudo systemctl start mariadb
    # sudo systemctl stop mariadb
```


+ 启动：`service mysqld start`
+ 关闭：`service mysqld stop`
+ 登录：`mysql -u root -p` 密码：[xxxxxx]
+ 初始化：
```sh
    mysql> source /root/app/mydream-java/sql/schema_v1.sql
    mysql> source /root/app/mydream-java/sql/insert_v1.sql
```

### ChatGPT key 
> - gltolx@163.com OPENAI_API_KEY 
> - 867673848@qq.com OPENAI_API_KEY_1

存放在系统环境变量中, java中通过以下方式获取, 其中"OPENAI_API_KEY_1"通过配置文件获取:
```java
    System.getenv("OPENAI_API_KEY_1");
```
apiKey别名，在系统环境变量中设置和查看:
```shell
> vim .bash_profile
> export OPENAI_API_KEY_1='sk-************'
> source .bash_profile 
```

