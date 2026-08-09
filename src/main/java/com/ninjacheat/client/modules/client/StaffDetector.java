package com.ninjacheat.client.modules.client;

import com.ninjacheat.client.command.CommandManager;
import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.StringSetting;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * StaffDetector — サーバースタッフ (管理者/モデレーター) を検出して警告。
 * FDPClient の StaffDetector / CheatUtils 系の staff 通知統合。
 * 教育目的: プレイヤー名のプレフィックス/権限表示を監視。
 */
public class StaffDetector extends Module {

    private final StringSetting prefixes = addSetting(new StringSetting("Prefixes", "Name prefixes of staff (comma-separated)", "[Admin],[Mod],[GM],Helper"));

    private final Set<String> staff = new HashSet<>();

    public StaffDetector() {
        super("StaffDetector", "Detects server staff and warns you", Category.CLIENT);
    }

    @Override
    protected void onDisable() {
        staff.clear();
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.world == null) return;
        String[] pfx = prefixes.get().split(",");
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;
            String name = p.getName().getString();
            for (String pf : pfx) {
                String t = pf.trim();
                if (!t.isEmpty() && name.toLowerCase().startsWith(t.toLowerCase())) {
                    if (staff.add(name)) {
                        CommandManager.sendChat("§c[NinjaCheat] §fStaff detected: §e" + name);
                    }
                }
            }
        }
    }

    public boolean isStaff(PlayerEntity player) {
        return player != null && staff.contains(player.getName().getString());
    }
}
