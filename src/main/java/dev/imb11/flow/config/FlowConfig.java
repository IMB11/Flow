package dev.imb11.flow.config;

import com.google.gson.GsonBuilder;
import dev.imb11.flow.Flow;
import dev.imb11.flow.api.FlowAPI;
import dev.imb11.flow.api.animation.AnimationType;
import dev.imb11.flow.api.animation.Easings;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlowConfig {
    public static ConfigClassHandler<FlowConfig> CONFIG_CLASS_HANDLER = ConfigClassHandler
            .createBuilder(FlowConfig.class)
            .id(Identifier.of("flow", "config"))
            .serializer(config -> GsonConfigSerializerBuilder
                    .create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("flow.config.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting).build())
            .build();

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
    public boolean disableAllBackgroundModifications = false;
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
                    .name(Text.translatable("flow" + ".config." + "easeInType"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "easeInType" + ".desc"))
                            .webpImage(Identifier.of("flow", "textures/config/" + "easeIn".toLowerCase() + ".webp"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(Easings.class)
                            .formatValue(value -> Text.translatable("flow.config.easingType." + value.name()))
                    )
                    .binding(defaults.easeInType, () -> config.easeInType, (val) -> config.easeInType = val)
                    .build();

            var easeInAnimationTypeOption = Option.<AnimationType>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "easeInAnimationType"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "easeInAnimationType" + ".desc"))
                            .webpImage(Identifier.of("flow", "textures/config/" + "easeIn".toLowerCase() + ".webp"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(AnimationType.class)
                            .formatValue(value -> Text.translatable("flow.config.animationType." + value.name()))
                    )
                    .binding(defaults.easeInAnimationType, () -> config.easeInAnimationType, (val) -> config.easeInAnimationType = val)
                    .build();

            var easeOutTypeOption = Option.<Easings>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "easeOutType"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "easeOutType" + ".desc"))
                            .webpImage(Identifier.of("flow", "textures/config/" + "easeOut".toLowerCase() + ".webp"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(Easings.class)
                            .formatValue(value -> Text.translatable("flow.config.easingType." + value.name()))
                    )
                    .binding(defaults.easeOutType, () -> config.easeOutType, (val) -> config.easeOutType = val)
                    .build();

            var easeOutAnimationTypeOption = Option.<AnimationType>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "easeOutAnimationType"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "easeOutAnimationType" + ".desc"))
                            .webpImage(Identifier.of("flow", "textures/config/" + "easeOut".toLowerCase() + ".webp"))
                            .build())
                    .controller(opt -> EnumControllerBuilder.create(opt)
                            .enumClass(AnimationType.class)
                            .formatValue(value -> Text.translatable("flow.config.animationType." + value.name()))
                    )
                    .binding(defaults.easeOutAnimationType, () -> config.easeOutAnimationType, (val) -> config.easeOutAnimationType = val)
                    .build();

            var easeInDurationOption = Option.<Float>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "easeInDuration"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "easeInDuration" + ".desc"))
                            .webpImage(Identifier.of("flow", "textures/config/" + "easeIn".toLowerCase() + ".webp"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 10f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 1000) + "ms"))
                    )
                    .binding(defaults.easeInDuration, () -> config.easeInDuration, (val) -> config.easeInDuration = val)
                    .build();

            var easeOutDurationOption = Option.<Float>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "easeOutDuration"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "easeOutDuration" + ".desc"))
                            .webpImage(Identifier.of("flow", "textures/config/" + "easeOut".toLowerCase() + ".webp"))
                            .build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 10f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 1000) + "ms"))
                    )
                    .binding(defaults.easeOutDuration, () -> config.easeOutDuration, (val) -> config.easeOutDuration = val)
                    .build();

            ArrayList<Option<?>> optionsToToggle1 = new ArrayList<>(List.of(
                    easeInTypeOption,
                    easeInAnimationTypeOption,
                    easeInDurationOption
            ));
            String id1 = "disable" + WordUtils.capitalize("easeIn");
            var descriptionBuilder1 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + id1 + ".desc"));

            descriptionBuilder1.webpImage(Identifier.of("flow", "textures/config/" + "easeIn".toLowerCase() + ".webp"));

            String id3 = "disable" + WordUtils.capitalize("easeIn");
            var builder5 = Option.<Boolean>createBuilder()
                    .name(Text.translatable("flow" + ".config." + id3))
                    .description(descriptionBuilder1.build())
                    .controller(opt3 -> BooleanControllerBuilder.create(opt3).yesNoFormatter())
                    .binding(defaults.disableEaseIn, () -> config.disableEaseIn, (val4) -> config.disableEaseIn = val4);

            var toggleOption1 = builder5
                    .listener((opt2, val3) -> {
                        optionsToToggle1.forEach(option1 -> option1.setAvailable(!val3));
                    }).build();

            var builder10 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + "group." + "easeIn" + ".desc"));

            builder10.webpImage(Identifier.of("flow", "textures/config/" + ("group." + "easeIn").replace("group.", "").toLowerCase() + ".webp"));

            var easeInOptionGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("flow" + ".config." + "group." + "easeIn"))
                    .collapsed(false)
                    .description(builder10.build())
                    .option(toggleOption1)
                    .options(optionsToToggle1)
                    .build();

            ArrayList<Option<?>> optionsToToggle = new ArrayList<Option<?>>(List.of(
                    easeOutTypeOption,
                    easeOutAnimationTypeOption,
                    easeOutDurationOption
            ));
            String id = "disable" + WordUtils.capitalize("easeOut");
            var descriptionBuilder = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + id + ".desc"));

            descriptionBuilder.webpImage(Identifier.of("flow", "textures/config/" + "easeOut".toLowerCase() + ".webp"));

            String id2 = "disable" + WordUtils.capitalize("easeOut");
            var builder4 = Option.<Boolean>createBuilder()
                    .name(Text.translatable("flow" + ".config." + id2))
                    .description(descriptionBuilder.build())
                    .controller(opt2 -> BooleanControllerBuilder.create(opt2).yesNoFormatter())
                    .binding(defaults.disableEaseOut, () -> config.disableEaseOut, (val1) -> config.disableEaseOut = val1);

            var toggleOption = builder4
                    .listener((opt1, val2) -> {
                        optionsToToggle.forEach(option -> option.setAvailable(!val2));
                    }).build();

            var builder9 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + "group." + "easeOut" + ".desc"));

            builder9.webpImage(Identifier.of("flow", "textures/config/" + ("group." + "easeOut").replace("group.", "").toLowerCase() + ".webp"));

            var easeOutOptionGroup = OptionGroup.createBuilder()
                    .name(Text.translatable("flow" + ".config." + "group." + "easeOut"))
                    .collapsed(false)
                    .description(builder9.build())
                    .option(toggleOption)
                    .options(optionsToToggle)
                    .build();

            // blurs + tint
            var builder8 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow"+ ".config." + "bgColorTint" + ".desc"));

            var bgColorTintOption = Option.<Color>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "bgColorTint"))
                    .description(builder8.build())
                    .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                    .available(!Flow.areBackgroundModsPresent())
                    .binding(defaults.bgColorTint, () -> config.bgColorTint, (val) -> config.bgColorTint = val)
                    .build();

            var builder3 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + "bgBlurIntensity" + ".desc"));

            builder3.webpImage(Identifier.of("flow", "textures/config/" + "bgBlurIntensity".replace("group.", "").toLowerCase() + ".webp"));

            var bgBlurIntensityOption = Option.<Float>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "bgBlurIntensity"))
                    .description(builder3.build())
                    .controller(opt -> FloatSliderControllerBuilder.create(opt)
                            .range(0.1f, 1f)
                            .step(0.1f)
                            .formatValue(value -> Text.of(Math.round(value * 100) + "%"))
                    )
                    .available(!Flow.areBackgroundModsPresent())
                    .binding(defaults.bgBlurIntensity, () -> config.bgBlurIntensity, (val) -> config.bgBlurIntensity = val)
                    .build();

            var builder2 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + "disableBgBlur" + ".desc"));

            builder2.webpImage(Identifier.of("flow", "textures/config/" + "disableBgBlur".toLowerCase() + ".webp"));

            var disableBgBlurOption = Option.<Boolean>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "disableBgBlur"))
                    .description(builder2.build())
                    .listener((opt, val) -> {
                        bgBlurIntensityOption.setAvailable(!val);
                    })
                    .available(!Flow.areBackgroundModsPresent())
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .binding(defaults.disableBgBlur, () -> config.disableBgBlur, (val) -> config.disableBgBlur = val)
                    .build();

            var builder1 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow" + ".config." + "disableBgTint" + ".desc"));

            builder1.webpImage(Identifier.of("flow", "textures/config/" + "disableBgTint".toLowerCase() + ".webp"));

            var disableBgTintOption = Option.<Boolean>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "disableBgTint"))
                    .description(builder1.build())
                    .listener((opt, val) -> {
                        bgColorTintOption.setAvailable(!val);
                    })
                    .available(!Flow.areBackgroundModsPresent())
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .binding(defaults.disableBgTint, () -> config.disableBgTint, (val) -> config.disableBgTint = val)
                    .build();

            var disableAllBackgroundModificationsOption = Option.<Boolean>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "disableAllBackgroundModifications"))
                    .description(OptionDescription.createBuilder()
                            .text(Text.translatable("flow" + ".config." + "disableAllBackgroundModifications" + ".desc"))
                            .build())
                    .listener((opt, val) -> {
                        bgColorTintOption.setAvailable(!val);
                        bgBlurIntensityOption.setAvailable(!val);
                        disableBgBlurOption.setAvailable(!val);
                        disableBgTintOption.setAvailable(!val);
                    })
                    .available(!Flow.areBackgroundModsPresent())
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .binding(defaults.disableAllBackgroundModifications, () -> config.disableAllBackgroundModifications, (val) -> config.disableAllBackgroundModifications = val)
                    .build();

            var builder7 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow"+ ".config." + "disabledScreens" + ".desc"));

            var disabledScreens = ListOption.<String>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "disabledScreens"))
                    .description(builder7.build())
                    .collapsed(false)
                    .binding(defaults.disabledScreens, () -> config.disabledScreens, (val) -> config.disabledScreens = val)
                    .controller(StringControllerBuilder::create)
                    .initial("ScreenClassName")
                    .build();

            var builder6 = OptionDescription.createBuilder()
                    .text(Text.translatable("flow"+ ".config." + "disableCrossInventoryAnimations" + ".desc"));

            var crossInventoryAnimations = Option.<Boolean>createBuilder()
                    .name(Text.translatable("flow" + ".config." + "disableCrossInventoryAnimations"))
                    .description(builder6.build())
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
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
                                    disableAllBackgroundModificationsOption,
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
