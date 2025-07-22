FROM gradle:8.5-jdk21

# 使用代理
ENV HTTP_PROXY="http://192.168.1.204:1278"
ENV HTTPS_PROXY="http://192.168.1.204:1278"
ENV http_proxy="http://192.168.1.204:1278"
ENV https_proxy="http://192.168.1.204:1278"

WORKDIR /app

# 测试网络连接
# RUN apt-get update && apt-get install -y iputils-ping curl dnsutils
# RUN echo "Testing DNS resolution" && nslookup services.gradle.org
# RUN echo "Testing connection to proxy" && ping -c 1 192.168.1.204
# RUN echo "Testing connection to services.gradle.org" && ping -c 1 services.gradle.org || echo "Cannot ping services.gradle.org"
# RUN echo "Testing connection to plugins.gradle.org" && curl -v --connect-timeout 10 https://plugins.gradle.org/ --proxy http://192.168.1.204:1278
# RUN echo "Testing connection to repo.maven.apache.org" && curl -v --connect-timeout 10 https://repo.maven.apache.org/ --proxy http://192.168.1.204:1278

# 复制gradle配置文件
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle/wrapper/gradle-wrapper.properties /app/gradle/wrapper/

# 下载依赖（这一步会被缓存，除非gradle文件发生变化）
RUN echo "Gradle version:" && gradle --version
RUN echo "Java version:" && java -version
RUN echo "Downloading dependencies with scan..."
RUN gradle dependencies --no-daemon

# 复制源代码
COPY src/ /app/src/
COPY .gitignore LICENSE README.md /app/

# 下载Gradle Wrapper
RUN gradle wrapper --gradle-version 8.5 --no-daemon

# 构建插件
RUN ./gradlew build --no-daemon

# 输出结果位置
RUN echo "构建完成！插件JAR文件位于: /app/build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar"