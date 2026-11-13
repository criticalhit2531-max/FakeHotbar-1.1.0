package dev.fakehotbar.mixin;

import dev.fakehotbar.config.HotbarConfig;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow @Final @Mutable public int x;

    /*
     * Player hotbar slots are moved by changing their GUI coordinates only.
     * Slot index and backing PlayerInventory are untouched, so clicks,
     * dragging and server slot IDs continue to refer to the real slot.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void fakehotbar$repositionPlayerHotbar(
            Inventory inventory, int index, int originalX, int originalY, CallbackInfo ci
    ) {
        if (!(inventory instanceof PlayerInventory)) return;
        if (index < 0 || index > 8) return;

        int visualPosition = HotbarConfig.positionOfActualSlot(index + 1);
        this.x = originalX + (visualPosition - index) * 18;
    }
}
