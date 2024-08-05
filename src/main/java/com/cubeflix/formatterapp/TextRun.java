/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class TextRun implements InlineObject {
    public String text;
    public TextStyle style;

    TextRun(String text) {
        this.text = text;
    }
    
    TextRun(String text, TextStyle style) {
        this.text = text;
        this.style = style;
    }
    
    void setStyle(TextStyle style) {
        this.style = style;
    }
}
