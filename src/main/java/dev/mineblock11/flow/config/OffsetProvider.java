package dev.mineblock11.flow.config;

import dev.mineblock11.flow.compat.EmiStackBatcherSetter;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import oshi.util.tuples.Triplet;

public class OffsetProvider {
    private final float x;
    private final float y;
    private final float z;
    private final boolean shouldScale;
    private final boolean shouldTranslate;

    public void setOverridenTranslation(float x, float y, float z) {
        this.overrideTranslation = true;
        this.overrideTranslationX = x;
        this.overrideTranslationY = y;
        this.overrideTranslationZ = z;
    }

    public void setOverridenScale(float x, float y, float z) {
        this.overrideScale = true;
        this.overrideScaleX = x;
        this.overrideScaleY = y;
        this.overrideScaleZ = z;
    }

    private boolean overrideScale = false;
    private boolean overrideTranslation = false;
    private float overrideScaleX = 0f;
    private float overrideScaleY = 0f;
    private float overrideScaleZ = 0f;
    private float overrideTranslationX = 0f;
    private float overrideTranslationY = 0f;
    private float overrideTranslationZ = 0f;

    public OffsetProvider(float x, float y, float z, boolean shouldScale, boolean shouldTranslate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.shouldScale = shouldScale;
        this.shouldTranslate = shouldTranslate;
    }

    public void apply(EmiStackBatcherSetter stackBatcher) {
        if(shouldTranslate) {
            stackBatcher.setX((int) x + stackBatcher.getInitialX());
            stackBatcher.setY((int) y);
        }
    }

    public void apply(MatrixStack matrices) {
        if(shouldScale) {
            if(overrideScale) {
                matrices.scale(overrideScaleX, overrideScaleY, overrideScaleZ);
            } else {
                matrices.scale(x, y, z);
            }
        }

        if(shouldTranslate) {
            if(overrideTranslation) {
                matrices.translate(overrideTranslationX, overrideTranslationY, overrideTranslationZ);
            } else {
                matrices.translate(x, y, z);
            }
        }
    }
}
