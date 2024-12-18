package dev.imb11.flow.mixin;

import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "drawTooltip*", at = @At("HEAD"), cancellable = true)
    public void cancelTooltips(CallbackInfo ci) {
        if(FlowAPI.isInTransition() || RenderHelper.isRendering) {
            ci.cancel();
        }
    }
}
