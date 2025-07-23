package com.lunazstudios.inspectability;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class InspectorScreen extends Screen {
    private final Minecraft client;
    private ItemStack heldItem;

    private float rotationX = 0.0f;
    private float rotationY = 0.0f;

    private double lastMouseX;
    private double lastMouseY;

    private boolean isLeftDragging = false;
    private boolean isRightDragging = false;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;

    private float scale = 150.0f;

    protected InspectorScreen(Minecraft client, ItemStack heldItem) {
        super(Component.literal("Item Inspector"));
        this.client = client;
        this.heldItem = heldItem;
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);

        int screenWidth = this.width;
        int screenHeight = this.height;

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        handleMouseInput(mouseX, mouseY);

        if (heldItem.isEmpty()) {
            heldItem = new ItemStack(Items.AIR);
        }

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();

        pose.translate(centerX, centerY, 0);
        pose.scale(scale, -scale, scale);
        pose.translate(offsetX, offsetY, 0);

        // Apply rotation
        pose.mulPose(Axis.YP.rotationDegrees(rotationY));
        pose.mulPose(Axis.XP.rotationDegrees(rotationX));

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        itemRenderer.render(
                heldItem,
                ItemDisplayContext.GUI,
                false,
                pose,
                bufferSource,
                0x00F000F0,
                OverlayTexture.NO_OVERLAY,
                itemRenderer.getModel(heldItem, null, null, 0)
        );

        bufferSource.endBatch();
        pose.popPose();
    }

    private void handleMouseInput(int mouseX, int mouseY) {
        long windowHandle = client.getWindow().getWindow();
        boolean leftPressed = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean rightPressed = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        if (leftPressed) {
            if (!isLeftDragging) {
                isLeftDragging = true;
                lastMouseX = mouseX;
                lastMouseY = mouseY;
            } else {
                double deltaX = mouseX - lastMouseX;
                double deltaY = mouseY - lastMouseY;

                rotationY += deltaX * 0.5f;
                rotationX += deltaY * 0.5f;

                lastMouseX = mouseX;
                lastMouseY = mouseY;
            }
        } else {
            isLeftDragging = false;
        }

        if (rightPressed) {
            if (!isRightDragging) {
                isRightDragging = true;
                lastMouseX = mouseX;
                lastMouseY = mouseY;
            } else {
                double deltaX = (mouseX - lastMouseX) / scale;
                double deltaY = (mouseY - lastMouseY) / scale;

                offsetX += deltaX;
                offsetY -= deltaY;

                lastMouseX = mouseX;
                lastMouseY = mouseY;
            }
        } else {
            isRightDragging = false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0) {
            scale += 10.0f;
        } else if (verticalAmount < 0) {
            scale -= 10.0f;
        }

        scale = Mth.clamp(scale, 10.0f, 400.0f);

        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}