import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author longnguyen
 * Main file to draw all the elements in the Analog Clock
 */
public class Setup implements Runnable {
	
	/**
	 * Window object 
	 */
	private Window window;
	/**
	 * Window title
	 */
	private String title;
	/**
	 * Size of the window
	 */
	private int size;
	/**
	 * Thread for clock
	 */
	private Thread thread;
	/**
	 * To get local Date Time
	 */
	private LocalDateTime localDateTime;
	/**
	 * Buffer
	 */
	private BufferStrategy buffer;
	/**
	 * {@link Graphics2D} to draw
	 */
	private Graphics2D gr;
	/**
	 * Store current temperature in Celsius
	 */
	private double currentTemp;
	
	/**
	 * Constructor
	 * @param title Window title
	 * @param size Window size
	 */
	public Setup(String title, int size) {
		this.title=title;
		this.size=size;
		this.currentTemp = this.getTemperature();
	}
	/**
	 * Method to get current temperature using rest API to openweathermap
	 * @return temperature in double
	 */
	public double getTemperature() {
		double temp = 0.0;
		try {
			String myAPIKey = "32a22cf1a4a420d85105239549955464";
			String cityID = "2165087"; // GOLD COAST CITY
			URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?id=2165087&APPID="+myAPIKey);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			String file = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				file = output;
			}
			
			int pos = file.indexOf("temp");
			String sub = file.substring(pos+6, pos+12);
			temp = Double.parseDouble(sub) - 273.15;
			conn.disconnect();
		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
		
	}
	public void init() {
		window = new Window(title,size);
	}
	/**
	 * Draw all the elements method
	 */
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
		
		// Draw temperature
		gr.setFont(new Font("Stencil", Font.BOLD, 18));
		int temp = (int) Math.round(this.currentTemp);
		gr.drawString(Integer.toString(temp) + "°C", center - 100, center -60);
		gr.drawString("Gold Coast", center - 100, center -40);
		// ////////////
		buffer.show();
		gr.dispose();
	}
	/**
	 * Start the thread
	 */
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
	/**
	 * Stop the thread
	 */
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		init();
		// Clock always runs when open
		while(true) {
			draw();
		}
	}
}
