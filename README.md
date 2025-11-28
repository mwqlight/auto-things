# 比佳佳商城 - 智能比价平台

比佳佳商城是一个智能比价平台，聚合各大电商平台的商品信息，为用户提供一站式购物决策支持。

## 项目简介

比佳佳商城的主要功能类似于京东、淘宝等电商平台，但核心差异在于：

1. **无需登录** - 用户可以直接浏览商品、比较价格，无需注册登录
2. **智能比价** - 聚合各大电商平台的商品信息，形成对比图表
3. **AI推荐** - 利用AI大模型为用户提供购买建议
4. **搬运工模式** - 不负责销售商品，仅提供各平台商品信息对比

## 技术栈

### 后端技术
- Spring Boot
- Spring Data JPA
- Spring Data Redis
- MySQL
- Spring AI Alibaba (集成AI大模型)
- Jsoup (网页爬虫)

### 前端技术
- Vue 3
- Element Plus
- Axios
- Vue Router

## 功能特性

1. **商品搜索** - 支持关键词搜索，爬取各大商城商品信息
2. **价格对比** - 将不同平台的同类商品进行价格对比
3. **AI建议** - 利用AI大模型分析商品信息，提供购买建议
4. **历史记录** - 记录用户搜索历史，用于个性化推荐

## 数据库配置

- 数据库名称：auto_things
- 数据库用户名：root
- 数据库密码：root
- 数据库端口：3306
- 数据库主机：localhost

## Redis配置

- Redis主机：localhost
- Redis端口：6379
- Redis密码：
- Redis数据库索引：0

## 项目结构

```
.
├── backend              # 后端代码
│   ├── src/main/java    # Java源代码
│   │   └── com.bjj.mall # 包结构
│   │       ├── entity   # 实体类
│   │       ├── repository # 数据访问层
│   │       ├── service  # 业务逻辑层
│   │       ├── controller # 控制器层
│   │       ├── crawler  # 爬虫模块
│   │       └── ai       # AI模块
│   └── src/main/resources # 配置文件
└── frontend             # 前端代码
    ├── public           # 静态资源
    └── src              # Vue源代码
        ├── assets       # 静态资源
        ├── components   # 组件
        ├── views        # 页面视图
        ├── router       # 路由配置
        ├── store        # 状态管理
        ├── utils        # 工具函数
        └── styles       # 样式文件
```

## 部署说明

### 后端部署

1. 确保已安装Java 8+和Maven
2. 修改`backend/src/main/resources/application.yml`中的数据库和Redis配置
3. 在项目根目录执行以下命令构建项目：
   ```bash
   cd backend
   mvn clean package
   ```
4. 运行打包后的JAR文件：
   ```bash
   java -jar target/mall-backend-0.0.1-SNAPSHOT.jar
   ```

### 前端部署

1. 确保已安装Node.js和npm
2. 安装依赖：
   ```bash
   cd frontend
   npm install
   ```
3. 开发环境运行：
   ```bash
   npm run serve
   ```
4. 生产环境构建：
   ```bash
   npm run build
   ```

## API接口

### 搜索商品
```
GET /api/search/products?keyword={关键词}
```

### 保存搜索记录
```
POST /api/search/record
```

## 开发计划

- [x] 完成项目架构设计
- [x] 实现后端基础框架
- [x] 实现前端基础框架
- [ ] 完善爬虫功能，接入真实电商平台数据
- [ ] 完善AI推荐功能，集成实际的大模型API
- [ ] 实现用户界面优化
- [ ] 完善测试用例
- [ ] 部署上线

## 贡献指南

欢迎提交Issue和Pull Request来改进本项目。

## 许可证

[MIT License](LICENSE)