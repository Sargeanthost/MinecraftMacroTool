package logic;

import java.awt.*;


public class Helper {
    public static int[] outputKey = {Config.forwardKey, Config.leftKey, Config.backKey, Config.rightKey, Config.sprintKey, Config.sneakKey, Config.jumpKey,
            Config.attackKey, Config.useKey};

    /**
     * Enum for getting dpi based off of screen resolutions to fix scaling problems which lead to wrong rotation values.
     */
    private enum ResolutionDpi {
        _1920(96), _1440(109), _2160(185) ;

        private final int dpi;

        ResolutionDpi(int dpi){
            this.dpi = dpi;
        }

        int getDpi(){
            return dpi;
        }

    }

    public static double getScalingFactor(){
        try{
            ResolutionDpi resWidth = ResolutionDpi.valueOf("_" + Toolkit.getDefaultToolkit().getScreenSize().width);
            return (double) Toolkit.getDefaultToolkit().getScreenResolution() / resWidth.getDpi();

        } catch (HeadlessException e){
            System.out.println("How are you even playing Minecraft??");
        }
        return 0;
    }
}
