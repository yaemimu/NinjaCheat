package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Module;

/**
 * ".panic" コマンド — 全モジュールを一括無効化 (banned 対策・緊急回避用)。
 */
public class PanicCommand extends Command {

    public PanicCommand() {
        super("panic", "emergency");
    }

    @Override
    public void execute(String[] args) {
        int count = 0;
        for (Module m : NinjaCheat.get().modules().all()) {
            if (m.isEnabled() && !(m instanceof com.ninjacheat.client.modules.client.ClickGUI)
                    && !(m instanceof com.ninjacheat.client.modules.client.Hud)) {
                m.setEnabled(false);
                count++;
            }
        }
        chatInfo("§cPANIC§r: disabled " + count + " module(s).");
    }

    @Override
    public String description() {
        return "Disable every module immediately (panic button)";
    }
}
