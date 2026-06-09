@echo off
rem ============================================================
rem Скрипт компіляції та запуску (Windows)
rem Вимоги: JDK 11+, javac у PATH
rem ============================================================

set SRC_DIR=src\main\java
set OUT_DIR=out
set MAIN_CLASS=store.Main

echo [*] Компіляція...
if not exist %OUT_DIR% mkdir %OUT_DIR%

dir /s /b %SRC_DIR%\*.java > sources.txt

javac -encoding UTF-8 -d %OUT_DIR% @sources.txt
if errorlevel 1 (
    echo [!] Помилка компіляції
    del sources.txt
    pause
    exit /b 1
)

del sources.txt
echo [OK] Компіляція успішна
echo.
echo [*] Запуск...
echo.

java -cp %OUT_DIR% %MAIN_CLASS%
pause
