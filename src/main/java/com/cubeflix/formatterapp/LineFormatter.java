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
    private List<Word> words;
    private ParagraphStyle style;
    private ParagraphLayout lineLayout;
    
    public List<Coordinate> calculatedOffsets = new ArrayList<>();

    LineFormatter(List<Word> words, 
            ParagraphStyle style) {
        this.words = words;
        this.style = style;
    }
    
    LineFormatter(List<Word> words, 
            ParagraphStyle style,
            ParagraphLayout lineLayout) {
        this.words = words;
        this.style = style;
        this.lineLayout = lineLayout;
    }
    
    public float getTotalWidth() {
        float width = 0;
        for (Word word : this.words) {
            width += word.getCalculatedSize().width;
        }
        return width;
    }
    
    public float getTotalHeight() {
        float height = 0;
        for (Word word : this.words) {
            height = Math.max(height, word.getCalculatedSize().height);
        }
        return height;
    }

    public void setLineLayout(ParagraphLayout lineLayout) {
        this.lineLayout = lineLayout;
    }
}
