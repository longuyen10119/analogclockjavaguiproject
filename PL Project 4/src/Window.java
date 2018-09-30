import javax.swing.JFrame;

public class Window {
	private String title;
	private int size;
	private JFrame frame;
	
	public Window(String title, int size) {
		this.title = title;
		this.size = size;
		display();
	}
	
	public void display() {
		frame = new JFrame(title);
		frame.setSize(size, size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	
		
				
	}
}
