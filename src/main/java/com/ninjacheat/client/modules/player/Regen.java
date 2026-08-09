package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

/**
 * Regen — 満腹度を使わずに高速で自然回復させる (簡易: 食料連続消費)。
 * FDPClient の Regen / CheatUtils の fastRegen 統合。
 * 教育目的: 満腹度が十分な時に食料を短く食べて回復を促す簡易版。
 */
public class Regen extends Module {

    private final NumberSetting healthThreshold = addSetting(new NumberSetting("Health", "Regen below this health", 15, 1, 20, 1));

    public Regen() {
        super("Regen", "Regenerates health faster", Category.PLAYER);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (mc.player.getHealth() >= healthThreshold.getFloat()) return;

        // 金リンゴを所持 & 満腹度十分なら食べて回復
        if (mc.player.getHungerManager().getFoodLevel() >= 18) return; // 既に満腹で自然回復中

        int gappleSlot = findItem(Items.GOLDEN_APPLE);
        if (gappleSlot >= 0) {
            int prev = mc.player.getInventory().getSelectedSlot();
            if (gappleSlot < 9) {
                mc.player.getInventory().setSelectedSlot(gappleSlot);
                mc.options.useKey.setPressed(true);
                mc.player.getInventory().setSelectedSlot(prev);
            }
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) mc.options.useKey.setPressed(false);
    }

    private int findItem(net.minecraft.item.Item item) {
        var inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() == item) return i;
        }
        return -1;
    }
}
