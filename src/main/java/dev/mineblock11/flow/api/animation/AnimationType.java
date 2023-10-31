package dev.mineblock11.flow.api.animation;

import dev.mineblock11.flow.config.FlowConfig;
import net.minecraft.util.math.MathHelper;

public enum AnimationType {
    slideUp((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), height, 0);
        return new OffsetProvider(0, easedValue, 0, false, true);
    }),
    slideDown((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), -height, 0);
        return new OffsetProvider(0, easedValue, 0, false, true);
    }),
    slideLeft((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), -width, 0);
        return new OffsetProvider(easedValue, 0, 0, false, true);
    }),
    slideRight((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), width, 0);
        return new OffsetProvider(easedValue, 0, 0, false, true);
    }),
    expandMiddleLeft((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height / 2f, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), -width, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandMiddleRight((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height / 2f, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandMiddleCenter((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height / 2f, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width / 2f, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandTopLeft((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), -height, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), -width, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandTopRight((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), -height, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandTopCenter((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), -height, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width / 2f, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandBottomLeft((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), -width, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandBottomRight((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    expandBottomCenter((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width / 2f, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),;

    /**
     * The offset distributor - used to calculate the offsets of the animation using the width, height, progress and easing.
     */
    private final OffsetDistributor offsetDistributor;

    /**
     * Calculate the offset for the animation.
     * @param width The width of the screen.
     * @param height The height of the screen.
     * @param progress The progress of the animation.
     * @param isClosing Is the animation closing.
     * @return The offset provider.
     */
    public OffsetProvider calculateOffset(float width, float height, float progress, boolean isClosing) {
        return this.calculateOffset(width, height, progress, isClosing ? FlowConfig.get().easeOutType : FlowConfig.get().easeInType);
    }

    /**
     * Calculate the offset for the animation.
     * @param width The width of the screen.
     * @param height The height of the screen.
     * @param progress The progress of the animation.
     * @param easing The easing of the animation.
     * @return The offset provider.
     */
    public OffsetProvider calculateOffset(float width, float height, float progress, Easings easing) {
        return offsetDistributor.distribute(width, height, progress, easing);
    }

    /**
     * Get the animation type for the screen from config depending on the situation.
     * @param isClosing Is the animation closing.
     * @return The animation type.
     */
    public static AnimationType getAnimationType(boolean isClosing) {
        if(isClosing) return FlowConfig.get().easeOutAnimationType;
        else return FlowConfig.get().easeInAnimationType;
    }

    /**
     * Create a new animation type.
     * @param offsetDistributor The offset distributor.
     */
    AnimationType(OffsetDistributor offsetDistributor) {
        this.offsetDistributor = offsetDistributor;
    }
}
