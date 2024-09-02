/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;

/**
 *
 * @author Kevin Chen
 */
public class TextRun implements InlineObject {
    public String text;
    public TextStyle style;

    TextRun(String text) {
        this.text = text;
    }
    
    TextRun(String text, TextStyle style) {
        this.text = text;
        this.style = style;
    }
    
    void setStyle(TextStyle style) {
        this.style = style;
    }
    
    public float getWidth() throws IOException {
        return this.style.family.getStringWidth(this.text) * 
                    this.style.size + 
                    this.text.length() * this.style.characterSpacing;
    }
    
    public float getHeight() throws IOException {
        float height = this.style.family.getFontDescriptor().getAscent() - 
                            this.style.family.getFontDescriptor().getDescent();
        height *= this.style.size;
        return height;
    }
    
    public boolean isVisible() {
        return true;
    }
    
    public boolean forceWordBreak() {
        return false;
    }
    
    public TextRun copy() {
        return new TextRun(
                this.text,
                this.style
        );
    }
}
