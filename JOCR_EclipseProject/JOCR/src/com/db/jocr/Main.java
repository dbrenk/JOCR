package com.db.jocr;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.pdfbox.Overlay;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

public class Main {
	static String tempDir;


	public static void main(String[] args) {
		
		
		String infile = "";
		String outfile = "";
		String mode = "";
		String lang = "";
		

		
		
		if(args.length == 4 && (args[1].equalsIgnoreCase("-READ") || args[1].equalsIgnoreCase("-OCR") || args[1].equalsIgnoreCase("-AUTO"))){
			lang = args[0];
			mode = args[1];
			infile = args[2];
			outfile = args[3];
			System.out.println("lang: " +lang);
			System.out.println("mode: " +mode);
			System.out.println("infile: " +infile);
			System.out.println("outfile: " +outfile);
			
			if(mode.equalsIgnoreCase("-READ")){
				System.out.println(getText(infile, outfile));
			}else if(mode.equalsIgnoreCase("-OCR")){
				System.out.println(tryOCR(lang, infile, outfile));
			}else if(mode.equalsIgnoreCase("-AUTO")){
				String readresult = "";
				try{
					readresult = getText(infile, outfile);
				}catch(Exception e){
					System.err.println(e.getMessage());
				}

				if(readresult.length() > 20){
					// did read successfully
				}else{
					readresult = tryOCR(lang, infile, outfile);
				}
				
				
			}
			
		}else if(args.length == 5 && args[0].equalsIgnoreCase("-OVERLAY")){
			mode = args[0];
			infile = args[1];
			outfile = args[2];
			String text = args[3];
			String color = args[4];
			System.out.println("mode: " +mode);
			System.out.println("infile: " +infile);
			System.out.println("outfile: " +outfile);
			System.out.println("text: " + text);
			Color ocolor = Color.WHITE;
			if(color.equalsIgnoreCase("Color.WHITE")){
				ocolor = Color.WHITE;
			}else{
				ocolor = Color.BLACK;
			}
			System.out.println("color: " + ocolor.toString());
			Boolean ok = overlay(infile, outfile, text, ocolor);
			
		}else{
			System.out.println("There are different modes how to use this Program:");
			System.out.println("1. OCR mode:  has 4 parameters: lang (-DEU/-ENG), mode (-OCR/-READ/-AUTO), infile (PDF/BMP/TIF/PNG) and outfile (TXT)");
			System.out.println("2. WhiteOnWhite Overlay mode: has 5 parameters: mode (-OVERLAY), infile (PDF), outfile (PDF), text (String) and color (Color.WHITE/Color.BLACK)");
		}
		
		
		
		
		
	}
	
	public static String getText(String infile, String outfile){
		String parsedText = "";
		
		{PDFTextStripper pdfStripper = null;
	    PDDocument pdDoc = null;
	    COSDocument cosDoc = null;
	    File file = new File(infile);
	    try {
	        PDFParser parser = new PDFParser(new FileInputStream(file));
	        parser.parse();
	        cosDoc = parser.getDocument();
	        pdfStripper = new PDFTextStripper();
	        pdDoc = new PDDocument(cosDoc);
	        pdfStripper.setStartPage(1);
	        //pdfStripper.setEndPage(5);
	        parsedText = pdfStripper.getText(pdDoc);
	        System.out.println(parsedText);
	        
	        try (PrintStream out = new PrintStream(new FileOutputStream(outfile))) {
	            out.print(parsedText);
	        }
	        
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        } 
	    }
		
		return parsedText;
	}

	public static String tryOCR(String lang, String infile, String outfile){
		File imageFile = new File(infile);
        Tesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
        //Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
        String result = "";
        
        lang = lang.replace("-", "");
        lang = lang.toLowerCase();
        instance.setLanguage(lang);
        
        
        try {
            result = instance.doOCR(imageFile);
            System.out.println(result);
	        try (PrintStream out = new PrintStream(new FileOutputStream(outfile))) {
	            out.print(result);
	        }

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        } catch (Exception e){
        	System.err.println(e.getMessage());
        }

        
        return result;
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
