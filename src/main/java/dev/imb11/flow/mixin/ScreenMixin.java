package dev.imb11.flow.mixin;

import dev.imb11.flow.Flow;
import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.Easings;
import dev.imb11.flow.api.animation.OffsetProvider;
import dev.imb11.flow.config.FlowConfig;
import dev.imb11.flow.render.FlowBackgroundHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(HandledScreen.class)
public abstract class ScreenMixin extends Screen {
    @Unique
    private float elapsed = 0f;
    @Unique
    public boolean isClosing = false;
    @Unique
    public volatile boolean finishedCloseAnimation = false;

    /*? if >=1.20.2 {*/
    @Shadow protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);
    /*?}*/

    @Shadow @Final protected ScreenHandler handler;

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

    @Unique
    public void safelyUnlockMouse() {
        assert this.client != null;
        if (this.client.isWindowFocused()) {
            if (!this.client.mouse.isCursorLocked()) {
                if (!MinecraftClient.IS_SYSTEM_MAC) {
                    KeyBinding.updatePressedStates();
                }

                this.client.mouse.cursorLocked = true;
                this.client.mouse.x = (double) this.client.getWindow().getWidth() / 2;
                this.client.mouse.y = (double) this.client.getWindow().getHeight() / 2;
                InputUtil.setCursorParameters(this.client.getWindow().getHandle(), 212995, this.client.mouse.x, this.client.mouse.y);
                this.client.attackCooldown = 10000;
                this.client.mouse.hasResolutionChanged = true;
            }
        }
    }

    @Inject(method = "close", at = @At("TAIL"), cancellable = true)
    private void $mark_exit_animation(CallbackInfo ci) {
        if(isDisabledScreen() || FlowAPI.shouldAvoidCalculation()) return;

        ci.cancel();
        if(isClosing) return;
        FlowAPI.setInTransition(true);
        elapsed = 0f;
        isClosing = true;
        this.client.getSoundManager().resumeAll();
        this.safelyUnlockMouse();

        new Thread(() -> {
            while (!finishedCloseAnimation) {
                Thread.onSpinWait();
            }
            assert this.client != null;
            assert this.client.player != null;
            this.client.execute(() -> {
                this.finishedCloseAnimation = false;
                FlowAPI.setInTransition(false);
                this.client.player.closeHandledScreen();
                super.close();
            });
        }).start();
    }

    @Unique
    public boolean isDisabledScreen() {
        String className = this.getClass().getName();
        return FlowConfig.get().disabledScreens.contains(className);
    }

    @Override
    /*? if <1.20.2 {*//*
    public void renderBackground(DrawContext context) {
    *//*?} else {*/
    public void renderInGameBackground(DrawContext context) {
    /*?}*/
        assert this.client != null;

        if (isClosing && FlowConfig.get().disableEaseOut || isDisabledScreen()) {
            FlowBackgroundHelper.renderStaticBg(this, context);
            return;
        } else if (!isClosing && (FlowConfig.get().disableEaseIn || temp_disableEaseIn)) {
            FlowBackgroundHelper.renderStaticBg(this, context);
            return;
        }

        if (this.client.world != null) {
            float progress = isClosing ? 1 - (elapsed / FlowConfig.get().easeOutDuration) : (elapsed / FlowConfig.get().easeInDuration);

            float eased = Easings.easeInOutCubic.eval(progress);

            int alpha = MathHelper.lerp(eased, 0x00, 0xCF);

            // Convert to color specified in config using the alpha from above.
            // format is 0xAARRGGBB
            int RRGGBB = FlowConfig.get().bgColorTint.getRGB();
            int AARRGGBB = (alpha << 24) | (RRGGBB & 0x00FFFFFF);

            // Lerp the blur intensity from 0 to FlowConfig.get().bgBlurIntensity
            float blurIntensity = MathHelper.lerp(eased, 0, FlowConfig.get().bgBlurIntensity * 16);

            FlowBackgroundHelper.renderBgEffects(this, context, blurIntensity, AARRGGBB);
        } else {
            /*? if <1.20.5 {*//*
            this.renderBackgroundTexture(context);
            *//*?} else {*/
            this.renderInGameBackground(context);
            /*?}*/
        }
    }

    @Inject(method = "isPointOverSlot", at = @At("HEAD"), cancellable = true)
    private void $isPointOverSlot(Slot slot, double pointX, double pointY, CallbackInfoReturnable<Boolean> cir) {
        if(isClosing) {
            cir.setReturnValue(false);
        }
    }

    /*? if <1.20.2 {*//*
    @Inject(method = "render", at = @At("HEAD"))
    private void $render_animation(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
    *//*?} else {*/
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private void $render_animation(Screen instance, DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
    /*?}*/

        /*? if <1.21 {*//*
        elapsed += MinecraftClient.getInstance().getLastFrameDuration() / 25;
        *//*?} else {*/
        elapsed += MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration() / 25;
        /*?}*/

        var totalTime = 0.3f;
        if(isClosing) {
            totalTime = FlowConfig.get().easeOutDuration;
        } else {
            totalTime = FlowConfig.get().easeInDuration;
        }

        if (elapsed > totalTime) elapsed = totalTime;

        float progress = isClosing ? 1 - (elapsed / totalTime) : (elapsed / totalTime);

        FlowAPI.setTransitionProgress(progress);
        FlowAPI.setClosing(isClosing);

        if(!isClosing && progress == 1.0f) {
            FlowAPI.setInTransition(false);
        }

        boolean shouldApply = true;
        if(isClosing) {
            this.finishedCloseAnimation = progress == 0;

            if(FlowConfig.get().disableEaseOut || isDisabledScreen()) {
                context.getMatrices().push();

                shouldApply = false;
            }
        } else if(FlowConfig.get().disableEaseIn || isDisabledScreen() || temp_disableEaseIn) {
            context.getMatrices().push();
            shouldApply = false;
        }

        if(shouldApply) {
            AnimationType animationType = AnimationType.getAnimationType(isClosing);
            OffsetProvider provider = animationType.calculateOffset(this.width, this.height, progress, isClosing);
            provider.apply(context.getMatrices());
        }

        /*? if <1.20.2 {*//*
        context.getMatrices().push();
        *//*?} else {*/
        this.drawBackground(context, delta, mouseX, mouseY);

        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            drawable.render(context, mouseX, mouseY, delta);
        }
        /*?}*/
    }
}