package org.sikuli.pptx.models;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Slide {
	
	List<SlideElement> elements = Lists.newArrayList();
	private int width = 9144000;
	private int height = 6858000;
	private int number; // one-based
	
	// Construct a blank slide
	public Slide(){		
	}
	
	// Construct a new slide by copying the content from a given slide
	public Slide(Slide slide){
		elements = Lists.newArrayList(slide.elements);
		number = slide.number;
	}
		
	public void add(SlideElement element){
		elements.add(element);
	}
	
	public void remove(SlideElement element){
		elements.remove(element);
	}
	
	public SlideElement[] getElements(){
		return elements.toArray(new SlideElement[]{});		
	}
	
	public int getElementCount(){
		return elements.size();
	}

	public String toString(){
		List<String> elementNames = Lists.newArrayList();
		for (SlideElement el : elements){
			elementNames.add(el.getClass().getSimpleName());
		}
		String txt = Joiner.on(";").join(elementNames);
		return Objects.toStringHelper(this).add("number", number).add("elements", txt).toString();		
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
