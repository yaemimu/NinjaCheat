package com.ninjacheat.client.modules.other;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

/**
 * MiddleClick — マウス中クリックで対象プレイヤーに金リンゴ/真珠を付与。
 * FDPClient の MiddleClick 統合。Pearl/Friend 切替可。
 */
public class MiddleClick extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Action", "What middle-click does", "Pearl", "Pearl", "Gapple", "Friend"));

    private boolean wasPressed = false;

    public MiddleClick() {
        super("MiddleClick", "Middle-click to use item on target (pearl/gapple)", Category.OTHER);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        boolean pressed = mc.options.pickItem.isPressed();
        if (pressed && !wasPressed) {
            doAction();
        }
        wasPressed = pressed;
    }

    private void doAction() {
        switch (mode.get()) {
            case "Pearl" -> useItem(Items.ENDER_PEARL);
            case "Gapple" -> useItem(Items.GOLDEN_APPLE);
            case "Friend" -> {
                // 視線先のプレイヤーをフレンドリストへ (教育目的: 何もしない簡易版)
            }
        }
    }

    private void useItem(net.minecraft.item.Item target) {
        var inv = mc.player.getInventory();
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).getItem() == target) { slot = i; break; }
        }
        if (slot < 0) return;
        int prev = inv.getSelectedSlot();
        inv.setSelectedSlot(slot);
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        mc.player.swingHand(Hand.MAIN_HAND);
        inv.setSelectedSlot(prev);
    }
}
