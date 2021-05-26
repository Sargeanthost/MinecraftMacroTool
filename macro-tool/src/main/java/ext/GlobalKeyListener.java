package ext;

import java.util.logging.Level;
import java.util.logging.Logger;

import logic.Config;
import logic.Macro;
import logic.Main;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;


public class GlobalKeyListener implements NativeKeyListener {
    private static int[] hotKeys = {NativeKeyEvent.VC_F7, NativeKeyEvent.VC_F8, NativeKeyEvent.VC_F9,
            NativeKeyEvent.VC_F10};

    public void nativeKeyPressed(NativeKeyEvent e) {
//        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if(e.getKeyCode() == hotKeys[0]){
            Main.getConsole().getTextOutput().clear();
        }
        if (e.getKeyCode() == hotKeys[1]) {
            Config.readOptions();
        }
        if (e.getKeyCode() == hotKeys[2]) {
            new Macro(Macro.prevMacro);
        }
        if (e.getKeyCode() == hotKeys[3]) {
            Macro.closeMacros();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    /**
     * Sets the specified hotkey.
     * @param index the index to modify. Must be between 0 and 3
     * @param keyConstant the key constant integer as specified in {@link NativeKeyEvent}
     */
    public static boolean setHotKey(int index, int keyConstant){
        if(index >= 0 && index <= 3){
            hotKeys[index] = keyConstant;
            return true;
        }
        return false;
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