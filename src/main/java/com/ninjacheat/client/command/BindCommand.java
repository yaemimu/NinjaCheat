package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Module;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

/**
 * ".bind <module> <key>" コマンド — モジュールにキーを割り当てる。
 * キー名は GLFW のキー名 (例: r, f, right_shift)。
 */
public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "key");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            chatUsage(".bind <module> <key>   (key 例: r, f, right_shift, none)");
            return;
        }
        Module m = NinjaCheat.get().modules().get(args[0]);
        if (m == null) {
            chatError("Module not found: " + args[0]);
            return;
        }
        String keyName = args[1].toLowerCase(Locale.ROOT);
        if (keyName.equals("none") || keyName.equals("unbind")) {
            m.setKey(GLFW.GLFW_KEY_UNKNOWN);
            chatInfo("Unbound " + m.getName());
            return;
        }
        int code = GLFWKeyLookup.byName(keyName);
        if (code < 0) {
            chatError("Unknown key: " + keyName);
            return;
        }
        m.setKey(code);
        chatInfo("Bound " + m.getName() + " to " + keyName.toUpperCase(Locale.ROOT));
    }

    @Override
    public String description() {
        return "Bind a module to a key";
    }
}
