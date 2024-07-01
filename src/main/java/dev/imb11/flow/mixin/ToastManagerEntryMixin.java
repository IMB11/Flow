package dev.imb11.flow.mixin;

import dev.imb11.flow.Flow;
import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.OffsetProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ToastManager.Entry.class)
public class ToastManagerEntryMixin {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"), cancellable = false)
    public void $negate_transition(int x, DrawContext context, CallbackInfoReturnable<Boolean> cir) {
        if(FlowAPI.isInTransition()) {
            Screen screen = Objects.requireNonNullElse(Flow.screenFadingOut, MinecraftClient.getInstance().currentScreen);
            if(FlowAPI.shouldAvoidCalculation() || screen == null) return;

            AnimationType animationType = AnimationType.getAnimationType(FlowAPI.isClosing());
            OffsetProvider offsetProvider = animationType.calculateOffset(screen.width, screen.height, FlowAPI.getTransitionProgress(), FlowAPI.isClosing());
            offsetProvider.negate(context.getMatrices());
        }
    }
}
