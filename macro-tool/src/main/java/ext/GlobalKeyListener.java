package ext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import logic.CommandHelper;
import logic.Config;
import logic.Macro;
import logic.Main;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;


public class GlobalKeyListener implements NativeKeyListener {
    private static final int[] hotKeys = {NativeKeyEvent.VC_F7, NativeKeyEvent.VC_F8, NativeKeyEvent.VC_F9,
            NativeKeyEvent.VC_F10};


    public void nativeKeyPressed(NativeKeyEvent e) {

        if(e.getKeyCode() == hotKeys[0]){
            Main.getConsole().getTextOutput().clear();
        }
        if (e.getKeyCode() == hotKeys[1]) {
            Config.readOptions();
            readHotKey();
        }
        if (e.getKeyCode() == hotKeys[2]) {
            new Macro(Macro.prevMacro);
        }
        if (e.getKeyCode() == hotKeys[3]) {
            Macro.closeMacros();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    /**
     * Checks if arguments are unique and are within the valid index range for {@link #hotKeys}
     * @param index the index in {@link #hotKeys}
     * @param keyConstant the keycode constant as listed in {@link NativeKeyEvent}
     * @return returns whether the arguments is valid or not
     */
    public static boolean isValidEntry(final int index, final int keyConstant) {
        return Arrays.stream(hotKeys).noneMatch(i -> i == keyConstant) && (index >= 0 && index <= hotKeys.length - 1);
    }

    /**
     * Reads all of the hotkey keycodes and writes them into the hotkey keycode array.
     */
    private void readHotKey(){
        try (BufferedReader keyCodeReader = new BufferedReader(new FileReader(Macro.WORKING_DIR + "hotkeys.txt"))){
            int index = 0;

            for (String line = keyCodeReader.readLine(); line != null; line = keyCodeReader.readLine()){
                hotKeys[index] = Integer.parseInt(line.substring(line.indexOf("=") + 1));
                index++;
            }

        } catch (FileNotFoundException e){
            CommandHelper.printError("Could not find the hotkeys.txt file.");
        } catch (IOException e) {
            CommandHelper.printError("Could not read from hotkeys.txt.");
        }
    }

    /**
     * Writes to the hotkeys.txt file with the updated hotkey:keycode pair.
     * @param hotKeyToMatch the hotkey to update
     * @param keyCodeToWrite the keycode to update the specified hotkey
     */
    public static void writeHotKey(String hotKeyToMatch, String keyCodeToWrite){
        List<String> newLines = new ArrayList<>();
        try {
            for(String line : Files.readAllLines(Paths.get(Macro.WORKING_DIR + "hotkeys.txt"))){
                if(line.contains(hotKeyToMatch)){
                    newLines.add(line.substring(0, line.indexOf("=") +1 ) + keyCodeToWrite);
                } else {
                    newLines.add(line);
                }
            }
            Files.write(Paths.get(Macro.WORKING_DIR + "hotkeys.txt"), newLines);
        } catch (FileNotFoundException e) {
            CommandHelper.printError("The hotkeys.txt file could not be found.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            CommandHelper.printError("The file could not be written to. Please check that you have");
            CommandHelper.printError("Sufficient file writing permission.");
        }
    }

    public static void start() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e1) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(e1.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
    }
}