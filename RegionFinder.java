import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Dartmouth CS 10, Winter 2014
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering
	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
														// so the identified regions are in a list of lists of points
	
	/**
	 * Accesses the currently-identified regions.
	 * @return
	 */
	public ArrayList<ArrayList<Point>> getRegions() {
		return regions;
	}
	
	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the static threshold).
	 * @param c1
	 * @param c2
	 * @return
	 */
	private static boolean colorMatch(Color c1, Color c2) {
//		System.out.println("color matching");
		//Store the respective RGB values seperately
		int r1 = c1.getRed();
		int g1 = c1.getGreen();
		int b1 = c1.getBlue();
		
		int r2 = c2.getRed();
		int g2 = c2.getGreen();
		int b2 = c2.getBlue();
		
		//Find how similar they are by using the absolute value function
		//test if this meets our threshold 
		return Math.abs(r2-r1)< maxColorDiff &&
				Math.abs(g2-g1) < maxColorDiff &&
				Math.abs(b2-b1) < maxColorDiff;
		
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the color.
	 * @param image
	 * @param color
	 */
	public void findRegions(BufferedImage image, Color color) {
		boolean[][] visited = new boolean[image.getWidth()][image.getHeight()];

		regions = new ArrayList<ArrayList<Point>>();
		//step through all the pixels
		for (int x=0;x<image.getWidth();x++){
			for (int y =0;y<image.getHeight();y++){
				Color c = new Color(image.getRGB(x,y));
				if (!visited[x][y] && colorMatch(color, c)){
					//potential new region
					ArrayList<Point> reg = new ArrayList<Point>();
					
					//keep track of points to visit 
					ArrayList<Point> toVisit = new ArrayList<Point>();
					Point p = new Point(x,y);
					toVisit.add(p);
					while (!toVisit.isEmpty()){
						
						//pop the last point off the array 
						Point lp = toVisit.remove(toVisit.size()-1);
						reg.add(lp);
						
						//get x and y coordinates to test neighbors
						int lx = lp.x;
						int ly = lp.y;
						for (int q=lx-1;q<=lx+1;q++){
							for (int v=ly-1;v<=ly+1;v++){
								//stay within bounds and make sure we don't keep testing the same pixel
								//avoiding an infinite loop 
									if (q >= 0 && 
											q<image.getWidth() && 
											v >=0 && 
											v <image.getHeight() && 
											!(lx==q && ly ==v) &&
											!visited[q][v]){
										Color nc = new Color(image.getRGB(q,v));

										if (colorMatch(color, nc)){
											toVisit.add(new Point(q,v));
										}
										//remember which we have checked to avoid repetition
										visited[q][v] = true;
									}
								
								
								
							}
						}
					}
					//check if the region size is significant enough to keep it
					if (reg.size()>minRegion){
						regions.add(reg);
						
					}
				}
				//keep tabs on pixels already iterated
				visited[x][y]=true;
			}
		}

	}
	
	/**
	 * Recolors image so that each region is a random uniform color, so we can see where they are
	 * @param image
	 */
	public void recolorRegions(BufferedImage image) {
	
		for (ArrayList<Point> region:regions){
			//create a pseudorandom bright color to paint each region
			int R = (int)(Math.random()*256);
			int G = (int)(Math.random()*256);
			int B = (int)(Math.random()*256);
			Color co = new Color(R,G,B);
			int rgb = (int) (co.getRGB());
			for (Point point:region){
				image.setRGB(point.x,point.y,rgb);
			}
		}
	}
}