/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class FormattedTextDecoration implements Formatted {
    public TextRun run;
    public Coordinate start;
    public float width;
    
    public FormattedTextDecoration(TextRun run, 
            Coordinate start, 
            float width) {
        this.run = run;
        this.start = start;
        this.width = width;
    }
    
    @Override
    public Coordinate getStart() {
        return start;
    }
}