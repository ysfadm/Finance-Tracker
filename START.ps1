Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  FINANCE TRACKER - Starting All Systems..." -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Checking ports..." -ForegroundColor Yellow
$ports = @(3000, 8080)
foreach ($port in $ports) {
    $process = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "Cleaning up port $port..."
        Stop-Process -Id $process.OwningProcess -Force -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 1
    }
}

Write-Host ""
Write-Host "Starting Backend (Port 8080)..." -ForegroundColor Cyan

$env:JAVA_HOME = "C:\Users\yusuf\.jdk\jdk-21.0.8(1)"
$backendPath = "c:\Users\yusuf\OneDrive\Masaüstü\Python github\Finance-Tracker\backend"
$jarPath = Join-Path $backendPath "target\finance-tracker-api-1.0.0.jar"

if (Test-Path $jarPath) {
    Start-Process -FilePath "C:\Users\yusuf\.jdk\jdk-21.0.8(1)\bin\java.exe" `
        -ArgumentList "-jar `"$jarPath`"" `
        -WorkingDirectory $backendPath `
        -NoNewWindow
    Write-Host "Backend started" -ForegroundColor Green
} else {
    Write-Host "JAR file not found!" -ForegroundColor Red
    exit
}

Start-Sleep -Seconds 8

Write-Host ""
Write-Host "Starting Frontend (Port 3000)..." -ForegroundColor Cyan

$frontendPath = "c:\Users\yusuf\OneDrive\Masaüstü\Python github\Finance-Tracker\frontend"
Start-Process -FilePath "python.exe" `
    -ArgumentList "-m http.server 3000 --directory `"$frontendPath`"" `
    -WorkingDirectory $frontendPath `
    -NoNewWindow

Write-Host "Frontend started" -ForegroundColor Green

Write-Host ""
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "  ALL SYSTEMS RUNNING!" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "ACCESS HERE:" -ForegroundColor Yellow
Write-Host "  Login: http://localhost:3000" -ForegroundColor White
Write-Host "  Admin: http://localhost:3000/admin" -ForegroundColor White
Write-Host "  H2 DB: http://localhost:8080/api/h2-console" -ForegroundColor White
Write-Host "  API:   http://localhost:8080/api" -ForegroundColor White
Write-Host ""

Start-Process "http://localhost:3000"
