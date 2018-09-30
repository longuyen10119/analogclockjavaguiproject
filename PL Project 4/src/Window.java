import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window {
	private String title;
	private int size;
	private JFrame frame;
	private Canvas canvas;
	
	public Window(String title, int size) {
		this.title = title;
		this.size = size;
		display();
	}
	
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
		canvas.setBackground(new Color(58, 135, 170));
		frame.add(canvas);
		frame.pack();
		
				
	}
}
