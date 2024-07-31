/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.List;

/**
 *
 * @author Kevin Chen
 */
public class LineFormatting {
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
}
