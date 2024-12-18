package dev.imb11.flow.render;

import dev.imb11.flow.Flow;
import dev.imb11.flow.config.FlowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.awt.*;

public class FlowBackgroundHelper {
    public static boolean shouldSkipRender() {
        return Flow.areBackgroundModsPresent() || FlowConfig.get().disableAllBackgroundModifications;
    }
    public static void renderStaticBg(Screen screen, DrawContext context) {
        if(shouldSkipRender()) return;

        Color color = FlowConfig.get().bgColorTint;
        int AARRGGBB = (color.getAlpha() << 24) | (color.getRGB() & 0x00FFFFFF);
        renderBgEffects(screen.width, screen.height, context, FlowConfig.get().bgBlurIntensity * 16, AARRGGBB);
    }

    public static void renderBgEffects(float width, float height, DrawContext context, float blurIntensity, int color) {
        if(shouldSkipRender()) return;

        if(!FlowConfig.get().disableBgTint) {
            context.fill(0, 0, (int) width, (int) height, color);
        }

        if(!FlowConfig.get().disableBgBlur) {
            var client = MinecraftClient.getInstance();

            //? if >=1.21.2 {
            PostEffectProcessor postEffectProcessor = client.getShaderLoader().loadPostEffect(Identifier.ofVanilla("blur"), net.minecraft.client.render.DefaultFramebufferSet.MAIN_ONLY);
            if (postEffectProcessor != null) {
                postEffectProcessor.setUniforms("Radius", blurIntensity);
                postEffectProcessor.render(client.getFramebuffer(), client.gameRenderer.pool);
            }
            //?} else {
            /*client.gameRenderer.blurPostProcessor.setUniforms("Radius", blurIntensity);
            client.gameRenderer.blurPostProcessor.render(client.getRenderTickCounter().getTickDelta(true));
            *///?}
        }
    }
}
