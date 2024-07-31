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
}
