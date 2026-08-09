package com.ninjacheat.client.modules.other;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

/**
 * ChestStealer — チェスト内のアイテムを自動でインベントリへ移動。
 * FDPClient の ChestStealer / CheatUtils の autoSteal 統合。
 * MixinHandledScreen.onRender から onScreenTick(handler) が呼ばれる。
 */
public class ChestStealer extends Module {

    private final NumberSetting delay = addSetting(new NumberSetting("Delay", "Ticks between thefts", 2, 0, 20, 1));

    private int tickCounter = 0;

    public ChestStealer() {
        super("ChestStealer", "Automatically steals items from chests", Category.OTHER);
    }

    /** MixinHandledScreen から呼ばれる */
    public void onScreenTick(ScreenHandler handler) {
        if (mc.player == null || mc.interactionManager == null) return;
        tickCounter++;
        if (tickCounter < delay.getInt()) return;
        tickCounter = 0;

        // チェスト側スロット (通常 0..size-1 の前半) から空でないものを盗む
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.getSlot(i);
            // プレイヤーインベントリのスロットは除外 (id で判定: chest の場合 player inv は後半)
            if (isPlayerSlot(handler, i)) continue;
            ItemStack stack = slot.getStack();
            if (stack.isEmpty()) continue;
            // shift-click でインベントリへ
            mc.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
            return; // 1tick 1個
        }
    }

    private boolean isPlayerSlot(ScreenHandler handler, int index) {
        // チェスト系: プレイヤーインベントリは下側 (size - 36 以降)
        // 簡易: ハンドラのスロット数が 63 (大チェスト) or 27 (小チェスト) + 36 (player) を想定
        int size = handler.slots.size();
        return index >= size - 36;
    }
}
