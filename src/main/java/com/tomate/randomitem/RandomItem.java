package com.tomate.randomitem;

import com.tomate.randomitem.client.RandomItemClient;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;

public class RandomItem implements ModInitializer {
    public static int min = 0;
    public static int max = 0;
    public static boolean enabled = false;

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (RandomItemClient.toggleKey.consumeClick()) {
                enabled = !enabled;

                if(client.player != null) {
                    client.player.sendSystemMessage(Component.literal("RandomItem is now " + (enabled ? "enabled" : "disabled")));
                }
            }
        });
    }
}
