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
    private Coordinate cursor = null;
    
    PDFRenderer(PDDocument document) {
        this.document = document;
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
    
    public void renderParagraph(Paragraph paragraph, 
            PDPage page, 
            ParagraphLayout layout) throws IOException {
        ParagraphFormatter formatter = new ParagraphFormatter(paragraph, 
                layout);
        formatter.format();
        
        PDPageContentStream contentStream = new PDPageContentStream(document, 
                page);
        
        contentStream.beginText();
        contentStream.setLeading(paragraph.style.leading / 1000.0f);
        
        List<LineFormatting> lines = formatter.getFormatting();
        this.cursor = new Coordinate(0, 0);
        for (LineFormatting line : lines) {
            Coordinate target = this.translateCoordinate(page, line.start);
            float offsetX = target.x - this.cursor.x;
            float offsetY = target.y - this.cursor.y;
            contentStream.newLineAtOffset(offsetX, offsetY);
            this.cursor = target;
            
            this.renderParagraphLine(contentStream, line);
        }
        
        contentStream.endText();
        contentStream.close();
    }
    
    private void renderParagraphLine(PDPageContentStream contentStream,
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
                case InlineSpacer spacer -> {
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
