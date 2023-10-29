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
    @SerialEntry
    public boolean disableBgBlur = false;
    @SerialEntry
    public boolean disableBgTint = false;

    public static FlowConfig get() {
        return CONFIG_CLASS_HANDLER.instance();
    }

    public static void load() {
        CONFIG_CLASS_HANDLER.load();
    }

    public static YetAnotherConfigLib getInstance() {
        return YetAnotherConfigLib.create(CONFIG_CLASS_HANDLER, (FlowConfig defaults, FlowConfig config, YetAnotherConfigLib.Builder builder) -> {
            var easeInTypeOption = Option.<Easings>createBuilder()
                    .name(getName("easeInType"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("easeInType"))
                            .webpImage(getImg("easeIn"))
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
                            .webpImage(getImg("easeOut"))
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
                            .webpImage(getImg("easeIn"))
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
                            .webpImage(getImg("easeOut"))
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
                            .webpImage(getImg("easeIn"))
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
                            .webpImage(getImg("easeOut"))
                            .build())
                    .controller(opt -> BooleanControllerBuilder.create(opt).onOffFormatter())
                    .binding(defaults.enableEaseOut, () -> config.enableEaseOut, (val) -> config.enableEaseOut = val)
                    .build();

            // blurs + tint
            var bgColorTintOption = Option.<Color>createBuilder()
                    .name(getName("bgColorTint"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("bgColorTint"))
                            .webpImage(getImg("bgColorTint"))
                            .build())
                    .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                    .binding(defaults.bgColorTint, () -> config.bgColorTint, (val) -> config.bgColorTint = val)
                    .build();

            var bgBlurIntensityOption = Option.<Float>createBuilder()
                    .name(getName("bgBlurIntensity"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("bgBlurIntensity"))
                            .webpImage(getImg("bgBlurIntensity"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 1f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 100) + "%"))
                    )
                    .binding(defaults.bgBlurIntensity, () -> config.bgBlurIntensity, (val) -> config.bgBlurIntensity = val)
                    .build();

            var disableBgBlurOption = Option.<Boolean>createBuilder()
                    .name(getName("disableBgBlur"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("disableBgBlur"))
                            .webpImage(getImg("disableBgBlur"))
                            .build())
                    .listener((opt, val) -> {
                        bgBlurIntensityOption.setAvailable(!val);
                    })
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                    .binding(defaults.disableBgBlur, () -> config.disableBgBlur, (val) -> config.disableBgBlur = val)
                    .build();

            var disableBgTintOption = Option.<Boolean>createBuilder()
                    .name(getName("disableBgTint"))
                    .description(OptionDescription.createBuilder()
                            .text(getDesc("disableBgTint"))
                            .webpImage(getImg("disableBgTint"))
                            .build())
                    .listener((opt, val) -> {
                        bgColorTintOption.setAvailable(!val);
                    })
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                    .binding(defaults.disableBgTint, () -> config.disableBgTint, (val) -> config.disableBgTint = val)
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
                                            .webpImage(getImg("easeIn"))
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
                                            .webpImage(getImg("easeOut"))
                                            .build())
                                    .options(List.of(
                                            enableEaseOutOption,
                                            easeOutTypeOption,
                                            easeOutDurationOption
                                    ))
                                    .build()
                            )
                            .options(List.of(
                                    disableBgTintOption,
                                    bgColorTintOption,
                                    disableBgBlurOption,
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

    private static Identifier getImg(String id) {
        return new Identifier("flow", "textures/config/" + id.toLowerCase() + ".webp");
    }
}
