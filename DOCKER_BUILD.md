# 使用Docker构建GoLand提交模板插件

本文档提供了使用Docker构建GoLand提交模板插件的详细说明。如果您的系统上没有安装Java JDK 21，这是一个很好的替代方案。

## 先决条件

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## 构建步骤

1. 确保Docker服务正在运行

2. 在终端中，导航到插件项目的根目录：

   ```bash
   cd /path/to/goland-commit-template-plugin
   ```

3. 使构建脚本可执行：

   ```bash
   chmod +x build-with-docker.sh
   ```

4. 运行构建脚本：

   ```bash
   ./build-with-docker.sh
   ```

   这个脚本将：
   - 检查Docker和Docker Compose是否已安装
   - 创建必要的目录
   - 使用Docker Compose构建插件
   - 在构建成功后提供安装说明

## 构建过程说明

构建过程使用了一个多阶段的Dockerfile，它：

1. 使用官方Gradle镜像作为基础
2. 首先复制Gradle配置文件以利用Docker的缓存机制
3. 下载依赖项
4. 复制源代码
5. 构建插件

构建完成后，插件JAR文件将位于`build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar`。

## 安装插件

构建成功后，您可以按照以下步骤在GoLand中安装插件：

1. 打开GoLand
2. 转到 Settings/Preferences > Plugins > ⚙️ > Install plugin from disk...
3. 选择`build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar`文件
4. 重启GoLand

## 故障排除

如果构建失败，请检查以下几点：

1. 确保Docker服务正在运行
2. 确保您有足够的磁盘空间
3. 检查网络连接，因为构建过程需要下载依赖项
4. 尝试使用`docker-compose down`清理之前的构建容器，然后重新运行构建脚本

如果问题仍然存在，请查看Docker日志以获取更详细的错误信息：

```bash
docker-compose logs
```