package dev.mineblock11.flow.config;

import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public enum EntryType {
    UP((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), -height, 0);
        return new OffsetProvider(0, easedValue, 0, false, true);
    }),
    DOWN((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), height, 0);
        return new OffsetProvider(0, easedValue, 0, false, true);
    }),
    LEFT((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), -width, 0);
        return new OffsetProvider(easedValue, 0, 0, false, true);
    }),
    RIGHT((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), width, 0);
        return new OffsetProvider(easedValue, 0, 0, false, true);
    }),
    EXPAND((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var offsetH = MathHelper.lerp(easing.eval(progress), height / 2f, 0f);
        var offsetW = MathHelper.lerp(easing.eval(progress), width / 2f, 0f);

        var provider = new OffsetProvider(easedValue, easedValue, 1f, true, true);

        provider.setOverridenTranslation(offsetW, offsetH, 0f);

        return provider;
    }),
    SHRINK((width, height, progress, easing) -> {
        var easedValue = MathHelper.lerp(easing.eval(progress), 0f, 1f);
        var provider = new OffsetProvider(1f / easedValue, 1f / easedValue, 1f, true, true);

        provider.setOverridenTranslation(width / 2f * (easedValue - 1), height / 2f * (easedValue - 1), 0f);

        return provider;
    });

    private final OffsetDistributor offsetDistributor;

    public OffsetProvider calculateOffset(float width, float height, float progress, Easings easing) {
        return offsetDistributor.distribute(width, height, progress, easing);
    }

    EntryType(OffsetDistributor offsetDistributor) {
        this.offsetDistributor = offsetDistributor;
    }
}
