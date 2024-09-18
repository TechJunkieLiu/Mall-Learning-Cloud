# Mall-Learning-Cloud
## 一、项目简介
`mall-learning-cloud`是一套微服务商城系统（学习），采用了 Spring Cloud Hoxton & Alibaba、Spring Boot 2.3、Oauth2、MyBatis、Elasticsearch、
Docker、Kubernetes 等核心技术，同时提供了基于Vue的管理后台方便快速搭建系统。本系统在电商业务的基础集成了注册中心、配置中心、监控中心、网关等系统功能。
## 二、系统架构图
[微服务系统架构图](D:\workspace_learning\two\Mall-Learning-Cloud\document\picture\微服务系统架构图.jpg)
## 三、项目结构 
```
mall-learning-cloud
├── mall-common -- 工具类及通用代码模块
├── mall-entity -- 对象实体模块
├── mall-auth -- 基于Spring Security Oauth2的统一认证中心
├── mall-gateway -- 基于Spring Cloud Gateway的微服务API网关服务
├── mall-monitor -- 基于Spring Boot Admin的微服务监控中心
├── mall-admin -- 后台管理系统服务
├── mall-search -- 基于Elasticsearch的商品搜索系统服务
├── mall-gate -- 移动端商城系统服务
├── mall-demo -- 微服务远程调用测试服务
└── config -- 配置中心存储的配置
```
## 四、技术选型
### 1、后端技术
| 技术                   | 说明                 | 官网                                                 |
| ---------------------- | -------------------- | ---------------------------------------------------- |
| Spring Cloud           | 微服务框架           | https://spring.io/projects/spring-cloud              |
| Spring Cloud Alibaba   | 微服务框架           | https://github.com/alibaba/spring-cloud-alibaba      |
| Spring Boot            | 容器+MVC框架         | https://spring.io/projects/spring-boot               |
| Spring Security Oauth2 | 认证和授权框架       | https://spring.io/projects/spring-security-oauth     |
| MyBatis                | ORM框架              | http://www.mybatis.org/mybatis-3/zh/index.html       |
| MyBatisGenerator       | 数据层代码生成       | http://www.mybatis.org/generator/index.html          |
| PageHelper             | MyBatis物理分页插件  | http://git.oschina.net/free/Mybatis_PageHelper       |
| Knife4j                | 文档生产工具         | https://github.com/xiaoymin/swagger-bootstrap-ui     |
| Elasticsearch          | 搜索引擎             | https://github.com/elastic/elasticsearch             |
| RabbitMq               | 消息队列             | https://www.rabbitmq.com/                            |
| Redis                  | 分布式缓存           | https://redis.io/                                    |
| MongoDb                | NoSql数据库          | https://www.mongodb.com/                             |
| Docker                 | 应用容器引擎         | https://www.docker.com/                              |
| Druid                  | 数据库连接池         | https://github.com/alibaba/druid                     |
| OSS                    | 对象存储             | https://github.com/aliyun/aliyun-oss-java-sdk        |
| MinIO                  | 对象存储             | https://github.com/minio/minio                       |
| JWT                    | JWT登录支持          | https://github.com/jwtk/jjwt                         |
| LogStash               | 日志收集             | https://github.com/logstash/logstash-logback-encoder |
| Lombok                 | 简化对象封装工具     | https://github.com/rzwitserloot/lombok               |
| Seata                  | 全局事务管理框架     | https://github.com/seata/seata                       |
| Portainer              | 可视化Docker容器管理 | https://github.com/portainer/portainer               |
| Jenkins                | 自动化部署工具       | https://github.com/jenkinsci/jenkins                 |
| Kubernetes             | 应用容器管理平台     | https://kubernetes.io/                               |
### 2、前端技术
| 技术       | 说明                  | 官网                           |
| ---------- | --------------------- | ------------------------------ |
| Vue        | 前端框架              | https://vuejs.org/             |
| Vue-router | 路由框架              | https://router.vuejs.org/      |
| Vuex       | 全局状态管理框架      | https://vuex.vuejs.org/        |
| Element    | 前端UI框架            | https://element.eleme.io/      |
| Axios      | 前端HTTP框架          | https://github.com/axios/axios |
| v-charts   | 基于Echarts的图表框架 | https://v-charts.js.org/       |

## 五、基于Kubernetes的应用部署（SpringCloud）
### 1、服务器规划
两台服务器部署所有服务：
- 基础服务器（192.168.146.27）：用于部署mall-learning-cloud的依赖服务，包括MySQL、Redis、Elasticsearch等与应用无关的服务，采用Docker方式来部署。
- 应用服务器（192.168.146.28）：用于部署mall-swarm的应用服务，包括mall-admin、mall-gate、mall-search等应用服务，采用K8S方式来部署。
### 2、基础服务器环境搭建（基于Docker）
| 工具          | 版本号 | 下载                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| JDK           | 1.8    | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Mysql         | 5.7    | https://www.mysql.com/                                       |
| Redis         | 5.0    | https://redis.io/download                                    |
| Elasticsearch | 7.6.2  | https://www.elastic.co/cn/downloads/elasticsearch            |
| Kibana        | 7.6.2  | https://www.elastic.co/cn/downloads/kibana                   |
| Logstash      | 7.6.2  | https://www.elastic.co/cn/downloads/logstash                 |
| MongoDb       | 4.2.5  | https://www.mongodb.com/download-center                      |
| RabbitMq      | 3.7.14 | http://www.rabbitmq.com/download.html                        |
| nginx         | 1.10   | http://nginx.org/en/download.html                            |

