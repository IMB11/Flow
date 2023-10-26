package dev.mineblock11.flow.mixin;

import dev.mineblock11.flow.config.Easings;
import dev.mineblock11.flow.config.FlowConfig;
import dev.mineblock11.flow.render.BlurHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ScreenMixin extends Screen {
    @Shadow protected int backgroundHeight;
    @Shadow protected int backgroundWidth;
    @Unique
    private float elapsed = 0f;
    @Unique
    public boolean isClosing = false;
    @Unique
    public volatile boolean finishedCloseAnimation = false;

    protected ScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    private void $mark_exit_animation(CallbackInfo ci) {
        ci.cancel();
        elapsed = 0f;
        isClosing = true;
        new Thread(() -> {
            while (!finishedCloseAnimation) {
                Thread.onSpinWait();
            }
            assert this.client != null;
            this.client.execute(() -> {
                assert this.client.player != null;
                this.client.player.closeHandledScreen();
                super.close();
            });
        }).start();
    }

    @Unique
    public void renderBlur(DrawContext context, float size, float quality) {
        var buffer = Tessellator.getInstance().getBuffer();
        var matrix = context.getMatrices().peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        buffer.vertex(matrix, 0, 0, 0).next();
        buffer.vertex(matrix, 0, this.height, 0).next();
        buffer.vertex(matrix, this.width, this.height, 0).next();
        buffer.vertex(matrix, this.width, 0, 0).next();

        BlurHelper.INSTANCE.setParameters(16, quality, size);
        BlurHelper.INSTANCE.use();

        Tessellator.getInstance().draw();
    }

    @Override
    public void renderBackground(DrawContext context) {
        assert this.client != null;
        if (this.client.world != null) {
            float progress = isClosing ? 1 - (elapsed / FlowConfig.get().easeOutDuration) : (elapsed / FlowConfig.get().easeInDuration);

            float eased = Easings.easeInOutCubic.eval(progress);

            int alpha = (int) MathHelper.lerp(eased, 0x00, 0xCF);

            // Convert to color specified in config using the alpha from above.
            // format is 0xAARRGGBB
            int RRGGBB = FlowConfig.get().bgColorTint.getRGB();
            int AARRGGBB = (alpha << 24) | (RRGGBB & 0x00FFFFFF);

            context.fill(0, 0, this.width, this.height, AARRGGBB);

            // Lerp the blur intensity from 0 to FlowConfig.get().bgBlurIntensity
            float blurIntensity = MathHelper.lerp(eased, 0, FlowConfig.get().bgBlurIntensity * 16);

            // Render the blur from the config.
            this.renderBlur(context, blurIntensity, 16);
        } else {
            this.renderBackgroundTexture(context);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void $render_animation(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        elapsed += MinecraftClient.getInstance().getLastFrameDuration() / 25;

        var totalTime = 0.3f;
        if(isClosing) {
            totalTime = FlowConfig.get().easeOutDuration;
        } else {
            totalTime = FlowConfig.get().easeInDuration;
        }

        if (elapsed > totalTime) elapsed = totalTime;

        float progress = isClosing ? 1 - (elapsed / totalTime) : (elapsed / totalTime);

        if(isClosing) {
            this.finishedCloseAnimation = progress == 0;

            if(FlowConfig.get().enableEaseOut) {
                float offset = MathHelper.lerp(FlowConfig.get().easeOutType.eval(progress), this.client.getWindow().getHeight(), 0);
                context.getMatrices().translate(0, offset, 0);
            }
        } else {
            if(FlowConfig.get().enableEaseIn) {
                float offset = MathHelper.lerp(FlowConfig.get().easeInType.eval(progress), -this.client.getWindow().getHeight(), 0);
                context.getMatrices().translate(0, -offset, 0);
            }
        }

        context.getMatrices().push();
    }
}