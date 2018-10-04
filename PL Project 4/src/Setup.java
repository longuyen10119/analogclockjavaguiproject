import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
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
//		System.out.println(Integer.toString(h));
		double time1 = System.currentTimeMillis();
		double time2 =  time1 + 3600000 + 14000 ;
		time = System.currentTimeMillis()/(60.0*60*12*1000)*Math.PI *2.0+24;
		int psXh = (int)(center + Math.sin(time)*radius);
		int psYh = (int)(center - Math.cos(time)*radius);
		gr.setStroke(new BasicStroke(6));
		gr.drawLine(center, center, psXh, psYh);
		
		
		// Draw the center
		gr.setColor(new Color(255,109,87));
		gr.fillOval(center-5, center-5, 10, 10);
		
		
		// Draw Date
		gr.setFont(new Font("Stencil", Font.ITALIC, 14));

		gr.setColor(new Color(58, 135, 170));
		DayOfWeek dayW = localDateTime.getDayOfWeek();
		int date = localDateTime.getDayOfMonth();
		Month month = localDateTime.getMonth();
		String stringMonth = month.toString();
		int year = localDateTime.getYear();
		String drawRest = (stringMonth+ " "+ Integer.toString(date) +" "+ Integer.toString(year) );
		gr.drawString(dayW.toString(), center+35, center-10);
		gr.drawString(drawRest, center+35, center+10);
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
