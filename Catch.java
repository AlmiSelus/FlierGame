import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based catch game
 * Dartmouth CS 10, Winter 2014
 */
public class Catch extends Webcam {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;  //eclipse wanted a final long for some reason, declared to stop complaints 
	private Color trackColor;				// color of regions of interest (set by mouse press)
	private Flier flier;					// it's flying once mouse is pressed
	private RegionFinder finder;			// handles finding regions
	
	public Catch() {
		finder = new RegionFinder();
	}
	
	/**
	 * Overrides Webcam method to do the region finding the flier stuff.
	 */
	public void processImage() {
		if (trackColor != null) {
			// Detect regions and recolor
			finder.findRegions(image, trackColor);
			finder.recolorRegions(image);

			// Move the flier and detect catches and misses
			flier.move();
			ArrayList<ArrayList<Point>> regions = finder.getRegions();
			
			//check if it was a hit, otherwise it was a miss
			if (flier.hit(regions)){
				flier.init();
			}
			if (flier.outOfBounds()){
				System.out.println("Miss :(");
				flier.init();
			}
		}		
	}
	
	/**
	 * Overrides the Webcam method to also draw the flier.
	 */
	public void draw(Graphics g) {
		super.draw(g);
		
		// Draw flier
		if (flier != null) {
			flier.draw(g);
		}
	}

	/**
	 * Overrides the Webcam method to set the track color and start the flier.
	 */
	public void handleMousePress(MouseEvent event) {
		// Set tracking color
		//first get x and y from mouse click
		int x = event.getX();
		int y = event.getY();
		trackColor = new Color(image.getRGB(x, y));

		// Start object flying
		flier = new Flier(0,0,image.getWidth(),image.getHeight());
		flier.move();
	}
	
	/**
	 * Main method for the application
	 * @param args		command-line arguments (ignored)
	 */	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Catch();
			}
		});
	}
}