/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kevin Chen
 */
public class LineFormatter {
    private List<Word> words;

    public List<Word> getWords() {
        return words;
    }
    private ParagraphStyle style;

    public void setStyle(ParagraphStyle style) {
        this.style = style;
    }

    public ParagraphStyle getStyle() {
        return style;
    }
    
    private ParagraphLayout lineLayout;
    
    private LineFormatting formatting;
    private List<Coordinate> calculatedOffsets = new ArrayList<>();

    public LineFormatting getFormatting() {
        return formatting;
    }

    public List<Coordinate> getCalculatedOffsets() {
        return calculatedOffsets;
    }

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
    
    public void format() throws IOException {
        // We trim spaces on the line so that they aren't rendered and aren't
        // accounted for in the width calculation.
        boolean spaceAfterLine = this.words.getLast().spaceAfter;
        this.trimSpaces();
        this.recalculateSizes();
        this.calculateSpacing();
        this.words.getLast().spaceAfter = spaceAfterLine;
        List<InlineObject> objects = this.joinAdjacentRunsInLine();
        this.formatting.objects = objects;
    }
    
    /**
     * Trim spaces at the left and right of the line.
     */
    private void trimSpaces() {
        this.words.getFirst().spaceBefore = false;
        this.words.getLast().spaceAfter = false;
    }
    
    /**
     * Recalculate the sizes of each of the words.
     */
    private void recalculateSizes() throws IOException {
        for (Word word : this.words) {
            word.calculateWordSize();
        }
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
    
    private void calculateSpacing() {
        switch (this.style.alignment) {
            case ParagraphAlignment.LEFT:
                this.calculateLeftAlign();
                break;
            case ParagraphAlignment.CENTER:
                this.calculateCenterAlign();
                break;
            case ParagraphAlignment.RIGHT:
                this.calculateRightAlign();
                break;
            case ParagraphAlignment.JUSTIFY:
                this.calculateJustifyAlign();
                break;
        }
    }
    
    private void calculateLeftAlign() {
        Coordinate start = this.lineLayout.getStart();
        this.formatting = new LineFormatting(start);
    }
    
    private void calculateCenterAlign() {
        Coordinate start = this.lineLayout.getStart();
        start.x += (this.lineLayout.getWidth() - this.getTotalWidth()) / 2.0;
        this.formatting = new LineFormatting(start);
    }
    
    private void calculateRightAlign() {
        Coordinate start = this.lineLayout.getStart();
        start.x += this.lineLayout.getWidth() - this.getTotalWidth();
        this.formatting = new LineFormatting(start);
    }
    
    private void calculateJustifyAlign() {
        // Count the number of spaces.
        int numSpaces = 0;
        for (Word word : this.words) {
            if (word.spaceBefore) {
                numSpaces++;
            }
            if (word.spaceAfter) {
                numSpaces++;
            }
        }
        
        Coordinate start = this.lineLayout.getStart();
        float spacing = 0;
        if (numSpaces > 0) {
            spacing = (this.lineLayout.getWidth()- 
                    this.getTotalWidth()) / (float)(numSpaces);
        }
        this.formatting = new LineFormatting(start, spacing);
    }
    
    private List<InlineObject> joinAdjacentRunsInLine() {
        List<InlineObject> objects = new ArrayList<>();
        TextRun currentRun = null;
        for (Word word : this.words) {
            if (currentRun != null && word.spaceBefore) {
                currentRun.text = " " + currentRun.text;
            }
            for (InlineObject object : word.objects) {
                if (!object.getClass().equals(TextRun.class)) {
                    if (!object.isVisible()) {
                        continue;
                    }
                    if (currentRun != null) {
                        objects.add((InlineObject)currentRun);
                        currentRun = null;
                    }
                    objects.add(object);
                    continue;
                }
                TextRun run = (TextRun)object;
                if (currentRun == null) {
                    currentRun = new TextRun(run.text, run.style);
                    if (word.spaceBefore) {
                        currentRun.text = " " + currentRun.text;
                    }
                    continue;
                } 
                if (currentRun.style.equals(run.style)) {
                    currentRun.text += run.text;
                } else {
                    objects.add((TextRun)currentRun);
                    currentRun = new TextRun(run.text, run.style);
                }
            }
            if (word.spaceAfter) {
                currentRun.text += " ";
            }
        }
        if (currentRun != null) {
            objects.add((InlineObject)currentRun);
        }
        return objects;
    }
}
