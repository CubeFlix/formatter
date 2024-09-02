/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a formatted layout tree on multiple pages from a given paragraph 
 * stream and layout. 
 * @author Kevin Chen
 */
public class ParagraphStreamFormatter {
    private ParagraphStream stream;
    private ParagraphStreamStyle style;
    private ParagraphStreamLayout layout;
    
    private ParagraphLayout currentArea; // The current layout area.
    private Paragraph currentParagraph; // The current paragraph (clone).
    private FormattedStream formatting; // The output stream.

    public FormattedStream getFormatting() {
        return formatting;
    }
    private int pageIndex = 0; // The current page index.
    private float heightUsed = 0.0f; // The height used in the current area.
    
    ParagraphStreamFormatter(ParagraphStream stream) {
        this.stream = stream;
        this.style = stream.style;
        this.layout = stream.layout;
    }
    
    public void format() throws IOException {
        this.formatting = new FormattedStream(
                new ArrayList<>()
        );
        this.currentArea = this.layout.requestNextArea();
        this.pageIndex = this.layout.getPageIndex();
        this.fitLayout();
    }
    
    private void fitLayout() throws IOException {
        while (!this.stream.objects.isEmpty()) {
            // Add a new page, if needed.
            if (this.pageIndex >= this.formatting.stream.size()) {
                this.formatting.stream.add(new ArrayList<>());
            }
            
            ParagraphStreamObject object = 
                    (ParagraphStreamObject)this.stream.objects.removeFirst();
            if (object instanceof Drawable) {
                switch (object) {
                    case RegionBreak rb -> {
                        this.nextRegion();
                    }
                    case PageBreak pb -> {
                        this.nextPage();
                    }
                    default -> {
                        this.outputToStream((Drawable)object);
                    }
                }
            }
            else if (!(object instanceof Paragraph)) {
                // At this point, the only other thing the object could be is a
                // paragraph.
                throw new UnsupportedOperationException();
            } else {
                this.currentParagraph = ((Paragraph)object).copy();
                this.fitParagraph();
            }
        }
    }
    
    private void nextRegion() {
        this.currentArea = this.layout.requestNextArea();
        this.pageIndex = this.layout.getPageIndex();
        this.heightUsed = 0.0f;
    }
    
    private void nextPage() {
        int currentPage = this.pageIndex;
        while (this.pageIndex == currentPage) {
            if (this.currentArea == null) {
                return;
            }
            this.nextRegion();
        }
    }
    
    private void fitParagraph() throws IOException {
        // Define the area for the paragraph and format it.
        ParagraphLayout area = this.currentArea.copy();
        area.getStart().y += heightUsed;
        if (heightUsed > 0.0f) {
            area.getStart().y += this.style.spacing;
        }
        ParagraphFormatter formatter = new ParagraphFormatter(
                this.currentParagraph,
                area
        );
        formatter.format();

        // Output the formatted text.
        List<LineFormatting> formattedLines = formatter.getFormatting();
        for (LineFormatting line : formattedLines) {
            this.formatting.stream.get(pageIndex).add((Formatted)line);
            this.heightUsed += line.getHeight();
        }
        
        // Return the unformatted words to the stream.
        if (formatter.getUnfitWords() != null && 
                !formatter.getUnfitWords().isEmpty()) {
            Paragraph unfit = new OverflowParagraph(
                    this.currentParagraph,
                    formatter.getUnfitWords()
            );
            this.stream.objects.addFirst(unfit);
        }
        
        // If no lines were fit, move on.
        if (formattedLines.isEmpty()) {
            this.nextRegion();
        }
    }
    
    private void outputToStream(Drawable object) throws IOException {
        // TODO: allow for x-positioning of drawables
        if (this.heightUsed + object.getHeight() > 
                this.currentArea.getHeight()) {
            // This won't fit, so move on and return the object to the stream.
            this.stream.objects.addFirst((ParagraphStreamObject)object);
            this.nextRegion();
            return;
        }

        Coordinate pos = new Coordinate(0, this.currentArea.getStart().y +
                        this.heightUsed);
        FormattedDrawable formattedObject = new FormattedDrawable(
                object,
                pos
        );
        this.heightUsed += object.getHeight();
        this.formatting.stream.get(pageIndex).add((Formatted)formattedObject);
    }
    
}
