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
public class Word implements InlineObject {
    public List<TextRun> runs;
    public List<InlineObject> objects;
    public boolean spaceBefore;
    public boolean spaceAfter;
    private WordSize calculatedSize;

    public WordSize getCalculatedSize() {
        return calculatedSize;
    }
    
    Word(List<TextRun> runs) {
        this.runs = runs;
        
        this.populateObjectsFromRuns();
    }
    
    Word(List<TextRun> runs, boolean spaceBefore, boolean spaceAfter) {
        this.runs = runs;
        this.spaceBefore = spaceBefore;
        this.spaceAfter = spaceAfter;
        
        this.populateObjectsFromRuns();
    }
    
    private void populateObjectsFromRuns() {
        this.objects = new ArrayList<>();
        for (TextRun run : this.runs) {
            this.objects.add((InlineObject)run);
        }
    }
    
    private float calculateWordWidth() throws IOException {
        float width = 0;
        if (this.spaceBefore) {
            width += this.runs.getFirst().style.spacing * 
                    this.runs.getFirst().style.size;
        }
        for (TextRun run : this.runs) {
            width += run.style.family.getStringWidth(run.text) * 
                    run.style.size;
        }
        if (this.spaceAfter) {
            width += this.runs.getLast().style.spacing * 
                    this.runs.getLast().style.size;
        }
        
        return width;
    }
    
    private float calculateWordHeight() {
        float height = 0;
        for (TextRun run : this.runs) {
            float runHeight = run.style.family.getFontDescriptor().getAscent() + 
                            run.style.family.getFontDescriptor().getDescent();
            runHeight *= run.style.size;
            height = Math.max(height, runHeight);
        }
        return height;
    }
    
    public void calculateWordSize() throws IOException {
        this.calculatedSize = new WordSize(this.calculateWordHeight(),
                this.calculateWordWidth());
    }
}
