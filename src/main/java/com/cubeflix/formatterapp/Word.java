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
public class Word {
    public List<TextRun> runs;
    public List<InlineObject> objects;
    public boolean spaceBefore;
    public boolean spaceAfter;
    private WordSize calculatedSize;
    private boolean didHyphenate = false;

    public boolean isDidHyphenate() {
        return didHyphenate;
    }

    public void setDidHyphenate(boolean didHyphenate) {
        this.didHyphenate = didHyphenate;
    }

    public WordSize getCalculatedSize() {
        return calculatedSize;
    }
    
    public float getHeight() throws IOException {
        return this.calculatedSize.height;
    }
    
    public float getWidth() throws IOException {
        return this.calculatedSize.width;
    }
    
    public boolean isVisible() {
        return true;
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
    
    Word(List<InlineObject> objects, 
            boolean spaceBefore, 
            boolean spaceAfter,
            WordSize calculatedSize) {
        this.objects = objects;
        this.spaceBefore = spaceBefore;
        this.spaceAfter = spaceAfter;
        this.calculatedSize = calculatedSize;
        
        this.populateRunsFromObjects();
    }
    
    public void populateObjectsFromRuns() {
        this.objects = new ArrayList<>();
        for (TextRun run : this.runs) {
            this.objects.add((InlineObject)run);
        }
    }
    
    public void populateRunsFromObjects() {
        this.runs = new ArrayList<>();
        for (int i = 0; i < this.objects.size(); i++) {
            if (this.objects.get(i).getClass().equals(TextRun.class)) {
                runs.add((TextRun)this.objects.get(i));
            }
        }
    }
    
    private float calculateWordWidth() throws IOException {
        float width = 0;
        if (this.spaceBefore) {
            width += this.runs.getFirst().style.spacing * 
                    this.runs.getFirst().style.size;
            width += this.runs.getFirst().style.characterSpacing;
        }
        for (InlineObject object : this.objects) {
            width += object.getWidth();
        }
        if (this.spaceAfter) {
            width += this.runs.getLast().style.spacing * 
                    this.runs.getLast().style.size;
            width += this.runs.getLast().style.characterSpacing;
        }
        
        return width;
    }
    
    private float calculateWordHeight() throws IOException {
        float height = 0;
        for (InlineObject object : this.objects) {
            float objectHeight = object.getHeight();
            height = Math.max(height, objectHeight);
        }
        return height;
    }
    
    public void calculateWordSize() throws IOException {
        this.calculatedSize = new WordSize(this.calculateWordHeight(),
                this.calculateWordWidth());
    }
    
    public Word copy() throws IOException {
        Word clone = new Word(new ArrayList<>(this.objects), 
            this.spaceBefore, 
            this.spaceAfter,
            this.calculatedSize);
        return clone;
    }
}
