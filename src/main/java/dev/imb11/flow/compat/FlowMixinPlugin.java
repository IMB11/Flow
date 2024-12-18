package dev.imb11.flow.compat;

import com.google.common.collect.ImmutableMap;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static dev.imb11.mru.LoaderUtils.isModInstalled;

public class FlowMixinPlugin implements IMixinConfigPlugin {
    private static final @NotNull Supplier<Boolean> TRUE = () -> true;

    // TODO: Remove this once EMI updates to 1.21.3
    @SuppressWarnings("ConstantValue")
    private static final @NotNull Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "dev.imb11.flow.mixin.compat.emi.ScreenSpaceMixin",
            () -> !SharedConstants.VERSION_NAME.equals("1.21.3") || !SharedConstants.VERSION_NAME.equals("1.21.2"),
            "dev.imb11.flow.mixin.compat.emi.SidebarPanelMixin",
            () -> !SharedConstants.VERSION_NAME.equals("1.21.3") || !SharedConstants.VERSION_NAME.equals("1.21.2"),
            "dev.imb11.flow.mixin.compat.emi.StackBatcherAccessor",
            () -> !SharedConstants.VERSION_NAME.equals("1.21.3") || !SharedConstants.VERSION_NAME.equals("1.21.2")
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public @Nullable String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public @Nullable List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
