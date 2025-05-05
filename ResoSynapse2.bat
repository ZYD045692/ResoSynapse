@echo off
setlocal enabledelayedexpansion

:: 设置目标文件夹路径
set "frontend_folder=frontend"
set "backend_folder=backend"

:: 设置子进程窗口标题
set "window_title=ResoSynapse"

:: 启动前端和后端的 .bat 文件，并强制设置窗口标题
cd "%frontend_folder%"
for %%f in (*.bat) do (
    start "%window_title%" /b cmd /c "title %window_title% & call "%%f""
)
cd ..

cd "%backend_folder%"
for %%f in (*.bat) do (
    start "%window_title%" /b cmd /c "title %window_title% & call "%%f""
)
cd ..

:: 等待用户指令
chcp 65001
echo 所有程序已启动，按任意键关闭所有进程...
pause > nul

:: 终止所有具有指定标题的进程
taskkill /f /fi "WINDOWTITLE eq %window_title%" /im cmd.exe >nul 2>&1