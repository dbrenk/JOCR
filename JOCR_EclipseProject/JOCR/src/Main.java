import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

public class Main {

	public static void main(String[] args) {
		
		
		String infile = "";
		String outfile = "";
		String mode = "";
		String lang = "";
		
		String helpInfo = "mode (-OCR/-READ), infile (PDF) and outfile (TXT)";
		String parameterInfo = "you have to give me 3 parameters: " + helpInfo;
		
		
		if(args.length == 4){
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
			
		}else{
			System.out.println(parameterInfo);
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
}
