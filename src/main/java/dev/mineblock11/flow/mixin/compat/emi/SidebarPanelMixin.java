package dev.mineblock11.flow.mixin.compat.emi;

import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.config.FlowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = EmiScreenManager.SidebarPanel.class, remap = false)
public class SidebarPanelMixin {
    @Inject(method = "drawBackground", at = @At("TAIL"), cancellable = false)
    public void $reset_matrices(EmiDrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Reset matrices - make sure transformations only apply to the non-affected background texture.
        context.matrices().pop();
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = false)
    public void $offset_theme_background(EmiDrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Offset the background texture to account for the transition animation - for some reason the background
        // texture isn't affected by the matrix transformations in the ScreenSpaceMixin, so we have to do it here as well.
        context.matrices().push();
        if(FlowAPI.isInTransition()) {
            var progress = FlowAPI.getTransitionProgress();
            int height = MinecraftClient.getInstance().getWindow().getFramebufferHeight();
            if(FlowAPI.isClosing()) {
                if(FlowConfig.get().enableEaseOut) {
                    float offset = MathHelper.lerp(FlowConfig.get().easeOutType.eval(progress), height, 0);
                    context.matrices().translate(0, offset, 0);
                }
            } else {
                if(FlowConfig.get().enableEaseIn) {
                    float offset = MathHelper.lerp(FlowConfig.get().easeInType.eval(progress), -height, 0);
                    context.matrices().translate(0, -offset, 0);
                }
            }
        }
    }
}
