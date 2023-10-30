package dev.mineblock11.flow.config;

@FunctionalInterface
public interface OffsetDistributor {
    OffsetProvider distribute(float width, float height, float progress, Easings easing);
}
