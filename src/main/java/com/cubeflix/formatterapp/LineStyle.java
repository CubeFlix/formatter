/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.awt.Color;

/**
 *
 * @author Kevin Chen
 */
public class LineStyle {
    public LineStrokeType strokeType;
    public Color color;
    public float thickness;
    
    public LineStyle(LineStrokeType strokeType, Color color, float thickness) {
        this.strokeType = strokeType;
        this.color = color;
        this.thickness = thickness;
    }
}
