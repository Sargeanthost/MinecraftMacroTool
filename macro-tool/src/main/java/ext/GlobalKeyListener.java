package ext;

import java.util.logging.Level;
import java.util.logging.Logger;

import logic.Config;
import logic.Macro;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;


public class GlobalKeyListener implements NativeKeyListener {
    public void nativeKeyPressed(NativeKeyEvent e) {
        //throws NullPointerException
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        //add keybind config

        if (e.getKeyCode() == NativeKeyEvent.VC_F10) {
            Macro.closeMacros();
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_F9) {
            System.out.println("Executing: " + Macro.prevMacro);
            new Macro(Macro.prevMacro);
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_F8) {
            Config.readOptions();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public static void start() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
    }
}