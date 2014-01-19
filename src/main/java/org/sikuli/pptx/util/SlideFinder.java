package org.sikuli.pptx.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Rectangle;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sikuli.pptx.models.ImageElement;
import org.sikuli.pptx.models.Slide;
import org.sikuli.pptx.models.SlideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

// Fluent class for finding elements on a given slide by filtering and sorting criteria
public class SlideFinder {
	static Logger logger = LoggerFactory.getLogger(SlideFinder.class);

	private List<Operator> operators = Lists.newArrayList();

	final Slide slide;
	SlideFinder(Slide slide){
		this.slide = slide;
	}

	public List<SlideElement> all(){
		return execute();
	}

	interface Operator {
		List<SlideElement> apply(List<SlideElement> input);		
	}

	static class Sorter implements Operator {
		private Comparator<SlideElement> comp;
		public Sorter(Comparator<SlideElement> comp) {
			this.comp = comp;
		}

		public List<SlideElement> apply(List<SlideElement> input) {
			Collections.sort(input, comp);
			return input;
		}	
	}

	static class Applier implements Operator {
		private Function<SlideElement, Void> proc;
		public Applier(Function<SlideElement, Void> proc) {
			this.proc = proc;
		}

		public List<SlideElement> apply(List<SlideElement> input) {
			for (SlideElement e : input){
				proc.apply(e);
			}
			return input;
		}
	}

	static class Filter implements Operator {
		private Predicate<SlideElement> predicate;
		Filter(Predicate<SlideElement> predicate){
			this.predicate = predicate;
		}

		public List<SlideElement> apply(List<SlideElement> input) {
			return Lists.newArrayList(Iterables.filter(input, predicate));
		}
	}

	public boolean exist() {
		return first() != null;
	}

	void addFilter(Predicate<SlideElement> pred){
		operators.add(new Filter(pred));
	}

	void addSorter(Comparator<SlideElement> comp){
		operators.add(new Sorter(comp));
	}

	void addApplier(Function<SlideElement, Void> proc){
		operators.add(new Applier(proc));
	}

	public SlideFinder below(final SlideElement element){	
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {
				return e != element && e.getBounds().y >= r.y;								
			}		
		});
		return this;
	}

	public SlideFinder rightOf(final SlideElement element){	
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {				
				return e != element && e.getBounds().x >= r.x;								
			}		
		});
		return this;
	}

	public SlideFinder intersects(final SlideElement element){		
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {			
				return e != element && r.intersects(e.getBounds());								
			}		
		});
		return this;
	}

	public SlideFinder near(final SlideElement element, int radius){	
		checkNotNull(element);
		final Rectangle r = element.getBounds();
		r.x -= radius;
		r.y -= radius;
		r.width += (2*radius);
		r.height += (2*radius);		
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {
				return e != element && r.intersects(e.getBounds());		
			}		
		});
		return this;
	}
	//	
	//	// vertical center line is within each other's y range
	//	// namely, if they have the same x range, they would intersect
	//	public Selector overlapVerticallyWith(final SlideElement element, final float minOverlapRatio){	
	//		checkNotNull(element);
	//		final Rectangle r1 = element.getBounds();
	//		r1.x = 0; r1.width = 1;		
	//		elements = Collections2.filter(elements, new Predicate<SlideElement>(){
	//			public boolean apply(SlideElement e) {
	//				if (e == element){
	//					return false;
	//				}				
	//				if (r1.height == 0){
	//					return false;
	//				}
	//				Rectangle r2 =e.getBounds();
	//				r2.x = 0; r2.width = 1;
	//				Rectangle intersection = r1.intersection(r2);
	//				float yOverlapRatio = 1f * intersection.height / r1.height;			
	//				return yOverlapRatio > minOverlapRatio;
	//			}		
	//		});		
	//		return this;
	//	}
	//	
	public SlideFinder orderByY(){
		addSorter(new Comparator<SlideElement>(){
			public int compare(SlideElement a, SlideElement b) {				
				return a.getOffy() - b.getOffy();
			}			
		});
		return this;
	}

	public SlideFinder orderByX(){
		addSorter(new Comparator<SlideElement>(){
			public int compare(SlideElement a, SlideElement b) {				
				return a.getOffx() - b.getOffx();
			}			
		});
		return this;
	}


	public SlideFinder print(final PrintStream out){
		addApplier(new Function<SlideElement, Void>(){
			public Void apply(SlideElement element) {
				out.println(element);
				return null;
			}				
		});
		return this;
	}

	private List<SlideElement> execute(){
		List<SlideElement> elements = Lists.newArrayList(slide.getElements());
		for (Operator op : operators){
			elements = op.apply(elements);
		};
		return elements;
	}

	public int count(){
		List<SlideElement> elements = execute();
		return elements.size();
	}		

	public SlideElement first(){
		List<SlideElement> elements = execute();
		if (elements.size() > 0)
			return elements.get(0);
		else
			return null;
	}

	public SlideFinder hasText(){
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {
				return hasText(e);
			}		
		});
		return this;
	}

	public SlideFinder textContains(final String str){
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {
				return e.getText() != null && e.getText().contains(str);
			}		
		});
		return this;
	}

	public SlideFinder textStartsWith(final String str){
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {
				return e.getText() != null && e.getText().startsWith(str);
			}		
		});
		return this;
	}

	public SlideFinder textMatches(final String regex){		
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {				
				return e.getText() != null && e.getText().matches(regex);
			}	
		});
		return this;
	}

	static private boolean hasText(SlideElement e){
		return e.getText() != null && ! e.getText().isEmpty();
	}

	public SlideFinder isImage() {
		addFilter(new Predicate<SlideElement>(){
			public boolean apply(SlideElement e) {				
				return e instanceof ImageElement;
			}	
		});
		return this;
	}

	public static SlideFinder find(Slide slide) {		
		return new SlideFinder(slide);
	}

}

