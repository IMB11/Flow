package dev.imb11.flow.mixin.compat.inventorytabs;

import dev.imb11.flow.api.FlowAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = {
        "folk.sisby.inventory_tabs.tabs.BlockTab",
        "folk.sisby.inventory_tabs.tabs.VehicleInventoryTab",
        "folk.sisby.inventory_tabs.tabs.PlayerInventoryTab",
        "folk.sisby.inventory_tabs.tabs.ItemTab",
        "folk.sisby.inventory_tabs.tabs.EntityTab"})
public class TabsMixin {
    @Inject(method = "open", cancellable = false, at = @At("HEAD"))
    public void $flow_open_tab(CallbackInfo ci) {
        FlowAPI.toggleTemporaryDisable();
    }
}
