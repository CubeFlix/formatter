/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 *
 * @author Kevin Chen
 */
public class FormatterApp {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        
        TextStyle style1 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
        TextStyle style2 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), 14);
        
        TextRun run1 = new TextRun("Hello, my name is K", style1);
        TextRun run2 = new TextRun("evin L", style2);
        TextRun run3 = new TextRun("u", style1);
        TextRun run4 = new TextRun(" Chen.", style1);
        
        TextRun[] runs = {run1, run2, run3, run4};
        Paragraph pg = new Paragraph(runs);
        
        WordSplitter splitter = new WordSplitter(pg);
        splitter.split();
        System.out.println(splitter.getWords().size());
        
        for (List<TextRun> word : splitter.getWords()) {
            for (TextRun run : word) {
                System.out.print(run.text);
            }
            System.out.print("\n");
        }
        
        ParagraphFormatter formatter = new ParagraphFormatter(pg, new ParagraphLayout());
        float width = formatter.calculateWordWidth(splitter.getWords().get(0));
        float height = formatter.calculateWordHeight(splitter.getWords().get(0));
        System.out.println(width);
        System.out.println(height);
    }
}
