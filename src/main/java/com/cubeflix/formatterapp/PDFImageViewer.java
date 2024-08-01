/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cubeflix.formatterapp;

import java.awt.image.BufferedImage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PDFImageViewer extends Application {

        static PDDocument doc;
    
	public static void main(String[] args, PDDocument toView) {
                doc = toView;
		launch(args);
	}

	int pdfScale = 1;

	@Override
	public void start(Stage primaryStage) throws Exception {
		int width = 590;
		int height = 840;

		PDFRenderer renderer = new PDFRenderer(doc);
		BufferedImage img = renderer.renderImage(0, pdfScale);

		Pane pane = new Pane();
		WritableImage fxImage = SwingFXUtils.toFXImage(img, null);
		ImageView imageView = new ImageView(fxImage);

		pane.getChildren().add(imageView);

		Scene scene = new Scene(pane, width, height);
		primaryStage.setTitle(getClass().getName());
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