参考mall-learning

### 3、项目部署
- 镜像打包及推送（为了方便部署，把所有应用镜像都上传到DockerHub上）
  - 修改项目根目录的pom.xml中的docker.host属性
  - 使用Maven插件将所有镜像打包到Linux服务器上，直接使用根项目下的package命令即可
  - 修改所有镜像标签名称，修改本地镜像标签名称为远程镜像标签名称
  - 修改完成后查询lzqdocker相关镜像（lzqdocker为我们在Docker Hub上的仓库地址），`docker images | grep lzqdocker`
  - 之后推送镜像到Docker Hub（如果不想自己推送镜像，可以使用已经上传好到镜像）
    - 登录Docker Hub，`docker login`
    - 推送到远程仓库，`docker push lzqdocker/mall-admin:1.0-SNAPSHOT`
- 基础服务器部署（基于Docker）
  - 参考 mall-learning 或使用 [Docker Compose 脚本](D:\workspace_learning\two\Mall-Learning-Cloud\document\docker\docker-compose-env.yml)，执行命令 `docker-compose -f docker-compose-env.yml up -d`
  - 查看 Docker 中运行服务，`docker ps`
- 应用服务器部署（将所有应用服务都部署到K8S上，使用Rancher来进行可视化管理）  
  - 安装Rancher
    - 下载镜像 `docker pull rancher/rancher:v2.5-head`
    - 运行Rancher服务，`docker run -p 80:80 -p 443:443 --name rancher \
      --privileged \
      --restart=unless-stopped \
      -d rancher/rancher:v2.5-head`
    - 访问主页：http://192.168.146.28
  - 修改Nacos配置（由于应用服务都部署到了K8S中，所以需要修改相关配置）
    - 将config目录下的配置信息添加到Nacos中，访问地址：http://192.168.146.27:8848/nacos/index.html
    - 修改mall-admin-prod.yaml配置，修改MySQL和Redis连接地址即可，之前是通过--link的形式访问的，这次需要改为IP进行访问
    - 修改mall-gateway-prod.yaml配置，修改Redis连接地址及JWT的publicKey访问地址即可（当在K8S中创建服务后，可以通过服务名进行访问）
    - 修改mall-gate-prod.yaml配置，修改MySQL、MongoDb、Redis、RabbitMq连接地址即可
    - 修改mall-search-prod.yaml配置，修改MySQL、Elasticsearch连接地址即可
  - 使用Rancher部署应用（当使用Rancher创建Deployment时，如果镜像下载过慢会出现超时，可以进入到Rancher容器中手动进行下载）
    - 进入Rancher容器内部，`docker exex -it rancher /bin/bash`
    - 通过crictl命令下载应用镜像，`k3s crictl pull lzqdocker/mall-admin:1.0-SNAPSHOT`
    - 查看镜像 `k3s crictl images |grep lzqdocker`
    - 使用Rancher可视化创建Deployment（YAML方式）
      - 直接将项目k8s文件夹中的mall-admin-deployment.yaml文件内容复制下即可
      - 之后再使用mall-admin-service.yaml文件创建Service
  - 验证
    - 查看所有创建的Deployment
    - 查看所有创建的Service
    - 在Pod列表查看应用的启动日志
    - 使用Nginx反向代理
      - 由于应用服务被部署在Rancher容器内部，`docker run -p 2080:2080 --name nginx \
                                            -v /mydata/nginx/html:/usr/share/nginx/html \
                                            -v /mydata/nginx/logs:/var/log/nginx  \
                                            -v /mydata/nginx/conf:/etc/nginx \
                                            -d nginx:1.10`
      - 获取Rancher容器运行的IP地址，`docker inspect rancher |grep IPAddress`
      - 创建完Nginx容器后，添加配置文件api.conf，将api.XXX.com域名的访问反向代理到K8S中的mall-gateway-service服务上去
      - 修改Linux服务器的本机host文件，添加记录：192.168.146.27 api.XXX.com
    - 或不使用Nginx反向代理
      - 创建容器的时候将mall-gateway-service的端口映射出来即可，`docker run -p 80:80 -p 443:443 -p 8805:30201 --name rancher \
                                                            --privileged \
                                                            --restart=unless-stopped \
                                                            -d rancher/rancher:v2.5-head`
      - 通过Nginx即可访问接口文档，访问地址：http://api.XXX.com:2080/doc.html

## 六、运行效果展示
- 查看注册中心注册服务信息，访问地址：http://localhost:8848/nacos/
- 监控中心应用信息，访问地址：http://localhost:8101
- API文档信息，访问地址：http://localhost:8201
- 日志收集系统信息，访问地址：http://localhost:5601
- 可视化容器管理，访问地址：http://localhost:9000
