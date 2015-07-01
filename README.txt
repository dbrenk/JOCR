dab 30.06.2015

included:
PDFBOX    - https://pdfbox.apache.org/
Tess4J    - http://tess4j.sourceforge.net/
Tesseract - https://code.google.com/p/tesseract-ocr/
Ghost4J   - http://www.ghost4j.org/index.html

licenses:
http://www.apache.org/licenses/LICENSE-2.0.html
http://www.gnu.org/licenses/lgpl.html


There are different modes how to use this Program:

1. OCR mode:  has 4 parameters: lang (-DEU/-ENG), mode (-OCR/-READ/-AUTO), infile (PDF/BMP/TIF/PNG) and outfile (TXT)
	-OCR  : using tesseract for character recognition
	-READ : using pdfbox to extract text from "text-based" PDF files
	-AUTO : tries to extract text with pdfbox and fallback to tesseract if it is not a text-based PDF file

2. WhiteOnWhite Overlay mode: has 5 parameters: mode (-OVERLAY), infile (PDF), outfile (PDF), text (String) and color (Color.WHITE/Color.BLACK)
	The overlay text is written on every page of the original document, the original is not changed
