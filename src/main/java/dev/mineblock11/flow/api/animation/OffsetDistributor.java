package dev.mineblock11.flow.api.animation;

/**
 * This interface is used to calculate offsets in the form of an {@link OffsetProvider}.
 */
@FunctionalInterface
public interface OffsetDistributor {
    /**
     * Calculate the offset.
     * @param width The width of the screen.
     * @param height The height of the screen.
     * @param progress The progress of the animation.
     * @param easing The easing of the animation.
     * @return The offset provider containing the offsets.
     */
    OffsetProvider distribute(float width, float height, float progress, Easings easing);
}
