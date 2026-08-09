package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * HitBox — 対象エンティティの当たり判定を拡大 (クライアント側のみ)。
 * FDPClient の HitBox と CheatUtils の HitboxSize を統合。
 */
public class HitBox extends Module {

    private final NumberSetting expand = addSetting(new NumberSetting("Expand", "Box expansion in blocks", 0.2, 0.0, 2.0, 0.05));

    public HitBox() {
        super("HitBox", "Enlarges entity hitboxes for easier aiming", Category.COMBAT);
    }
}
