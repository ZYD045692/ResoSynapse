@echo off
setlocal enabledelayedexpansion

:: 设置目标文件夹路径
set "frontend_folder=frontend"
set "backend_folder=backend"

:: 遍历 frontend 文件夹中的所有 .bat 文件
cd "%frontend_folder%"
for %%f in (*.bat) do (
    start "" "%%f"
)
cd ..

:: 遍历 backend 文件夹中的所有 .bat 文件
cd "%backend_folder%"
for %%f in (*.bat) do (
    start "" "%%f"
)
cd ..