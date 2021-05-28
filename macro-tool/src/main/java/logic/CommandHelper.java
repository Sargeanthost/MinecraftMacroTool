package logic;

import ext.GlobalKeyListener;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.File;

public class CommandHelper {

    public static String input = "";

    public void listen() {
        if (!input.equals("")) {
            String line = input;
            input = "";
            String command;
            String[] params = line.substring(line.indexOf(" ") + 1).split(" ");
            boolean singleWordCommand;

            if (line.contains(" ")) {
                command = line.substring(0, line.indexOf(" "));
                singleWordCommand = false;
            } else {
                command = line;
                singleWordCommand = true;
            }


            switch (command) {
                // command [required parameter] <optional parameter> 'user text'.
                case "help":
                    if (singleWordCommand) {
                        printMessage("Write commands into the console.");
                        printMessage("To execute a macro type \"start ['file']\".");
                        printMessage("To stop all macros press the F10 key.");
                        printMessage("To repeat the last executed macro file press the F9 key.");
                        printMessage("To reload options.txt and hotkey configuration press the " +
                                             "F8 key.");
                        printMessage("To clear the console press the F7 key");
                        printMessage("To see a list of all commands type \"help commands\".");
                        printMessage("To see a detailed description of a single command type \"help <'command'>\".");
                        printMessage(
                                "For more information on how to create macros and a FAQ read the README.txt on github" +
                                        ".");
                    } else {
                        if (params.length > 1) {
                            System.out.println(
                                    "WARNING: \"help\" has at most a single parameter (help <commands|'command'>)");
                        }
                        switch (params[0]) {
                            case "commands":
                                printMessage("help <commands | 'command'>");
                                printMessage("list");
                                printMessage("start ['file'] <'numberOfTicks'>");
                                printMessage("info");
                                printMessage("set [CLEAR|RELOAD|REPLAY|STOP] ['keyCode']");
                                printMessage("clear");
                                break;
                            case "help":
                                printMessage("Syntax: help <commands|'command'>");
                                printMessage(
                                        "\"help\" gives basic information about the usage of the Minecraft Macro " +
                                                "Tool.");
                                printMessage("\"help commands\" lists all commands and their syntax.");
                                printMessage("\"help <'command'>\" gives a detailed description of the command.\"");
                                break;
                            case "list":
                                printMessage("Syntax: list");
                                printMessage("\"list\" lists all macros that are in the current working directory.");
                                break;
                            case "start":
                                printMessage("Syntax: start ['file'] <'numberOfTicks'>");
                                printMessage("\"start ['file']\" begins executing the macro file.");
                                printMessage(
                                        "\"start ['file'] <'numberOfTicks'>\" opens the macro file after the " +
                                                "specified amount of ticks.");
                                printMessage(
                                        "The file must be in the same directory as the Minecraft Macro Tool.");
                                break;
                            case "info":
                                printMessage("Syntax: info");
                                printMessage(
                                        "\"info\" shows information about the tool's version, author and release date.");
                                break;
                            case "set":
                                printMessage("Syntax: set [CLEAR|RELOAD|REPLAY|STOP] ['keyCode']");
                                printMessage("\"set\" rebinds the hotkey for selected action");
                                printMessage("Make sure that the keycode is a valid input as listed in the " +
                                                     "NativeKeyEvent class.");
                                break;
                            case "clear":
                                printMessage("Syntax: clear");
                                printMessage("Clears the text output area.");
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
                        printError("\"start\" needs filename parameter (start ['file'] <'numberOfTicks'>).");
                    } else {
                        Macro.prevMacro = params[0];
                        if (params.length > 1) {
                            try {
                                int sleepMillis = Integer.parseInt(params[1]) * 50;
                                Thread.sleep(sleepMillis);
                                Main.milliseconds += sleepMillis;
                                new Macro(params[0]);
                            } catch (IllegalArgumentException e2) {
                                printError("numberOfTicks parameter may only be a full, positive number.");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Macro.prevMacro = params[0];
                        }
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
                case "clear":
                    if(!singleWordCommand){
                        printWarning("\"clear\" has no parameters: (clear)");
                    }else {
                        Main.getConsole().getTextOutput().clear();
                    }
                    break;
                case "set":
                    if(singleWordCommand){
                        printWarning("\"set hotkey\" must have two parameters: (<set> |CLEAR, RELOAD, REPLAY, STOP| " +
                                             "|keycode|");
                    } else {
                        try{
                            if (params.length == 2){
                                switch (params[0]) {
                                    case "CLEAR":
                                        if(GlobalKeyListener.isValidEntry(0, Integer.parseInt(params[1]))) {
                                            GlobalKeyListener.writeHotKey("CLEAR", params[1]);
                                            printMessage("Set clear hotkey to: " + NativeKeyEvent.getKeyText(
                                                    Integer.parseInt(params[1])));
                                        }
                                        break;
                                    case "RELOAD":
                                        if(GlobalKeyListener.isValidEntry(1, Integer.parseInt(params[1]))) {
                                            GlobalKeyListener.writeHotKey("RELOAD", params[1]);
                                            printMessage("Set reload settings hotkey to: " + NativeKeyEvent.getKeyText(
                                                    Integer.parseInt(params[1])));
                                        }
                                        break;
                                    case "REPLAY":
                                        if(GlobalKeyListener.isValidEntry(2, Integer.parseInt(params[1]))){
                                            GlobalKeyListener.writeHotKey("REPLAY", params[1]);
                                            printMessage("Set execute previous macro hotkey to: " + NativeKeyEvent.getKeyText(
                                                    Integer.parseInt(params[1])));
                                        }
                                        break;
                                    case "STOP":
                                        if(GlobalKeyListener.isValidEntry(3, Integer.parseInt(params[1]))) {
                                            GlobalKeyListener.writeHotKey("STOP", params[1]);
                                            printMessage("Set stop current macro hotkey to: " + NativeKeyEvent.getKeyText(
                                                    Integer.parseInt(params[1])));
                                        }
                                        break;
                                    default:
                                        printError("Invalid hotkey parameter. Please type <help> <set> for a list of");
                                        printError("Valid parameters.");
                                        break;
                                }
                            } else {
                                printError("Invalid parameter length.");
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

    public static void printMessage(String m) {
        System.out.println("MESSAGE: " + m);
    }

    public static void printWarning(String m) {
        System.out.println("WARNING: " + m);
    }

    public static void printError(String m) {
        System.out.println("ERROR: " + m);
    }

}
