#Sonic Scream Design Notes

##High-level hierarchy
Profile (Collection of Scripts)

* Category (Collection of scripts, and places to look in the VPK) (Represented by a single tab)
	* Script file (Collection of nodes & reference to parent category)
		* Sound
		* Sound
		* Sound
	* Script file	
	* ...
* Category
* ...

Profile

* ...

##Basic Process
* blah blah fill in later
* Run formatted vsndevts and mp3s/wavs through Valve's ResourceCompiler in %steamapps%\common\dota 2 beta\game\bin\win64\resourcecompiler.exe (TBD for other platforms)(TBD if exists without workshop tools)


Note: The Resource compile doesn't diff files, it just checks for filenames. We'll probably have to use `-f` flag to force recompilation.