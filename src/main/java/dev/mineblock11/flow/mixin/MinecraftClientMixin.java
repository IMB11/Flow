package dev.mineblock11.flow.mixin;

import dev.mineblock11.flow.api.events.WindowResizeEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract Window getWindow();

    @Inject(method = "onResolutionChanged", at = @At("TAIL"), cancellable = false)
    private void $window_resize_invoker(CallbackInfo ci) {
        WindowResizeEvent.EVENT.invoker().invoke(this.getWindow().getFramebufferWidth(), this.getWindow().getFramebufferHeight());
    }
}
