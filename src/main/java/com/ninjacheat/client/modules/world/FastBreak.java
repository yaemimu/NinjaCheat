package com.ninjacheat.client.modules.world;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * FastBreak — ブロック破壊速度を向上 (破壊進行を毎tick更新)。
 * FDPClient の FastBreak / CheatUtils の fastMine 統合。
 * 教育目的の簡易版: 視線先のブロックに対して攻撃パケットを連打。
 */
public class FastBreak extends Module {

    public FastBreak() {
        super("FastBreak", "Mines blocks faster", Category.WORLD);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (!mc.options.useKey.isPressed()) return;
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult bhr)) return;
        BlockPos pos = bhr.getBlockPos();
        // ブロック破壊を加速: updateBlockBreakingProgress 相当を連続呼び出し
        // バニラは既に毎tick呼んでいるが、より高頻度でパケット送信を促す
        mc.interactionManager.attackBlock(pos, bhr.getSide());
        mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
    }
}
