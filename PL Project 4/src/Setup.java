import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Setup implements Runnable {
	
	private Window window;
	private String title;
	private int size;
	private Thread thread;
	private LocalDateTime localDateTime;
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
		double time = System.currentTimeMillis();
		for (int i=1;i<=12;i++) {
			pos = i/12.0 * Math.PI*2.0;
			psX = (int)(center + Math.sin(pos)*radius);
			psY = (int)(center - Math.cos(pos)*radius);
			gr.drawString(Integer.toString(i), psX, psY);
//          gr.drawString(Integer.toString(i),center-(i/12)*11+(int)(210*Math.sin(i*Math.PI/6)),center-(int)(210*Math.cos(i*Math.PI/6)));
		}
		
		localDateTime = LocalDateTime.now();
		int s = localDateTime.getSecond();
        int m = localDateTime.getMinute();
		int h = localDateTime.getHour();
		
		// Draw Second hand
		radius = center - 50;
		time = System.currentTimeMillis()/(60.0 *1000.0)*Math.PI*2.0;
		psX = (int)(center + Math.sin(time)*radius);
		psY = (int)(center - Math.cos(time)*radius);
		gr.drawLine(center, center, psX, psY);
		
		// Draw Minute hand
		radius = center - 70;
		time = System.currentTimeMillis()/(60.0 *60*1000.0)*Math.PI*2.0;
		psX = (int)(center + Math.sin(time)*radius);
		psY = (int)(center - Math.cos(time)*radius);
		gr.setStroke(new BasicStroke(3));
		gr.drawLine(center, center, psX, psY);
		

		// Draw Hour hand
		radius = center - 90;
		
		time = h*Math.PI *2.0;
		psX = (int)(center + Math.sin(time)*radius);
		psY = (int)(center - Math.cos(time)*radius);
		gr.setStroke(new BasicStroke(6));
		gr.drawLine(center, center, psX, psY);
		
		
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
