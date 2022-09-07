package sankey;

import java.awt.Canvas;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.swing.JFrame;


public class Sankey {
	
	private ArrayList<String> lines;
	private LinkedHashMap<String, Double> dataSet;
	
	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	public Sankey() {
		this.lines = new ArrayList<String>();
		this.dataSet = new LinkedHashMap<String, Double>();
	}

	public LinkedHashMap<String, Double> getDataSet() {
		return dataSet;
	}

	public void setDataSet(LinkedHashMap<String, Double> dataSet) {
		this.dataSet = dataSet;
	}

	public ArrayList<String> getLines() {
		return lines;
	}

	// converts a text file to an ArrayList
	public ArrayList<String> readFileLines(String fileName) {
		// hold a reference to the text file
		BufferedReader buffer = null;
	    // make list to put lines in
	    ArrayList<String> lines = new ArrayList<String>();
	    try {
	      // Open the text file for reading.
	      FileReader in = new FileReader(fileName);
	      // Wrap FileReader in BufferedReader so we can read lines from the file not characters.
	      buffer = new BufferedReader(in);
	      // Read a line from file, returns null if there are no more lines to read.
	      String line = buffer.readLine();
	      // While we have not reached the end of the file 
	      while (line != null) {
	        // add line to list
	    	 lines.add(line);
	        // then read the next line
	        line = buffer.readLine();
	      }
	      // now close the file
	    }
	    catch (FileNotFoundException e) {
	      // executed if the file does not exist.
	      e.printStackTrace();
	    }
	    catch (IOException e) {
	      // executed if something goes wrong while reading the file.
	      e.printStackTrace(); 
	    }
	    finally {
	    	// error handling etc
	      if (buffer != null) {
	        try {
	          buffer.close();
	        }
	        catch (IOException e) {
	          System.out.println("Could not close the file");
	          e.printStackTrace(); 
	        }
	      }
	    }
	    // lastly, return the list
	    return lines;
	}
	
	// takes an arraylist in the required format and turns it into a map of the data
	public LinkedHashMap<String, Double> parseData(ArrayList<String> linesList) {
		LinkedHashMap<String, Double> dataSet = new LinkedHashMap<String, Double>();
		String label;
		String numberString;
		Double numberDouble;
		
		for (int i = 2; i < linesList.size(); i++) {
			label = linesList.get(i).replaceAll("[0-9]|,|\\.", "");
			numberString= linesList.get(i).toLowerCase().replaceAll("[a-z]|,| |\\/", "");
			numberDouble = Double.parseDouble(numberString);
			dataSet.put(label, numberDouble);
		}
		
		return dataSet;
	}
	
	public void drawSankey(LinkedHashMap<String, Double> data) {
		// get the number of pixels, will use this for canvas size and bar size calculations
		int pixels = 450 - (data.size() - 1) * 10;
		
		// draw the canvas and graph
		JFrame frame = new JFrame(lines.get(0));
		Canvas canvas = new Drawing(this.lines, data, pixels);
        canvas.setSize(pixels * 2, pixels * 2);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setBackground(Color.lightGray); 
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		Sankey sankey = new Sankey();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter file path (example: src\\baseball.txt) : ");
		String filePath = scanner.next();
		sankey.setLines(sankey.readFileLines(filePath)); 
		sankey.setDataSet(sankey.parseData(sankey.getLines()));
		LinkedHashMap<String, Double> data = sankey.getDataSet();
		sankey.drawSankey(data);
		scanner.close();
	}
	
}


	


