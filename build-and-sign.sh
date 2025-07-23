#!/bin/bash

echo '=== Initializing Gradle Wrapper ==='
gradle wrapper --gradle-version 8.5

echo '=== Starting build process ==='
./gradlew build --no-daemon --stacktrace

echo '=== Build completed, checking generated files ==='
ls -la /app/build/libs/
ls -la /app/build/distributions/ 2>/dev/null || echo 'No distributions directory found'

echo '=== Starting signing process ==='
./gradlew signPlugin --no-daemon --stacktrace

echo '=== Signing completed, checking signed files ==='
ls -la /app/build/libs/
ls -la /app/build/distributions/ 2>/dev/null || echo 'No distributions directory found'

echo '=== Build and signing process completed ==='