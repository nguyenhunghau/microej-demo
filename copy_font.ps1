$src = "C:\Users\lucas\Documents\microej\test\nxpvee-mimxrt1170-prj\nxpvee-mimxrt1170-evk\apps\aiSample\src\main\resources\fonts\SourceSansPro_15px-600.ejf"
$dstDir = "C:\Users\lucas\Documents\microej\MicroEJ\src\main\resources\fonts"
Write-Host "SRC=$src"
Write-Host "DSTDIR=$dstDir"
Write-Host "SRC exists?" (Test-Path -LiteralPath $src)
Write-Host "DST exists?" (Test-Path -LiteralPath $dstDir)
Copy-Item -LiteralPath $src -Destination $dstDir -Force
Write-Host "After copy:"
Get-ChildItem -LiteralPath $dstDir -Force | Select-Object Name,Length | Format-Table -AutoSize
