package org.sikuli.pptx;

import java.io.InputStream;

public class PPTXs {
	public static InputStream fiveSlides() { return PPTXs.class.getResourceAsStream("FiveSlides.pptx"); };
	public static InputStream bad()        { return PPTXs.class.getResourceAsStream("BadBundle.pptx"); };
}
