package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;

/**
 * NoRender — 不要な視覚エフェクトを非表示にする。
 * CheatUtils の noOverlay / FDPClient の NoRender 統合。
 * MixinBackgroundRenderer が isNoFog() を参照して霧を無効化する。
 */
public class NoRender extends Module {

    private final BoolSetting noFog = addSetting(new BoolSetting("No Fog", "Remove fog rendering", true));
    private final BoolSetting noHurtCam = addSetting(new BoolSetting("No Hurt Cam", "Remove hurt-camera shake", true));
    private final BoolSetting noFire = addSetting(new BoolSetting("No Fire", "Hide first-person fire overlay", false));
    private final BoolSetting noPumpkin = addSetting(new BoolSetting("No Pumpkin", "Hide pumpkin blur overlay", true));
    private final BoolSetting noWater = addSetting(new BoolSetting("No Water Overlay", "Hide underwater overlay", false));
    private final BoolSetting noVignette = addSetting(new BoolSetting("No Vignette", "Remove dark screen edges", false));
    private final BoolSetting noBossBar = addSetting(new BoolSetting("No Boss Bar", "Hide boss bars", false));
    private final BoolSetting noSky = addSetting(new BoolSetting("No Sky", "Hide sky rendering", false));

    public NoRender() {
        super("NoRender", "Hides visual effects like fog, hurt cam, overlays", Category.RENDER);
    }

    /** MixinBackgroundRenderer が参照: 霧を無効化するか */
    public boolean isNoFog() {
        return noFog.get();
    }

    public boolean isNoHurtCam() {
        return noHurtCam.get();
    }

    public boolean isNoFire() {
        return noFire.get();
    }

    public boolean isNoPumpkin() {
        return noPumpkin.get();
    }

    public boolean isNoWater() {
        return noWater.get();
    }

    public boolean isNoVignette() {
        return noVignette.get();
    }

    public boolean isNoBossBar() {
        return noBossBar.get();
    }

    public boolean isNoSky() {
        return noSky.get();
    }
}
