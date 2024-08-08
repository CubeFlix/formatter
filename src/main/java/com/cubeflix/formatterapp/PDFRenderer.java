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
        Coordinate cursor = new Coordinate(0, 0);
        for (LineFormatting line : lines) {
            Coordinate target = this.translateCoordinate(page, line.start);
            float offsetX = target.x - cursor.x;
            float offsetY = target.y - cursor.y;
            contentStream.newLineAtOffset(offsetX, offsetY);
            cursor = target;
            
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
        for (int i = 0; i < objects.size(); i++) {
            InlineObject object = objects.get(i);
            if (!object.isVisible()) {
                continue;
            }
            switch (object) {
                case TextRun run -> {
                    contentStream.setFont(run.style.family, run.style.size);
                    contentStream.showText(run.text);
                }
                default -> {
                    // TODO
                    throw new UnsupportedOperationException();
                }
            }
        }
    }
    
    public static Coordinate translateCoordinate(PDPage page, Coordinate co) {
        return new Coordinate(co.x / 1000.0f, 
                page.getMediaBox().getHeight() - (co.y / 1000.0f));
    }
}
