package dev.imb11.flow.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.WindowFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;

public class RenderHelper {
    private static WindowFramebuffer framebuffer;
    private static int fbWidth, fbHeight;
    public static boolean isRendering = false;

    public static void renderOutput(DrawContext context) {
        if(framebuffer == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        var window = client.getWindow();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());

        var w = client.getWindow().getScaledWidth();
        var h = client.getWindow().getScaledHeight();

        var buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        var pose = context.getMatrices().peek().getPositionMatrix();
        buffer.vertex(pose, 0, h, 0).texture(0f, 0f);
        buffer.vertex(pose, w, h, 0).texture(1f, 0f);
        buffer.vertex(pose, w, 0, 0).texture(1f, 1f);
        buffer.vertex(pose, 0, 0, 0).texture(0f, 1f);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.disableBlend();
    }

    public static void cacheScreen(HandledScreen<?> screenToCache, DrawContext ctx, float tickDelta) {
        isRendering = true;
        MinecraftClient client = MinecraftClient.getInstance();
        var window = client.getWindow();

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
        screenToCache.render(ctx, -1, -1, tickDelta);
        framebuffer.endWrite();
        client.getFramebuffer().beginWrite(true);
        isRendering = false;
    }
}
