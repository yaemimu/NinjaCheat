package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

/**
 * AutoArmor — インベントリ内のより良い防具を自動装備。
 * FDPClient の AutoArmor 統合。
 * 1.21.1 Yarn API 検証済み:
 *   - ArmorItem.getMaterial() は RegistryEntry<ArmorMaterial> を返す → .value() で実体
 *   - ArmorItem.getProtection() は int を返す (健在)
 *   - ArmorItem.getSlotType() は EquipmentSlot を直接返す (getEntitySlotId は不要)
 *   - ArmorItem.getType() は ArmorItem.Type を返し getType().getEquipmentSlot() も可
 */
public class AutoArmor extends Module {

    public AutoArmor() {
        super("AutoArmor", "Automatically equips best armor", Category.PLAYER);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.interactionManager == null) return;
        var inv = mc.player.getInventory();

        // 各防具スロット (head/body/legs/feet)
        EquipmentSlot[] armorSlots = {
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
        };

        for (EquipmentSlot eqSlot : armorSlots) {
            // getArmorStack(index): 0=feet, 1=legs, 2=chest, 3=head
            // EquipmentSlot.getEntitySlotId(): FEET=0, LEGS=1, CHEST=2, HEAD=3 → そのまま使える
            ItemStack current = inv.getArmorStack(eqSlot.getEntitySlotId());
            int bestInvSlot = -1;
            int bestArmor = armorValue(current);

            for (int i = 9; i < inv.size(); i++) {
                ItemStack stack = inv.getStack(i);
                if (stack.isEmpty()) continue;
                if (!(stack.getItem() instanceof ArmorItem armor)) continue;
                if (armor.getSlotType() != eqSlot) continue;
                int val = armorValue(stack);
                if (val > bestArmor) {
                    bestArmor = val;
                    bestInvSlot = i;
                }
            }

            if (bestInvSlot >= 0) {
                try {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                            bestInvSlot, 0, net.minecraft.screen.slot.SlotActionType.QUICK_MOVE, mc.player);
                } catch (Throwable ignored) {}
                return; // 1tick 1回
            }
        }
    }

    /**
     * 防具値 = ArmorMaterial.getProtection(ArmorItem.Type) + ArmorItem.getProtection()
     * 1.21.1 Yarn API 検証済み:
     *   - getMaterial() は RegistryEntry<ArmorMaterial> を返す → .value() で ArmorMaterial record
     *   - ArmorMaterial.getProtection(ArmorItem.Type) が防御ポイントを返す
     *   - ArmorItem.getProtection() も健在 (バックアップ)
     *   - ArmorItem.getToughness() も考慮に組み込む
     */
    private int armorValue(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem armor)) return 0;
        int protection = 0;
        try {
            protection = armor.getMaterial().value().getProtection(armor.getType());
        } catch (Throwable ignored) {
            protection = armor.getProtection();
        }
        // toughness を防御値に加味 (×10 で整数化して重み付け)
        int toughness = (int) (armor.getToughness() * 10);
        return protection * 100 + toughness;
    }
}
