package com.lunazstudios.inspectability.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class HeldItemTransformManager {
    private static boolean inspectorMode = false;
    private static float transitionProgress = 0.0f;
    private static final float TRANSITION_SPEED = 0.05f;

    private static float initialRotationX = 0.0f;
    private static float initialRotationY = 0.0f;
    private static float initialRotationZ = 0.0f;
    private static float targetRotationX = 0.0f;
    private static float targetRotationY = 0.0f;
    private static float targetRotationZ = 0.0f;

    private static float initialOffsetX = 0.0f;
    private static float initialOffsetY = 0.0f;
    private static float initialOffsetZ = 0.0f;
    private static float targetOffsetX = 0.0f;
    private static float targetOffsetY = 0.0f;
    private static float targetOffsetZ = -2.0f;

    private static float initialScale = 1.0f;
    private static float targetScale = 2.0f;

    public static boolean isInspectorMode() {
        return inspectorMode;
    }

    public static void setInspectorMode(boolean enabled) {
        inspectorMode = enabled;
        if (enabled) {
            transitionProgress = 0.0f;

            Player player = Minecraft.getInstance().player;
            if (player != null) {
                InteractionHand hand = InteractionHand.MAIN_HAND;
                boolean isMainHand = hand == InteractionHand.MAIN_HAND;

                initialOffsetX = isMainHand ? 0.4f : -0.4f;
                initialRotationY = isMainHand ? -15.0f : 15.0f;

                initialOffsetY = -0.5f;
                initialOffsetZ = -1.2f;
                initialRotationX = 0.0f;
                initialRotationZ = 0.0f;
            }
        }
    }

    public static void updateTransition() {
        if (inspectorMode && transitionProgress < 1.0f) {
            transitionProgress = Math.min(1.0f, transitionProgress + TRANSITION_SPEED);
        }
    }

    public static float getOffsetX() {
        return lerp(easeInOut(transitionProgress), initialOffsetX, targetOffsetX);
    }

    public static float getOffsetY() {
        return lerp(easeInOut(transitionProgress), initialOffsetY, targetOffsetY);
    }

    public static float getOffsetZ() {
        return lerp(easeInOut(transitionProgress), initialOffsetZ, targetOffsetZ);
    }

    public static float getScale() {
        return lerp(easeInOut(transitionProgress), initialScale, targetScale);
    }

    public static float getRotationX() {
        return lerp(easeInOut(transitionProgress), initialRotationX, targetRotationX);
    }

    public static float getRotationY() {
        return lerp(easeInOut(transitionProgress), initialRotationY, targetRotationY);
    }

    public static float getRotationZ() {
        return lerp(easeInOut(transitionProgress), initialRotationZ, targetRotationZ);
    }

    public static void setTransformations(float offsetX, float offsetY, float offsetZ, float scale, float rotationX, float rotationY, float rotationZ) {
        targetOffsetX = offsetX;
        targetOffsetY = offsetY;
        targetOffsetZ = offsetZ;
        targetScale = scale;

        targetRotationX = rotationX;
        targetRotationY = rotationY;
        targetRotationZ = rotationZ;

        initialOffsetX = getOffsetX();
        initialOffsetY = getOffsetY();
        initialOffsetZ = getOffsetZ();
        initialScale = getScale();

        initialRotationX = getRotationX();
        initialRotationY = getRotationY();
        initialRotationZ = getRotationZ();

        transitionProgress = 0.0f;
    }

    private static float easeInOut(float progress) {
        return (float) (-0.5 * (Math.cos(Math.PI * progress) - 1.0));
    }

    private static float lerp(float progress, float start, float end) {
        return start + progress * (end - start);
    }

    public static void setInitialTransformations(Matrix4f matrix, InteractionHand hand) {
        Vector3f translation = new Vector3f();
        matrix.getTranslation(translation);

        initialOffsetX = translation.x();
        initialOffsetY = translation.y();
        initialOffsetZ = translation.z();

        initialRotationY = (hand == InteractionHand.MAIN_HAND) ? -15.0f : 15.0f;
        initialRotationX = 0.0f;
        initialRotationZ = 0.0f;
    }
}
