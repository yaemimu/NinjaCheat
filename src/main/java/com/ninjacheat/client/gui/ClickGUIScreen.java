package com.ninjacheat.client.gui;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * NinjaCheat の ClickGUI 画面。
 * FDPClient の ClickGUI (カテゴリ別パネル) をシンプルに再実装したもの。
 * 左にカテゴリタブ、右にそのカテゴリのモジュール一覧を表示し、
 * モジュールをクリックでトグル、設定は展開式。
 */
public class ClickGUIScreen extends Screen {

    private static final int PANEL_X = 20;
    private static final int PANEL_Y = 20;
    private static final int CATEGORY_W = 110;
    private static final int MODULE_W = 200;
    private static final int ROW_H = 16;

    private Category selected = Category.COMBAT;
    private int scroll = 0;
    private Module expandedModule = null;

    public ClickGUIScreen() {
        super(Text.literal("NinjaCheat"));
    }

    @Override
    protected void init() {
        // 各カテゴリのタブボタン
        Category[] cats = Category.values();
        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            addDrawableChild(ButtonWidget.builder(
                            Text.literal(cat.getDisplayName()), b -> {
                        selected = cat;
                        expandedModule = null;
                        scroll = 0;
                    })
                    .dimensions(PANEL_X, PANEL_Y + i * (ROW_H + 4), CATEGORY_W, ROW_H)
                    .build());
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // 背景
        ctx.fill(0, 0, width, height, 0x90000000);
        // ヘッダ
        ctx.drawCenteredTextWithShadow(textRenderer, "§b" + NinjaCheat.MOD_NAME + " §7v" + NinjaCheat.MOD_VERSION,
                width / 2, 4, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, "§7Click: toggle  §7Right-click: expand  §7ESC: close",
                width / 2, 16, 0xFFAAAAAA);

        // モジュール一覧 (右パネル)
        int listX = PANEL_X + CATEGORY_W + 10;
        int listY = PANEL_Y;
        Map<Category, List<Module>> byCat = NinjaCheat.get().modules().byCategory();
        List<Module> mods = byCat.getOrDefault(selected, new ArrayList<>());

        // パネル背景
        ctx.fill(listX - 2, listY - 2, listX + MODULE_W + 2, listY + mods.size() * ROW_H + 2, 0x70000000);

        for (int i = 0; i < mods.size(); i++) {
            Module m = mods.get(i);
            int y = listY + i * ROW_H;
            boolean hover = mouseX >= listX && mouseX <= listX + MODULE_W && mouseY >= y && mouseY <= y + ROW_H;
            int bg = m.isEnabled() ? (m.getCategory().getColor() & 0x80FFFFFF) : (hover ? 0x40FFFFFF : 0x20FFFFFF);
            ctx.fill(listX, y, listX + MODULE_W, y + ROW_H - 1, bg);
            String prefix = m.isEnabled() ? "§a§l» §r" : "§7";
            ctx.drawTextWithShadow(textRenderer, prefix + m.getName() + (m.hasKey() ? " §8[" + keyName(m.getKey()) + "]" : ""),
                    listX + 4, y + 4, 0xFFFFFFFF);
        }

        // 展開中モジュールの設定
        if (expandedModule != null) {
            renderExpandedSettings(ctx, mouseX, mouseY, listX + MODULE_W + 10, listY);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderExpandedSettings(DrawContext ctx, int mx, int my, int x, int y) {
        List<Setting<?>> settings = expandedModule.getSettings();
        ctx.fill(x - 2, y - 2, x + 220, y + settings.size() * 20 + 30, 0x90000000);
        ctx.drawTextWithShadow(textRenderer, "§e" + expandedModule.getName() + " §7settings", x + 4, y + 2, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, "§7" + expandedModule.getDescription(), x + 4, y + 14, 0xFFAAAAAA);
        for (int i = 0; i < settings.size(); i++) {
            Setting<?> s = settings.get(i);
            int sy = y + 28 + i * 20;
            ctx.drawTextWithShadow(textRenderer, "§f" + s.getName() + ": §b" + s.get(), x + 4, sy + 4, 0xFFFFFFFF);
        }
        if (settings.isEmpty()) {
            ctx.drawTextWithShadow(textRenderer, "§8(no settings)", x + 4, y + 28, 0xFF888888);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int listX = PANEL_X + CATEGORY_W + 10;
        int listY = PANEL_Y;
        Map<Category, List<Module>> byCat = NinjaCheat.get().modules().byCategory();
        List<Module> mods = byCat.getOrDefault(selected, new ArrayList<>());
        for (int i = 0; i < mods.size(); i++) {
            Module m = mods.get(i);
            int y = listY + i * ROW_H;
            if (mouseX >= listX && mouseX <= listX + MODULE_W && mouseY >= y && mouseY <= y + ROW_H) {
                if (button == 0) {
                    m.toggle();
                } else if (button == 1) {
                    expandedModule = (expandedModule == m) ? null : m;
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scroll += (int) -verticalAmount;
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static String keyName(int code) {
        return net.minecraft.client.util.InputUtil.fromKeyCode(code, -1).getLocalizedText().getString();
    }
}
