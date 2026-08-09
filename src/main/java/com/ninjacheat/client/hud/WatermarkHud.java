package com.ninjacheat.client.hud;

import com.ninjacheat.client.NinjaCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * 左上に表示される "NinjaCheat v1.0.0" ウォーターマーク。
 */
public class WatermarkHud extends HudElement {

    public WatermarkHud() {
        super("Watermark");
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public int render(int x, int y, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        String text = "§b" + NinjaCheat.MOD_NAME + " §7v" + NinjaCheat.MOD_VERSION;
        MatrixStack stack = new MatrixStack();
        tr.draw(stack, text, x + 1, y + 1, 0xFFFFFFFF);
        return tr.fontHeight;
    }
}
