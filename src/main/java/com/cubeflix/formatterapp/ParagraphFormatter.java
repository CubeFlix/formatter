/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Generates a formatted layout tree from a given paragraph and layout. 
 * @author Kevin Chen
 */
public class ParagraphFormatter {
    private Paragraph paragraph;
    private ParagraphLayout layout;
    private List<List<TextRun>> words;
    private List<WordSize> calculatedWordSizes;
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
    
    private static float calculateWordWidth(List<TextRun> word) throws IOException {
        float width = 0;
        for (TextRun run : word) {
            width += run.style.family.getStringWidth(run.text) * 
                    run.style.size;
        }
        return width;
    }
    
    private static float calculateWordHeight(List<TextRun> word) {
        float height = 0;
        for (TextRun run : word) {
            height = Math.max(height, 
                    run.style.family.getFontDescriptor().getAscent() + 
                            run.style.family.getFontDescriptor().getDescent());
        }
        return height;
    }
    
    private void calculateWordSizes() throws IOException {
        this.calculatedWordSizes = new ArrayList<>();
        for (List<TextRun> word : this.words) {
            float height = this.calculateWordHeight(word);
            float width = this.calculateWordWidth(word);
            this.calculatedWordSizes.add(new WordSize(height, width));
        }
    }
    
    public LineFormatter fitWordsOnLine() {
        float widthUsed = 0;
        List<List<TextRun>> words = new ArrayList<>();
        List<WordSize> calculatedWordSizes = new ArrayList<>();
        while (widthUsed < this.layout.getWidth() &&
                !this.words.isEmpty()) {
            if (widthUsed != 0) {
                // If there's already words in this line, add the width for
                // spaces.
                widthUsed += this.paragraph.style.spaceWidth;
            }
            words.add(this.words.removeFirst());
            WordSize wordSize = this.calculatedWordSizes.removeFirst();
            calculatedWordSizes.add(wordSize);
            widthUsed += wordSize.width;
        }
        
        LineFormatter formatter = new LineFormatter(words, 
            calculatedWordSizes,
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
