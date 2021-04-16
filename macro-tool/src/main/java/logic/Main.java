package logic;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import ext.GlobalKeyListener;
import ui.JConsole;

public abstract class Main {

    static public String versionName = "Macro Parkour Tool 0.1.4";
    static public String versionDate = "2021-04-15";

    static long millis;
    static String[] args;

    static JConsole console;
    static CommandHelper ch;


    public static void main(String[] args) throws AWTException {
        Main.args = args;

        console = new JConsole(Main.versionName);
        console.setVisible(true);

        ch = new CommandHelper();
        Robot robot = new Robot();
        GlobalKeyListener.start();

        ArrayList<Macro> mac = new ArrayList<>();
        Config.readOptions();

        millis = System.currentTimeMillis();
        millis = millis - millis % 50;
        sleepTillTick();
        //noinspection InfiniteLoopStatement
        while (true) {
// runs twice when running start command
            ch.listen();
            if (Macro.currentMacros.size() != 0) {
                try {
                    mac.addAll(Macro.currentMacros);
                    mac.forEach(Macro::readLine);
                    mac.clear();
                } catch (ConcurrentModificationException e1) {
                    e1.printStackTrace();
                }

                Macro.currentMacros.removeIf((Macro m) -> {
                    try {
                        return !m.userInput.ready();
                    } catch (IOException e2) {
                        System.out.println("main 48");
                        e2.printStackTrace();
                    } catch (NullPointerException e3) {
                        System.out.println("main 72");
                        e3.printStackTrace();
                    }
                    return false;
                });
            }
            if (!Macro.allOutputsClosed) {
                outputStuff(robot);
            } else if (Macro.justClosed) {
                for (int i = 0; i <= 8; i++) {
                    if (Helper.outputKey[i] < 1000) {
                        robot.keyRelease(Helper.outputKey[i]);
                    } else {
                        robot.mouseRelease(Helper.outputKey[i]);
                    }
                }
                Macro.justClosed = false;
            }

            sleepTillTick();

            millis += 50;
        }
    }

    public static void outputStuff(Robot robot) {
        for (int i = 0; i < 9; i++) {
            if (Macro.pressIterations[i] > -1) {
                Macro.pressIterations[i]--;
                //System.out.println(logic.Helper.outputKey[i]);
                if (Helper.outputKey[i] < 1000) {
                    if (Macro.pressIterations[i] > -1) {
                        robot.keyPress(Helper.outputKey[i]);
                    } else {
                        robot.keyRelease(Helper.outputKey[i]);
                    }
                } else {
                    if (Macro.pressIterations[i] > -1) {
                        robot.mousePress(Helper.outputKey[i]);
                    } else {
                        robot.mouseRelease(Helper.outputKey[i]);
                    }
                }
            }
        }

        //TODO restart math part with original, no turns are working now.
        if (Macro.currentXRotation != Macro.xRotation) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            int x = p.x;
            int y = p.y;

            int fullX = calcYaw();
//          now x                start x ? should be 0 the first time, try setting 45
            int deltaX = fullX - Macro.currentXPixels;
            Macro.currentXPixels = fullX;
            //System.out.println(logic.Macro.currentXPixels);
            //System.out.println(fullX);
            //System.out.println(deltaX);

            robot.mouseMove(x + deltaX, y);
            Macro.currentXRotation = Macro.xRotation;
        }
        if (Macro.currentYRotation != Macro.yRotation) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            int x = p.x;
            int y = p.y;

            int fullY = calcPitch();

            int deltaY = fullY - Macro.currentYPixels;
            Macro.currentYPixels = fullY;
            //System.out.println(logic.Macro.currentXPixels);
            //System.out.println(fullX);
            //System.out.println(deltaX);

            robot.mouseMove(x, y + deltaY);
            Macro.currentYRotation = Macro.yRotation;
        }

    }

    static int calcYaw() {
        //calcfullx
        float num;
        num = Macro.xRotation;
        num = (float) ((double) num / 0.15);

        float f = (float) (Config.mouseSensitivity * 0.6F + 0.2F);
        float f1 = f * f * f * 8.0F;

        return (int) (num / f1);

//        return (int) ((45 * Math.pow(Config.mouseSensitivity * 0.6F + 0.2F, 3) * 8.0F) * 0.15D);
// Macro.xRotation


    }

    static int calcPitch() {
        //calcfully
        float num;
        num = Macro.yRotation;
        num = (float) ((double) num / 0.15);

        float f = (float) (Config.mouseSensitivity * 0.6F + 0.2F);
        float f1 = f * f * f * 8.0F;
        return (int) (num / f1);

    }

    public static void sleepTillTick() {
        //System.out.println(Arrays.toString(logic.Macro.pressIterations));
        //System.out.println(50 - (System.currentTimeMillis() - millis));

        if (50 - (System.currentTimeMillis() - millis) > 0) {
            try {
                Thread.sleep((50 - (System.currentTimeMillis() - millis)));
                //System.out.println(System.currentTimeMillis());
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }

}


