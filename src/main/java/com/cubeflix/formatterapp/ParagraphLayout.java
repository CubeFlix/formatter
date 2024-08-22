/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class ParagraphLayout {
    private Coordinate start;

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }
    private Coordinate end;
        
    public float getWidth() {
        return this.end.x - this.start.x;
    }
    
    public float getHeight() {
        return this.end.y - this.start.y;
    }
    
    ParagraphLayout(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }
    
    public ParagraphLayout copy() {
        return new ParagraphLayout(
                new Coordinate(this.start.x, this.start.y),
                new Coordinate(this.end.x, this.end.y)
        );
    }
}
