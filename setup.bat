@echo off
setlocal enabledelayedexpansion

:: 设置目标文件夹路径
set "setup_folder=setup"

:: 遍历setup 文件夹中的所有 .bat 文件
cd "%setup_folder%"
for %%f in (*.bat) do (
    start "" "%%f"
)
cd ..