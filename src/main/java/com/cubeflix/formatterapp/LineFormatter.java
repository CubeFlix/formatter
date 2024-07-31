/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kevin Chen
 */
public class LineFormatter {
    private List<List<TextRun>> words;
    private List<WordSize> calculatedWordSizes;
    private ParagraphStyle style;
    private ParagraphLayout lineLayout;
    
    public List<Coordinate> calculatedOffsets = new ArrayList<>();

    LineFormatter(List<List<TextRun>> words, 
            List<WordSize> calculatedWordSizes,
            ParagraphStyle style) {
        this.words = words;
        this.calculatedWordSizes = calculatedWordSizes;
        this.style = style;
    }
    
    LineFormatter(List<List<TextRun>> words, 
            List<WordSize> calculatedWordSizes,
            ParagraphStyle style,
            ParagraphLayout lineLayout) {
        this.words = words;
        this.calculatedWordSizes = calculatedWordSizes;
        this.style = style;
        this.lineLayout = lineLayout;
    }
    
    public float getTotalWidth() {
        float width = 0;
        for (WordSize word : this.calculatedWordSizes) {
            width += word.width + this.style.spaceWidth;
        }
        return width;
    }
    
    public float getTotalHeight() {
        float height = 0;
        for (WordSize word : this.calculatedWordSizes) {
            height = Math.max(height, word.height);
        }
        return height;
    }

    public void setLineLayout(ParagraphLayout lineLayout) {
        this.lineLayout = lineLayout;
    }
}
