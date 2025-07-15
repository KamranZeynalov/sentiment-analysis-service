package com.company.analyze.model;

public class ConfidenceScore {
    private float positive;
    private float negative;
    private float neutral;
    private float mixed;

    public ConfidenceScore(float positive, float negative, float neutral, float mixed) {
        this.positive = positive;
        this.negative = negative;
        this.neutral = neutral;
        this.mixed = mixed;
    }

    public float getPositive() { return positive; }
    public float getNegative() { return negative; }
    public float getNeutral() { return neutral; }
    public float getMixed() { return mixed; }
}
