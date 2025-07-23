package com.lunazstudios.inspectability;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public final class InspectabilityClient {
    public static final String MOD_ID = "inspectability";
    private static KeyMapping inspectorKey;

    public static void init() {
        inspectorKey = new KeyMapping(
                "key." + MOD_ID + ".openiteminspector",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category." + MOD_ID
        );

        KeyMappingRegistry.register(inspectorKey);

        ClientTickEvent.CLIENT_POST.register(InspectabilityClient::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        // Check if the key was pressed
        while (inspectorKey.consumeClick()) {
            LocalPlayer player = client.player;
            if (player != null) {
                ItemStack heldItem = player.getMainHandItem();
                client.setScreen(new InspectorScreen(client, heldItem));
            }
        }
    }
}
