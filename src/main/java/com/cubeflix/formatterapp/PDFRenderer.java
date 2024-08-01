/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 *
 * @author Kevin Chen
 */
public class PDFRenderer {
    private PDDocument document;
    
    PDFRenderer(PDDocument document) {
        this.document = document;
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
        for (Word word : line.words) {
            for (int i = 0; i < word.runs.size(); i++) {
                TextRun run = word.runs.get(i);
                contentStream.setFont(run.style.family, run.style.size);
                
                String text = run.text;
                if (i == 0 && word.spaceBefore) {
                    text = " " + text;
                }
                if (i == word.runs.size() - 1 && word.spaceAfter) {
                    text = text + " ";
                }
                contentStream.showText(text);
            }
        }
    }
    
    public static Coordinate translateCoordinate(PDPage page, Coordinate co) {
        return new Coordinate(co.x / 1000.0f, 
                page.getMediaBox().getHeight() - (co.y / 1000.0f));
    }
}
