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

    static public String versionName = "Macro Parkour Tool 0.2.3";
    static public String versionDate = "2021-05-27";

    static long milliseconds;
    static String[] args;

    static JConsole console;
    static CommandHelper commandHelper;


    public static void main(String[] args) throws AWTException {
        Main.args = args;

        console = new JConsole(Main.versionName);
        console.setVisible(true);

        commandHelper = new CommandHelper();
        Robot robot = new Robot();
        GlobalKeyListener.start();

        ArrayList<Macro> macroList = new ArrayList<>();
        Config.readOptions();
        System.out.println("Current scaling factor: " + Helper.getScalingFactor());
        milliseconds = System.currentTimeMillis();
        milliseconds = milliseconds - milliseconds % 50;
        sleepTillTick();
        //noinspection InfiniteLoopStatement
        while (true) {
//TODO runs twice when running start command
            commandHelper.listen();
            if (Macro.currentMacros.size() != 0) {
                try {
                    macroList.addAll(Macro.currentMacros);
                    macroList.forEach(Macro::readLine);
                    macroList.clear();
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

            milliseconds += 50;
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

        if (Macro.currentXRotation != Macro.xRotation) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            int x = p.x;
            int y = p.y;

            int fullX = calcYaw();
            int deltaX = fullX - Macro.currentXPixels;
            Macro.currentXPixels = fullX;

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

            robot.mouseMove(x, y + deltaY);
            Macro.currentYRotation = Macro.yRotation;
        }
    }

    static int calcYaw() {
        float num;
        num = Macro.xRotation;
        num = (float) ((double) num / 0.15);

        float f = (float) (Config.mouseSensitivity * 0.6F + 0.2F);
        float f1 = f * f * f * 8.0F;
        return (int) ((num / f1) / Helper.getScalingFactor());
    }

    static int calcPitch() {
        float num;
        num = Macro.yRotation;
        num = (float) ((double) num / 0.15);

        float f = (float) (Config.mouseSensitivity * 0.6F + 0.2F);
        float f1 = f * f * f * 8.0F;
        return (int) (-1 * ((num / f1) / Helper.getScalingFactor()));
    }

    public static void sleepTillTick() {
        if (50 - (System.currentTimeMillis() - milliseconds) > 0) {
            try {
                Thread.sleep((50 - (System.currentTimeMillis() - milliseconds)));
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }

    public static JConsole getConsole(){
        return console;
    }
}