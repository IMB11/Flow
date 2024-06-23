package dev.imb11.flow.mixin.fixes.vanilla;

import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.OffsetProvider;
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

    /*? if <1.20.2 {*//*
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    public void $apply_transition(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(FlowAPI.isInTransition()) {
            var progress = FlowAPI.getTransitionProgress();

            if(FlowAPI.shouldAvoidCalculation()) return;

            AnimationType animationType = AnimationType.getAnimationType(FlowAPI.isClosing());
            OffsetProvider provider = animationType.calculateOffset(this.client.currentScreen.width, this.client.currentScreen.height, progress, FlowAPI.isClosing());
            provider.apply(context.getMatrices());
        }
    }
    *//*?}*/
}
