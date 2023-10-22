package dev.mineblock11.flow.mixin.fixes.vanilla;

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

//    @Inject(method = "render", at = @At("HEAD"))
//    public void $invoke_render_background(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        this.renderBackground(context);
//    }
//
//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GrindstoneScreen;renderBackground(Lnet/minecraft/client/gui/DrawContext;)V"))
//    public void $cancel_render_background(GrindstoneScreen instance, DrawContext context) {}

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GrindstoneScreen;drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V"))
    public void $cancel_draw_background(GrindstoneScreen instance, DrawContext context, float delta, int mouseX, int mouseY) {}
}
