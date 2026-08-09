package com.ninjacheat.client.hud;

import java.util.ArrayList;
import java.util.List;

/**
 * HUD エレメントを管理するマネージャ。
 */
public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();

    public void register(HudElement element) {
        elements.add(element);
        elements.sort((a, b) -> Integer.compare(a.priority(), b.priority()));
    }

    public List<HudElement> all() {
        return elements;
    }

    public HudElement get(String name) {
        for (HudElement e : elements) if (e.getName().equalsIgnoreCase(name)) return e;
        return null;
    }

    public void registerDefaults() {
        register(new WatermarkHud());
        register(new ArrayListHud());
        register(new CoordinatesHud());
        register(new FpsHud());
        register(new PingHud());
        register(new SessionHud());
    }

    /** 全有効エレメントを描画 */
    public void render(float tickDelta) {
        int x = 4;
        int y = 4;
        for (HudElement e : elements) {
            if (!e.isEnabled()) continue;
            int h = e.render(x, y, tickDelta);
            y += h + 2;
        }
    }
}
