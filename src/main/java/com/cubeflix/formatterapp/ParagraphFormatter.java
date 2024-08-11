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
            widthUsed -= lastWord.getCalculatedSize().width;
            boolean spaceAfter = lastWord.spaceAfter;
            lastWord.spaceAfter = false;
            lastWord.calculateWordSize();
            float lastWordWidthWithoutSpace = 
                    lastWord.getCalculatedSize().width;
            widthUsed += lastWordWidthWithoutSpace;
            lastWord.spaceAfter = 
                    spaceAfter; // Remember to restore spaceAfter.
            if (widthUsed <= this.layout.getWidth()) {
                words.add(lastWord);
            } else {
                this.words.addFirst(lastWord);
                widthUsed -= lastWordWidthWithoutSpace;
            }
        }
        
        if (this.paragraph.style.hyphenation.hyphenate && 
                !this.words.isEmpty()) {
             this.hyphenate(widthUsed, words);
        }
        
        if (words.isEmpty() && !this.words.isEmpty()) {
            // No words could be fit on the line, but there's still words left.
            words.add(this.words.removeFirst());
        }
        
        LineFormatter formatter = new LineFormatter(words, 
            this.paragraph.style);
        return formatter;
    }
    
    private void addHyphenToEnd(Word word) {
        for (int i = word.objects.size() - 1; i >= 0; i--) {
            if (!word.objects.get(i).isVisible()) {
                continue;
            }
            if (word.objects.get(i).getClass().equals(TextRun.class)) {
                TextRun last = ((TextRun)word.objects.get(i));
                last.text += "-";
            }
            break;
        }
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
        float originalWidthUsed = widthUsed;
        while (widthUsed < this.layout.getWidth() && i < word.objects.size()) {
            InlineObject object = word.objects.get(i);
            if (object.getClass().equals(SoftHyphen.class)) {
                // If the soft hyphen falls after a TextRun, add a hyphen to
                // the end and test the width.
                if (beforeHyphen.objects.getLast().getClass().
                        equals(TextRun.class)) {
                    TextRun last = ((TextRun)beforeHyphen.objects.getLast());
                    String originalLastText = 
                            last.text.substring(0, last.text.length());
                    last.text += "-";
                    beforeHyphen.populateRunsFromObjects();
                    beforeHyphen.calculateWordSize();
                    widthUsed = originalWidthUsed + beforeHyphen.getWidth();
                    
                    // Remove the hyphen we just added (we'll add it back at 
                    // the end).
                    last.text = originalLastText;
                    
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
            widthUsed = originalWidthUsed + lastFitting.getWidth();
            if (widthUsed / this.layout.getWidth() >= 
                this.paragraph.style.hyphenation.widthRatioThreshold) {
                // Put the new word on the line and return the other half of 
                // the word.
                lastFitting.populateRunsFromObjects();
                this.addHyphenToEnd(lastFitting);
                words.add(lastFitting);
                for (int j = 0; j < lastFitting.objects.size(); j++) {
                    word.objects.removeFirst();
                }
                this.words.addFirst(word);
                return;
            }
        }
        widthUsed = originalWidthUsed;
        this.words.addFirst(word);
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
        if (this.words.isEmpty()) {
            return;
        }
        
        // Try to fit objects on the line.
        Word word = this.words.removeFirst();
        Word fitting = new Word(new ArrayList<>(), word.spaceBefore, false);
        while (widthUsed < this.layout.getWidth() && word.objects.size() > 0) {
            InlineObject object = word.objects.getFirst();
            if (object.getClass().equals(TextRun.class)) {
                // Try to fit individual characters on the line.
                TextRun run = (TextRun)object;
                if (run.text.length() < 1) {
                    continue;
                }
                TextRun splitRun = new TextRun("", run.style);
                int j = 1;
                float originalWidthUsed = widthUsed;
                boolean ranOutOfSpace = false;
                while (widthUsed < this.layout.getWidth() && 
                        j <= run.text.length()) {
                    splitRun.text = run.text.substring(0, j) + "-";
                    float runWidth = splitRun.getWidth();
                    if (originalWidthUsed + runWidth <= 
                            this.layout.getWidth()) {
                        // Add the run width.
                        widthUsed = originalWidthUsed + runWidth;
                        j++;
                    } else {
                        // Cut off the used portion of the original run.
                        j--;
                        splitRun.text = run.text.substring(0, j);
                        run.text = run.text.substring(j, run.text.length());
                        ranOutOfSpace = true;
                        break;
                    }
                }
                if (!ranOutOfSpace) {
                    splitRun.text = run.text.substring(0, run.text.length());
                    run.text = "";
                }
                widthUsed = originalWidthUsed + splitRun.getWidth();
                fitting.objects.add(splitRun);
                if (run.text.length() == 0) {
                    // All the text is used up.
                    word.objects.removeFirst();
                }
                if (ranOutOfSpace) {
                    break;
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
        
        if (fitting.objects.isEmpty()) {
            return;
        }
        fitting.populateRunsFromObjects();
        if (!word.objects.isEmpty()) {
            word.populateRunsFromObjects();
            word.calculateWordSize();
            this.words.addFirst(word);
        }
                
        // Add the dash to the end.
        this.addHyphenToEnd(fitting);

        fitting.calculateWordSize();
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
