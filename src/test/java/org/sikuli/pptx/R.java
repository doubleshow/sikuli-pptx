package org.sikuli.pptx;

import java.io.InputStream;
import java.net.URL;

public class R {
	public static class PPTX {
		public static URL fiveSlides = R.class.getResource("FiveSlides.pptx");
		public static URL bad = R.class.getResource("BadBundle.pptx");
	}
	
	public static class Images {
		public static URL ocean = R.class.getResource("ocean.png");
	}
}
