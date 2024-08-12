/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class LineBreak implements InlineObject {
    LineBreak() {
        
    }
    
    public boolean isVisible() {
        return false;
    }
    
    public float getWidth() {
        return 0.0f;
    }
    
    public float getHeight() {
        return 0.0f;
    }
    
    public boolean forceWordBreak() {
        return true;
    }
}
