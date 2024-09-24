##FROM crpi-xr9mxv8txu0tj08i.cn-huhehaote.personal.cr.aliyuncs.com/technophile-liu/mall-learning-cloud:1.0.0 as builder
##WORKDIR /java/src/k8s.io/mall-learning-cloud/
##COPY . /java/src/k8s.io/mall-learning-cloud/
##
##RUN make install-tools && make build-local
##
##FROM gcr.io/distroless/static:latest-${GOARCH}
##COPY --from=builder /java/src/k8s.io/mall-learning-cloud/Mall-Learning-Cloud /
##
##USER nobody
##
##ENTRYPOINT ["/mall-learning-cloud", "--port=8080", "--telemetry-port=8081"]
##
##EXPOSE 8080 8081



FROM xldevops/jdk17-lts:latest
RUN mkdir -p /opt/test-docker
WORKDIR /opt/test-docker
COPY mall-learning-cloud.jar /opt/test-docker/mall-learning-cloud.jar
CMD ["java","-jar", "/opt/test-docker/mall-learning-cloud.jar"]



#
##依赖镜像
#FROM crpi-xr9mxv8txu0tj08i.cn-huhehaote.personal.cr.aliyuncs.com/technophile-liu/mall-learning-cloud:1.0.0 as builder
##FROM docker.io/centos
##作者名称邮箱
#MAINTAINER liuzhiqi<13171077730@139.com>
##启动落脚点
#  #定义环境变量
#ENV MY_PATH /opt/
#  #使用环境变量指定落脚点
#WORKDIR $MY_PATH
##需要运行的命令
#  #安装需要的指令
#RUN yum -y install vim
#RUN yum -y install net-tools
##对外暴露的端口号
#EXPOSE 80
##启动容器时需要运行的命令
#CMD /bin/bas
