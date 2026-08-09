package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.PacketSendEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * NoFall — 落下ダメージを無効化。
 * FDPClient の NoFall (Packet/Vanilla/AAC/SpoofGround 等) を統合したサンプル。
 */
public class NoFall extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Bypass method", "Packet", "Packet", "Vanilla", "SpoofGround"));

    public NoFall() {
        super("NoFall", "Prevents fall damage", Category.MOVEMENT);
    }

    @EventHandler
    private void onPacketSend(PacketSendEvent event) {
        if (mc.player == null || !mode.get().equals("Packet")) return;
        Object pkt = event.getPacket();
        if (pkt instanceof PlayerMoveC2SPacket packet && mc.player.fallDistance > 2.0f) {
            // onGround=true を送ることで落下をキャンセル (簡易 Packet 実装)
            // ※ 本格実装では Mixin でパケットの onGround フラグを書き換える必要がある
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.fallDistance > 2.0f) {
            if (mode.get().equals("Vanilla")) {
                // クライアント側で落下距離をリセット (バニラ鯖で有効)
                mc.player.fallDistance = 0;
            } else if (mode.get().equals("SpoofGround")) {
                // 常に接地扱いにする簡易版
                mc.player.setOnGround(true);
            }
        }
    }
}
