package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

/**
 * AutoTotem — オフハンドに不死のトーテムを自動補充。
 * Crystal PvP で必須。CheatUtils の AutoTotem 由来。
 * 1.21.1 では DEATH_PROTECTION コンポーネントが存在しないため、
 * Items.TOTEM_OF_UNDYING のアイテム種別で判定する。
 */
public class AutoTotem extends Module {

    private final BoolSetting skipIfUsing = addSetting(new BoolSetting("Skip If Using", "Don't refill while using an item", true));
    private final NumberSetting healthThreshold = addSetting(new NumberSetting("Health", "Only refill below this health (0=always)", 0, 0, 20, 1));

    public AutoTotem() {
        super("AutoTotem", "Automatically refills totem of undying to off-hand", Category.PLAYER);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.interactionManager == null) return;
        if (skipIfUsing.get() && mc.player.isUsingItem()) return;

        // オフハンドがトーテムなら何もしない
        ItemStack offhand = mc.player.getOffHandStack();
        if (!offhand.isEmpty() && offhand.isOf(Items.TOTEM_OF_UNDYING)) return;

        // 体力条件 (0 = 常時)。オフハンドが空でない場合は体力条件を適用
        if (healthThreshold.get() > 0 && mc.player.getHealth() > healthThreshold.get()
                && !offhand.isEmpty()) return;

        // インベントリからトーテムを探す
        var inv = mc.player.getInventory();
        int totemSlot = -1;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                totemSlot = i;
                break;
            }
        }
        if (totemSlot < 0) return;

        // プレイヤー画面ハンドラのスロットIDに変換
        // inv index: 0..8 = hotbar → handler slot 36..44
        //            9..35 = main → handler slot 9..35
        //            40 = offhand storage → handler slot 45
        int sourceSlot = totemSlot < 9 ? totemSlot + 36 : totemSlot;
        int offhandSlot = 45;

        // PICKUP で掴んで → オフハンドへ置く → 余った元アイテムを戻す
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                sourceSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                offhandSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                sourceSlot, 0, SlotActionType.PICKUP, mc.player);
    }
}
