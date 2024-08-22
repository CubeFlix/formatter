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
public class OverflowParagraph extends Paragraph {
    private List<Word> words;
    
    OverflowParagraph(Paragraph original, List<Word> words) {
        this.style = original.style;
        this.words = words;
    }
    
    public List<Word> getWords() {
        return this.words;
    }

    @Override
    public OverflowParagraph copy() {
        List<Word> words = new ArrayList<>(this.words);
        OverflowParagraph clone = new OverflowParagraph(this, words);
        return clone;
    }
}
