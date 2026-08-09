package com.ninjacheat.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * XYZ 座標 + 顔向きを表示。
 */
public class CoordinatesHud extends HudElement {

    public CoordinatesHud() {
        super("Coordinates");
    }

    @Override
    public int priority() {
        return 20;
    }

    @Override
    public int render(int x, int y, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        PlayerEntity p = mc.player;
        if (p == null) return 0;
        BlockPos pos = p.getBlockPos();
        String line = String.format("§7XYZ §f%d §7/ §f%d §7/ §f%d   §7(%s)", pos.getX(), pos.getY(), pos.getZ(), p.getMovementDirection());
        MatrixStack stack = new MatrixStack();
        tr.draw(stack, line, x + 1, y + 1, 0xFFFFFFFF);
        return tr.fontHeight;
    }
}
