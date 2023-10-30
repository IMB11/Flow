package dev.mineblock11.flow.mixin.fixes.vanilla;

import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.config.FlowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.util.math.MathHelper;
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
            if(FlowAPI.isClosing()) {
                if(!FlowConfig.get().disableEaseOut) {
                    float offset = MathHelper.lerp(FlowConfig.get().easeOutType.eval(progress), this.client.getWindow().getHeight(), 0);
                    context.getMatrices().translate(0, offset, 0);
                }
            } else {
                if(!FlowConfig.get().disableEaseIn) {
                    float offset = MathHelper.lerp(FlowConfig.get().easeInType.eval(progress), -this.client.getWindow().getHeight(), 0);
                    context.getMatrices().translate(0, -offset, 0);
                }
            }
        }
    }
}
