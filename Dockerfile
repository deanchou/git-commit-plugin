FROM gradle:8.5-jdk21

WORKDIR /app

# Copy gradle configuration files
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle/wrapper/gradle-wrapper.properties /app/gradle/wrapper/

# Download dependencies (this step will be cached unless gradle files change)
RUN echo "Gradle version:" && gradle --version
RUN echo "Java version:" && java -version
RUN echo "Downloading dependencies..."
RUN gradle dependencies --no-daemon

# Copy source code
COPY src/ /app/src/
COPY .gitignore LICENSE README.md /app/

# Download Gradle Wrapper
RUN gradle wrapper --gradle-version 8.5 --no-daemon

# Build plugin
RUN ./gradlew build --no-daemon

# Output result location
RUN echo "Build completed! Plugin JAR file is located at: /app/build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar"