package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * AutoEat — 満腹度が下がったら自動で食料を食べる。
 * FDPClient の AutoEat / CheatUtils の autoEat 統合。
 */
public class AutoEat extends Module {

    private final NumberSetting threshold = addSetting(new NumberSetting("Hunger Threshold", "Start eating below this hunger", 16, 1, 20, 1));

    private boolean wasEating = false;

    public AutoEat() {
        super("AutoEat", "Automatically eats when hunger is low", Category.PLAYER);
    }

    @Override
    protected void onDisable() {
        stopEating();
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.interactionManager == null) return;

        if (mc.player.getHungerManager().getFoodLevel() <= threshold.getInt()) {
            int slot = findFoodSlot();
            if (slot >= 0) {
                int prevSlot = mc.player.getInventory().getSelectedSlot();
                if (slot != prevSlot) {
                    // ホットバー内ならスロット切替
                    if (slot < 9) {
                        mc.player.getInventory().setSelectedSlot(slot);
                    } else {
                        // インベントリ内なら移動 (簡易: swap)
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                                slot + (slot >= 9 ? 9 : 0), prevSlot,
                                net.minecraft.screen.slot.SlotActionType.SWAP,
                                mc.player);
                    }
                }
                mc.options.useKey.setPressed(true);
                wasEating = true;
            }
        } else {
            stopEating();
        }
    }

    private void stopEating() {
        if (wasEating) {
            mc.options.useKey.setPressed(false);
            wasEating = false;
        }
    }

    private int findFoodSlot() {
        var inv = mc.player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            var food = stack.getComponents().get(DataComponentTypes.FOOD);
            if (food != null) return i;
        }
        return -1;
    }
}
