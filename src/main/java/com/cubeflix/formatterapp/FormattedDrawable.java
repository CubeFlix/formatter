/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class FormattedDrawable implements Formatted {
    public Drawable object;
    public Coordinate start;
    
    FormattedDrawable(Drawable object, Coordinate start) {
        this.object = object;
        this.start = start;
    }
    
    public Coordinate getStart() {
        return this.start;
    }
}
