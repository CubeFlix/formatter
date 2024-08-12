/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class InlineSpacer implements InlineObject {
    public float width = 0.0f;
    public float height = 0.0f;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    InlineSpacer(float width) {
        this.width = width;
    }
    
    InlineSpacer(float width, float height) {
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
