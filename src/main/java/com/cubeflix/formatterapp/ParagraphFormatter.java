/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a formatted layout tree on a single page from a given paragraph 
 * and layout. 
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
        if (this.paragraph instanceof OverflowParagraph) {
            this.words = ((OverflowParagraph)this.paragraph).getWords();
        } else {
            this.splitIntoWords();
        }
        this.calculateWordSizes();
        this.fitLines();
        
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
        
        // Fit words.
        while (widthUsed <= this.layout.getWidth() &&
                !this.words.isEmpty()) {
            Word word = this.words.removeFirst();
            if (!word.objects.isEmpty() &&
                    word.objects.getFirst().
                            getClass().equals(LineBreak.class)) {
                word.objects.removeFirst();
                if (!word.objects.isEmpty()) {
                    this.words.addFirst(word);
                }
                LineFormatter formatter = new LineFormatter(words, 
                    this.paragraph.style);
                return formatter;
            }
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
                if (last.text.endsWith(" ")) {
                    break;
                }
                if (last.text.isEmpty()) {
                    break;
                }
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
                // Add the soft hyphen.
                beforeHyphen.objects.add(object);
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
                lastFitting.setDidHyphenate(true);
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
        while (widthUsed < this.layout.getWidth() && !word.objects.isEmpty()) {
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
        if (!fitting.objects.isEmpty() && 
                fitting.objects.getLast().getClass().equals(TextRun.class) && 
                ((TextRun)fitting.objects.getLast()).text.equals("")) {
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
        fitting.setDidHyphenate(true);
        
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
            formatter.format();
            heightUsed += formatter.getFormatting().getHeight();
            if (!this.words.isEmpty()) {
                // On all but the last line, add the trailing line height.
                heightUsed += this.paragraph.style.leading;
            }
            
            this.lineFormatters.add(formatter);
        }
        
        if (heightUsed > this.layout.getHeight() && 
                !this.lineFormatters.isEmpty()) {
            List<Word> lastLineWords = 
                    this.lineFormatters.removeLast().getWords();
            if (!lastLineWords.isEmpty() && 
                    lastLineWords.getLast().isDidHyphenate()) {
                // De-hyphenate the last word and connect it with the first 
                // word of the word queue.
                Word lastWord = lastLineWords.removeLast();
                this.removeHyphenFromEnd(lastWord);
                Word firstRemovedWord = this.words.removeFirst();
                firstRemovedWord.objects.addAll(0, lastWord.objects);
                firstRemovedWord.spaceBefore = lastWord.spaceBefore;
                firstRemovedWord.populateRunsFromObjects();
                firstRemovedWord.calculateWordSize();
                this.unfitWords.addAll(lastLineWords);
                this.unfitWords.add(firstRemovedWord);
                this.unfitWords.addAll(this.words);
            } else {
                this.unfitWords.addAll(lastLineWords);
                this.unfitWords.addAll(this.words);
            }
        } else {
            this.unfitWords.addAll(this.words);
        }
        
        if (this.paragraph.style.alignment == ParagraphAlignment.JUSTIFY &&
                !this.lineFormatters.isEmpty()) {
            if (this.unfitWords != null && !this.unfitWords.isEmpty()) {
                return;
            }
            ParagraphStyle style = this.lineFormatters.getLast().
                    getStyle().copy();
            style.alignment = ParagraphAlignment.LEFT;
            this.lineFormatters.getLast().setStyle(style);
            this.lineFormatters.getLast().format();
        }
    }
    
    private void removeHyphenFromEnd(Word word) {
        for (int i = word.objects.size() - 1; i >= 0; i--) {
            if (!word.objects.get(i).isVisible()) {
                continue;
            }
            if (word.objects.get(i).getClass().equals(TextRun.class)) {
                TextRun last = ((TextRun)word.objects.get(i));
                if (!last.text.endsWith("-")) {
                    break;
                }
                last.text = last.text.substring(0, last.text.length() - 1);
            }
            break;
        }
    }
}
