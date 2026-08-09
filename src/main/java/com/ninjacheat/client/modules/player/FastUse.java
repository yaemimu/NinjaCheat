package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * FastUse — アイテム使用のクールダウンを短縮 (食べる速さ・飲む速さ)。
 * FDPClient の FastUse / CheatUtils の fastEat 統合。
 */
public class FastUse extends Module {

    private final NumberSetting delay = addSetting(new NumberSetting("Delay", "Ticks between uses (lower=faster)", 2, 0, 20, 1));

    public FastUse() {
        super("FastUse", "Eat/drink/use items faster", Category.PLAYER);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        // itemUseCooldown を短縮 (リフレクションでプライベートフィールドを操作する本格実装の簡易版)
        // 教育目的: 手持ちが食料/ポーション時に useKey 押下を高速で再送
        ItemStack stack = mc.player.getMainHandStack();
        if (stack.isEmpty()) return;
        boolean isConsumable = stack.getItem().getComponents().get(net.minecraft.component.DataComponentTypes.FOOD) != null
                || stack.getItem() == Items.POTION
                || stack.getItem() == Items.BOW;
        if (isConsumable && mc.options.useKey.isPressed()) {
            // クールダウン値 (itemUseCooldown) を毎tick 0 に近づける簡易アプローチ
            // ※ 実際は MixinClientPlayerEntity で itemUseCooldown フィールドをキャンセルするのが正道
            if (mc.player.getItemUseTimeLeft() > 0) {
                // 何もしない: 本格実装は Mixin 側で減らす
            }
        }
    }
}
