package org.sikuli.pptx;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.pptx.io.PPTXBundle;

public class PPTXBundleTest {
	
	
	private PPTXBundle bundle;

	@Before
	public void setUp() throws IOException{
		bundle = PPTXBundle.createFrom(R.PPTX.fiveSlides);		
	}
	
	@Test
	public void canGetSlideCount(){
		assertThat(bundle.getSlideCount(), equalTo(5));
	}
	
	@Test
	public void canGetSlideXMLAsFile(){
		File f = bundle.getSlideXML(1);
		assertThat(f.exists(), equalTo(true));
		assertThat(f.getAbsolutePath().endsWith("ppt/slides/slide1.xml"), equalTo(true));		
	}

	@Test
	public void canGetSlideXMLRelAsFile(){
		File f = bundle.getSlideXMLRel(1);
		assertThat(f.exists(), equalTo(true));
		assertThat(f.getAbsolutePath(), endsWith((String)"ppt/slides/_rels/slide1.xml.rels"));		
	}
	
	@Test
	public void canCheckValid() throws IOException{
		assertThat(bundle.isValid(), equalTo(true));		
	}
	
	@Test
	public void canCheckInvalid() throws IOException{
		PPTXBundle badBundle = PPTXBundle.createFrom(R.PPTX.bad);
		assertThat(badBundle.isValid(), equalTo(false));
	}


}
