' dab 30.06.2015
'
'
Set fso  = CreateObject("Scripting.FileSystemObject")

Dim strTempFile
strTempFile = fso.GetSpecialFolder(2) & "\" & fso.GetTempName & ".txt"
Dim file
Dim oFile

Dim folder
Set folder = fso.getFolder("./in/process")
For Each oFile in folder.Files
	'ShortPath follows the 8.3 notation -> it was necessary here,
	'because parameters with german "umlaut" characters (ö,ä,ü) caused problems in oShell.run
	file = oFile.ShortPath
	'msgbox file
	'shellCommand="JOCR.jar " & "-AUTO " & file & " " & strTempFile
	shellCommand="JOCR.jar " & "-DEU " & "-AUTO " & """" & file & """" & " " & strTempFile
	Set oShell=CreateObject("Wscript.Shell")
	iReturn=oShell.run(shellCommand,1,True)


	Set file = fso.OpenTextFile(strTempFile, 1)
	text = file.ReadAll
	file.Close

	If fso.FileExists(strTempFile) Then
		fso.DeleteFile strTempFile 
	End if

	If fso.FileExists(oFile) Then
		fso.DeleteFile oFile 
	End if

	msgbox text
Next

Set fso = nothing
