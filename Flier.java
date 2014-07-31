import java.util.*;
import java.awt.*;

/**
 * An animated object flying across the scene in a fixed direction
 * template from Chris Bailey-Kellogg
 * @author Scott Nelson
 * PS-1, Dartmouth CS 10, Winter 2014
 */
public class Flier extends Agent {
	private int xmax, ymax; //boundaries
	private double dx,dy;
	
	// instance variables, constructor, helper methods, etc.
	
	public Flier(double x, double y, int xmax, int ymax) {
		super(x,y);
		//remember the boundaries
		this.xmax = xmax;
		this.ymax = ymax;
		
		//initialize its location and speed
		init();
	}
	public void init() {
		if (Math.random() >.5){
			//come in from the left side, going right
			x = 0;
			y = ymax*Math.random();
			dx = 20*(Math.random());
			dy = 20*(Math.random());
		}
		else {
			//come in from the right side, going left
			x = xmax;
			y = ymax*Math.random();
			dx = -20*(Math.random());
			dy = 20*(Math.random());
		}
	}
	
	public boolean outOfBounds() {
		//check if intersection with the boundaries
		boolean boo = (x > xmax || 
				x<0 ||
				y>ymax ||
				y<0);
		return boo;
	}
	public void move() {
		//add the change in x and y to make it change position
		x+= dx;
		y+= dy;
	}
	public boolean hit(ArrayList<ArrayList<Point>> arr){
		//in order to see if there is a hit, need to see if the 'ball'
		//and the region(s) have overlapping points
		//do this by comparing their coordinates 
		
		//step through the array, testing each point 
		for (ArrayList<Point> a: arr){
			for (Point point:a){
				if (point.x == (int)x && point.y ==(int)y){
					System.out.println("Hit!");
					return true;
				}
			}
		}
		//if not, there was no hit 
		return false;
	}
}