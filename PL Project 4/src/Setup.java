
public class Setup implements Runnable {
	
	private Window window;
	private String title;
	private int size;
	private Thread thread;
	
	public Setup(String title, int size) {
		this.title=title;
		this.size=size;
	}
	public void init() {
		window = new Window(title,size);
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
	}
}
