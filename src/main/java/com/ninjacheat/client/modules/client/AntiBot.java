package com.ninjacheat.client.modules.client;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * AntiBot — サーバーが生成した bot プレイヤーを検出して無視対象にする。
 * FDPClient の AntiBot 統合。教育目的のヒューリスティック実装。
 */
public class AntiBot extends Module {

    private final BoolSetting removeInvisible = addSetting(new BoolSetting("Remove Invisible", "Flag invisible players as bots", true));
    private final BoolSetting removeNoHealth = addSetting(new BoolSetting("Remove No Health", "Flag players with weird health", false));

    private final Set<UUID> bots = new HashSet<>();

    public AntiBot() {
        super("AntiBot", "Detects and ignores server bots", Category.CLIENT);
    }

    @Override
    protected void onDisable() {
        bots.clear();
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.world == null) return;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;
            boolean isBot = false;
            if (removeInvisible.get() && p.isInvisible()) isBot = true;
            if (removeNoHealth.get() && (p.getHealth() <= 0 || p.getHealth() > 20)) isBot = true;
            // ピンが表示されない (NPC の可能性)
            var entry = mc.player == null ? null : mc.getNetworkHandler().getPlayerListEntry(p.getUuid());
            if (entry == null) isBot = true;

            if (isBot) bots.add(p.getUuid());
            else bots.remove(p.getUuid());
        }
    }

    public boolean isBot(PlayerEntity player) {
        return player != null && bots.contains(player.getUuid());
    }

    public boolean isBot(UUID uuid) {
        return bots.contains(uuid);
    }
}
