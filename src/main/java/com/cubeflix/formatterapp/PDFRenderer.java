/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 *
 * @author Kevin Chen
 */
public class PDFRenderer {
    private final PDDocument document;
    private List<PDPage> pages;
    private Coordinate cursor = null;
    private PDPage currentPage = null;
    private boolean textStarted;
    
    PDFRenderer(PDDocument document) {
        this.document = document;
    }
    
    private PDPage requestNewPage() {
        PDPage page = new PDPage();
        this.document.addPage(page);
        return page;
    }
    
    private List<InlineObject> joinAdjacentRunsInLine(LineFormatting line) {
        List<InlineObject> objects = new ArrayList<>();
        TextRun currentRun = null;
        for (Word word : line.words) {
            if (currentRun != null && word.spaceBefore) {
                currentRun.text = " " + currentRun.text;
            }
            for (InlineObject object : word.objects) {
                if (!object.getClass().equals(TextRun.class)) {
                    if (!object.isVisible()) {
                        continue;
                    }
                    if (currentRun != null) {
                        objects.add((InlineObject)currentRun);
                        currentRun = null;
                    }
                    objects.add(object);
                    continue;
                }
                TextRun run = (TextRun)object;
                if (currentRun == null) {
                    currentRun = new TextRun(run.text, run.style);
                    if (word.spaceBefore) {
                        currentRun.text = " " + currentRun.text;
                    }
                    continue;
                } 
                if (currentRun.style.equals(run.style)) {
                    currentRun.text += run.text;
                } else {
                    objects.add((TextRun)currentRun);
                    currentRun = new TextRun(run.text, run.style);
                }
            }
            if (word.spaceAfter) {
                currentRun.text += " ";
            }
        }
        if (currentRun != null) {
            objects.add((InlineObject)currentRun);
        }
        return objects;
    }
    
    public void renderFormattedStream(FormattedStream stream) 
            throws IOException {
        for (List<Formatted> streamPage : stream.stream) {
            PDPage page = this.requestNewPage();
            this.currentPage = page;
            this.textStarted = false;
            this.renderFormattedStreamPage(page, streamPage);
        }
    }
        
    private void renderFormattedStreamPage(PDPage page,
            List<Formatted> objects) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(
                this.document, 
                page);
        this.cursor = new Coordinate(0, 0);
        for (Formatted object : objects) {
            this.renderFormatted(contentStream, object);
        }
        if (this.textStarted) {
            contentStream.endText();
        }
        contentStream.close();
    }
    
    private void renderFormatted(PDPageContentStream contentStream,
            Formatted object) throws IOException {
        switch (object) {
            case LineFormatting line -> {
                if (!this.textStarted) {
                    contentStream.beginText();
                    this.textStarted = true;
                }
                
                // The start value points to the top left point of the line. 
                // We must translate the value to point to the baseline.
                line.start.y += line.getHeight();
                Coordinate target = this.translateCoordinate(
                        this.currentPage,
                        line.start);

                float offsetX = target.x - this.cursor.x;
                float offsetY = target.y - this.cursor.y;
                contentStream.newLineAtOffset(offsetX, offsetY);
                this.cursor = target;
                
                this.renderLine(contentStream, line);
            }
            case FormattedDrawable drawable -> {
                if (this.textStarted) {
                    contentStream.endText();
                }
                this.renderFormattedDrawable(contentStream, drawable);
            }
            default -> {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private void renderFormattedDrawable(PDPageContentStream contentStream,
            FormattedDrawable object) throws IOException {
        Drawable drawable = object.object;
        switch (drawable) {
            case LineBreak _ -> {}
            case SoftHyphen _ -> {}
            case Spacer _ -> {}
            case WordBreak _ -> {}
            default -> {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private void renderLine(PDPageContentStream contentStream,
            LineFormatting line) throws IOException {
        if (line.overrideSpacing) {
            contentStream.setWordSpacing(line.spacingOverride / 1000.0f);
        }
        List<InlineObject> objects = this.joinAdjacentRunsInLine(line);
        float xOffset = 0.0f;
        boolean shouldStartNewLine = false;
        for (int i = 0; i < objects.size(); i++) {
            InlineObject object = objects.get(i);
            if (!object.isVisible()) {
                continue;
            }
            switch (object) {
                case TextRun run -> {
                    if (shouldStartNewLine) {
                        contentStream.newLineAtOffset(xOffset / 1000.0f, 0.0f);
                        this.cursor.x += xOffset / 1000.0f;
                        xOffset = 0.0f;
                        shouldStartNewLine = false;
                    }
                    xOffset += this.renderTextRun(contentStream,
                            line,
                            run);
                }
                case Spacer spacer -> {
                    xOffset += spacer.width;
                    shouldStartNewLine = true;
                }
                default -> {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }
    
    private float renderTextRun(PDPageContentStream contentStream,
            LineFormatting line,
            TextRun run) throws IOException {
        float width = 0.0f;
        contentStream.setFont(run.style.family, run.style.size);
        contentStream.setNonStrokingColor(run.style.color);
        contentStream.showText(run.text);
        
        // Calculate the width.
        width += run.style.family.getStringWidth(run.text) * run.style.size;
        if (line.overrideSpacing) {
            int lastIndex = 0;
            int spaceCount = 0;
            while ((lastIndex = run.text.indexOf(" ", lastIndex)) != -1) {
                lastIndex++;
                spaceCount++;
            }
            
            // Add the extra space for the spacing override.
            width += line.spacingOverride * spaceCount;
        }
        
        return width;
    }
    
    public static Coordinate translateCoordinate(PDPage page, Coordinate co) {
        return new Coordinate(co.x / 1000.0f, 
                page.getMediaBox().getHeight() - (co.y / 1000.0f));
    }
}
