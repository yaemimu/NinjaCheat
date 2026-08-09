package com.ninjacheat.client.hud;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

/**
 * 右上に有効モジュール一覧を表示 (ArrayList)。
 * FDPClient の ArrayListModule / CheatUtils の Modules List 相当。
 */
public class ArrayListHud extends HudElement {

    public ArrayListHud() {
        super("ArrayList");
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public int render(int x, int y, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        List<Module> enabled = NinjaCheat.get().modules().enabled();
        if (enabled.isEmpty()) return 0;

        // 名前長でソート (長い順 = 上)
        enabled.sort((a, b) -> Integer.compare(b.getName().length(), a.getName().length()));

        int screenW = mc.getWindow().getScaledWidth();
        MatrixStack stack = new MatrixStack();
        int lineY = y;
        for (Module m : enabled) {
            String name = m.getName();
            int width = tr.getWidth(name);
            int drawX = screenW - width - 4;
            tr.draw(stack, name, drawX + 1, lineY + 1, m.getCategory().getColor() | 0xFF000000);
            lineY += tr.fontHeight + 1;
        }
        return lineY - y;
    }
}
