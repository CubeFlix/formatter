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
    private List<LineFormatting> formatting;
    private List<Word> unfitWords;

    public List<Word> getUnfitWords() {
        return unfitWords;
    }

    public List<LineFormatting> getFormatting() {
        return formatting;
    }
    
    ParagraphFormatter(Paragraph paragraph, ParagraphLayout layout) {
        this.paragraph = paragraph;
        this.layout = layout;
    }
    
    public void format() throws IOException {
        this.splitIntoWords();
        this.calculateWordSizes();
        this.fitLines();
        this.formatLines();
        
        this.formatting = new ArrayList<>();
        for (LineFormatter formatter : this.lineFormatters) {
            this.formatting.add(formatter.getFormatting());
        }
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
    
    private LineFormatter fitWordsOnLine() throws IOException {
        float widthUsed = 0;
        List<Word> words = new ArrayList<>();
        while (widthUsed <= this.layout.getWidth() &&
                !this.words.isEmpty()) {
            Word word = this.words.removeFirst();
            words.add(word);
            widthUsed += word.getCalculatedSize().width;
        }
        
        // If we went over the width limit, remove the last word and add it 
        // back to the word list.
        if (widthUsed > this.layout.getWidth()) {
            // First, check if removing the space at the end will help.
            Word lastWord = words.removeLast();
            boolean spaceAfter = lastWord.spaceAfter;
            lastWord.spaceAfter = false;
            widthUsed -= lastWord.getCalculatedSize().width;            
            lastWord.calculateWordSize();
            widthUsed += lastWord.getCalculatedSize().width;
            lastWord.spaceAfter = 
                    spaceAfter; // Remember to restore spaceAfter.
            if (widthUsed <= this.layout.getWidth()) {
                words.add(lastWord);
            } else {
                this.words.addFirst(words.removeLast());
            }
        }
        if (words.isEmpty() && !this.words.isEmpty()) {
            // No words could be fit on the line, but there's still words left.
            words.add(this.words.removeFirst());
        }
        
        if (this.paragraph.style.hyphenation.hyphenate && 
                !this.words.isEmpty()) {
            this.hyphenate(widthUsed, words);
        }
        
        LineFormatter formatter = new LineFormatter(words, 
            this.paragraph.style);
        return formatter;
    }
    
    private void hyphenate(float widthUsed, List<Word> words) 
            throws IOException {
        if (widthUsed / this.layout.getWidth() >= 
                this.paragraph.style.hyphenation.widthRatioThreshold) {
            return;
        }
        
        Word word = this.words.removeFirst();
        
        // Try soft hyphens. Preserve the space before. There will be no space
        // after.
        Word beforeHyphen = new Word(new ArrayList<>(), 
                word.spaceBefore, 
                false);
        Word lastFitting = null;
        int i = 0;
        while (widthUsed < this.layout.getWidth() && i < word.objects.size()) {
            InlineObject object = word.objects.get(i);
            if (object.getClass().equals(SoftHyphen.class)) {
                // If the soft hyphen falls after a TextRun, add a hyphen to
                // the end and test the width.
                if (beforeHyphen.objects.getLast().getClass().
                        equals(TextRun.class)) {
                    ((TextRun)beforeHyphen.objects.getLast()).text += "-";
                    beforeHyphen.populateRunsFromObjects();
                    widthUsed -= beforeHyphen.getWidth();
                    beforeHyphen.calculateWordSize();
                    widthUsed += beforeHyphen.getWidth();
                    
                    // If the word fits, set lastFitting.
                    if (widthUsed < this.layout.getWidth()) {
                        lastFitting = beforeHyphen.copy();
                    } else {
                        break;
                    }
                }
            }
            beforeHyphen.objects.add(word.objects.get(i));
            i++;
        }
        
        // Okay. Okay, okay, okay. Here we go. Ha!
        if (lastFitting != null) {
            // Check if the the new width is above the threshold.
            widthUsed -= beforeHyphen.getWidth();
            widthUsed += lastFitting.getWidth();
            if (widthUsed / this.layout.getWidth() >= 
                this.paragraph.style.hyphenation.widthRatioThreshold) {
                // Put the new word on the line and return the other half of 
                // the word.
                words.add(lastFitting);
                for (int j = 0; j < lastFitting.objects.size(); j++) {
                    word.objects.removeFirst();
                }
                this.words.addFirst(word);
                return;
            } else {
                widthUsed -= lastFitting.getWidth();
                this.words.addFirst(word);
            }
        }
        this.hyphenationFallback(widthUsed, words);
    }
    
    private void hyphenationFallback(float widthUsed, List<Word> words) 
            throws IOException {
        switch (this.paragraph.style.hyphenation.fallback) {
            case HyphenationFallback.HYPHENATE_ANYWHERE -> {
                this.hyphenateAnywhere(widthUsed, words);
            }
            case HyphenationFallback.DO_NOT_HYPHENATE -> {
                return;
            }
        }
    }
    
    private void hyphenateAnywhere(float widthUsed, List<Word> words) 
            throws IOException {
        Word word = this.words.removeFirst();
        
        Word fitting = new Word(new ArrayList<>(), word.spaceBefore, false);
        while (widthUsed < this.layout.getWidth() && word.objects.size() < 0) {
            InlineObject object = word.objects.getFirst();
            if (object.getClass().equals(TextRun.class)) {
                TextRun run = (TextRun)object;
                if (run.text.length() < 1) {
                    continue;
                }
                TextRun splitRun = new TextRun("");
                int j = 1;
                float originalWidthUsed = widthUsed;
                while (widthUsed < this.layout.getWidth() && 
                        j < run.text.length()) {
                    splitRun.text = run.text.substring(0, j) + "-";
                    float runWidth = splitRun.getWidth();
                    if (originalWidthUsed + runWidth <= 
                            this.layout.getWidth()) {
                        // Add the run width.
                        widthUsed = originalWidthUsed + runWidth;
                        j++;
                    } else {
                        // Cut off the used portion of the original run.
                        run.text = run.text.substring(j, run.text.length());
                        break;
                    }
                }
                fitting.objects.add(object);
                if (j == run.text.length()) {
                    // All the text is used up.
                    word.objects.removeFirst();
                }
            } else {
                float width = object.getWidth();
                if (widthUsed + width > this.layout.getWidth()) {
                    break;
                }
                fitting.objects.add(object);
                word.objects.removeFirst();
            }
        }
        words.add(fitting);
    }
    
    private void fitLines() throws IOException {
        this.lineFormatters = new ArrayList<>();
        this.unfitWords = new ArrayList<>();
        float heightUsed = 0;
        while (heightUsed < this.layout.getHeight() && 
                !this.words.isEmpty()) {
            LineFormatter formatter = this.fitWordsOnLine();
            float height = formatter.getTotalHeight();

            Coordinate start = new Coordinate(this.layout.getStart().x, 
                    this.layout.getStart().y + heightUsed);
            Coordinate end = new Coordinate(this.layout.getEnd().x, 
                    this.layout.getStart().y + heightUsed + height);
            ParagraphLayout lineLayout = new ParagraphLayout(start, end);
            formatter.setLineLayout(lineLayout);
            heightUsed += this.paragraph.style.leading;
            this.lineFormatters.add(formatter);
        }
        
        if (heightUsed > this.layout.getHeight()) {
            this.unfitWords.addAll(this.lineFormatters.removeLast().getWords());
        }
        this.unfitWords.addAll(this.words);
    }
    
    private void formatLines() throws IOException {
        for (LineFormatter formatter : this.lineFormatters) {
            formatter.format();
        }
    }
}
