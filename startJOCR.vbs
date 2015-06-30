' dab 30.06.2015
'
'
Set fso  = CreateObject("Scripting.FileSystemObject")

Dim strTempFile
strTempFile = fso.GetSpecialFolder(2) & "\" & fso.GetTempName & ".txt"
Dim file
'file = "./in/eurotext.bmp"
'file = "./in/eurotext.pdf"
'file = "./in/eurotext.png"
'file = "./in/eurotext.tif"
'file = "./in/test.pdf"
'file = "./in/test - Kopie.pdf"
'file = "./in/öä.pdf"
'file = "./in/Edle Hölzer ERECH BC.pdf"
file = "./in/Hamburger Moebel ERECH BC.pdf"


'shellCommand="JOCR.jar " & "-AUTO " & file & " " & strTempFile
shellCommand="JOCR.jar " & "-AUTO " & """" & file & """" & " " & strTempFile
Set oShell=CreateObject("Wscript.Shell")
iReturn=oShell.run(shellCommand,1,True)

Set file = fso.OpenTextFile(strTempFile, 1)
text = file.ReadAll
file.Close

If fso.FileExists(strTempFile) Then
	fso.DeleteFile strTempFile 
End if

msgbox text