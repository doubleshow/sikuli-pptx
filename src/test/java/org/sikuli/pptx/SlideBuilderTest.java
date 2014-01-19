package org.sikuli.pptx;


import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.pptx.models.Slide;
import org.sikuli.pptx.util.SlideEditor;

public class SlideBuilderTest {


	private Slide slide;

	@Before
	public void setUp(){
		slide = new Slide();
	}

	@Test
	public void testInsertElement() throws IOException{
		SlideEditor.edit(slide).insert().image().source(R.Images.ocean).bounds(50,20,200,150);
		SlideEditor.edit(slide).insert().element().text("Click").bounds(5,5,20,20).textSize(15).backgroundColor("FFFF00");
		SlideEditor.edit(slide).insert().element().bounds(80,50,50,30).lineColor("00FF00");
	}

}
