FROM openjdk:11-jre
ENV TZ=Asia/Shanghai
MAINTAINER lee
VOLUME /tmp
ADD bncc-analysis-alarm-service.jar bncc-analysis-alarm-service.jar
ENTRYPOINT ["java", \
			"-Dspring.cloud.nacos.discovery.server-addr=172.16.0.23:8848", \
			"-Dspring.cloud.nacos.discovery.namespace=bncc-test", \
			"-jar", \
			"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:28092", \
			"/bncc-analysis-alarm-service.jar", \
			"--server.port=18092", \
			"--spring.profiles.active=local"]
EXPOSE 18092
EXPOSE 28092
