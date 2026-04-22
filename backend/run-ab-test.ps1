$ErrorActionPreference = "Stop"

$ApiUrl = "http://127.0.0.1:8080/api/auth/login"
$PayloadFile = Join-Path $PSScriptRoot "login.json"
$TotalRequests = 500
$Concurrency = 20
$AbCmd = $null

if (-not (Test-Path $PayloadFile)) {
    Write-Error "Request payload file not found: $PayloadFile"
}

$abCommand = Get-Command ab -ErrorAction SilentlyContinue
if ($abCommand) {
    $AbCmd = $abCommand.Source
} else {
    $candidatePaths = @(
        "E:\tools\Apache24\bin\ab.exe",
        "C:\Users\34254\AppData\Local\Microsoft\WinGet\Packages\ApacheLounge.httpd_Microsoft.Winget.Source_8wekyb3d8bbwe\Apache24\bin\ab.exe",
        "C:\Apache24\bin\ab.exe"
    )
    $AbCmd = $candidatePaths | Where-Object { Test-Path $_ } | Select-Object -First 1
}

if (-not $AbCmd) {
    Write-Error "ab command not found. Install Apache Bench first, or restart shell to refresh PATH."
}

Write-Host "Start benchmark: $ApiUrl"
Write-Host "Arguments: -n $TotalRequests -c $Concurrency -p $PayloadFile"

& $AbCmd -n $TotalRequests -c $Concurrency -p $PayloadFile -T "application/json" $ApiUrl
