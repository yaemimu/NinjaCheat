package com.ninjacheat.client;

import com.ninjacheat.client.config.ConfigManager;
import com.ninjacheat.client.event.EventManager;
import com.ninjacheat.client.hud.HudManager;
import com.ninjacheat.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fabric クライアントエントリポイント。
 * CheatUtils の ModMain と FDPClient の ClientInitializer の役割を統合。
 */
public class NinjaCheatClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(NinjaCheat.MOD_NAME);

    @Override
    public void onInitializeClient() {
        long start = System.currentTimeMillis();

        // グローバルインスタンス構築
        NinjaCheat.setInstance(new NinjaCheat());

        // モジュール・コマンドを全登録
        NinjaCheat.get().modules().registerDefaults();
        NinjaCheat.get().commands().registerDefaults();
        NinjaCheat.get().hud().registerDefaults();

        // デフォルト有効モジュール (ClickGUI/Hud 等) のイベント購読を確立
        for (com.ninjacheat.client.module.Module m : NinjaCheat.get().modules().all()) {
            if (m.isEnabled()) NinjaCheat.get().events().subscribe(m);
        }

        // 設定読み込み (存在すれば)
        NinjaCheat.get().config().load();

        // 終了時に設定を保存
        ClientLifecycleEvents.CLIENT_STOPPING.register(client ->
                NinjaCheat.get().config().save());

        long elapsed = System.currentTimeMillis() - start;
        LOGGER.info("[{}] v{} loaded in {} ms ({} modules, {} commands)",
                NinjaCheat.MOD_NAME,
                NinjaCheat.MOD_VERSION,
                elapsed,
                NinjaCheat.get().modules().size(),
                NinjaCheat.get().commands().size());
    }
}
