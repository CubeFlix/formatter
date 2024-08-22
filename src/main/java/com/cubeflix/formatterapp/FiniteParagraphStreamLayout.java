/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.List;

/**
 *
 * @author Kevin Chen
 */
public class FiniteParagraphStreamLayout implements ParagraphStreamLayout {
    private List<List<ParagraphLayout>> areas;
    private int index = 0;
    private int pageIndex = 0;
    
    FiniteParagraphStreamLayout(List<List<ParagraphLayout>> areas) {
        this.areas = areas;
    }
    
    public ParagraphLayout requestNextArea() {
        while (index >= this.areas.get(pageIndex).size() - 1) {
            pageIndex++;
            index = 0;
            
            if (pageIndex >= this.areas.size()) {
                return null;
            }
        }
        ParagraphLayout area = this.areas.get(pageIndex).get(index);
        index++;
        return area;
    }

    public int getIndex() {
        return index;
    }

    public int getPageIndex() {
        return pageIndex;
    }
}
