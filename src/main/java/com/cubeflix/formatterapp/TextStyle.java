/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Kevin Chen
 */
public class TextStyle {
    public PDFont family;
    public float size;
    public float spacing;
    
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
