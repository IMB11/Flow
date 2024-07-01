package dev.imb11.flow.mixin.fixes.vanilla;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneScreen.class)
public class GrindstoneFixMixin extends Screen {
    protected GrindstoneFixMixin(Text title) {
        super(title);
    }

    /*? if <1.20.2 {*/
    /*@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GrindstoneScreen;drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V"))
    public void $cancel_draw_background(GrindstoneScreen instance, DrawContext context, float delta, int mouseX, int mouseY) {}
    *//*?}*/
}
