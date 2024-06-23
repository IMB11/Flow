package dev.imb11.flow.render;

import dev.imb11.flow.api.rendering.FlowBlurHelper;
import dev.imb11.flow.config.FlowConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class FlowBackgroundHelper {
    public static void renderStaticBg(Screen screen, DrawContext context) {
        var alpha = 0xCF;
        var AARRGGBB = (alpha << 24) | (FlowConfig.get().bgColorTint.getRGB() & 0x00FFFFFF);
        renderBgEffects(screen, context, FlowConfig.get().bgBlurIntensity * 16, AARRGGBB);
    }

    public static void renderBgEffects(Screen screen, DrawContext context, float blurIntensity, int color) {
        if(!FlowConfig.get().disableBgTint) {
            context.fill(0, 0, screen.width, screen.height, color);
        }

        if(!FlowConfig.get().disableBgBlur) {
            FlowBlurHelper.apply(screen.width, screen.height, context, blurIntensity, 4);
        }
    }
}
