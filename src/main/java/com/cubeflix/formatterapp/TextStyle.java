/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.awt.Color;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Kevin Chen
 */
public class TextStyle {
    public PDFont family;
    public float size;
    public float spacing;
    public Color color = Color.BLACK;
    public float characterSpacing = 0.0f;
    public TextDecoration textDecoration = null;
    
    TextStyle(PDFont family, float size) {
        this.family = family;
        this.size = size;
        this.spacing = family.getSpaceWidth();
    }
    
    TextStyle(PDFont family, float size, float spacing) {
        this.family = family;
        this.size = size;
        this.spacing = spacing;
    }
    
    TextStyle(PDFont family, float size, float spacing, Color color) {
        this.family = family;
        this.size = size;
        this.spacing = spacing;
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TextStyle other = (TextStyle) obj;
        if (Float.floatToIntBits(this.size) != Float.floatToIntBits(other.size)) {
            return false;
        }
        if (Float.floatToIntBits(this.spacing) != Float.floatToIntBits(other.spacing)) {
            return false;
        }
        return this.family == other.family;
    }
}
