# 文件管理系统

这是一个基于Spring Boot的文件系统后端服务，提供了文件加解密、传输和管理等功能。

## 技术栈

- Java 17
- Spring Boot 3.3.11
- MyBatis-Plus 3.5.12
- MySQL
- Maven
- Lombok
- Hutool 5.8.38
- Guava 31.1-jre

## 项目结构

```
src/main/java/cn/renranz/backend/
├── config/         # 配置类
├── constant/       # 常量定义
├── controller/     # 控制器层
├── exception/      # 异常处理
├── filter/         # 过滤器
├── mapper/         # MyBatis映射层
├── model/          # 数据模型
├── service/        # 业务逻辑层
└── utils/          # 工具类
```

## 功能特性

- 文件上传和下载
- 文件Aes加解密
- 用户认证和授权
- 异常处理机制

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

## 快速开始

1. 克隆项目
```bash
git clone https://github.com/renranSy/mini-file-system-backend.git
```

2. 配置数据库
- 创建MySQL数据库
- 修改`application.properties`中的数据库配置

3. 编译运行
```bash
mvn clean install
mvn spring-boot:run
```

## 项目配置

主要配置文件位于`src/main/resources/application.properties`，包含：
- 数据库连接配置
- 服务器端口配置
- 文件上传配置
- 其他系统参数

## 开发指南

1. 代码规范
- 遵循阿里巴巴Java开发规范
- 使用Lombok简化代码
- 统一的异常处理机制

2. 分支管理
- main: 主分支
- develop: 开发分支
- feature/*: 功能分支

## 部署说明

1. 打包
```bash
mvn clean package
```

2. 运行
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 发起 Pull Request

## 许可证

[待补充]

## 联系方式

[待补充] 