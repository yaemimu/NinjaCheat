package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.client.option.SimpleOption;
import org.lwjgl.glfw.GLFW;

/**
 * Zoom — OptiFine 風のズーム機能。
 * キーを押している間だけ FOV を狭くし、離すと元に戻す。
 * CheatUtils の zoom / FDPClient の Zoom 統合。
 */
public class Zoom extends Module {

    private final NumberSetting factor = addSetting(new NumberSetting("Factor", "Zoom multiplier (lower = more zoom)", 0.3, 0.05, 1.0, 0.05));
    private final BoolSetting smooth = addSetting(new BoolSetting("Smooth", "Smoothly transition FOV", true));

    private double originalFov = -1;
    private double current = 1.0;

    public Zoom() {
        super("Zoom", "Zoom in like OptiFine (hold key)", Category.RENDER);
        setKey(GLFW.GLFW_KEY_C);
    }

    @Override
    protected void onEnable() {
        originalFov = -1;
        current = 1.0;
    }

    @Override
    protected void onDisable() {
        restoreFov();
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        // ズーム中は FOV を factor に近づける
        double target = factor.get();
        if (smooth.get()) {
            current += (target - current) * 0.2;
        } else {
            current = target;
        }
        applyFov(current);
    }

    private void applyFov(double f) {
        try {
            SimpleOption<Integer> fovOpt = mc.options.getFov();
            if (originalFov < 0) originalFov = fovOpt.getValue();
            int zoomed = (int) Math.round(originalFov * f);
            // クライアント側の FOV オプションを一時的に上書き (リセット時に戻す)
            // ※ SimpleOption#setValue はバニラの制約で値域にクランプされるため、
            //   教育目的の簡易実装として直接オプションへ反映。
            fovOpt.setValue(Math.max(1, zoomed));
        } catch (Throwable ignored) {}
    }

    private void restoreFov() {
        if (originalFov > 0 && mc.options != null) {
            try {
                mc.options.getFov().setValue((int) Math.round(originalFov));
            } catch (Throwable ignored) {}
        }
        originalFov = -1;
        current = 1.0;
    }
}
