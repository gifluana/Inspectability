package com.lunazstudios.inspectability.client;

import com.lunazstudios.inspectability.client.util.HeldItemTransformManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public class InspectorScreen extends Screen {
    private float rotationX = 0.0f;
    private float rotationY = 0.0f;
    private float rotationZ = 0.0f;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private float offsetZ = -2.0f;
    private float scale = 2.0f;

    private double lastMouseX;
    private double lastMouseY;

    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 20.0f;

    private long screenOpenedTime;

    public InspectorScreen() {
        super(Component.literal("Item Inspector"));
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (minecraft != null && (System.currentTimeMillis() - screenOpenedTime) < 10000) {
            graphics.drawString(this.font, Component.translatable("tooltip.inspectability.move"), 10, 10, -1, true);
            graphics.drawString(this.font, Component.translatable("tooltip.inspectability.reposition"), 10, 20, -1, true);
            graphics.drawString(this.font, Component.translatable("tooltip.inspectability.rotate"), 10, 30, -1, true);
            graphics.drawString(this.font, Component.translatable("tooltip.inspectability.zoom"), 10, 40, -1, true);
        }

        handleMouseInput(mouseX, mouseY);

        HeldItemTransformManager.setTransformations(
                offsetX, offsetY, offsetZ,
                scale,
                rotationX, rotationY, rotationZ
        );
    }

    @Override
    protected void init() {
        HeldItemTransformManager.setInspectorMode(true);
        HeldItemTransformManager.setTransformations(0.0f, 0.0f, -2.0f, 2.0f, 0.0f, 0.0f, 0.0f);

        lastMouseX = Minecraft.getInstance().mouseHandler.xpos();
        lastMouseY = Minecraft.getInstance().mouseHandler.ypos();

        screenOpenedTime = System.currentTimeMillis();
    }

    @Override
    public void removed() {
        HeldItemTransformManager.setInspectorMode(false);
    }

    private void handleMouseInput(int mouseX, int mouseY) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        boolean leftPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean rightPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        if (leftPressed) {
            if (lastMouseX != 0 || lastMouseY != 0) {
                rotationY += (mouseX - lastMouseX) * 0.5f;
                rotationX += (mouseY - lastMouseY) * 0.5f;
            }
        }

        if (rightPressed) {
            if (lastMouseX != 0 || lastMouseY != 0) {
                offsetX += (mouseX - lastMouseX) * 0.01f;
                offsetY -= (mouseY - lastMouseY) * 0.01f;
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scale += verticalAmount > 0 ? 0.1f : -0.1f;
        scale = Mth.clamp(scale, MIN_SCALE, MAX_SCALE);
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}