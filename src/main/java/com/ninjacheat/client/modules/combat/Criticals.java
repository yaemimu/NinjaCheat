package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.PacketSendEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * Criticals — ジャンプせずクリティカル発生。
 * FDPClient の Criticals (各種モード) をシンプル化。Always/Jump/Packet モード。
 */
public class Criticals extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "How criticals are triggered", "Packet", "Packet", "Jump", "Always"));

    public Criticals() {
        super("Criticals", "Deals critical hits without jumping", Category.COMBAT);
    }

    @EventHandler
    private void onPacketSend(PacketSendEvent event) {
        if (mc.player == null || !mode.get().equals("Packet")) return;
        Object pkt = event.getPacket();
        if (pkt instanceof PlayerMoveC2SPacket move && mc.player.isOnGround()) {
            // onGround=false を 2 連続送る簡易的 Packet クリティカル (教育用サンプル)
            // 本格運用では実際のパケット改変が必要 (実装は簡略化)
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mode.get().equals("Jump") && mc.player.isOnGround() && mc.player.getAttacking() != null) {
            mc.player.jump();
        }
    }
}
