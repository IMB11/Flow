package dev.mineblock11.flow.mixin.fixes.vanilla;

import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.config.EntryType;
import dev.mineblock11.flow.config.FlowConfig;
import dev.mineblock11.flow.config.OffsetProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookWidget.class)
public class RecipeBookFixMixin {
    @Shadow protected MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    public void $apply_transition(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(FlowAPI.isInTransition()) {
            var progress = FlowAPI.getTransitionProgress();
            if(FlowAPI.isClosing() && FlowConfig.get().disableEaseOut) {
                return;
            } else if(FlowConfig.get().disableEaseIn) {
                return;
            }

            OffsetProvider provider = EntryType.LEFT.calculateOffset(this.client.currentScreen.width, this.client.currentScreen.height, progress, FlowAPI.isClosing() ? FlowConfig.get().easeOutType : FlowConfig.get().easeInType);
            provider.apply(context.getMatrices());
        }
    }
}
