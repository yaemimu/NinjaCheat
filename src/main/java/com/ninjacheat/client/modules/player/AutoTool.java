package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;

/**
 * AutoTool — ブロック破壊・攻撃時に最適なツールへ自動切替。
 * FDPClient の AutoTool / CheatUtils の autoTool 統合。
 * 1.21.1 では ATTACK_DAMAGE コンポーネントが存在しないため、
 * SwordItem の ToolMaterial ティアの攻撃力ボーナスで武器を評価する。
 */
public class AutoTool extends Module {

    private final BoolSetting preferSilkTouch = addSetting(new BoolSetting("Prefer Silk", "Prefer Silk Touch tools", false));
    private final BoolSetting swapBack = addSetting(new BoolSetting("Swap Back", "Return to original slot after", true));

    private int prevSlot = -1;
    private boolean swapped = false;

    public AutoTool() {
        super("AutoTool", "Switches to the best tool automatically", Category.PLAYER);
    }

    @Override
    protected void onDisable() {
        swapBackNow();
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        // 攻撃/破壊キーが押されている時
        boolean attacking = mc.options.attackKey.isPressed();
        boolean mining = mc.options.useKey.isPressed()
                && mc.crosshairTarget != null
                && mc.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK;

        if (attacking) {
            // 戦闘: 剣を優先
            int best = findBestWeapon();
            if (best >= 0 && best != mc.player.getInventory().getSelectedSlot()) {
                rememberAndSwap(best);
            }
        } else if (mining) {
            // 採掘: ブロックに最適なツール
            if (mc.crosshairTarget instanceof net.minecraft.util.hit.BlockHitResult bhr) {
                BlockPos pos = bhr.getBlockPos();
                BlockState state = mc.world.getBlockState(pos);
                int best = findBestTool(state);
                if (best >= 0 && best != mc.player.getInventory().getSelectedSlot()) {
                    rememberAndSwap(best);
                }
            }
        } else {
            swapBackNow();
        }
    }

    private void rememberAndSwap(int target) {
        if (!swapped) {
            prevSlot = mc.player.getInventory().getSelectedSlot();
            swapped = true;
        }
        mc.player.getInventory().setSelectedSlot(target);
    }

    private void swapBackNow() {
        if (swapBack.get() && swapped && prevSlot >= 0) {
            mc.player.getInventory().setSelectedSlot(prevSlot);
        }
        swapped = false;
        prevSlot = -1;
    }

    /**
     * 1.21.1 では ItemStack に ATTACK_DAMAGE コンポーネントが無いため、
     * SwordItem の ToolMaterial から攻撃力ボーナスを取得して比較する。
     * ToolItem.getMaterial() は ToolMaterial インターフェイスを直接返す
     * (.value() 不要)。ToolMaterial.getAttackDamage() がティアの攻撃力ボーナスを返す。
     */
    private int findBestWeapon() {
        int best = -1;
        float bestDmg = -1;
        var inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SwordItem sword) {
                float dmg = swordDamage(sword);
                if (dmg > bestDmg) {
                    bestDmg = dmg;
                    best = i;
                }
            }
        }
        return best;
    }

    private float swordDamage(SwordItem sword) {
        try {
            ToolMaterial mat = sword.getMaterial();
            // ToolMaterial インターフェイスの getAttackDamage() が攻撃力ボーナスを返す
            return mat.getAttackDamage();
        } catch (Throwable ignored) {
            // フォールバック: 推測不可なので最低値
            return 1f;
        }
    }

    private int findBestTool(BlockState state) {
        int best = -1;
        float bestSpeed = 1f;
        var inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                best = i;
            }
        }
        return best;
    }
}
