package dev.imb11.flow.compat.emi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmiCompat {
    public static final Logger LOGGER = LoggerFactory.getLogger("flow:emi");
    public static void displayExpandWarningToast() {
        LOGGER.info("Displaying EMI warning toast.");
        LOGGER.warn("EMI is not compatible with Flow's expand animation. It's recommended to use a different animation instead.");
        MinecraftClient client = MinecraftClient.getInstance();

        client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("flow.toast.emi.title"), Text.translatable("flow.toast.emi.description")));
    }
}
