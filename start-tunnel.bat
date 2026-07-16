@echo off
chcp 65001 >nul
echo ============================================================
echo   DUA WEB AITASKER RA INTERNET (Cloudflare Tunnel)
echo   Cho vai giay, link dang https://xxx.trycloudflare.com
echo   se hien ra ben duoi - gui link do cho nguoi khac.
echo   LUU Y: dong cua so nay la link ngung hoat dong.
echo          Moi lan chay lai se ra link MOI.
echo ============================================================
cd /d %~dp0
cloudflared.exe tunnel --url http://localhost:3000
pause
