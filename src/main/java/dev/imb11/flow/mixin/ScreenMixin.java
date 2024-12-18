package dev.imb11.flow.mixin;

import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.Easings;
import dev.imb11.flow.api.animation.OffsetProvider;
import dev.imb11.flow.config.FlowConfig;
import dev.imb11.flow.render.FlowBackgroundHelper;
import dev.imb11.flow.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class ScreenMixin extends Screen {
    @Unique
    private float elapsed = 0f;

    @Shadow protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);

    protected ScreenMixin(Text title) {
        super(title);
    }

    @Unique
    public boolean temp_disableEaseIn = false;

    @Inject(method = "init", at = @At("HEAD"), cancellable = false)
    private void $mark_open_animation(CallbackInfo ci) {
        FlowAPI.setInTransition(true);
        if(FlowAPI.DISABLE_TEMPORARILY) {
            temp_disableEaseIn = true;
            boolean inCreative = ((Object) this) instanceof InventoryScreen;
            if (!((inCreative && this.client.interactionManager.hasCreativeInventory()))) {
                FlowAPI.toggleTemporaryDisable();
            }
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void $mark_exit_animation(CallbackInfo ci) {
        if(isDisabledScreen() || FlowConfig.get().disableEaseOut) return;

        FlowAPI.setInTransition(true);
        FlowAPI.setClosing(true);
        RenderHelper.elapsed = 0f;
    }

    @Unique
    public boolean isDisabledScreen() {
        String className = this.getClass().getName();
        return FlowConfig.get().disabledScreens.contains(className);
    }

    @Override
    public void renderInGameBackground(DrawContext context) {
        if(RenderHelper.isRendering || FlowBackgroundHelper.shouldSkipRender()) {
            if(!RenderHelper.isRendering) {
                super.renderInGameBackground(context);
            }
            return;
        }
        assert this.client != null;

        if (isDisabledScreen() || (FlowConfig.get().disableEaseIn)) {
            FlowBackgroundHelper.renderStaticBg(this, context);
            return;
        }

        float progress = (elapsed / FlowConfig.get().easeInDuration);
        float eased = Easings.easeInOutCubic.eval(progress);

        if (temp_disableEaseIn) {
            eased = 1.0f;
        }

        int alpha = MathHelper.lerp(eased, 0x00, 0x40);
        int RRGGBB = FlowConfig.get().bgColorTint.getRGB();
        int AARRGGBB = (alpha << 24) | (RRGGBB & 0x00FFFFFF);
        float blurIntensity = MathHelper.lerp(eased, 0, FlowConfig.get().bgBlurIntensity * 16);

        FlowBackgroundHelper.renderBgEffects(this.width, this.height, context, blurIntensity, AARRGGBB);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private void $render_animation(Screen instance, DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        if(RenderHelper.isRendering) {
            super.render(context, mouseX, mouseY, delta);
            return;
        }

        RenderHelper.cacheScreen((HandledScreen<?>) (Object) this, context, delta, mouseX, mouseY);

        elapsed += MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration() / 25;

        var totalTime = FlowConfig.get().easeInDuration;

        if (elapsed > totalTime) elapsed = totalTime;

        float progress = (elapsed / totalTime);

        FlowAPI.setTransitionProgress(progress);
        FlowAPI.setClosing(false);

        if(progress == 1.0f) {
            FlowAPI.setInTransition(false);
        }

        boolean shouldApply = true;

        if((FlowConfig.get().disableEaseIn || isDisabledScreen() || temp_disableEaseIn) && !FlowAPI.isClosing()) {
            context.getMatrices().push();
            shouldApply = false;
        }

        if(shouldApply) {
            AnimationType animationType = AnimationType.getAnimationType(false);
            OffsetProvider provider = animationType.calculateOffset(this.width, this.height, progress, false);
            provider.apply(context.getMatrices());
        }

        this.drawBackground(context, delta, mouseX, mouseY);

        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            drawable.render(context, mouseX, mouseY, delta);
        }
    }
}