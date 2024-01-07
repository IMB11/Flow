package dev.imb11.flow.compat.emi;

import com.mineblock11.mru.entry.CompatabilityEntrypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmiCompat implements CompatabilityEntrypoint {
    public static final Logger LOGGER = LoggerFactory.getLogger("flow:emi");
    public static void displayExpandWarningToast() {
        LOGGER.info("Displaying EMI warning toast.");
        LOGGER.warn("EMI is not compatible with Flow's expand animation. It's recommended to use a different animation instead.");
        MinecraftClient client = MinecraftClient.getInstance();
        client.getToastManager().add(SystemToast.create(client, SystemToast.Type.TUTORIAL_HINT, Text.translatable("flow.toast.emi.title"), Text.translatable("flow.toast.emi.description")));
    }

    @Override
    public void initialize() {
        LOGGER.info("Initialized EMI compatability.");
    }
}
