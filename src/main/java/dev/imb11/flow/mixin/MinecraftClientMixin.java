package dev.imb11.flow.mixin;

import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.events.WindowResizeEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract Window getWindow();

    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "onResolutionChanged", at = @At("TAIL"), cancellable = false)
    private void $window_resize_invoker(CallbackInfo ci) {
        WindowResizeEvent.EVENT.invoker().invoke(this.getWindow().getFramebufferWidth(), this.getWindow().getFramebufferHeight());
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = false)
    private void $setScreen(@Nullable Screen screen, CallbackInfo ci) {
        FlowAPI.setPreviousScreen(this.currentScreen);
        FlowAPI.setNextScreen(screen);
    }
}
