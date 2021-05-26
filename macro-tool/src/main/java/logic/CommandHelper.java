package logic;

import ext.GlobalKeyListener;
import ext.TextAreaOutputStream;

import java.io.File;

public class CommandHelper {

    public static String input = "";

    public void listen() {
        if (!input.equals("")) {
            String line = input;
            input = "";
            String command;
            boolean singleWordCommand;
            String[] params = line.substring(line.indexOf(" ") + 1).split(" ");

            if (line.contains(" ")) {
                command = line.substring(0, line.indexOf(" "));
                singleWordCommand = false;
            } else {
                command = line;
                singleWordCommand = true;
            }


            switch (command) {
                case "help":
                    if (singleWordCommand) {
                        printMessage("Write commands into the console.");
                        printMessage("To execute a macro type \"start <'file'>\".");
                        printMessage("To stop all macros type either \"stop\" or press the F10 key.");
                        printMessage("To repeat the last executed macro file press the F9 key.");
                        printMessage("To see a list of all commands type \"help commands\".");
                        printMessage("To see a detailed description of a single command type \"help <'command'>\".");
                        printMessage(
                                "For more information on how to create macros and a FAQ read the README.txt in the download folder.");
                    } else {
                        if (params.length > 1) {
                            System.out.println(
                                    "WARNING: \"help\" has only a single parameter (help [commands|'command'])");
                        }
                        switch (params[0]) {
                            case "commands":
                                printMessage("help [commands | 'command']");
                                printMessage("list");
                                printMessage("start <'file'> ['numberOfTicks']");
                                printMessage("stop ['file']");
                                printMessage("info");
                                break;
                            case "help":
                                printMessage("Syntax: help [commands|'command']");
                                printMessage(
                                        "\"help\" gives basic information about the usage of the logic.Macro Parkour Tool.");
                                printMessage("\"help commands\" lists all commands and their syntax.");
                                printMessage("\"help 'command'\" gives a detailed description of the command.\"");
                                printMessage("Examples: \"help\", \"help commands\", \"help start\"");
                                break;
                            case "list":
                                printMessage("Syntax: list");
                                printMessage("\"list\" lists all macros that are in the current working directory.");
                                break;
                            case "start":
                                printMessage("Syntax: start <'file'> ['numberOfTicks']");
                                printMessage("\"start 'file'\" begins executing the macro file.");
                                printMessage(
                                        "\"start 'file' 'numberOfTicks'\" opens the macro file after the specified amount of ticks.");
                                printMessage(
                                        "The file got to be in the same directory as the logic.Macro Parkour Tool.");
                                printMessage(
                                        "You also have to specify the file type with the corresponding filename extension.");
                                printMessage("Examples: \"start hard_jump.txt\", \"start double_triple_neo.txt 40\"");
                                break;
                            case "stop":
                                printMessage("Syntax: stop ['file']");
                                printMessage("\"stop\" stops all running macros and simulated key presses.");
                                printMessage("\"stop 'file'\" stops interpreting the file.");
                                printMessage(" Examples: \"stop\", \"stop quad_neo\"");
                                break;
                            case "info":
                                printMessage("Syntax: info");
                                printMessage(
                                        "\"info\" shows information about the tool's version, author and release date.");
                                break;
                            default:
                                printError("Unknown command \"" + params[0] + "\".");
                                break;
                        }

                    }
                    System.out.println();
                    break;
                case "list":
                    if (!singleWordCommand) {
                        printWarning("\"list\" has no parameters (list)");
                    } else {
                        FileFilter txtFilter = new FileFilter(".txt");
                        File dirFile = new File(Macro.WORKING_DIR);
                        String[] listOfTextFiles = dirFile.list(txtFilter);

                        if(listOfTextFiles != null) {
                            if (listOfTextFiles.length == 0) {
                                System.out.println("There are no text files in this directory!");
                                return;
                            }
                            for (String file : listOfTextFiles) {
                                System.out.println(file);
                            }
                        }
                    }
                    break;
                case "start":
                    if (singleWordCommand) {
                        printError("\"start\" needs filename parameter (start <'file'> ['numberOfTicks']).");
                    } else {
                        Macro.prevMacro = params[0];
                        if (params.length > 1) {
                            try {
                                int sleepMillis = Integer.parseInt(params[1]) * 50;
                                Thread.sleep(sleepMillis);
                                Main.millis += sleepMillis;
                                new Macro(params[0]);
                            } catch (IllegalArgumentException e2) {
                                printError("numberOfTicks parameter may only be a full, positive number.");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            new Macro(params[0]);
                        }

                    }

                    break;
                case "stop":
                    if (singleWordCommand) {
                        Macro.closeMacros();
                    } else {
                        Macro.currentMacros.removeIf((Macro macro) ->
                                                             macro.getFile().equals(
                                                                     line.substring(line.indexOf(" ") + 1)));
                    }

                    break;
                case "info":
                    if (!singleWordCommand) {
                        printWarning("\"info\" has no parameters: (info)");
                    } else {

                        printMessage(Main.versionName);
                        printMessage(Main.versionDate);
                        printMessage("Developed by Kideneb, forked by girraiffe");
                        System.out.println();
                    }
                    break;
                    //TODO add help for clear and set hotkey
                case "clear":
                    if(!singleWordCommand){
                        printWarning("\"clear\" has no parameters: (clear)");
                    }else {
                        Main.getConsole().getTextOutput().clear();
                    }
                    break;
                case "set":
                    if(singleWordCommand){
                        printWarning("\"set hotkey\" has two parameters: (set |CLEAR, READ, RELOAD, CLOSE| |keycode|");
                    } else {
                        try{
                            //TODO make sure ints are unique
                            if (params.length == 2){
                                switch (params[0]) {
                                    case "CLEAR":
                                        GlobalKeyListener.setHotKey(0, Integer.parseInt(params[1]));
                                        break;
                                    case "READ":
                                        GlobalKeyListener.setHotKey(1, Integer.parseInt(params[1]));
                                        break;
                                    case "RELOAD":
                                        GlobalKeyListener.setHotKey(2, Integer.parseInt(params[1]));
                                        break;
                                    case "CLOSE":
                                        GlobalKeyListener.setHotKey(3, Integer.parseInt(params[1]));
                                        break;
                                    default:
                                        printError("Invalid hotkey parameter");
                                        break;
                                }
                            } else {
                                printWarning("Invalid parameter length.");
                                break;
                            }
                        } catch(NumberFormatException e){
                            printError("Keycode is not an integer.");
                            break;
                        }
                    }
                    break;
                default:
                    printError("Unknown command \"" + command + "\".");
                    break;
            }


        }

    }

    static void printMessage(String m) {
        System.out.println("MESSAGE: " + m);
    }

    static void printWarning(String m) {
        System.out.println("WARNING: " + m);
    }

    static void printError(String m) {
        System.out.println("ERROR: " + m);
    }

}
