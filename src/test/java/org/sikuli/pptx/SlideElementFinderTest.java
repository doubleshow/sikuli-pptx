package org.sikuli.pptx;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.pptx.models.ImageElement;
import org.sikuli.pptx.models.Slide;
import org.sikuli.pptx.models.SlideElement;
import org.sikuli.pptx.util.SlideEditor;
import org.sikuli.pptx.util.SlideElementFinder;

public class SlideElementFinderTest {


	private Slide slide;
	private SlideElement text;
	private ImageElement image;

	@Before
	public void setUp(){
		slide = new Slide();
		text = new SlideElement();
		text.setText("text");
		
		image = new ImageElement();
	}

	@Test
	public void exist(){
		assertThat(SlideElementFinder.find(slide).exist(), equalTo(false));
		
		slide.add(text);
		assertThat(SlideElementFinder.find(slide).exist(), equalTo(true));
	}
	
	@Test
	public void all(){
		assertThat(SlideElementFinder.find(slide).all().size(), equalTo(0));
		
		slide.add(text);
		assertThat(SlideElementFinder.find(slide).all().size(), equalTo(1));
		assertThat(SlideElementFinder.find(slide).all().get(0), equalTo(text));
	}

	
	@Test
	public void canFindText(){
		assertThat(SlideElementFinder.find(slide).hasText().count(), equalTo(0));
		
		slide.add(text);
		assertThat(SlideElementFinder.find(slide).hasText().count(), equalTo(1));

		slide.add(text);
		assertThat(SlideElementFinder.find(slide).hasText().count(), equalTo(2));
	}
	
	@Test
	public void canFindImage(){
		slide.add(text);
		assertThat(SlideElementFinder.find(slide).isImage().count(), equalTo(0));
		
		slide.add(image);
		assertThat(SlideElementFinder.find(slide).isImage().count(), equalTo(1));
	}
	
	@Test
	public void canFindFirstMatch(){
		assertThat(SlideElementFinder.find(slide).hasText().first(), nullValue());
		
		slide.add(text);
		assertThat(SlideElementFinder.find(slide).hasText().first(), equalTo(text));
	}

	@Test
	public void canFindTextContains(){
		SlideElement apple = new SlideElement();
		apple.setText("apple");
		slide.add(apple);
		
		SlideElement orange = new SlideElement();
		orange.setText("orange");
		slide.add(orange);
		
		assertThat(SlideElementFinder.find(slide).textContains("ang").first(), equalTo(orange));
	}
	
	@Test
	public void canFindTextStartWith(){
		SlideElement apple = new SlideElement();
		apple.setText("apple");
		slide.add(apple);
		
		SlideElement orange = new SlideElement();
		orange.setText("orange");
		slide.add(orange);
		
		SlideElement appliance = new SlideElement();
		appliance.setText("appliance");
		slide.add(appliance);
		
		assertThat(SlideElementFinder.find(slide).count(), equalTo(3));
		assertThat(SlideElementFinder.find(slide).textContains("app").count(), equalTo(2));
	}
	
	@Test
	public void canFindTextMatches(){
		SlideElement apple = new SlideElement();
		apple.setText("apppple");
		slide.add(apple);
		
		SlideElement orange = new SlideElement();
		orange.setText("orange");
		slide.add(orange);
		
		SlideElement appliance = new SlideElement();
		appliance.setText("pple");
		slide.add(appliance);
		
		assertThat(SlideElementFinder.find(slide).count(), equalTo(3));
		assertThat(SlideElementFinder.find(slide).textMatches(".*p+le").count(), equalTo(2));
	}
	
	@Test
	public void canFindNear(){
		SlideElement e = new SlideElement();
		e.setOffx(100);
		e.setOffy(100);
		e.setCx(10);
		e.setCy(10);
		
		SlideElement near = new SlideElement();
		near.setOffx(130);
		near.setOffy(130);
		near.setCx(10);
		near.setCy(10);
		
		SlideElement far = new SlideElement();
		far.setOffx(200);
		far.setOffy(200);
		far.setCx(10);
		far.setCy(10);
		
		slide.add(e);

		assertThat(SlideElementFinder.find(slide).near(near, 50).count(), equalTo(1));
		assertThat(SlideElementFinder.find(slide).near(far, 50).count(), equalTo(0));
	}
	
