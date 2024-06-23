package dev.imb11.flow.config;

import com.mineblock11.mru.config.YACLHelper;
import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.Easings;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
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
    public AnimationType easeInAnimationType = AnimationType.slideUp;
    @SerialEntry
    public Easings easeOutType = Easings.easeInOutExpo;
    @SerialEntry
    public AnimationType easeOutAnimationType = AnimationType.slideUp;

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

    @SerialEntry
    public List<String> disabledScreens = List.of("top.theillusivec4.curios.client.gui.CuriosScreen");

    @SerialEntry
    public boolean disableCrossInventoryAnimations = true;

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

            var easeInAnimationTypeOption = Option.<AnimationType>createBuilder()
                    .name(HELPER.getName("easeInAnimationType"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("easeInAnimationType"))
                            .webpImage(HELPER.getImg("easeIn"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(AnimationType.class)
                            .formatValue(value -> Text.translatable("flow.config.animationType." + value.name()))
                    )
                    .binding(defaults.easeInAnimationType, () -> config.easeInAnimationType, (val) -> config.easeInAnimationType = val)
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

            var easeOutAnimationTypeOption = Option.<AnimationType>createBuilder()
                    .name(HELPER.getName("easeOutAnimationType"))
                    .description(OptionDescription.createBuilder()
                            .text(HELPER.getDesc("easeOutAnimationType"))
                            .webpImage(HELPER.getImg("easeOut"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(AnimationType.class)
                            .formatValue(value -> Text.translatable("flow.config.animationType." + value.name()))
                    )
                    .binding(defaults.easeOutAnimationType, () -> config.easeOutAnimationType, (val) -> config.easeOutAnimationType = val)
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
                    easeInAnimationTypeOption,
                    easeInDurationOption
            )), defaults.disableEaseIn, () -> config.disableEaseIn, (val) -> config.disableEaseIn = val);

            var easeOutOptionGroup = HELPER.createToggleableGroup("easeOut", true, new ArrayList<>(List.of(
                    easeOutTypeOption,
                    easeOutAnimationTypeOption,
                    easeOutDurationOption
            )), defaults.disableEaseOut, () -> config.disableEaseOut, (val) -> config.disableEaseOut = val);

            // blurs + tint
            var bgColorTintOption = Option.<Color>createBuilder()
                    .name(HELPER.getName("bgColorTint"))
                    .description(HELPER.description("bgColorTint"))
                    .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                    .binding(defaults.bgColorTint, () -> config.bgColorTint, (val) -> config.bgColorTint = val)
                    .build();

            var bgBlurIntensityOption = Option.<Float>createBuilder()
                    .name(HELPER.getName("bgBlurIntensity"))
                    .description(HELPER.description("bgBlurIntensity", true))
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 1f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 100) + "%"))
                    )
                    .binding(defaults.bgBlurIntensity, () -> config.bgBlurIntensity, (val) -> config.bgBlurIntensity = val)
                    .build();

            var disableBgBlurOption = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("disableBgBlur"))
                    .description(HELPER.description("disableBgBlur", true))
                    .listener((opt, val) -> {
                        bgBlurIntensityOption.setAvailable(!val);
                    })
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                    .binding(defaults.disableBgBlur, () -> config.disableBgBlur, (val) -> config.disableBgBlur = val)
                    .build();

            var disableBgTintOption = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("disableBgTint"))
                    .description(HELPER.description("disableBgTint", true))
                    .listener((opt, val) -> {
                        bgColorTintOption.setAvailable(!val);
                    })
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                    .binding(defaults.disableBgTint, () -> config.disableBgTint, (val) -> config.disableBgTint = val)
                    .build();

            var disabledScreens = ListOption.<String>createBuilder()
                    .name(HELPER.getName("disabledScreens"))
                    .description(HELPER.description("disabledScreens"))
                    .collapsed(false)
                    .binding(defaults.disabledScreens, () -> config.disabledScreens, (val) -> config.disabledScreens = val)
                    .controller(StringControllerBuilder::create)
                    .initial("ScreenClassName")
                    .build();

            var crossInventoryAnimations = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("disableCrossInventoryAnimations"))
                    .description(HELPER.description("disableCrossInventoryAnimations"))
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                    .binding(defaults.disableCrossInventoryAnimations, () -> config.disableCrossInventoryAnimations, (val) -> config.disableCrossInventoryAnimations = val)
                    .build();

            return builder
                    .title(Text.translatable("flow.config.title"))
                    .save(FlowAPI::handleConfigSaving)
                    .category(ConfigCategory.createBuilder()
                            .name(Text.translatable("flow.config.category.easings"))
                            .group(easeInOptionGroup)
                            .group(easeOutOptionGroup)
                            .build())
                    .category(ConfigCategory.createBuilder()
                            .name(Text.translatable("flow.config.category.background"))
                            .options(List.of(
                                    disableBgTintOption,
                                    bgColorTintOption,
                                    disableBgBlurOption,
                                    bgBlurIntensityOption
                            )).build())
                    .category(ConfigCategory.createBuilder()
                            .name(Text.translatable("flow.config.category.compat"))
                            .option(disabledScreens)
                            .option(crossInventoryAnimations)
                            .build());
        });
    }
}
