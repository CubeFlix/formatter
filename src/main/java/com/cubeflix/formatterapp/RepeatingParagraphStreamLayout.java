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
public class RepeatingParagraphStreamLayout implements ParagraphStreamLayout {
    private List<List<ParagraphLayout>> areas;
    private int index = 0;
    private int pageIndex = 0;
    
    RepeatingParagraphStreamLayout(List<List<ParagraphLayout>> areas) {
        this.areas = areas;
    }
    
    public ParagraphLayout requestNextArea() {
        while (index >= 
                this.areas.get(pageIndex % this.areas.size()).size()) {
            pageIndex++;
            index = 0;
        }
        ParagraphLayout area = this.areas.get(
                pageIndex % this.areas.size()
        ).get(index);
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
