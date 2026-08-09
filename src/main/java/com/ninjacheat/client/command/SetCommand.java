package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import com.ninjacheat.client.setting.Setting;

import java.util.Locale;

/**
 * ".set <module> <setting> <value>" コマンド — 設定値を変更。
 */
public class SetCommand extends Command {

    public SetCommand() {
        super("set", "setting");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            chatUsage(".set <module> <setting> <value>");
            return;
        }
        Module m = NinjaCheat.get().modules().get(args[0]);
        if (m == null) {
            chatError("Module not found: " + args[0]);
            return;
        }
        Setting<?> target = null;
        for (Setting<?> s : m.getSettings()) {
            if (s.getName().equalsIgnoreCase(args[1])) {
                target = s;
                break;
            }
        }
        if (target == null) {
            chatError("Setting not found: " + args[1] + " on " + m.getName());
            return;
        }
        String value = args[2];
        try {
            switch (target.getType()) {
                case "bool" -> ((Setting<Boolean>) target).set(Boolean.parseBoolean(value));
                case "number" -> ((Setting<Double>) target).set(Double.parseDouble(value));
                case "enum", "string" -> ((Setting<String>) target).set(value);
                case "color" -> ((Setting<Integer>) target).set((int) Long.parseLong(value.replace("#", ""), 16));
                default -> {
                    chatError("Unsupported setting type: " + target.getType());
                    return;
                }
            }
            chatInfo(m.getName() + "." + target.getName() + " = " + value);
        } catch (Exception e) {
            chatError("Invalid value for " + target.getType() + ": " + value);
        }
    }

    @Override
    public String description() {
        return "Change a module setting";
    }
}
