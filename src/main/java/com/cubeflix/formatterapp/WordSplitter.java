/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Splits a paragraph of text into individual runs of words.
 * @author Kevin Chen
 */
public class WordSplitter {
    private String delimeter = "\\s+";
    private Pattern whitespacePattern;
    private Paragraph paragraph;
    private List<Word> words = new ArrayList<>();

    public String getDelimeter() {
        return delimeter;
    }

    public List<Word> getWords() {
        return words;
    }
    
    WordSplitter(Paragraph paragraph) {
        this.paragraph = paragraph;
        
        this.whitespacePattern = Pattern.compile(this.delimeter);
    }
    
    WordSplitter(Paragraph paragraph, String delimeter) {
        this.paragraph = paragraph;
        this.delimeter = delimeter;
        
        this.whitespacePattern = Pattern.compile(this.delimeter);
    }
    
    public void split() {
        Word lastWord = null;
        for (int i = 0; i < this.paragraph.objects.length; i++) {
            InlineObject object = paragraph.objects[i];
            if (object.getClass().equals(TextRun.class)) {
                if (lastWord != null) {
                    lastWord.objects.add(object);
                } else {
                    lastWord = new Word(new ArrayList<>(), false, false);
                }
                continue;
            }
            
            TextRun run = (TextRun)object;
            List<TextRun> fragments = this.splitRun(run);
            int numOriginalFragments = fragments.size();
            
            // Join up the first fragment with the last fragment of the previous 
            // run. This way if there are words spanning multiple runs, they 
            // will be concatenated into a single word.
            boolean ateFirstFragment = false;
            if (lastWord != null) {
                // If the run starts with whitespace, the last word should be 
                // pushed without ever adding the first fragment.
                if (this.startsWithWhitespace(run.text)) {
                    lastWord.spaceAfter = false;
                    this.words.add(lastWord);
                    lastWord = null;
                } else {
                    ateFirstFragment = true;
                    TextRun runToAdd = fragments.remove(0);
                    lastWord.runs.add(runToAdd);
                    lastWord.objects.add((InlineObject)runToAdd);
                    // If there was more than one fragment in this run, or if 
                    // there's only one fragment and it ends in whitespace, we 
                    // can push this word.
                    if (!fragments.isEmpty() || 
                            (fragments.isEmpty() && 
                            this.endsWithWhitespace(run.text))) {
                        lastWord.spaceAfter = true;
                        this.words.add(lastWord);
                        lastWord = null;
                    }
                }
            }
            
            if (fragments.isEmpty()) {
                continue;
            }
            
            // Add everything except for the last fragment, since it might need
            // to become the new lastWord.
            for (int j = 0; j < fragments.size() - 1; j++) {
                List<TextRun> wordRuns = new ArrayList<>();
                wordRuns.add(fragments.get(j));
                boolean spaceAfter = true;
                boolean spaceBefore = false;
                if (j == 0 
                        && !ateFirstFragment
                        && this.startsWithWhitespace(run.text)) {
                    spaceBefore = true;
                }
                this.words.add(new Word(wordRuns, spaceBefore, spaceAfter));
            }
            
            // If the run ends with whitespace, push the last fragment as its
            // own word, otherwise set lastWord.
            if (this.endsWithWhitespace(run.text)) {
                List<TextRun> wordRuns = new ArrayList<>();
                wordRuns.add(fragments.getLast());
                this.words.add(new Word(wordRuns, false, true));
            } else {
                boolean spaceBefore = false;
                if (numOriginalFragments == 1 && 
                        this.startsWithWhitespace(run.text)) {
                    // This is the only fragment, and it starts with whitespace,
                    // so add a space before.
                    spaceBefore = true;
                }
                lastWord = new Word(new ArrayList<>(), spaceBefore, false);
                lastWord.runs.add(fragments.getLast());
            }
        }
        
        // If lastWord is still not null, push it.
        if (lastWord != null) {
            this.words.add(lastWord);
        }
    }
    
    private List<TextRun> splitRun(TextRun run) {
        String[] textFragments = run.text.split(this.delimeter);
        List<TextRun> fragments = new ArrayList<>();
        for (int i = 0; i < textFragments.length; i++) {
            if (textFragments[i].isEmpty()) {
                continue;
            }
            fragments.add(new TextRun(textFragments[i], run.style));
        }
        return fragments;
    }
    
    private boolean endsWithWhitespace(String text) {
        Matcher matcher = this.whitespacePattern.matcher(text);
        if (matcher.find()) {
            return matcher.end() == text.length();
        }
        return false;
    }
    
    private boolean startsWithWhitespace(String text) {
        Matcher matcher = this.whitespacePattern.matcher(text);
        if (matcher.find()) {
            return matcher.start() == 0;
        }
        return false;
    }
}
