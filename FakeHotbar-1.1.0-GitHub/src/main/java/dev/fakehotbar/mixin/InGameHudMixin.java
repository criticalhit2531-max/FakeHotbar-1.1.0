package dev.fakehotbar.mixin;

import dev.fakehotbar.config.HotbarConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private static Identifier HOTBAR_SELECTION_TEXTURE;

    /*
     * The vanilla loop passes the real hotbar slot number as the final seed
     * argument to renderHotbarItem. Move only its screen x coordinate.
     * The ItemStack still comes from the original inventory slot.
     */
    @ModifyArgs(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"
            )
    )
    private void fakehotbar$moveHudItems(Args args) {
        int actualIndex = args.get(6);
        if (actualIndex < 0 || actualIndex > 8) return;

        int visualPosition = HotbarConfig.positionOfActualSlot(actualIndex + 1);
        int oldX = args.get(1);
        args.set(1, oldX + (visualPosition - actualIndex) * 20);
    }

    /*
     * Move the vanilla selected-slot frame to the visual position of the
     * selected real inventory slot.
     */
    @ModifyArgs(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"
            )
    )
    private void fakehotbar$moveSelectionFrame(Args args) {
        Identifier sprite = args.get(1);
        if (!HOTBAR_SELECTION_TEXTURE.equals(sprite)) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int selectedActualIndex = client.player.getInventory().selectedSlot;
        int visualPosition = HotbarConfig.positionOfActualSlot(selectedActualIndex + 1);
        int oldX = args.get(2);
        args.set(2, oldX + (visualPosition - selectedActualIndex) * 20);
    }
}