	@Test
	public void canFindBelow(){
		SlideElement e = new SlideElement();
		e.setOffx(100);
		e.setOffy(100);
		e.setCx(10);
		e.setCy(10);
		
		SlideElement below1 = new SlideElement();
		below1.setOffx(130);
		below1.setOffy(150);
		below1.setCx(10);
		below1.setCy(10);
		
		SlideElement below2 = new SlideElement();
		below2.setOffx(130);
		below2.setOffy(150);
		below2.setCx(10);
		below2.setCy(10);
		
		SlideElement above1 = new SlideElement();
		above1.setOffx(130);
		above1.setOffy(60);
		above1.setCx(10);
		above1.setCy(10);
		
		slide.add(below1);
		slide.add(below2);

		assertThat(SlideElementFinder.find(slide).below(e).count(), equalTo(2));
		assertThat(SlideElementFinder.find(slide).below(e).all(), not(hasItem(above1)));
	}
	
	@Test
	public void canFindRightOf(){
		SlideElement e = new SlideElement();
		e.setOffx(100);
		e.setOffy(100);
		e.setCx(10);
		e.setCy(10);
		
		SlideElement right1 = new SlideElement();
		right1.setOffx(130);
		right1.setOffy(150);
		right1.setCx(10);
		right1.setCy(10);
		
		SlideElement right2 = new SlideElement();
		right2.setOffx(160);
		right2.setOffy(150);
		right2.setCx(10);
		right2.setCy(10);
		
		SlideElement left1 = new SlideElement();
		left1.setOffx(80);
		left1.setOffy(60);
		left1.setCx(10);
		left1.setCy(10);
		
		slide.add(right1);
		slide.add(right2);

		assertThat(SlideElementFinder.find(slide).rightOf(e).count(), equalTo(2));
		assertThat(SlideElementFinder.find(slide).rightOf(e).all(), not(hasItem(left1)));
	}
	
	
	@Test
	public void canFindIntersects(){
		SlideElement e = new SlideElement();
		e.setOffx(100);
		e.setOffy(100);
		e.setCx(10);
		e.setCy(10);
		
		SlideElement intersect1 = new SlideElement();
		intersect1.setOffx(105);
		intersect1.setOffy(105);
		intersect1.setCx(10);
		intersect1.setCy(10);
		
		SlideElement intersect2 = new SlideElement();
		intersect2.setOffx(95);
		intersect2.setOffy(95);
		intersect2.setCx(10);
		intersect2.setCy(10);
		
		SlideElement outside = new SlideElement();
		outside.setOffx(80);
		outside.setOffy(60);
		outside.setCx(10);
		outside.setCy(10);
		
		slide.add(intersect1);
		slide.add(intersect2);

		assertThat(SlideElementFinder.find(slide).intersects(e).count(), equalTo(2));
		assertThat(SlideElementFinder.find(slide).intersects(e).all(), not(hasItem(outside)));
	}

	
	@Test
	public void canFindOrderByY(){
		SlideElement top = new SlideElement();
		top.setOffx(100);
		top.setOffy(100);
		top.setCx(10);
		top.setCy(10);
		
		SlideElement middle = new SlideElement();
		middle.setOffx(105);
		middle.setOffy(135);
		middle.setCx(10);
		middle.setCy(10);
		
		SlideElement bottom = new SlideElement();
		bottom.setOffx(95);
		bottom.setOffy(160);
		bottom.setCx(10);
		bottom.setCy(10);
		
		slide.add(middle);
		slide.add(top);
		slide.add(bottom);
		
		assertThat(SlideElementFinder.find(slide).first(), equalTo(middle));
		assertThat(SlideElementFinder.find(slide).orderByY().first(), equalTo(top));
	}
	
	@Test
	public void canFindOrderByX(){
		SlideElement left = new SlideElement();
		left.setOffx(100);
		left.setOffy(100);
		left.setCx(10);
		left.setCy(10);
		
		SlideElement center = new SlideElement();
		center.setOffx(145);
		center.setOffy(135);
		center.setCx(10);
		center.setCy(10);
		
		SlideElement right = new SlideElement();
		right.setOffx(195);
		right.setOffy(160);
		right.setCx(10);
		right.setCy(10);
		
		slide.add(center);
		slide.add(left);
		slide.add(right);
		
		assertThat(SlideElementFinder.find(slide).first(), equalTo(center));
		assertThat(SlideElementFinder.find(slide).orderByY().first(), equalTo(left));
	}
}
