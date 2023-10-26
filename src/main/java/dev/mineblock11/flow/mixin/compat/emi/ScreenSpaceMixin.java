package dev.mineblock11.flow.mixin.compat.emi;

import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.StackBatcher;
import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.config.FlowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = EmiScreenManager.ScreenSpace.class, remap = false)
public abstract class ScreenSpaceMixin {
    @Shadow @Final public StackBatcher batcher;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/runtime/EmiDrawContext;push()V", shift = At.Shift.AFTER), cancellable = false, remap = false)
    public void $disable_baking_during_transition(EmiDrawContext context, int mouseX, int mouseY, float delta, int startIndex, CallbackInfo ci) {
        // The item renderer bakes its results, so we need to disable baking during the transition animation.
        // And because of a goofy renderer, the y values are inverted, so we need to invert them back twice
        // to ensure the items are rendered in the correct position.
        if(FlowAPI.isInTransition()) {
            var progress = FlowAPI.getTransitionProgress();
            int height = MinecraftClient.getInstance().getWindow().getFramebufferHeight();
            if(FlowAPI.isClosing()) {
                if(FlowConfig.get().enableEaseOut) {
                    float offset = MathHelper.lerp(FlowConfig.get().easeOutType.eval(progress), height, 0) * 2;
                    context.matrices().translate(0, -offset, 0);
                }
            } else {
                if(FlowConfig.get().enableEaseIn) {
                    float offset = MathHelper.lerp(FlowConfig.get().easeInType.eval(progress), -height, 0) * 2;
                    context.matrices().translate(0, offset, 0);
                }
            }
            this.batcher.repopulate();
        }
    }
}
