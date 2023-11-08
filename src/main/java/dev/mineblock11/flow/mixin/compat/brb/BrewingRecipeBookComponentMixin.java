package dev.mineblock11.flow.mixin.compat.brb;

import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.api.animation.AnimationType;
import dev.mineblock11.flow.api.animation.OffsetProvider;
import marsh.town.brb.brewingstand.BrewingRecipeBookComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(BrewingRecipeBookComponent.class)
public class BrewingRecipeBookComponentMixin {
    @Shadow
    MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", shift = At.Shift.AFTER), cancellable = false)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(FlowAPI.isInTransition()) {
            var progress = FlowAPI.getTransitionProgress();

            if(!FlowAPI.shouldCalculate()) return;

            AnimationType animationType = AnimationType.getAnimationType(FlowAPI.isClosing());
            OffsetProvider provider = animationType.calculateOffset(this.client.currentScreen.width, this.client.currentScreen.height, progress, FlowAPI.isClosing());
            provider.apply(context.getMatrices());
        }
    }
}
