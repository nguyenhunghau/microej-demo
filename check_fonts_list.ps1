$list = "C:\Users\lucas\Documents\microej\MicroEJ\src\main\resources\fonts\fonts.list"
$dir = Split-Path -Parent $list
Write-Host "List file:" $list
Write-Host "Dir:" $dir
Write-Host "Exists(list):" (Test-Path -LiteralPath $list)
$lines = Get-Content -LiteralPath $list
$i = 0
foreach($l in $lines){
  $i++
  $trim = $l.Trim()
  if([string]::IsNullOrWhiteSpace($trim)){ continue }
  $p = Join-Path $dir $trim
  Write-Host ("Line {0}: '{1}' => {2} (exists={3})" -f $i,$trim,$p,(Test-Path -LiteralPath $p))
}
Write-Host "Dest dir listing:"
Get-ChildItem -LiteralPath $dir -Force | Select-Object Name,Length | Format-Table -AutoSize
