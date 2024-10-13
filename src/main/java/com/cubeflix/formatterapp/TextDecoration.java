/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.awt.Color;

/**
 *
 * @author Kevin Chen
 */
public class TextDecoration {
    public TextDecorationType type;
    public LineStyle style;
    public float yOffset = 0.0f;
    
    public TextDecoration(TextDecorationType type, LineStyle style) {
        this.type = type;
        this.style = style;
    }
    
    public TextDecoration(TextDecorationType type, LineStyle style, 
            float yOffset) {
        this.type = type;
        this.style = style;
        this.yOffset = yOffset;
    }
}
