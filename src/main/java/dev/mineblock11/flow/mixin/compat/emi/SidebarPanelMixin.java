package dev.mineblock11.flow.mixin.compat.emi;

import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.api.animation.AnimationType;
import dev.mineblock11.flow.config.FlowConfig;
import dev.mineblock11.flow.api.animation.OffsetProvider;
import net.minecraft.client.MinecraftClient;
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
            if(FlowAPI.isClosing() && FlowConfig.get().disableEaseOut) {
                return;
            } else if(FlowConfig.get().disableEaseIn) {
                return;
            }

            OffsetProvider provider = AnimationType.expandTopRight.calculateOffset(MinecraftClient.getInstance().currentScreen.width, MinecraftClient.getInstance().currentScreen.height, progress, FlowAPI.isClosing());
            provider.apply(context.matrices());
        }
    }
}
