# Seed
一个开箱即用的 Spring Boot 3 后端模板，集成 RBAC 权限、JWT 认证、文件上传、Redis 缓存等常用功能，适合快速启动新项目。
## 技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 最低要求 |
| Spring Boot | 3.5.14 | 基础框架 |
| MyBatis-Plus | 3.5.9 | ORM + 通用 CRUD |
| MySQL | 8.0 | 数据库 |
| Redis | 3.2.100 | 缓存 / Token 存储 |
| jjwt | 0.12.6 | JWT 生成与校验 |
| SpringDoc | 2.8.6 | Swagger UI 接口文档 |
| Docker Compose | - | 一键启动 MySQL + Redis |
## 功能清单
- **用户认证**：注册 / 登录 / 修改密码，BCrypt 加密，JWT + Redis 双 Token 管理
- **RBAC 权限**：用户 → 角色 → 菜单/权限点，`@RequiresPerms` 注解鉴权，权限缓存 30 分钟
- **文件上传下载**：本地存储，支持扩展名白名单、文件大小限制
- **统一响应体**：`Result<T>` 包装，全局异常处理
- **XSS 防护**：请求参数过滤
- **登录限流**：Guava RateLimiter
- **接口文档**：Swagger UI 自动生成
- **逻辑删除**：MyBatis-Plus 全局逻辑删除
- **自动填充**：创建时间 / 更新时间自动写入
- **多环境配置**：dev / prod 分离，敏感信息环境变量化
- **Docker Compose**：MySQL + Redis 一键启动
- **健康检查**：Actuator + ServiceHealthChecker
## 快速开始
### 前置条件
- JDK 17+
- Maven 3.5+
- MySQL 8.0+
- Redis 3.2+
### 1. 克隆项目
```bash
git clone https://github.com/learning-to-code-pxq/seed.git
cd seed
```
### 2. 初始化数据库

执行 `src/main/resources/sql/V1_init_schema.sql` 创建表结构。

### 3. 配置

修改 `src/main/resources/application-dev.yml` 中的数据库和 Redis 连接信息：

```yaml
spring:
  datasource:
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      password: ${REDIS_PASSWORD:}
jwt:
  secret: ${JWT_SECRET:please-change-this-secret-in-production}
```
也可以通过环境变量覆盖默认值。

### 4. 运行

```bash
mvn spring-boot:run
```
启动后访问：

- 接口地址：`http://localhost:8801`
- Swagger UI：`http://localhost:8801/swagger-ui.html`

使用 Docker Compose（可选）

```bash
docker-compose up -d
```
会启动 MySQL（3306）和 Redis（6379）。

### 项目结构

```plaintext
com.tools.seed
├── common/                  # 通用模块
│   ├── annotation/          # 自定义注解（@RequiresPerms）
│   ├── aspect/              # AOP 切面（权限校验）
│   ├── dto/                 # 通用 DTO
│   ├── exception/           # 全局异常处理
│   ├── limiter/             # 限流器
│   ├── result/              # 统一响应体
│   ├── validator/           # 自定义校验
│   ├── vo/                  # 通用 VO
│   └── xss/                 # XSS 过滤
├── config/                  # 配置类
│   ├── CorsFilter           # 跨域
│   ├── JwtInterceptor       # JWT 拦截器
│   ├── MyMetaObjectHandler  # 自动填充
│   └── ServiceHealthChecker # 健康检查
├── controller/              # 认证 / 用户 / 文件
├── entity/                  # 基础实体
├── mapper/                  # 基础 Mapper
├── service/                 # 基础 Service
└── system/                  # RBAC 模块
    ├── controller/          # 角色 / 菜单 / 权限点 接口
    ├── dto/                 # RBAC 相关 DTO
    ├── entity/              # RBAC 实体
    ├── mapper/              # RBAC Mapper
    └── service/             # RBAC Service
```
### 环境变量

| 变量 | 默认值 | 说明 |
| ---- | ---- | ---- |
|DB_USERNAME|root|数据库用户名|
|DB_PASSWORD|123456|数据库密码|
|REDIS_HOST|localhost|Redis 地址|
|REDIS_PASSWORD|空|Redis 密码|
|JWT_SECRET|please-change-this-secret-in-production|JWT 签名密钥|

生产环境务必修改默认值。

### 数据库脚本命名规则
Flyway 默认读 `src/main/resources/db/migration/`，SQL 文件命名规则：
```plaintext
V{版本号}__{描述}.sql
```
双下划线，版本号不能重复。比如：
```plaintext
db/migration/
├── V1__init_schema.sql          # 建表
├── V2__add_rbac_tables.sql      # 以后新加表
└── V3__add_xxx_field.sql        # 以后改字段
```

### 待完善

- [ ] 分页查询示例
- [ ] API 版本前缀（/api/v1）
- [ ] @RequiresRole 角色级鉴权
- [ ] 数据权限（按部门过滤）
- [ ] MapStruct DTO 映射
- [ ] 单元测试补充

### License

[MIT](LICENSE)

```plaintext
直接复制到项目根目录的 `README.md` 就行。
```