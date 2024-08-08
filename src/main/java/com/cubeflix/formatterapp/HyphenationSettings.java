/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class HyphenationSettings {
    public boolean hyphenate;
    public float widthRatioThreshold = 0.75f;
    public HyphenationFallback fallback = 
            HyphenationFallback.HYPHENATE_ANYWHERE;
    
    HyphenationSettings() {
        this.hyphenate = false;
    }
    
    HyphenationSettings(boolean hyphenate) {
        this.hyphenate = hyphenate;
    }
    
    HyphenationSettings(boolean hyphenate, float widthRatioThreshold) {
        this.hyphenate = hyphenate;
        this.widthRatioThreshold = widthRatioThreshold;
    }
    HyphenationSettings(boolean hyphenate, 
            float widthRatioThreshold,
            HyphenationFallback fallback) {
        this.hyphenate = hyphenate;
        this.widthRatioThreshold = widthRatioThreshold;
        this.fallback = fallback;
    }
}
