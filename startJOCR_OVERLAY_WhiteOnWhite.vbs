' dab 30.06.2015
'
'
Set fso  = CreateObject("Scripting.FileSystemObject")

Dim strTempFile
strTempFile = fso.GetSpecialFolder(2) & "\" & fso.GetTempName & ".pdf"

Dim file
Dim oFile

Dim folder
Set folder = fso.getFolder("./in/process")
For Each oFile in folder.Files
	file = oFile.ShortPath
	'msgbox file
	'shellCommand="JOCR.jar " & "-AUTO " & file & " " & strTempFile
	shellCommand="JOCR.jar " & "-OVERLAY " & """" & file & """" & " " & strTempFile & " " & "dbrenkTestWhiteOnWhite" & " " & "Color.WHITE"
	Set oShell=CreateObject("Wscript.Shell")
	iReturn=oShell.run(shellCommand,1,True)




	msgbox strTempFile
Next

Set fso = nothing