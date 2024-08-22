/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Kevin Chen
 */
public class LineFormatting implements Formatted {
    public Coordinate start;
    public boolean overrideSpacing = false;
    public float spacingOverride = 0;
    public List<Word> words;
    
    LineFormatting(Coordinate start) {
        this.start = start;
    }
    
    LineFormatting(Coordinate start, float spacingOverride) {
        this.start = start;
        this.overrideSpacing = true;
        this.spacingOverride = spacingOverride;
    }
    
    public boolean isVisible() {
        return true;
    }
    
    public float getWidth() throws IOException {
        float width = 0.0f;
        for (Word word : this.words) {
            width += word.getWidth();
        }
        return width;
    }
    
    public float getHeight() throws IOException {
        float height = 0.0f;
        for (Word word : this.words) {
            word.calculateWordSize();
            height = Math.max(height, word.getHeight());
        }
        return height;
    }
    
    public Coordinate getStart() {
        return this.start;
    }
}
