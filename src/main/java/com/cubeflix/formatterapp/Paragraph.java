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
public class Paragraph implements ParagraphStreamObject {
    public InlineObject[] objects;
    public ParagraphStyle style;
    private List<TextRun> runs;
    
    Paragraph() {
        
    }
    
    Paragraph(InlineObject[] objects) {
        this.objects = objects;
        this.populateRunsFromObjects();
    }
    
    Paragraph(InlineObject[] objects, ParagraphStyle style) {
        this.objects = objects;
        this.style = style;
        this.populateRunsFromObjects();
    }
    
    void setStyle(ParagraphStyle style) {
        this.style = style;
    }
    
    void populateRunsFromObjects() {
        this.runs = new ArrayList<>();
        for (int i = 0; i < this.objects.length; i++) {
            if (this.objects[i].getClass().equals(TextRun.class)) {
                runs.add((TextRun)this.objects[i]);
            }
        }
    }

    public List<TextRun> getRuns() {
        return runs;
    }
    
    public Paragraph copy() {
        InlineObject[] objects = new InlineObject[this.objects.length];
        for (int i = 0; i < this.objects.length; i++) {
            objects[i] = this.objects[i];
        }
        Paragraph clone = new Paragraph(objects, this.style);
        clone.populateRunsFromObjects();
        return clone;
    }
}
