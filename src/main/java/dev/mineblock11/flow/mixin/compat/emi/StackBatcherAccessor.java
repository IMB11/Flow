package dev.mineblock11.flow.mixin.compat.emi;

import dev.emi.emi.screen.StackBatcher;
import dev.mineblock11.flow.compat.EmiStackBatcherSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StackBatcher.class)
public class StackBatcherAccessor implements EmiStackBatcherSetter {
    @Shadow private int y;

    @Override
    public void setY(int y) {
        this.y = y;
    }
}
