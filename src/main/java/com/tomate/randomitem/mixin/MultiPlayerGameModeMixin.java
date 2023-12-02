package com.tomate.randomitem.mixin;

import com.tomate.randomitem.RandomItem;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow
    public abstract void handleInventoryMouseClick(int i, int j, int k, ClickType clickType, Player player);

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V"))
    void useItemOn(LocalPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if(!RandomItem.enabled)
            return;

        var nextItem = weightedRandomSelection(localPlayer.getInventory(), RandomItem.min, RandomItem.max);
        if (nextItem != -1) {
            swapOffhand(localPlayer, nextItem);
        }
    }

    @Unique
    void swapOffhand(LocalPlayer player, int slot) {
        handleInventoryMouseClick(0, slot, 40, ClickType.SWAP, player);
    }

    @Unique
    int weightedRandomSelection(Inventory inv, int min, int max) {
        int totalItems = inv.offhand.get(0).getCount();

        for (int i = min; i <= max; i++) {
            if (i >= 36) {
                totalItems += inv.getItem(i - 36).getCount();
            } else {
                totalItems += inv.getItem(i).getCount();
            }
        }

        int chosenItem = (int) (Math.random() * totalItems);

        for (int i = min; i <= max; i++) {
            if (i >= 36) {
                chosenItem -= inv.getItem(i - 36).getCount();
            } else {
                chosenItem -= inv.getItem(i).getCount();
            }

            if (chosenItem < 0) {
                return i;
            }
        }

        return -1;
    }
}
