package com.tomate.randomitem.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class RandomItemClient implements ClientModInitializer {
    public static KeyMapping minKey;
    public static KeyMapping maxKey;
    public static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        minKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.random-item.min", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "category.random-item.keys"));
        maxKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.random-item.max", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "category.random-item.keys"));
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.random-item.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.random-item.keys"));
    }
}
