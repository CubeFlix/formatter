/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kevin Chen
 */
public class FormattedLine {
    public List<List<TextRun>> words = new ArrayList<>();
    public List<WordSize> calculatedWordSizes = new ArrayList<>();
    public List<Coordinate> calculatedOffsets = new ArrayList<>();
    public float totalHeight;
    public float totalWidth;
    
    FormattedLine() {
        
    }
}
