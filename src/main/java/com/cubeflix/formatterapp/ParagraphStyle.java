/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class ParagraphStyle {
    public float leading;
    public ParagraphAlignment alignment;
    
    ParagraphStyle(float leading, float spaceWidth, ParagraphAlignment alignment) {
        this.leading = leading;
        this.alignment = alignment;
    }
}
