package dev.mineblock11.flow.mixin;

import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.api.animation.AnimationType;
import dev.mineblock11.flow.api.animation.OffsetProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.toast.ToastManager$Entry")
public class ToastManagerEntryMixin {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"), cancellable = false)
    public void $negate_transition(int x, DrawContext context, CallbackInfoReturnable<Boolean> cir) {
        if(FlowAPI.isInTransition()) {
            if(!FlowAPI.shouldCalculate()) return;

            AnimationType animationType = AnimationType.getAnimationType(FlowAPI.isClosing());
            OffsetProvider offsetProvider = animationType.calculateOffset(MinecraftClient.getInstance().currentScreen.width, MinecraftClient.getInstance().currentScreen.height, FlowAPI.getTransitionProgress(), FlowAPI.isClosing());
            offsetProvider.negate(context.getMatrices());
        }
    }
}
