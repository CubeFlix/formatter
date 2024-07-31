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
    private List<List<TextRun>> words = new ArrayList<>();

    public String getDelimeter() {
        return delimeter;
    }

    public List<List<TextRun>> getWords() {
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
        List<TextRun> lastWord = null;
        for (int i = 0; i < this.paragraph.runs.length; i++) {
            List<TextRun> fragments = this.splitRun(paragraph.runs[i]);
            
            // Join up the first fragment with the last fragment of the previous 
            // run. This way if there are words spanning multiple runs, they 
            // will be concatenated into a single word.
            if (lastWord != null) {
                // If the run starts with whitespace, the last word should be 
                // pushed without ever adding the first fragment.
                if (this.startsWithWhitespace(paragraph.runs[i].text)) {
                    this.words.add(lastWord);
                    lastWord = null;
                } else {
                    lastWord.add(fragments.remove(0));
                    // If there was more than one fragment in this run, or if 
                    // there's only one fragment and it ends in whitespace, we 
                    // can push this word.
                    if (fragments.size() > 0 || 
                            (fragments.size() == 0 && 
                            this.endsWithWhitespace(paragraph.runs[i].text))) {
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
                List<TextRun> word = new ArrayList<>();
                word.add(fragments.get(j));
                this.words.add(word);
            }
            
            // If the run ends with whitespace, push the last fragment as its
            // own word, otherwise set lastWord.
            if (this.endsWithWhitespace(paragraph.runs[i].text)) {
                List<TextRun> word = new ArrayList<>();
                word.add(fragments.getLast());
                this.words.add(word);
            } else {
                lastWord = new ArrayList<>();
                lastWord.add(fragments.getLast());
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
