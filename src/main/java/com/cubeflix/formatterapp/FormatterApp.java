/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 *
 * @author Kevin Chen
 */
public class FormatterApp {
    public static void main(String[] args) throws IOException {        
        /*TextStyle style1 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
        TextStyle style2 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), 14);
        TextStyle style3 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        
        TextRun run1 = new TextRun("I remember you was conflicted. Misusing your influence. Sometimes I did the same. Hello, my name is K", style1);
        TextRun run2 = new TextRun("evin L", style3);
        TextRun run3 = new TextRun("u", style1);
        TextRun run4 = new TextRun(" Chen. Goo goo ga ga. Test 123 \"The first lumen is connected downwards from the first bag to the second pump from the bottom-left side of the pole.\"", style2);
        
        TextRun[] runs = {run1, run2, run3, run4};
        ParagraphStyle pgstyle = new ParagraphStyle(14.5f * 1000.0f, ParagraphAlignment.JUSTIFY);
        Paragraph pg = new Paragraph(runs, pgstyle);
        
        Coordinate start = new Coordinate(14000, 14000);
        Coordinate end = new Coordinate(200000, 200000);
        ParagraphLayout layout = new ParagraphLayout(start, end);
        
        
        WordSplitter splitter = new WordSplitter(pg);
        splitter.split();
        System.out.println(splitter.getWords().size());
        
        for (Word word : splitter.getWords()) {
            for (TextRun run : word.runs) {
                System.out.print(run.text);
            }
            System.out.print("\n");
            System.out.println("space before: " + (word.spaceBefore ? "yes" : "no"));
            System.out.println("space after: " + (word.spaceAfter ? "yes" : "no"));
        }
        
        ParagraphFormatter formatter = new ParagraphFormatter(pg, new ParagraphLayout(start, end));
        formatter.format();
        List<LineFormatting> formatting = formatter.getFormatting();
        for (LineFormatting line : formatting) {
            System.out.printf("Line - words: %d, x: %f, y: %f, spacingOverride: %f\n", line.words.size(), line.start.x, line.start.y, line.spacingOverride);
        }
        // float width = formatter.calculateWordWidth(splitter.getWords().get(0));
        // float height = formatter.calculateWordHeight(splitter.getWords().get(0));
        // System.out.println(width);
        // System.out.println(height);
        */
        
        TextStyle style1 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
        TextRun run1 = new TextRun("Lorem ipsum odor amet, consectetuer adipiscing elit. Senectus eu accumsan cras cubilia penatibus ex. Proin ornare congue adipiscing suscipit porttitor. Neque placerat urna, dictum lorem quam nostra. Sociosqu leo euismod volutpat mattis neque non suscipit dictum habitant. Volutpat malesuada mattis hac inceptos vehicula aptent maximus condimentum. Montes volutpat feugiat consequat eget egestas phasellus. Dapibus nunc vel ornare, litora sapien congue fringilla. Blandit molestie sodales fringilla tempor, conubia duis? Id ante lacus dis porta curabitur. Ligula massa mus fermentum felis bibendum donec taciti. Fermentum lacinia ornare habitant ipsum quis diam a faucibus. Lacus ipsum nostra ut nec inceptos pellentesque dolor praesent. Faucibus convallis habitant; etiam phasellus turpis iaculis. Integer dolor maximus metus ac dictumst nunc penatibus. Imperdiet molestie pellentesque tempor eu fusce adipiscing per. Curae ipsum id elit eleifend tellus. Congue venenatis eros accumsan consequat neque class facilisis.", style1);
        TextRun[] runs = {run1};
        ParagraphStyle pgstyle = new ParagraphStyle(14.5f * 1000.0f, ParagraphAlignment.LEFT);
        Paragraph pg = new Paragraph(runs, pgstyle);
        
        Coordinate start = new Coordinate(14500, 14500);
        Coordinate end = new Coordinate(600000, 600000);
        ParagraphLayout layout = new ParagraphLayout(start, end);
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFRenderer renderer = new PDFRenderer(document);
        renderer.renderParagraph(pg, page, layout);
        
        document.save("testrender.pdf");
        PDFImageViewer viewer = new PDFImageViewer();
        viewer.main(args, document);
        
        document.close();
        
        
        

    }
}
