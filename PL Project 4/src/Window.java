import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Window class sets up sizes for frames and canvas
 */
public class Window {
	/**
	 * title of the frame
	 */
	private String title;
	/**
	 * size of frame
	 */
	private int size;
	/**
	 * JFrame Java
	 */
	private JFrame frame;
	/**
	 * Canvas for drawing the Clock
	 */
	public static Canvas canvas;
	
	/**
	 * Constructor
	 * @param title Title of the Window
	 * @param size Size of window in pixels
	 */
	public Window(String title, int size) {
		this.title = title;
		this.size = size;
		display();
	}
	
	/**
	 * Method to set up the frame and canvas
	 */
	public void display() {
		// Set up frame
		frame = new JFrame(title);
		frame.setSize(size, size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Set up canvas
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(size, size));
		canvas.setBackground(new Color(58, 135, 170));//Blue
		frame.add(canvas);
		frame.pack();
		
				
	}
}
