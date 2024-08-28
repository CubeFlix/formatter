/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
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
        
        /*
        TextStyle style1 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
        // TextRun run1 = new TextRun("Hello1Hello2Hello3Hello4Hello5Hello6 Hello7Hello8Hello9Hello10Hello11Hello12 ", style1);
        // TextRun run2 = new TextRun("Hello4Hello5Hello6 Hello1Hello2Hello3Hello4Hello5Hello6 Hello1Hello2Hello3Hello4Hello5Hello6 Hello1Hello2Hello3Hello4Hello5Hello6", style1);
        TextRun run1 = new TextRun("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. ", style1);
        TextRun run2 = new TextRun("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.", style1);
        InlineObject[] runs = {run1, new LineBreak(), new Spacer(40000.0f), run2};
        ParagraphStyle pgstyle = new ParagraphStyle(14.5f * 1000.0f, ParagraphAlignment.JUSTIFY, new HyphenationSettings(true, 0.95f));
        Paragraph pg = new Paragraph(runs, pgstyle);
        
        Coordinate start = new Coordinate(14500, 14500);
        Coordinate end = new Coordinate(500000, 600000);
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
        
        */
        
        TextStyle style1 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 26);
        TextStyle style2 = new TextStyle(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 26);
        style2.characterSpacing = 1000.0f;
        // style2.color = Color.BLUE;
        TextRun run1 = new TextRun("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. ", style1);
        TextRun run2 = new TextRun("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.", style2);
        InlineObject[] runs1 = {new Spacer(40000.0f), run1};
        InlineObject[] runs2 = {new Spacer(40000.0f), run2};
        InlineObject[] runs3 = {new Spacer(40000.0f), run1.copy()};
        InlineObject[] runs4 = {new Spacer(40000.0f), run2.copy()};
        InlineObject[] runs5 = {new Spacer(40000.0f), run1.copy()};
        InlineObject[] runs6 = {new Spacer(40000.0f), run2.copy()};
        InlineObject[] runs7 = {new Spacer(40000.0f), run1.copy()};
        InlineObject[] runs8 = {new Spacer(40000.0f), run2.copy()};
        InlineObject[] runs9 = {new Spacer(40000.0f), run1.copy()};
        InlineObject[] runs10 = {new Spacer(40000.0f), run2.copy()};
        ParagraphStyle pgstyle = new ParagraphStyle(14.5f * 1000.0f / 12.0f * 20.0f, ParagraphAlignment.JUSTIFY, new HyphenationSettings(true, 0.95f));
        List<ParagraphStreamObject> stream = new ArrayList<>();
        stream.add((ParagraphStreamObject)(new Paragraph(runs1, pgstyle)));
        stream.add((ParagraphStreamObject)(new PageBreak()));
        stream.add((ParagraphStreamObject)(new Paragraph(runs2, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs3, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs4, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs5, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs6, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs7, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs8, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs9, pgstyle)));
        stream.add((ParagraphStreamObject)(new Paragraph(runs10, pgstyle)));
        
        Coordinate start = new Coordinate(14500, 14500);
        Coordinate end = new Coordinate(500000, 600000);
        ParagraphLayout layout = new ParagraphLayout(start, end);
        List<List<ParagraphLayout>> streamlayout = new ArrayList<List<ParagraphLayout>>();
        streamlayout.add(new ArrayList<ParagraphLayout>());
        streamlayout.get(0).add(layout);
        ParagraphStreamFormatter formatter = new ParagraphStreamFormatter(
                new ParagraphStream(stream,
                        new ParagraphStreamStyle(0.0f),
                        (ParagraphStreamLayout)(new RepeatingParagraphStreamLayout(streamlayout))
                )
        );
        formatter.format();
        System.out.println(formatter.getFormatting());
        
        PDDocument document = new PDDocument();
        PDFRenderer renderer = new PDFRenderer(document);
        renderer.renderFormattedStream(formatter.getFormatting());
        document.save("testrender.pdf");
        PDFImageViewer viewer = new PDFImageViewer();
        viewer.main(args, document);
        
        document.close();
    }
}
