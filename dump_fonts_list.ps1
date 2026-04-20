$p = "C:\Users\lucas\Documents\microej\MicroEJ\src\main\resources\fonts\fonts.list"
Write-Host "Path=$p"
$bytes = [System.IO.File]::ReadAllBytes($p)
Write-Host "Length(bytes)=" $bytes.Length
Write-Host "First 16 bytes:" ([BitConverter]::ToString($bytes[0..([Math]::Min(15,$bytes.Length-1))]))
# Print lines with visible special chars
$content = [System.IO.File]::ReadAllText($p)
Write-Host "ReadAllText length(chars)=" $content.Length
Write-Host "Content visible:" ($content.Replace("`r","<CR>").Replace("`n","<LF>"))
