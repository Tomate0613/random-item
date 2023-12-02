package com.tomate.randomitem.mixin;

import com.tomate.randomitem.RandomItem;
import com.tomate.randomitem.client.RandomItemClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Inject(method = "checkHotbarKeyPressed", at = @At("HEAD"), cancellable = true)
    void checkHotbarKeyPressed(int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (hoveredSlot == null || !RandomItem.enabled) return;

        if (RandomItemClient.minKey.matches(i, j)) {
            RandomItem.min = getIndex(hoveredSlot);
            cir.setReturnValue(true);
        }

        if (RandomItemClient.maxKey.matches(i, j)) {
            RandomItem.max = getIndex(hoveredSlot);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderSlot", at = @At("HEAD"))
    void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if(!RandomItem.enabled)
            return;

        var idx = getIndex(slot);
        if(idx >= RandomItem.min && idx <= RandomItem.max) {
            int color = -2132706433;
            guiGraphics.fillGradient(RenderType.guiOverlay(), slot.x, slot.y, slot.x + 16, slot.y + 16, color, color, 0);
        }
    }

    @Unique
    int getIndex(Slot slot) {
        if(slot instanceof CreativeModeInventoryScreen.SlotWrapper sw) {
            return sw.target.index;
        }

        if(slot == null)
            return -1;

        return slot.index;
    }
}
