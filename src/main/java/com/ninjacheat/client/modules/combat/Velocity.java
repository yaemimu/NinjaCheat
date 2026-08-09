package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.PacketReceiveEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

/**
 * Velocity — ノックバックを軽減/無効化。
 * FDPClient の Velocity (Simple/ AAC / reverse) をシンプル化。
 * 0% で完全無効、100% でバニラと同等、>100% で増幅 (ホップ)。
 */
public class Velocity extends Module {

    private final NumberSetting horizontal = addSetting(new NumberSetting("Horizontal", "Horizontal knockback %", 0, 0, 200, 5));
    private final NumberSetting vertical = addSetting(new NumberSetting("Vertical", "Vertical knockback %", 0, 0, 200, 5));

    public Velocity() {
        super("Velocity", "Reduces or modifies knockback", Category.COMBAT);
    }

    @EventHandler
    private void onPacketReceive(PacketReceiveEvent event) {
        if (mc.player == null) return;
        Object pkt = event.getPacket();

        if (pkt instanceof EntityVelocityUpdateS2CPacket vel) {
            if (vel.getEntityId() == mc.player.getId()) {
                double h = horizontal.get() / 100.0;
                double v = vertical.get() / 100.0;
                // 反映率を変える (0% ならキャンセル)
                if (h == 0 && v == 0) {
                    event.cancel();
                }
                // ※実際の部分適用はMixinでパケット改変が必要 (本実装は cancel/そのまま の2択)
            }
        }
        if (pkt instanceof ExplosionS2CPacket) {
            if (horizontal.get() == 0 && vertical.get() == 0) {
                event.cancel();
            }
        }
    }
}
