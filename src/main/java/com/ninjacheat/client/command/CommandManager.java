package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * コマンドを登録・ディスパッチする中央管理。
 * FDPClient の CommandManager 相当。
 */
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void register(Command command) {
        commands.add(command);
    }

    public int size() {
        return commands.size();
    }

    public List<Command> all() {
        return commands;
    }

    /** デフォルトコマンドを一括登録 */
    public void registerDefaults() {
        register(new ToggleCommand());
        register(new ListCommand());
        register(new BindCommand());
        register(new HelpCommand());
        register(new SetCommand());
        register(new ConfigCommand());
        register(new PanicCommand());
    }

    /** チャットメッセージを解析して実行。プレフィックスで始まれば true を返す */
    public boolean dispatch(String message) {
        if (!message.startsWith(NinjaCheat.COMMAND_PREFIX)) return false;
        String body = message.substring(NinjaCheat.COMMAND_PREFIX.length()).trim();
        if (body.isEmpty()) {
            sendChat("§7[§bNinjaCheat§7]§r Type §b.help§r for a list of commands.");
            return true;
        }
        String[] parts = body.split("\\s+");
        String cmdName = parts[0].toLowerCase(Locale.ROOT);
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);

        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(cmdName) ||
                    cmd.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(cmdName))) {
                try {
                    cmd.execute(args);
                } catch (Throwable t) {
                    sendChat("§7[§bNinjaCheat§7]§c Error running ." + cmdName + ": " + t.getMessage());
                }
                return true;
            }
        }
        sendChat("§7[§bNinjaCheat§7]§c Unknown command: ." + cmdName + " (try .help)");
        return true;
    }

    /** クライアントのチャットHUDにメッセージを送信 */
    public static void sendChat(String message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.inGameHud == null) return;
        ChatHud hud = mc.inGameHud.getChatHud();
        if (hud != null) {
            hud.addMessage(Text.literal(message));
        }
    }

    public static void sendColored(String message, Formatting color) {
        sendChat(color + message);
    }
}
