package dev.mineblock11.flow.mixin.compat.emi;

import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.StackBatcher;
import dev.mineblock11.flow.api.FlowAPI;
import dev.mineblock11.flow.compat.EmiStackBatcherSetter;
import dev.mineblock11.flow.config.FlowConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Pseudo
@Mixin(value = EmiScreenManager.ScreenSpace.class, remap = false)
public abstract class ScreenSpaceMixin {
    @Shadow @Final public StackBatcher batcher;

//    @Shadow public abstract int getY(int x, int y);

//    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/EmiScreenManager$ScreenSpace;getY(II)I"), ordinal = 1, argsOnly = true)
//    public int modifyArgsRender(int yo) {
//        // args[4] = y value.
//        // Offset args[4] to account for the transition animation.
//
////        System.out.println(args.get(4).toString());
//
//
//        return yo;
//    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/runtime/EmiDrawContext;push()V", shift = At.Shift.AFTER), cancellable = false, remap = false)
    public void $disable_baking_during_transition(EmiDrawContext context, int mouseX, int mouseY, float delta, int startIndex, CallbackInfo ci) {
            if(FlowAPI.isInTransition()) {
                this.batcher.repopulate();

                if(FlowAPI.isInTransition()) {
                    var progress = FlowAPI.getTransitionProgress();
                    int height = MinecraftClient.getInstance().getWindow().getFramebufferHeight();
                    if(FlowAPI.isClosing()) {
                        if(FlowConfig.get().enableEaseOut) {
                            float offset = MathHelper.lerp(FlowConfig.get().easeOutType.eval(progress), height, 0);
                            ((EmiStackBatcherSetter) this.batcher).setY((int) offset);
                        }
                    } else {
                        if(FlowConfig.get().enableEaseIn) {
                            float offset = MathHelper.lerp(FlowConfig.get().easeInType.eval(progress), height, 0);
                            ((EmiStackBatcherSetter) this.batcher).setY((int) offset);
                        }
                    }
                }
            }
    }
}
