package com.ninjacheat.client.hud;

import com.ninjacheat.client.NinjaCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * 接続先サーバー・セッション時間を表示。
 */
public class SessionHud extends HudElement {

    private static long sessionStart = System.currentTimeMillis();

    public SessionHud() {
        super("Session");
    }

    @Override
    public int priority() {
        return 50;
    }

    @Override
    public int render(int x, int y, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        String server = "§7Server §f" + (mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : "Singleplayer");
        long elapsed = (System.currentTimeMillis() - sessionStart) / 1000L;
        String time = String.format("§7Time §f%02d:%02d:%02d", elapsed / 3600, (elapsed % 3600) / 60, elapsed % 60);
        MatrixStack stack = new MatrixStack();
        tr.draw(stack, server, x + 1, y + 1, 0xFFFFFFFF);
        tr.draw(stack, time, x + 1, y + 1 + tr.fontHeight + 1, 0xFFFFFFFF);
        return tr.fontHeight * 2 + 1;
    }
}
