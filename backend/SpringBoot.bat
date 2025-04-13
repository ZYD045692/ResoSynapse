@echo off
setlocal enabledelayedexpansion

:: 设置项目路径
set "project_path=./AITool"

:: 运行 Spring Boot 项目
cd /d "%project_path%"
mvn spring-boot:run