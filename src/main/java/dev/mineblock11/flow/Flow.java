package dev.mineblock11.flow;

import dev.mineblock11.flow.config.FlowConfig;
import dev.mineblock11.flow.render.BlurHelper;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Flow implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("flow");
	@Override
	public void onInitializeClient() {
		FlowConfig.load();

		CoreShaderRegistrationCallback.EVENT.register(context -> {
			context.register(new Identifier("flow", "blur"), VertexFormats.POSITION, shaderProgram -> {
				BlurHelper.INSTANCE.load(shaderProgram);
			});
		});
	}
}