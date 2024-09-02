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
    public List<InlineObject> objects;
    
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
        for (InlineObject object : this.objects) {
            width += object.getWidth();
        }
        return width;
    }
    
    public float getHeight() throws IOException {
        float height = 0.0f;
        for (InlineObject object : this.objects) {
            height = Math.max(height, object.getHeight());
        }
        return height;
    }
    
    public Coordinate getStart() {
        return this.start;
    }
    
    public float calculateBaseline() throws IOException {
        float maxAscent = 0.0f;
        for (InlineObject object : this.objects) {
            if (object.getClass().equals(TextRun.class)) {
                TextRun run = (TextRun)object;
                maxAscent = Math.max(maxAscent, 
                        run.style.family.getFontDescriptor().getAscent() * 
                                run.style.size);
            } else {
                maxAscent = Math.max(maxAscent, object.getHeight());
            }
        }
        
        float baseline = this.start.y + maxAscent;
        return baseline;
    }
}
