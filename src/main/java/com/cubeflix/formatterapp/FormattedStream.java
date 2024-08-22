/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.List;

/**
 * The output formatting data. Contains lists of drawable objects, one for each 
 * subsequent page.
 * @author Kevin Chen
 */
public class FormattedStream {
    public List<List<Formatted>> stream;
    
    FormattedStream(List<List<Formatted>> formatting) {
        this.stream = formatting;
    }
}
