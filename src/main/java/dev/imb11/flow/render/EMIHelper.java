package dev.imb11.flow.render;

import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenBase;
import dev.emi.emi.screen.EmiScreenManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class EMIHelper {
    public static void renderEMI(Screen screen, DrawContext context, int mouseX, int mouseY, float delta) {
        EmiScreenManager.render(EmiDrawContext.wrap(context), mouseX, mouseY, delta);
    }
}
