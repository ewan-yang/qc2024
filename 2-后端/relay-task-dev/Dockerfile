FROM openjdk:11-jre
MAINTAINER lee lijiacheng@tecpie.com
ADD bncc-user-starter.jar bncc-user-starter.jar
# 运行jar包
RUN bash -c 'touch /bncc-user-starter.jar'
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java", \
			"-Djava.security.egd=file:/dev/./urandom", \
			"-Duser.timezone=Asia/Shanghai", \
			"-Dfile.encoding=utf-8", \
			"-Dspring.cloud.nacos.discovery.server-addr=172.16.0.23:8848", \
			"-Dspring.cloud.nacos.discovery.namespace=bncc-test", \
			"-jar", \
			"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:28091", \
			"/bncc-user-starter.jar", \
			"--server.port=18091", \
			"--spring.profiles.active=local"]
EXPOSE 18091
EXPOSE 28091
