/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

/**
 * The layout for the paragraph stream. The layout is defined by a list of 
 * lists of areas, each list defining one page.
 * @author Kevin Chen
 */
public interface ParagraphStreamLayout {
    public ParagraphLayout requestNextArea();
    public int getPageIndex();
}
