package com.ninjacheat.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * FPS 表示。
 */
public class FpsHud extends HudElement {

    public FpsHud() {
        super("FPS");
    }

    @Override
    public int priority() {
        return 30;
    }

    @Override
    public int render(int x, int y, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        int fps = mc.getCurrentFps();
        String color = fps >= 60 ? "§a" : fps >= 30 ? "§e" : "§c";
        String line = "§7FPS " + color + fps;
        MatrixStack stack = new MatrixStack();
        tr.draw(stack, line, x + 1, y + 1, 0xFFFFFFFF);
        return tr.fontHeight;
    }
}
