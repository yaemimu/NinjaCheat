package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;

/**
 * ".config <save|load|reset>" コマンド — 設定ファイル操作。
 */
public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "cfg");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            chatUsage(".config <save|load|reset>");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "save" -> {
                NinjaCheat.get().config().save();
                chatInfo("Configuration saved.");
            }
            case "load" -> {
                NinjaCheat.get().config().load();
                chatInfo("Configuration loaded.");
            }
            case "reset" -> {
                NinjaCheat.get().config().reset();
                chatInfo("Configuration reset to defaults.");
            }
            default -> chatUsage(".config <save|load|reset>");
        }
    }

    @Override
    public String description() {
        return "Save / load / reset the config file";
    }
}
