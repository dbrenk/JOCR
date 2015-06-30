dab 30.06.2015

included:
PDFBOX    - https://pdfbox.apache.org/
Tess4J    - http://tess4j.sourceforge.net/
Tesseract - https://code.google.com/p/tesseract-ocr/
Ghost4J   - http://www.ghost4j.org/index.html

licenses:
http://www.apache.org/licenses/LICENSE-2.0.html
http://www.gnu.org/licenses/lgpl.html

HOWTO:
you have to give me 3 parameters: mode (-OCR/-READ/-AUTO), infile (PDF/TIF/BMP/PNG) and outfile (TXT)";

-OCR  : using tesseract for character recognition
-READ : using pdfbox to extract text from "text-based" PDF files
-AUTO : tries to extract text with pdfbox and fallback to tesseract if it is not a text-based PDF file
