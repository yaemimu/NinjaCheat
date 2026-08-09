package com.ninjacheat.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

/**
 * サーバー ping 表示。
 */
public class PingHud extends HudElement {

    public PingHud() {
        super("Ping");
    }

    @Override
    public int priority() {
        return 40;
    }

    @Override
    public int render(int x, int y, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.getNetworkHandler() == null) return 0;
        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(p.getUuid());
        int ping = entry != null ? entry.getLatency() : -1;
        String color = ping < 0 ? "§7" : ping < 100 ? "§a" : ping < 250 ? "§e" : "§c";
        String line = "§7Ping " + color + (ping < 0 ? "?" : ping + "ms");
        MatrixStack stack = new MatrixStack();
        tr.draw(stack, line, x + 1, y + 1, 0xFFFFFFFF);
        return tr.fontHeight;
    }
}
