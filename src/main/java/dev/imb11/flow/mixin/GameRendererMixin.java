//package dev.imb11.flow.mixin;
//
//import dev.imb11.flow.Flow;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.render.*;
//import net.minecraft.client.util.BufferAllocator;
//import net.minecraft.client.util.Window;
//import org.joml.Matrix4f;
//import org.joml.Matrix4fStack;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//
//@Mixin(GameRenderer.class)
//public class GameRendererMixin {
//    @Shadow @Final private MinecraftClient client;
//
//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER), cancellable = false, locals = LocalCapture.CAPTURE_FAILSOFT)
//    public void $test_capture_framebuffer(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext drawContext) {
////        BufferAllocator allocator = new BufferAllocator(0);
////        DrawContext customContext = new DrawContext(client, VertexConsumerProvider.immediate(allocator));
////        this.client.currentScreen.render(customContext,  i, j, tickCounter.getLastDuration());
////        Flow.buf = customContext;
//    }
//}
