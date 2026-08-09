package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * Reach — 攻撃・ブロック相互作用のリーチ (届く距離) を延長。
 * FDPClient の Reach をシンプル化。バニラは ~3.0 (クリエイティブ 5.0)。
 */
public class Reach extends Module {

    private final NumberSetting combatReach = addSetting(new NumberSetting("Combat Reach", "Attack reach", 4.5, 3.0, 6.0, 0.1));
    private final NumberSetting blockReach = addSetting(new NumberSetting("Block Reach", "Block interaction reach", 5.5, 4.5, 7.0, 0.1));

    public Reach() {
        super("Reach", "Extends interaction and attack range", Category.COMBAT);
    }

    public double getCombatReach() {
        return isEnabled() ? combatReach.get() : 3.0;
    }

    public double getBlockReach() {
        return isEnabled() ? blockReach.get() : 4.5;
    }
}
