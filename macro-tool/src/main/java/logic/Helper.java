package logic;

import java.awt.*;


public class Helper {
    public static int[] outputKey = {Config.forwardKey, Config.leftKey, Config.backKey, Config.rightKey, Config.sprintKey, Config.sneakKey, Config.jumpKey,
            Config.attackKey, Config.useKey};

    public static double getScalingFactor(){
        try{
            //as far as I could find, it is always 96 for 100% no matter the resolution
            return (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;

        } catch (HeadlessException e) {
            CommandHelper.printError("How are you even playing Minecraft??");
        }
        return 1;
    }
}
