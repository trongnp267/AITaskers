@echo off
chcp 65001 >nul
echo ============================================
echo   KHOI DONG WEB AITASKER
echo   (2 cua so se mo ra - DUNG DONG chung
echo    khi dang dung web)
echo ============================================
start "AITasker BACKEND - dong cua so nay la web mat ket noi" cmd /k "cd /d %~dp0 && mvn spring-boot:run"
start "AITasker FRONTEND - dong cua so nay la web mat ket noi" cmd /k "cd /d %~dp0frontend && (if not exist node_modules npm install) && npm run dev"
echo.
echo Da mo 2 cua so server. Doi khoang 20-30 giay
echo roi vao:  http://localhost:3000
echo.
pause
