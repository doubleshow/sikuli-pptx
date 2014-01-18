package org.sikuli.pptx.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.pptx.R;
import org.sikuli.pptx.io.PPTXBundle;
import org.sikuli.pptx.models.ImageElement;
import org.sikuli.pptx.models.Slide;

public class SlideParserTest {
		
	private SlideParser parser;


	@Before
	public void setUp(){
		parser = new SlideParser();
	}
	
	@Test
	public void canParaseWithSingleTextElement() throws IOException{
		PPTXBundle fiveSlides = PPTXBundle.createFrom(R.PPTX.fiveSlides);				
		Slide slide = parser.parse(fiveSlides, 0);
		assertThat(slide.getElements()[0].getText(), containsString("Slide1"));		
	}
	
	@Test
	public void canParseSlideWithTwoTextElements() throws IOException{
		PPTXBundle fiveSlides = PPTXBundle.createFrom(R.PPTX.fiveSlides);				
		Slide slide = parser.parse(fiveSlides, 1);
		assertThat(slide.getElements().length, equalTo(2));
		assertThat(slide.getElements()[0].getText(), containsString("Slide2"));
		assertThat(slide.getElements()[1].getText(), containsString("Another Text Element"));
	}
	
	@Test
	public void canParaseWithSingleTextElementAndSingleImageElement() throws IOException{
		PPTXBundle fiveSlides = PPTXBundle.createFrom(R.PPTX.fiveSlides);				
		Slide slide = parser.parse(fiveSlides, 2);
		assertThat(slide.getElements()[1].getText(), containsString("Slide3"));
		assertThat(slide.getElements()[0], instanceOf(ImageElement.class));
		assertThat(((ImageElement)slide.getElements()[0]).getFileName(), endsWith("media/image1.png"));
	}	

	
//	@Test
//	public void canParseSlideWithSingleTextElement() throws IOException{
//		PPTXBundle fiveSlides = PPTXBundle.createFrom(PPTXs.fiveSlides());				
//		Slide slide = parser.parse(fiveSlides, 0);
//		assertThat(slide.getElements()[0].getText());		
//	}	

	
//	@Test 
//	// PowerPoint Format
//	public void testParseSlide1(){
//		File xml = new File(getClass().getResource("slide1.xml").getFile());
//		File rel = new File(getClass().getResource("slide1.xml.rels").getFile());
//		//Files.re
//		SlideParser parser = new SlideParser();
//		Slide slide = parser.parse(xml, rel);
//		
////		System.out.println(slide);	
////		assertEquals("size", 4, slide.getElements().size());
//		
////		assertThat(slide.select().isImage().exist(), equalTo(true));
////		ImageElement image = (ImageElement) slide.select().isImage().first();
////		assertThat(image.getFileName(), containsString("image"));
//	}

//	@Test 
//	// Google Presentation Format
//	public void testParseSlide2(){
//		File xml = new File(getClass().getResource("slide2.xml").getFile());
//		File rel = new File(getClass().getResource("slide2.xml.rels").getFile());
//		//Files.re
//		SlideParser parser = new SlideParser();
//		Slide slide = parser.parse(xml, rel);
//		
//		
//		assertEquals("size", 3, slide.getElements().size());
//		assertThat(slide.select().isImage().exist(), equalTo(true));
//		ImageElement image = (ImageElement) slide.select().isImage().first();
//		assertThat(image.getFileName(), containsString("image"));
//	}
//	

}
