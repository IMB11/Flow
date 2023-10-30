package dev.mineblock11.flow.mixin.compat.emi;

import dev.emi.emi.screen.StackBatcher;
import dev.mineblock11.flow.compat.EmiStackBatcherSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = StackBatcher.class, remap = false)
public class StackBatcherAccessor implements EmiStackBatcherSetter {
    @Shadow private int y;

    @Shadow private int x;

    @Unique
    private int initialX = 0;
    @Unique
    private boolean capturedInitialX = false;

    @Inject(method = "begin", at = @At("HEAD"))
    public void captureInitialX(int x, int y, int z, CallbackInfo ci) {
        if(!capturedInitialX) {
            initialX = x;
            capturedInitialX = true;
        }
    }

    @Override
    public int getInitialX() {
        return initialX;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setX(int x) { this.x = x; }
}
