# MinecraftMacroTool

The tool is an external unofficial tool for the game Minecraft, which allows the implementation of movement macros.
Those macros can include movements which are humanly impossible.
Since this can be considered cheating, the author advises to use the tool only for testing purposes and never on multiplayer servers.



# How to start the tool:

1. Grab the latest release jar, or, for an up-to-date package, compile from source by using `mvn package` or a built-in maven plugin. Make sure you're in the folder with the `pom.xml` file in it.
2. Run the jar; a console should pop up with basic information.
	If this does not work then make sure you have java installed correctly.
3. Now you can input commands. A list of commands can be found below.
   
   
   
# How to create macros:
1. Start by choosing an editor. 
	You can simply choose any text editor but using either Microsoft Office's Excel or Open Office's Calc is **highly** encouraged.
2. Copy the default.txt file and open the copy in your editor:
	* Excel: Drag the file into Excel and keep the default settings for interpreting text files.
	* LibreOffice Calc: Drag the file into Calc and change 'Separated by' to Tab.
3. Don't change the first line in the spreadsheet. It shows you, which column is for what kind of input.
	Every line after the first one then represents one tick. 
  * Macros written in the column "Files" will  be opened or closed. If you write hh_timing.txt into the third line for example (which is tick 2), it will register the macro and start executing it in tick 3.
		If you write a '-' in front of a macro, it will be closed, if it is currently executed (e.g. -jump_loop.txt).
  * In the columns "W", "A", "S", "D", "Sprint", "Sneak", "Jump", "LMB" and "RMB" you write integers.
		The integer represents the amount of ticks the keyboard key or mouse button will be held down, starting in this tick.
		If you write a "1", the key will only be pressed for a single tick. 
		For example, if you write "5" into the sneak column, line 5, the sneak key will be pressed during ticks 4-8.
  * The column "Direction" accepts any number. The number represents the amount of rotation
		your character will undergo in the tick. e.g. "-15.6" will make you turn about 15.6° to the left.
		The actual rotation you undergo is just the closest possible value and will increase in accuracy the closer your in game sensitivity is to 100%.
4. After the macro has been edited, it should be saved into the text file again.
5. Make sure that you save the file as a `.txt` file with tab seperation.



# Hotkeys:
* F8: Reloads all configs from the config file (Should be used after changing the mouse sensitivity and hotkeys).
* F9: Repeats the last macro you input into the console. When you change the macro before pressing F9,
    those changes will be instantly applied.
* F10: Stops all currently running macros.



## Commands:
### help [commands | 'command']                                       
* `<help>` gives basic information about the usage of the Macro Parkour Tool.
* `<help commands>` lists all commands and their syntax.              
* `<help 'command'>` gives a detailed description of the command.      
* Examples: `<help>`, `<help commands>`, <help start>
	
### info                                                             
* `<info>` shows information about the tool's version, author and release date.	
	
### list                                                     
* `<list>` lists all macros that are currently being read and executed.
			
### start <'file'> ['numberOfTicks']                                           
* `<start 'file'>` begins executing the macro file. Make sure you include just the name, not the extension.                                      
* `<start 'file' 'numberOfTicks'>` opens the macro file after the specified amount of ticks.
* The file needs to be in the same directory as the Macro Parkour Tool.                    

### stop ['file']                                   
* `<stop>` stops all running macros and simulated key presses.
* `<stop 'file'>` stops interpreting the file.            
*  Examples: `<stop>`, `<stop quad_neo>`


		 
# FAQ:
### Q: What is the tool for?
A: With the tool, players are on the one hand supposed to push the boundaries of parkour in Minecraft.
It doesn't matter, if this means doing very hard jumps or creating a TAS, 
even though the latter may become a tedious task.
On the other hand you might learn how to do jumps more easily by actually creating a macro that does the jump.
The tool might also help find new techniques, if there are any.

### Q: Why is my macro inconsistent?
A: The cause for consistencies is often lag. If Minecraft has to skip a tick, or the tool takes longer 
than a minecraft tick to compute its own tick, the run is basically ruined. If that happens make sure 
you reduce settings in Minecraft and close background programs. Also check the Task Manager to make sure
you have no other instances of the tool running.
   
### Q: What features are planned in the future?
A: A `load` command so your macro is loaded yet not executed until you press F9. This prevents annoying behavior when 
executing macros on the desktop. It is also planned to turn the tool into an internal mod, in order to fix
some major issues and implement features like seeing effects of changes in your macros in game or
skipping between ticks, so you don't have to run macros from the start each time.
