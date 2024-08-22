/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.List;

/**
 * A stream of multiple subsequent objects.
 * @author Kevin Chen
 */
public class ParagraphStream {
    public List<ParagraphStreamObject> objects;
    public ParagraphStreamStyle style;
    public ParagraphStreamLayout layout;
    
    ParagraphStream(List<ParagraphStreamObject> objects, 
            ParagraphStreamStyle style,
            ParagraphStreamLayout layout) {
        this.objects = objects;
        this.style = style;
        this.layout = layout;
    }
}
