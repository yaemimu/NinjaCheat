package com.ninjacheat.client.modules.client;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;

/**
 * DiscordRPC — Discord Rich Presence でプレイ状況を表示。
 * FDPClient の DiscordRPC 統合。
 * 教育目的のスタブ: 実際の RPC は Discord IPC ライブラリが必要なため、
 * ここでは有効時に定期的にプレイ状況をログ出力するのみ。
 */
public class DiscordRPC extends Module {

    private int tickCounter = 0;
    private static final int UPDATE_INTERVAL = 200; // 10秒 (20tick * 10)

    public DiscordRPC() {
        super("DiscordRPC", "Shows playing status in Discord", Category.CLIENT);
    }

    @Override
    protected void onEnable() {
        tickCounter = 0;
        // 教育目的: 実際はここで IPC 接続を確立
        // DiscordIPC.connect();
    }

    @Override
    protected void onDisable() {
        // DiscordIPC.disconnect();
    }

    @EventHandler
    private void onTick(TickEvent event) {
        tickCounter++;
        if (tickCounter < UPDATE_INTERVAL) return;
        tickCounter = 0;
        updatePresence();
    }

    private void updatePresence() {
        if (mc.player == null) return;
        String details = NinjaCheat.MOD_NAME + " v" + NinjaCheat.MOD_VERSION;
        String state;
        if (mc.getCurrentServerEntry() != null) {
            state = "Playing on " + mc.getCurrentServerEntry().address;
        } else if (mc.isInSingleplayer()) {
            state = "Singleplayer";
        } else {
            state = "In menu";
        }
        // 教育目的スタブ: 本来は DiscordIPC.updatePresence(details, state, ...)
        // ここでは何もしない (依存ライブラリを要するため)
    }
}
