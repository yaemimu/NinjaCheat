package com.ninjacheat.client.modules.other;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * AutoSoup — 体力低下時にスープを飲んで即回復。
 * FDPClient の AutoSoup (Soup/PvP) 統合。
 */
public class AutoSoup extends Module {

    private final com.ninjacheat.client.setting.NumberSetting health =
            new com.ninjacheat.client.setting.NumberSetting("Health", "Eat soup below this health", 10, 1, 20, 1);

    public AutoSoup() {
        super("AutoSoup", "Automatically eats soup when low on health", Category.OTHER);
        addSetting(health);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.interactionManager == null) return;
        if (mc.player.getHealth() >= health.getFloat()) return;

        int slot = findSoup();
        if (slot < 0) return;

        int prev = mc.player.getInventory().getSelectedSlot();
        mc.player.getInventory().setSelectedSlot(slot);
        mc.interactionManager.interactItem(mc.player, net.minecraft.util.Hand.MAIN_HAND);
        mc.player.getInventory().setSelectedSlot(prev);
    }

    private int findSoup() {
        var inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() == Items.MUSHROOM_STEW
                    || stack.getItem() == Items.RABBIT_STEW
                    || stack.getItem() == Items.BEETROOT_SOUP
                    || stack.getItem() == Items.SUSPICIOUS_STEW) {
                return i;
            }
        }
        return -1;
    }
}
