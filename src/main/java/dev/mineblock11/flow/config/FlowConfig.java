package dev.mineblock11.flow.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;

public class FlowConfig {
    private static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("flow.config.json");
    public static ConfigClassHandler<FlowConfig> CONFIG_CLASS_HANDLER = ConfigClassHandler
            .createBuilder(FlowConfig.class)
            .id(new Identifier("flow", "config"))
            .serializer(config -> GsonConfigSerializerBuilder
                    .create(config)
                    .setPath(CONFIG_FILE_PATH)
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .build())
            .build();
    @SerialEntry
    public Easings easeInType = Easings.easeInOutExpo;
    @SerialEntry
    public Easings easeOutType = Easings.easeInOutExpo;
    @SerialEntry
    public float easeInDuration = 0.3f;
    @SerialEntry
    public float easeOutDuration = 0.3f;
    @SerialEntry
    public boolean enableEaseIn = true;
    @SerialEntry
    public boolean enableEaseOut = true;
    @SerialEntry
    public Color bgColorTint = Color.BLACK;
    @SerialEntry
    public float bgBlurIntensity = 0.6f;

    public static FlowConfig get() {
        return CONFIG_CLASS_HANDLER.instance();
    }

    public static void load() {
        CONFIG_CLASS_HANDLER.load();
    }

    public static void save() {
        CONFIG_CLASS_HANDLER.save();
    }

    public static YetAnotherConfigLib getInstance() {
        return YetAnotherConfigLib.create(CONFIG_CLASS_HANDLER, (FlowConfig defaults, FlowConfig config, YetAnotherConfigLib.Builder builder) -> {
            var easeInTypeOption = Option.<Easings>createBuilder()
                    .name(getName("easeInType"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("easeInType"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(Easings.class)
                            .formatValue(value -> Text.translatable("flow.config.easingType." + value.name()))
                    )
                    .binding(defaults.easeInType, () -> config.easeInType, (val) -> config.easeInType = val)
                    .build();

            var easeOutTypeOption = Option.<Easings>createBuilder()
                    .name(getName("easeOutType"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("easeOutType"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(Easings.class)
                            .formatValue(value -> Text.translatable("flow.config.easingType." + value.name()))
                    )
                    .binding(defaults.easeOutType, () -> config.easeOutType, (val) -> config.easeOutType = val)
                    .build();

            var easeInDurationOption = Option.<Float>createBuilder()
                    .name(getName("easeInDuration"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("easeInDuration"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 10f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 1000) + "ms"))
                    )
                    .binding(defaults.easeInDuration, () -> config.easeInDuration, (val) -> config.easeInDuration = val)
                    .build();

            var easeOutDurationOption = Option.<Float>createBuilder()
                    .name(getName("easeOutDuration"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("easeOutDuration"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 10f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 1000) + "ms"))
                    )
                    .binding(defaults.easeOutDuration, () -> config.easeOutDuration, (val) -> config.easeOutDuration = val)
                    .build();

            var enableEaseInOption = Option.<Boolean>createBuilder()
                    .name(getName("enableEaseIn"))
                    .listener((opt, val) -> {
                            easeInTypeOption.setAvailable(val);
                            easeInDurationOption.setAvailable(val);
                    })
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("enableEaseIn"))
                            .build())
                    .controller(opt -> BooleanControllerBuilder.create(opt).onOffFormatter())
                    .binding(defaults.enableEaseIn, () -> config.enableEaseIn, (val) -> config.enableEaseIn = val)
                    .build();

            var enableEaseOutOption = Option.<Boolean>createBuilder()
                    .name(getName("enableEaseOut"))
                    .listener((opt, val) -> {
                            easeOutTypeOption.setAvailable(val);
                            easeOutDurationOption.setAvailable(val);
                    })
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("enableEaseOut"))
                            .build())
                    .controller(opt -> BooleanControllerBuilder.create(opt).onOffFormatter())
                    .binding(defaults.enableEaseOut, () -> config.enableEaseOut, (val) -> config.enableEaseOut = val)
                    .build();

            // blurs + tint
            var bgColorTintOption = Option.<Color>createBuilder()
                    .name(getName("bgColorTint"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("bgColorTint"))
                            .build())
                    .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                    .binding(defaults.bgColorTint, () -> config.bgColorTint, (val) -> config.bgColorTint = val)
                    .build();

            var bgBlurIntensityOption = Option.<Float>createBuilder()
                    .name(getName("bgBlurIntensity"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("bgBlurIntensity"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0f, 1f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 100) + "%"))
                    )
                    .binding(defaults.bgBlurIntensity, () -> config.bgBlurIntensity, (val) -> config.bgBlurIntensity = val)
                    .build();

            return builder
                    .title(Text.translatable("flow.config.title"))
                    .category(ConfigCategory.createBuilder()
                            .name(Text.translatable("flow.config.category.general"))
                            .group(OptionGroup.createBuilder()
                                    .name(Text.translatable("flow.config.group.easeIn"))
                                    .collapsed(false)
                                    .description(OptionDescription.createBuilder()
                                            .text(Text.translatable("flow.config.group.easeIn.description"))
                                            .build())
                                    .options(List.of(
                                            enableEaseInOption,
                                            easeInTypeOption,
                                            easeInDurationOption
                                    ))
                                    .build()
                            ).group(OptionGroup.createBuilder()
                                    .name(Text.translatable("flow.config.group.easeOut"))
                                    .collapsed(false)
                                    .description(OptionDescription.createBuilder()
                                            .text(Text.translatable("flow.config.group.easeOut.description"))
                                            .build())
                                    .options(List.of(
                                            enableEaseOutOption,
                                            easeOutTypeOption,
                                            easeOutDurationOption
                                    ))
                                    .build()
                            )
                            .options(List.of(
                                    bgColorTintOption,
                                    bgBlurIntensityOption
                            )).build());
        });
    }

    private static Text getName(String id) {
        return Text.translatable("flow.config." + id);
    }

    private static Text getDesc(String id) {
        return Text.translatable("flow.config." + id + ".desc");
    }
}
