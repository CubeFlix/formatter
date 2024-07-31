/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 *
 * @author Kevin Chen
 */
public class Paragraph {
    public TextRun[] runs;
    public ParagraphStyle style;
    
    Paragraph(TextRun[] runs) {
        this.runs = runs;
    }
    
    Paragraph(TextRun[] runs, ParagraphStyle style) {
        this.runs = runs;
        this.style = style;
    }
    
    void setStyle(ParagraphStyle style) {
        this.style = style;
    }
}
