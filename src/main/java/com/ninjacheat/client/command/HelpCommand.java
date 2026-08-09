package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;

/**
 * ".help" コマンド — 利用可能コマンド一覧を表示。
 */
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "h", "?");
    }

    @Override
    public void execute(String[] args) {
        chatInfo("§fAvailable commands:");
        for (Command cmd : NinjaCheat.get().commands().all()) {
            chatRaw(" §b." + cmd.getName() + "§7 - " + cmd.description());
        }
        chatRaw(" §7Press §bRIGHT SHIFT§7 to open ClickGUI");
    }

    @Override
    public String description() {
        return "Show this help message";
    }
}
