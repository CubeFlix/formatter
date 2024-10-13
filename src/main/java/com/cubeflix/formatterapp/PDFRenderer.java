/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
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
    
    private Deque<FormattedTextDecoration> decorationsToDraw;
    
    PDFRenderer(PDDocument document) {
        this.document = document;
        this.decorationsToDraw = new ArrayDeque();
    }
    
    private PDPage requestNewPage() {
        PDPage page = new PDPage();
        this.document.addPage(page);
        return page;
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

        // Draw any text decorations.
        this.renderTextDecorations(contentStream);
        
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
                Coordinate start = new Coordinate(
                        line.start.x,
                        line.calculateBaseline()
                );
                Coordinate target = this.translateCoordinate(
                        this.currentPage,
                        start);

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
        List<InlineObject> objects = line.objects;
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
                    float runWidth = this.renderTextRun(contentStream,
                            line,
                            run);
                    
                    if (run.style.textDecoration != null) {
                        // Add a formatted text decoration.
                        Coordinate start = new Coordinate(
                                this.cursor.x + xOffset / 1000.0f,
                                this.cursor.y
                        );
                        FormattedTextDecoration decoration = 
                                new FormattedTextDecoration(run, 
                                        start, 
                                        runWidth);
                        this.decorationsToDraw.push(decoration);
                    }
                    xOffset += runWidth;
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
        contentStream.setCharacterSpacing(run.style.characterSpacing / 1000.0f);
        contentStream.showText(run.text);
        
        // Calculate the width.
        String strippedText = run.text.stripTrailing();
        width += run.style.family.getStringWidth(strippedText) * 
                    run.style.size + 
                    strippedText.length() * run.style.characterSpacing;
        
        // Add the extra space for the spacing override.
        if (line.overrideSpacing) {
            int lastIndex = 0;
            int spaceCount = 0;
            while ((lastIndex = strippedText.indexOf(" ", lastIndex)) != -1) {
                lastIndex++;
                spaceCount++;
            }
            width += line.spacingOverride * (spaceCount); 
        }

        return width;
    }
    
    private void renderTextDecorations(PDPageContentStream contentStream) 
            throws IOException {
        while (!this.decorationsToDraw.isEmpty()) {
            FormattedTextDecoration decoration = this.decorationsToDraw.pop();
            this.renderTextDecoration(contentStream, decoration);
        }
    }
    
    private void renderTextDecoration(PDPageContentStream contentStream, 
            FormattedTextDecoration decoration) throws IOException {
        if (decoration.run.style.textDecoration == null) return;
        TextDecoration textDecoration = decoration.run.style.textDecoration;
        contentStream.setLineWidth(textDecoration.style.thickness);
        contentStream.setStrokingColor(textDecoration.style.color);

        // Calculate the location of the start of the line.
        Coordinate start = decoration.start;
        switch (textDecoration.type) {
            case TextDecorationType.STRIKETHROUGH:
                start.y += (decoration.run.getHeight() / 2) / 1000.0f;
                break;
            case TextDecorationType.UNDERLINE:
                break;
            default:
                throw new UnsupportedOperationException();
        }
        start.y -= textDecoration.yOffset / 1000.0f;
        
        // Account for the thickness of the line.
        start.y -= textDecoration.style.thickness;

        // Calculate the location of the end of the line.
        Coordinate end = new Coordinate(
                start.x + decoration.width / 1000.0f,
                start.y
        );
        
        // Draw the line.
        contentStream.moveTo(start.x, start.y);
        contentStream.lineTo(end.x, end.y);
        contentStream.stroke();
    }
    
    public static Coordinate translateCoordinate(PDPage page, Coordinate co) {
        return new Coordinate(co.x / 1000.0f, 
                page.getMediaBox().getHeight() - (co.y / 1000.0f));
    }
}
