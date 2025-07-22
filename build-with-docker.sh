#!/bin/bash

# 确保脚本在出错时停止执行
set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}开始使用Docker构建GoLand提交模板插件...${NC}"

# 检查Docker是否已安装
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: 未找到Docker。请先安装Docker: https://docs.docker.com/get-docker/${NC}"
    exit 1
fi

# 检查docker-compose是否已安装
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}错误: 未找到docker-compose。请先安装docker-compose: https://docs.docker.com/compose/install/${NC}"
    exit 1
fi

# 检查Docker服务是否运行
docker info > /dev/null 2>&1 || { echo -e "${RED}错误: Docker服务未运行。请启动Docker服务后重试。${NC}"; exit 1; }

# 创建build目录（如果不存在）
mkdir -p build

# 清理之前的构建（如果存在）
if [ -f "build/libs/git-commit-plugin-*-SNAPSHOT.jar" ]; then
    echo -e "${YELLOW}清理之前的构建...${NC}"
    rm -f build/libs/*
    rm -f build/distributions/*
fi

# 使用docker-compose构建插件
echo -e "${YELLOW}正在构建插件...${NC}"
echo -e "${YELLOW}这可能需要几分钟时间，取决于您的网络速度和计算机性能。${NC}"
echo -e "${YELLOW}首次构建需要下载Docker镜像和Gradle依赖，可能会比较慢。${NC}"
echo -e "${YELLOW}使用代理: http://192.168.1.204:1278${NC}"

# 使用代理
export HTTP_PROXY="http://192.168.1.204:1278"
export HTTPS_PROXY="http://192.168.1.204:1278"
export http_proxy="http://192.168.1.204:1278"
export https_proxy="http://192.168.1.204:1278"

# 使用docker-compose构建插件
docker-compose up --build

# 检查构建是否成功
if [ -f "build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar" ]; then
    echo -e "${GREEN}构建成功！${NC}"
    echo -e "${GREEN}插件JAR文件位于: build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar${NC}"
    echo -e "${GREEN}您可以在GoLand中通过以下步骤安装此插件:${NC}"
    echo -e "${GREEN}1. 打开GoLand${NC}"
    echo -e "${GREEN}2. 转到 Settings/Preferences > Plugins > ⚙️ > Install plugin from disk...${NC}"
    echo -e "${GREEN}3. 选择上面的JAR文件${NC}"
    echo -e "${GREEN}4. 重启GoLand${NC}"
    
    # 显示文件大小
    FILE_SIZE=$(du -h "build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar" | cut -f1)
    echo -e "${GREEN}插件文件大小: ${FILE_SIZE}${NC}"
    
    # 在macOS上，提供在Finder中显示文件的选项
    if [[ "$(uname)" == "Darwin" ]]; then
        echo -e "${YELLOW}是否要在Finder中显示插件文件? (y/n)${NC}"
        read -r response
        if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
            open -R "build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar"
        fi
    fi
else
    echo -e "${RED}构建失败。请检查上面的错误信息。${NC}"
    exit 1
fi