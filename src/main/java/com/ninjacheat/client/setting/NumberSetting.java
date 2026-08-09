package com.ninjacheat.client.setting;

/**
 * 数値設定 (スライダー)。最小・最大・刻みを持つ。
 */
public class NumberSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public NumberSetting(String name, String description, double defaultValue, double min, double max) {
        this(name, description, defaultValue, min, max, 0.01);
    }

    @Override
    public void set(Double value) {
        if (value < min) value = min;
        if (value > max) value = max;
        // step にスナップ
        double snapped = Math.round(value / step) * step;
        super.set(snapped);
    }

    public float getFloat() {
        return value.floatValue();
    }

    public int getInt() {
        return value.intValue();
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    @Override
    public String getType() {
        return "number";
    }
}
