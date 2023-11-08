package dev.mineblock11.flow.api;

import dev.mineblock11.flow.compat.emi.EmiCompat;
import dev.mineblock11.flow.config.FlowConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

public class FlowAPI {
    /**
     * This boolean is used to determine whether the screen is currently undergoing a transition animation.
     */
    protected static boolean IN_TRANSITION = false;

    /**
     * This float is used to determine the progress of the transition animation.
     * If the float is -1, the screen is not undergoing a transition animation.
     */
    protected static float TRANSITION_PROGRESS = -1f;

    /**
     * This boolean is used to determine whether the screen is currently closing.
     */
    protected static boolean IS_CLOSING = false;

    /**
     * This method is used to get the progress of the transition animation.
     * @return The progress of the transition animation.
     */
    public static float getTransitionProgress() {
        return TRANSITION_PROGRESS;
    }

    /**
     * This method is used to set the progress of the transition animation.
     * @param progress The progress of the transition animation.
     */
    @ApiStatus.Internal
    public static void setTransitionProgress(float progress) {
        TRANSITION_PROGRESS = progress;
    }

    /**
     * This method is used to get whether the screen is currently closing.
     */
    public static boolean isClosing() {
        return IS_CLOSING;
    }

    /**
     * This method is used to set whether the screen is currently closing.
     * @param closing Whether the screen is currently closing.
     */
    @ApiStatus.Internal
    public static void setClosing(boolean closing) {
        IS_CLOSING = closing;
    }

    /**
     * This method is used to determine whether you should calculate the transition animation and apply it.
     */
    public static boolean shouldCalculate() {
        if(FlowAPI.isClosing() && !FlowConfig.get().disableEaseOut) {
            return true;
        } else return !FlowConfig.get().disableEaseIn;
    }

    /**
     * This method is used to determine whether the screen is currently undergoing a transition animation.
     * @return Whether the screen is currently undergoing a transition animation.
     */
    public static boolean isInTransition() {
        return IN_TRANSITION;
    }

    /**
     * This method is used to set whether the screen is currently undergoing a transition animation.
     * @param inTransition Whether the screen is currently undergoing a transition animation.
     */
    @ApiStatus.Internal
    public static void setInTransition(boolean inTransition) {
        IN_TRANSITION = inTransition;

        if(!IN_TRANSITION) {
            TRANSITION_PROGRESS = -1f;
        }
    }

    /**
     * Called when the YACL screen closes and the config is saved.
     */
    public static void handleConfigSaving() {
        FlowConfig.CONFIG_CLASS_HANDLER.save();

        FabricLoader loader = FabricLoader.getInstance();

        if(loader.isModLoaded("emi")) {
            if(FlowConfig.get().easeInAnimationType.name().contains("expand")
            || FlowConfig.get().easeOutAnimationType.name().contains("expand")) {
                EmiCompat.displayExpandWarningToast();
            }
        }
    }
}
