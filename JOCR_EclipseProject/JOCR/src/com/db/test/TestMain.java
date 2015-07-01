package com.db.test;


import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.pdfbox.*;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TestMain {
	
	static String tempDir;

	public static void main(String[] args) {
		System.out.println(overlay("./in/צה.pdf", "./in/צה_overlay.pdf", "TestText", Color.WHITE));
		
	}
	
	public static Boolean overlay(String pathInputDoc, String pathOutputDoc, String text, Color color){
		int pageCount = 0; 
		PDDocument watermarkDoc = null;
		PDDocument realDoc = null;
		try{				
			tempDir = System.getProperty("java.io.tmpdir");
			realDoc = PDDocument.load(pathInputDoc);
			pageCount = realDoc.getPageCount();
			System.out.println("PageCount: " + pageCount);
			
			String pathWatermarkDoc = createWhiteOnWhiteDoc(text, pageCount, color);
			watermarkDoc = PDDocument.load(pathWatermarkDoc);
			System.out.println("PageCount: " + watermarkDoc.getPageCount());

			
    		File file = new File(pathWatermarkDoc);
    		 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}

			Overlay overlay = new Overlay();
			overlay.overlay(realDoc,watermarkDoc);
			watermarkDoc.save(pathOutputDoc);
			System.out.println(pathOutputDoc);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} 
		
		return true;
	}
	
	
	private static String createWhiteOnWhiteDoc(String text, int pageCount, Color color) throws COSVisitorException, IOException{
		
		
		// Create a new empty document
		PDDocument document = new PDDocument();


		
		
		for(int i = 0; i<pageCount; i++){
			// Create a new blank page and add it to the document
			PDPage page = new PDPage();
			document.addPage( page );
			// Create a new font object selecting one of the PDF base fonts
			PDFont font = PDType1Font.HELVETICA_BOLD;

			// Start a new content stream which will "hold" the to be created content
			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
			contentStream.beginText();
			contentStream.setFont( font, 12 );
			contentStream.setNonStrokingColor(color);
			contentStream.moveTextPositionByAmount( 100, 700 );
			contentStream.drawString(text);
			contentStream.endText();

			// Make sure that the content stream is closed:
			contentStream.close();
		}
		
		

		// Save the newly created document
		String outputWatermark = tempDir + "WhiteOnWhiteWatermark.pdf";
		System.out.println(outputWatermark);
		document.save(outputWatermark);

		// finally make sure that the document is properly
		// closed.
		document.close();
		return outputWatermark;
	}

}


