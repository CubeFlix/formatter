/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class Spacer implements InlineObject, ParagraphStreamObject, Drawable {
    public float width = 0.0f;
    public float height = 0.0f;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    Spacer(float width) {
        this.width = width;
    }
    
    Spacer(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    public boolean isVisible() {
        return true;
    }
    
    public boolean forceWordBreak() {
        return false;
    }
}
