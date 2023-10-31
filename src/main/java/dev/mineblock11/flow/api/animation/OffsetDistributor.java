package dev.mineblock11.flow.api.animation;

@FunctionalInterface
public interface OffsetDistributor {
    OffsetProvider distribute(float width, float height, float progress, Easings easing);
}
