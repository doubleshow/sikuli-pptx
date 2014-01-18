package org.sikuli.pptx;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.pptx.models.Slide;
import org.sikuli.pptx.models.SlideElement;

import com.google.common.collect.ImmutableList;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class SlideTest {
	
	private Slide slide;
	
	@Before
	public void setUp(){
		slide = new Slide();
	}
	
	@Test
	public void canAddSlideElement(){
		SlideElement el = mock(SlideElement.class);
		slide.add(el);		
	}
	
	@Test
	public void canIncrementSlideElementCount(){
		SlideElement el = mock(SlideElement.class);
		slide.add(el);		
		assertThat(slide.getElementCount(), equalTo(1));
	}
	
	@Test
	public void canGetAllElementsAsArray(){
		SlideElement e1 = mock(SlideElement.class);
		SlideElement e2 = mock(SlideElement.class);
		
		slide.add(e1);
		slide.add(e2);
		assertArrayEquals(slide.getElements(), ImmutableList.of(e1, e2).toArray());
	}
	
}
