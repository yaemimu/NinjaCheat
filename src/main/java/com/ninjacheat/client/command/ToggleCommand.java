package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Module;

import java.util.Locale;

/**
 * ".toggle <module>" コマンド。
 */
public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "t", "enable", "disable");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            chatUsage(".toggle <module>");
            return;
        }
        Module m = NinjaCheat.get().modules().get(args[0]);
        if (m == null) {
            chatError("Module not found: " + args[0]);
            return;
        }
        m.toggle();
        chatInfo(m.getName() + " is now " + (m.isEnabled() ? "§aON" : "§cOFF"));
    }

    @Override
    public String description() {
        return "Toggle a module on/off";
    }
}
