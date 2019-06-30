# Web高级编程 大作业

## 运行环境

### 操作系统
[Ubuntu Server](https://www.ubuntu.com/download/server) - 18.04.2 LTS x64

### 软件

[MySQL Community Server](https://dev.mysql.com/downloads/mysql/) - 8.0.16

[Redis](https://redis.io/download) - 5.0.5

[MongoDB](https://www.mongodb.com/)

OracleJDK - 1.8_201

Node.js - v10.15.3

NPM - 6.4.1

Windows 10 - 1809 (17763.529)

数据库连接信息参考 [`application.properties`](https://gitee.com/Nchu162012322/webhw/raw/master/src/main/resources/application.properties)

## 构建

### 构建Webpack
```
cd wpack
npm install
npx webpack
```

### 构建Jar
```
gradlew build
```
