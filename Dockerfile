FROM gradle:8.7-jdk21

WORKDIR /app

# 首先复制gradle配置文件，利用Docker缓存机制
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle/wrapper/gradle-wrapper.properties /app/gradle/wrapper/

# 下载依赖（这一步会被缓存，除非gradle文件发生变化）
RUN gradle dependencies --no-daemon

# 复制源代码
COPY src/ /app/src/
COPY .gitignore LICENSE README.md /app/

# 下载Gradle Wrapper
RUN gradle wrapper --gradle-version 8.7 --no-daemon

# 构建插件
RUN ./gradlew build --no-daemon

# 输出结果位置
RUN echo "构建完成！插件JAR文件位于: /app/build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar"