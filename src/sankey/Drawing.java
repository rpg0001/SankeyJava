package sankey;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;


public class Drawing extends Canvas {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> lines;
	private LinkedHashMap<String, Double> data;
	private int pixels;
	
	public Drawing(ArrayList<String> lines, LinkedHashMap<String, Double> data, int pixels) {
		this.lines = lines;
		this.data = data;
		this.pixels = pixels;
	}
	
	// main thing!
	public void paint(Graphics g) {
		// set text colour
		g.setColor(Color.black);
		
		// calculate total flow
		double flow = 0;
		for (Double d : data.values()) {flow += d;}
		// pixels per unit of flow:
		double pixelsPerUnit = pixels / flow;

		// draw title
		drawTitle(g, lines.get(0), pixels - (lines.get(0).length() * 5), 50);
		// draw source label
		drawText(g, lines.get(1), 25, pixels);
		
		// set up variables
		double height = 0;
		int srcx = 100;
		int srcy = pixels/2;
		int destx = (pixels * 2) - pixels/3;
		int desty = srcx + 50;
		// cumulative height, for the polygon
		int totalHeight = 0;
		// height, but updated earlier, for the polygon
		int aheadHeight = 0;
		
		
		// set up colour rotation
		ArrayList<Color> colours = new ArrayList<Color>();
		colours.add(Color.red);
		colours.add(Color.orange);
		colours.add(Color.yellow);
		colours.add(Color.green);
		colours.add(Color.cyan);
		colours.add(Color.blue);
		colours.add(Color.magenta);
		colours.add(Color.pink);
		int colourIndex = 0;
		Color destColour;
		
		// draw source bar
		// height is total flow * pixels per unit of flow
		Random r = new Random();
		Color srcColour = colours.get(r.nextInt(colours.size()));
		g.setColor(srcColour);
		drawLongRectangle(g, (int)(flow * pixelsPerUnit), srcx, srcy);
		
		// draw source outline
		g.setColor(Color.black);
		// source vertical line
	    g.drawLine(srcx, srcy, srcx, srcy + (int)(flow * pixelsPerUnit));
	    // source upper horizontal line
	    g.drawLine(srcx,  srcy, srcx + 30,  srcy);
	    // source lower horizontal line
	    g.drawLine(srcx, srcy + (int)(flow * pixelsPerUnit), srcx + 30, srcy + (int)(flow * pixelsPerUnit));
	
		// draw destination bars and labels
		for (String s : data.keySet()) {
				
			// y is the cumulative height, plus the gaps
			desty += height + 10;
			
			// need these for the polygons
			totalHeight += height;
			aheadHeight = (int) (data.get(s) * pixelsPerUnit);
			
			// draw polygon
			// second set are the same but lower
			int[] xpoints = {
					// src to dest lines
					srcx + 30,
					destx,
					srcx + 30,
					destx,
					// now the join lines
					srcx + 30,
					srcx + 30,
					destx,
					destx
					
			};
			
			int[] ypoints = {
					// src to dest lines
					srcy + totalHeight,
					desty,
					srcy + totalHeight + aheadHeight,
					desty + aheadHeight,
					// now the join lines
					srcy + totalHeight,
					srcy + totalHeight + aheadHeight,
					desty,
					desty + aheadHeight
			};
			
			// paint a colourful polygon
			destColour = colours.get(colourIndex);
			Graphics2D g2 = (Graphics2D) g;
		    GradientPaint gradient = new GradientPaint(srcx + 30, srcy + totalHeight, srcColour,
		    destx, desty, destColour);
		    g2.setPaint(gradient);
		    g2.fill(new Polygon(xpoints, ypoints, 8));
			
		    
		    // OUTLINE 
		    g.setColor(Color.black);
		    // upper flow line
		    g.drawLine(srcx + 30, srcy + totalHeight, destx, desty);
		    // lower flow line
		    g.drawLine(srcx + 30, srcy + totalHeight + aheadHeight, destx, desty + aheadHeight);
		    // destination vertical line
		    g.drawLine(destx + 30, desty, destx + 30, desty + aheadHeight);
		    // destination upper horizontal line
		    g.drawLine(destx, desty-1, destx + 30, desty-1);
		    // destination lower horizontal line
		    g.drawLine(destx,  desty + aheadHeight , destx + 30, desty + aheadHeight);
		    
		    
			// height of bar is flow * pixels per unit of flow
			height = data.get(s) * pixelsPerUnit;
			
			// get a random colour and draw destination bar
			g.setColor(destColour);
			drawLongRectangle(g, (int)height, destx, desty);
			
			
			// draw destination label
			g.setColor(Color.black);
			drawText(g, s, (pixels * 2) - pixels/4, desty + (int)(height/2));
			
			// increment so we have a new colour next time 
			// reset if we've hit max to avoid errors
			if (colourIndex != colours.size()-1) {
				colourIndex++;
			} else {
				colourIndex = 0;
			}
			
		}
	}
	
	// draws long black bar of width 30
	public void drawLongRectangle(Graphics g, int height, int x, int y) {
		g.fillRect(x, y, 30, height);
	}
	
	// draws big bold text
	public void drawTitle(Graphics g, String s, int x, int y) {
		g.setFont(new Font("Title", Font.BOLD, 20));
		g.drawString(s, x, y);
	}
	
	// draws small basic text
	public void drawText(Graphics g, String s, int x, int y) {
		g.setFont(new Font("Basic", Font.PLAIN, 12));
		g.drawString(s, x, y);
	}
	
	

}