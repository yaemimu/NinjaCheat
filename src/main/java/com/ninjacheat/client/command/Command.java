package com.ninjacheat.client.command;

import java.util.Arrays;
import java.util.List;

/**
 * 全コマンドの基底クラス。
 * FDPClient の Command をシンプルにしたもの。
 * chat* ヘルパはサブクラスが使う。
 */
public abstract class Command {

    private final String name;
    private final List<String> aliases;

    protected Command(String name, String... aliases) {
        this.name = name;
        this.aliases = Arrays.asList(aliases);
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    /** 引数配列 (プレフィックスとコマンド名自体は除外済み) を受け取る */
    public abstract void execute(String[] args);

    public abstract String description();

    // ---- チャット送信ヘルパ ----

    protected void chatRaw(String message) {
        CommandManager.sendChat(message);
    }

    protected void chatInfo(String message) {
        CommandManager.sendChat("§7[§bNinjaCheat§7]§r " + message);
    }

    protected void chatError(String message) {
        CommandManager.sendChat("§7[§bNinjaCheat§7]§c " + message);
    }

    protected void chatUsage(String message) {
        CommandManager.sendChat("§7[§bNinjaCheat§7]§e Usage: " + message);
    }
}
