package dev.imb11.flow;

import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.rendering.FlowBlurHelper;
import dev.imb11.flow.config.FlowConfig;
import dev.imb11.flow.render.EMIHelper;
import dev.imb11.flow.render.RenderHelper;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Flow implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("flow");
	@Override
	public void onInitializeClient() {
		FlowConfig.load();

		CoreShaderRegistrationCallback.EVENT.register(context -> {
			context.register(Identifier.of("flow", "blur"), VertexFormats.POSITION, shaderProgram -> FlowBlurHelper.INSTANCE.load(shaderProgram));
		});

		HudRenderCallback.EVENT.register((context, tickDeltac) -> {
			/*? if <1.21 {*/
			/*float frameDuration = MinecraftClient.getInstance().getLastFrameDuration() / 25;
			*//*?} else {*/
			float frameDuration = MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration() / 25;
			/*?}*/

			context.getMatrices().push();
			context.getMatrices().translate(0, 0, 5000);

			if(FlowAPI.isInTransition() && FlowAPI.isClosing()) {
				RenderHelper.renderOutput(context, frameDuration);
			}
			context.getMatrices().pop();

//			if(buf != null && MinecraftClient.getInstance().currentScreen == null) {
////				new BuiltBuffer(buf.getAllocated(), new BuiltBuffer.DrawParameters(RenderLayer.getGui().getVertexFormat(), ))
////				context.getVertexConsumers().draw(RenderLayer.getGui(), buf);
////				buf.draw();
//			}
		});
	}
}