package dev.mineblock11.flow.config;

import com.mineblock11.mru.config.YACLHelper;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlowConfig {
    private static final YACLHelper.NamespacedHelper HELPER = new YACLHelper.NamespacedHelper("flow");
    public static ConfigClassHandler<FlowConfig> CONFIG_CLASS_HANDLER = HELPER.createHandler(FlowConfig.class);
    @SerialEntry
    public Easings easeInType = Easings.easeInOutExpo;
    @SerialEntry
    public Easings easeOutType = Easings.easeInOutExpo;
    @SerialEntry
    public float easeInDuration = 0.3f;
    @SerialEntry
    public float easeOutDuration = 0.3f;
    @SerialEntry
    public boolean disableEaseIn = false;
    @SerialEntry
    public boolean disableEaseOut = false;
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
                    .name(HELPER.getName("easeInType"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("easeInType"))
                            .webpImage(HELPER.getImg("easeIn"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(Easings.class)
                            .formatValue(value -> Text.translatable("flow.config.easingType." + value.name()))
                    )
                    .binding(defaults.easeInType, () -> config.easeInType, (val) -> config.easeInType = val)
                    .build();

            var easeOutTypeOption = Option.<Easings>createBuilder()
                    .name(HELPER.getName("easeOutType"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("easeOutType"))
                            .webpImage(HELPER.getImg("easeOut"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(Easings.class)
                            .formatValue(value -> Text.translatable("flow.config.easingType." + value.name()))
                    )
                    .binding(defaults.easeOutType, () -> config.easeOutType, (val) -> config.easeOutType = val)
                    .build();

            var easeInDurationOption = Option.<Float>createBuilder()
                    .name(HELPER.getName("easeInDuration"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("easeInDuration"))
                            .webpImage(HELPER.getImg("easeIn"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 10f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 1000) + "ms"))
                    )
                    .binding(defaults.easeInDuration, () -> config.easeInDuration, (val) -> config.easeInDuration = val)
                    .build();

            var easeOutDurationOption = Option.<Float>createBuilder()
                    .name(HELPER.getName("easeOutDuration"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("easeOutDuration"))
                            .webpImage(HELPER.getImg("easeOut"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 10f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 1000) + "ms"))
                    )
                    .binding(defaults.easeOutDuration, () -> config.easeOutDuration, (val) -> config.easeOutDuration = val)
                    .build();

            var easeInOptionGroup = HELPER.createToggleableGroup("easeIn", true, new ArrayList<>(List.of(
                    easeInTypeOption,
                    easeInDurationOption
            )), defaults.disableEaseIn, () -> config.disableEaseIn, (val) -> config.disableEaseIn = val);

            var easeOutOptionGroup = HELPER.createToggleableGroup("easeOut", true, new ArrayList<>(List.of(
                    easeOutTypeOption,
                    easeOutDurationOption
            )), defaults.disableEaseOut, () -> config.disableEaseOut, (val) -> config.disableEaseOut = val);

            // blurs + tint
            var bgColorTintOption = Option.<Color>createBuilder()
                    .name(HELPER.getName("bgColorTint"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("bgColorTint"))
                            .webpImage(HELPER.getImg("bgColorTint"))
                            .build())
                    .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                    .binding(defaults.bgColorTint, () -> config.bgColorTint, (val) -> config.bgColorTint = val)
                    .build();

            var bgBlurIntensityOption = Option.<Float>createBuilder()
                    .name(HELPER.getName("bgBlurIntensity"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("bgBlurIntensity"))
                            .webpImage(HELPER.getImg("bgBlurIntensity"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 1f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 100) + "%"))
                    )
                    .binding(defaults.bgBlurIntensity, () -> config.bgBlurIntensity, (val) -> config.bgBlurIntensity = val)
                    .build();

            var disableBgBlurOption = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("disableBgBlur"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("disableBgBlur"))
                            .webpImage(HELPER.getImg("disableBgBlur"))
                            .build())
                    .listener((opt, val) -> {
                        bgBlurIntensityOption.setAvailable(!val);
                    })
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                    .binding(defaults.disableBgBlur, () -> config.disableBgBlur, (val) -> config.disableBgBlur = val)
                    .build();

            var disableBgTintOption = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("disableBgTint"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("disableBgTint"))
                            .webpImage(HELPER.getImg("disableBgTint"))
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
                            .group(easeInOptionGroup)
                            .group(easeOutOptionGroup)
                            .options(List.of(
                                    disableBgTintOption,
                                    bgColorTintOption,
                                    disableBgBlurOption,
                                    bgBlurIntensityOption
                            )).build());
        });
    }
}
