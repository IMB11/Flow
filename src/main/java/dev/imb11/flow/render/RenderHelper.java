package dev.imb11.flow.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.imb11.flow.Flow;
import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.Easings;
import dev.imb11.flow.api.animation.OffsetDistributor;
import dev.imb11.flow.api.animation.OffsetProvider;
import dev.imb11.flow.api.rendering.FlowBlurHelper;
import dev.imb11.flow.config.FlowConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.WindowFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.util.math.MathHelper;

public class RenderHelper {
    private static WindowFramebuffer framebuffer;
    private static int fbWidth, fbHeight;
    public static float elapsed = 0f;
    public static boolean isRendering = false;

    public static void renderOutput(DrawContext context, float frameDuration) {
        if(framebuffer == null) return;
        MinecraftClient client = MinecraftClient.getInstance();

        var w = client.getWindow().getScaledWidth();
        var h = client.getWindow().getScaledHeight();

        if(w <= 0 || h <= 0) {
            framebuffer = null;
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());

        elapsed += frameDuration;
        float progress = 1.0f - (elapsed / (FlowConfig.get().easeOutDuration));
        FlowAPI.setTransitionProgress(progress);

        if(progress <= 0.01f || elapsed > FlowConfig.get().easeOutDuration) {
            progress = 0f;
            FlowAPI.setInTransition(false);
            FlowAPI.setClosing(false);
        }

        context.getMatrices().push();
        float eased = Easings.easeInOutCubic.eval(progress);
        int alpha = MathHelper.lerp(eased, 0x00, 0x40);
        int RRGGBB = FlowConfig.get().bgColorTint.getRGB();
        int AARRGGBB = (alpha << 24) | (RRGGBB & 0x00FFFFFF);
        float blurIntensity = MathHelper.lerp(eased, 0, FlowConfig.get().bgBlurIntensity * 16);


        FlowBackgroundHelper.renderBgEffects(w, h, context, blurIntensity, AARRGGBB);
        context.getMatrices().pop();

        context.getMatrices().push();
        AnimationType animationType = AnimationType.getAnimationType(true);

        OffsetProvider provider = animationType.calculateOffset(w, h, progress, true);
        provider.apply(context.getMatrices());

        /*? if >=1.21 {*/
        var buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        var pose = context.getMatrices().peek().getPositionMatrix();
        buffer.vertex(pose, 0, h, 0).texture(0f, 0f);
        buffer.vertex(pose, w, h, 0).texture(1f, 0f);
        buffer.vertex(pose, w, 0, 0).texture(1f, 1f);
        buffer.vertex(pose, 0, 0, 0).texture(0f, 1f);
        /*?} else {*/
        /*var tesselator = Tessellator.getInstance();
        var buffer = tesselator.getBuffer();
        var pose = context.getMatrices().peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(pose, 0, h, 0).texture(0f, 0f).next();
        buffer.vertex(pose, w, h, 0).texture(1f, 0f).next();
        buffer.vertex(pose, w, 0, 0).texture(1f, 1f).next();
        buffer.vertex(pose, 0, 0, 0).texture(0f, 1f).next();
        *//*?}*/
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);

        /*? if <1.21 {*//*
        tesselator.draw();
        *//*?} else {*/
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        /*?}*/

        RenderSystem.disableBlend();

        context.getMatrices().pop();
    }

    public static void cacheScreen(HandledScreen<?> screenToCache, DrawContext ctx, float tickDelta, int mouseX, int mouseY) {
        isRendering = true;
        FabricLoader.getInstance().getObjectShare().put("flow:is_caching_screen", true);

        MinecraftClient client = MinecraftClient.getInstance();
        var window = client.getWindow();

        if(window.getWidth() <= 0 || window.getHeight() <= 0) {
            framebuffer = null;
            return;
        }

        if (framebuffer == null) {
            framebuffer = new WindowFramebuffer(window.getWidth(), window.getHeight());
            framebuffer.setClearColor(0f, 0f, 0f, 0f);
        }

        if (window.getWidth() != fbWidth || window.getHeight() != fbHeight) {
            fbWidth = window.getWidth();
            fbHeight = window.getHeight();
            framebuffer.resize(fbWidth, fbHeight, MinecraftClient.IS_SYSTEM_MAC);
        }

        framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        framebuffer.beginWrite(true);
        screenToCache.render(ctx, mouseX, mouseY, tickDelta);
        framebuffer.endWrite();
        client.getFramebuffer().beginWrite(true);
        isRendering = false;
        FabricLoader.getInstance().getObjectShare().put("flow:is_caching_screen", false);
    }
}
