package com.ninjacheat.client.command;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ".list" / ".modules" コマンド — 全モジュールをカテゴリ別に表示。
 */
public class ListCommand extends Command {

    public ListCommand() {
        super("list", "modules");
    }

    @Override
    public void execute(String[] args) {
        Map<Category, List<Module>> byCat = NinjaCheat.get().modules().byCategory();
        chatInfo("§f" + NinjaCheat.get().modules().size() + " modules loaded:");
        for (Category cat : Category.values()) {
            List<Module> mods = byCat.get(cat);
            if (mods == null || mods.isEmpty()) continue;
            String line = mods.stream()
                    .map(m -> (m.isEnabled() ? "§a" : "§7") + m.getName())
                    .collect(Collectors.joining("§r, "));
            chatRaw("§e[" + cat.getDisplayName() + "]§r " + line);
        }
    }

    @Override
    public String description() {
        return "List all modules grouped by category";
    }
}
