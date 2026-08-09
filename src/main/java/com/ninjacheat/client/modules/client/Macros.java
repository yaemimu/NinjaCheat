package com.ninjacheat.client.modules.client;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.KeyEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Macros — キーにマクロ (チャット送信/コマンド) を割り当て。
 * FDPClient の Macros / CheatUtils 系のマクロ統合。
 * 教育目的: key -> メッセージ の単純なマッピング。
 */
public class Macros extends Module {

    private final BoolSetting sendOnPress = addSetting(new BoolSetting("Send On Press", "Run macro on key press", true));

    // 教育目的: 固定マクロのサンプル (実運用は設定で永続化すべき)
    private final Map<Integer, String> macroMap = new LinkedHashMap<>();

    public Macros() {
        super("Macros", "Bind chat commands to keys", Category.CLIENT);
        // サンプルマクロ
        macroMap.put(GLFW.GLFW_KEY_K, "kit");
        macroMap.put(GLFW.GLFW_KEY_J, "home");
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (!sendOnPress.get()) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        String msg = macroMap.get(event.getKey());
        if (msg != null && mc.player != null) {
            // プレフィックス付きで送信 (コマンドなら ., それ以外はチャット)
            if (msg.startsWith(".") || msg.startsWith("/")) {
                if (msg.startsWith("/")) {
                    mc.player.networkHandler.sendChatCommand(msg.substring(1));
                } else {
                    com.ninjacheat.client.command.CommandManager.sendChat(msg);
                }
            } else {
                mc.player.networkHandler.sendChatMessage(msg);
            }
        }
    }

    public void bind(int key, String message) {
        macroMap.put(key, message);
    }

    public void unbind(int key) {
        macroMap.remove(key);
    }

    public Map<Integer, String> getMacros() {
        return macroMap;
    }
}
