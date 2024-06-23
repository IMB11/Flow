package dev.imb11.flow;

import dev.imb11.flow.api.rendering.FlowBlurHelper;
import dev.imb11.flow.config.FlowConfig;
import dev.imb11.flow.render.EMIHelper;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Flow implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("flow");
	public static Screen screenFadingOut = null;
	@Override
	public void onInitializeClient() {
		FlowConfig.load();

		CoreShaderRegistrationCallback.EVENT.register(context -> {
			context.register(Identifier.of("flow", "blur"), VertexFormats.POSITION, shaderProgram -> FlowBlurHelper.INSTANCE.load(shaderProgram));
		});

		HudRenderCallback.EVENT.register((context, tickDeltac) -> {
			if(screenFadingOut != null) {
				context.getMatrices().push();
				context.getMatrices().translate(0f, 0f, 1000f);
				int mouseX = (int) MinecraftClient.getInstance().mouse.x;
				int mouseY = (int) MinecraftClient.getInstance().mouse.y;

				/*? if <1.21 {*//*
				float tickDelta = tickDeltac;
				screenFadingOut.render(context, mouseX, mouseY, tickDelta);
				*//*?} else {*/
				float tickDelta = tickDeltac.getTickDelta(true);
				screenFadingOut.render(context, mouseX, mouseY, tickDelta);
				/*?}*/

				if(FabricLoader.getInstance().isModLoaded("emi")) {
					EMIHelper.renderEMI(screenFadingOut, context, mouseX, mouseY, tickDelta);
				}

				context.getMatrices().pop();
			}
		});
	}
}