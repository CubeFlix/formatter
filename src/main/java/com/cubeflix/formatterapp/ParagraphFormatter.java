/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a formatted layout tree from a given paragraph and layout. 
 * @author Kevin Chen
 */
public class ParagraphFormatter {
    private Paragraph paragraph;
    private ParagraphLayout layout;
    private List<Word> words;
    private List<LineFormatter> lineFormatters;
    
    ParagraphFormatter(Paragraph paragraph, ParagraphLayout layout) {
        this.paragraph = paragraph;
        this.layout = layout;
    }
    
    private void splitIntoWords() {
        WordSplitter splitter = new WordSplitter(this.paragraph);
        splitter.split();
        this.words = splitter.getWords();
    }
    
        
    private void calculateWordSizes() throws IOException {
        for (Word word : this.words) {
            word.calculateWordSize();
        }
    }
    
    public LineFormatter fitWordsOnLine() {
        float widthUsed = 0;
        List<Word> words = new ArrayList<>();
        while (widthUsed < this.layout.getWidth() &&
                !this.words.isEmpty()) {
            Word word = this.words.removeFirst();
            words.add(word);
            widthUsed += word.getCalculatedSize().width;
        }
        
        LineFormatter formatter = new LineFormatter(words, 
            this.paragraph.style);
        return formatter;
    }
    
    public void fitLines() {
        this.lineFormatters = new ArrayList<>();
        float heightUsed = 0;
        while (heightUsed < this.layout.getHeight() && 
                !this.words.isEmpty()) {
            if (heightUsed != 0) {
                // If there's already lines in this paragraph, add the leading.
                heightUsed += this.paragraph.style.leading;
            }
            LineFormatter formatter = this.fitWordsOnLine();
            float height = formatter.getTotalHeight();

            Coordinate start = new Coordinate(this.layout.getStart().x, 
                    this.layout.getStart().y + heightUsed);
            Coordinate end = new Coordinate(this.layout.getEnd().x, 
                    this.layout.getStart().y + heightUsed + height);
            ParagraphLayout lineLayout = new ParagraphLayout(start, end);
            formatter.setLineLayout(lineLayout);
            
            this.lineFormatters.add(formatter);
            heightUsed += height;
        }
    }
}
