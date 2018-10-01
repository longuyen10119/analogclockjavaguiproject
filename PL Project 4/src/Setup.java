import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class Setup implements Runnable {
	
	private Window window;
	private String title;
	private int size;
	private Thread thread;
	
	private BufferStrategy buffer;
	private Graphics2D gr;
	
	public Setup(String title, int size) {
		this.title=title;
		this.size=size;
	}
	public void init() {
		window = new Window(title,size);
	}
	public void draw() {
		buffer = window.canvas.getBufferStrategy();
		if(buffer==null) {
			window.canvas.createBufferStrategy(3);
			return;
		}
		// Set up graphics2d
		int center = size/2;
		gr = (Graphics2D) buffer.getDrawGraphics();
		gr.clearRect(0, 0, size, size);
		// Draw ////
		
		// draw a the circle line for clock
		gr.setColor(new Color(255,109,87));
		gr.fillOval(0, 0, size, size);
		gr.setColor(new Color(255,193,87));
		gr.fillOval(5, 5, size-10, size-10);
		
		// Draw Numbers
		gr.setColor(new Color(58, 135, 170));
		gr.setFont(new Font("arial", Font.PLAIN, 18));
		int psX, psY;
		double pos;
		int radius = center - 30; // radius of the numbers
		for (int i=1;i<=12;i++) {
			pos = i/12.0 * Math.PI*2;
			psX = (int)(center-8 + Math.sin(pos)*radius);
			psY = (int)(center+10 - Math.cos(pos)*radius);
			gr.drawString(Integer.toString(i), psX, psY);
//          gr.drawString(Integer.toString(i),center-(i/12)*11+(int)(210*Math.sin(i*Math.PI/6)),center-(int)(210*Math.cos(i*Math.PI/6)));
		}
		// Draw the center
		gr.setColor(new Color(255,109,87));
		gr.fillOval(center-5, center-5, 10, 10);
		
		// 
		// ////////////
		buffer.show();
		gr.dispose();
	}
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		init();
		// Clock always runs when open
		while(true) {
			draw();
		}
	}
}
