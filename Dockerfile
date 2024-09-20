ARG GOVERSION=1.20
ARG GOARCH
ARG GOARCH=amd64
FROM crpi-xr9mxv8txu0tj08i.cn-huhehaote.personal.cr.aliyuncs.com/technophile-liu/mall-learning-cloud:1.0.0 as builder
ARG GOARCH
ARG GOARCH=${GOARCH}
WORKDIR /java/src/k8s.io/mall-learning-cloud/
COPY . /java/src/k8s.io/mall-learning-cloud/

RUN make install-tools && make build-local

FROM gcr.io/distroless/static:latest-${GOARCH}
COPY --from=builder /java/src/k8s.io/mall-learning-cloud/Mall-Learning-Cloud /

USER nobody

ENTRYPOINT ["/mall-learning-cloud", "--port=8080", "--telemetry-port=8081"]

EXPOSE 8080 8081
