/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;

/**
 *
 * @author Kevin Chen
 */
public class RegionBreak implements Drawable, ParagraphStreamObject {
    public float getWidth() throws IOException {
        return 0.0f;
    }
    
    public float getHeight() throws IOException {
        return 0.0f;
    }
    
    public boolean isVisible() {
        return true;
    }
}
