package dev.imb11.flow.api.animation;

import dev.imb11.flow.compat.emi.EmiStackBatcherSetter;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.ApiStatus;

public class OffsetProvider {
    private final float x;
    private final float y;
    private final float z;
    private final boolean shouldScale;
    private final boolean shouldTranslate;

    /**
     * Override the translation of the animation.
     * @param x The x translation.
     * @param y The y translation.
     * @param z The z translation.
     */
    public void setOverridenTranslation(float x, float y, float z) {
        this.overrideTranslation = true;
        this.overrideTranslationX = x;
        this.overrideTranslationY = y;
        this.overrideTranslationZ = z;
    }

    /**
     * Override the scaling of the animation.
     * @param x The x scale.
     * @param y The y scale.
     * @param z The z scale.
     */
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

    /**
     * Create a new offset provider.
     * @param x The x offset.
     * @param y The y offset.
     * @param z The z offset.
     * @param shouldScale Should the offset scale the screen.
     * @param shouldTranslate Should the offset translate the screen.
     */
    public OffsetProvider(float x, float y, float z, boolean shouldScale, boolean shouldTranslate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.shouldScale = shouldScale;
        this.shouldTranslate = shouldTranslate;
    }

    /**
     * Apply the offset to EMI's stack batcher.
     * @param stackBatcher The stack batcher to apply the offset to.
     */
    @ApiStatus.Internal
    public void apply(EmiStackBatcherSetter stackBatcher) {
        if(shouldTranslate) {
            stackBatcher.setX((int) x + stackBatcher.getInitialX());
            stackBatcher.setY((int) y);
        }
    }

    /**
     * Negate the offset of the animation, useful to fix anything you don't want animated on the screen.
     * @param matrices The matrix stack to apply the negation to.
     */
    public void negate(MatrixStack matrices) {
        if(shouldScale) {
            if(overrideScale) {
                matrices.scale(1f/overrideScaleX, 1f/overrideScaleY, 1f/overrideScaleZ);
            } else {
                matrices.scale(1f/x, 1f/y, 1f/z);
            }
        }

        if(shouldTranslate) {
            if(overrideTranslation) {
                matrices.translate(-overrideTranslationX, -overrideTranslationY, -overrideTranslationZ);
            } else {
                matrices.translate(-x, -y, -z);
            }
        }
    }

    /**
     * Apply the animation offset to the matrix stack.
     * @param matrices The matrix stack to apply the offset to.
     */
    public void apply(MatrixStack matrices) {
        if(shouldTranslate) {
            if(overrideTranslation) {
                matrices.translate(overrideTranslationX, overrideTranslationY, overrideTranslationZ);
            } else {
                matrices.translate(x, y, z);
            }
        }

        if(shouldScale) {
            if(overrideScale) {
                matrices.scale(overrideScaleX, overrideScaleY, overrideScaleZ);
            } else {
                matrices.scale(x, y, z);
            }
        }
    }
}
