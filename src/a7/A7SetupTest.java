package a7;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class A7SetupTest {
	public static void main(String[] args) throws IOException {
		Picture p = A7Helper.readFromURL("http://www.cs.unc.edu/~kmp/kmp-in-namibia.jpg"); //have to call the class
		p.setCaption("KMP in Namibia");
		SimplePictureViewWidget simple_widget = new SimplePictureViewWidget(p);
		
		JFrame main_frame = new JFrame(); //frame surrounds it, have to set this to visible later
		main_frame.setTitle("Assignment 7 Simple Picture View"); //title
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pretty much copy these lines of code

		JPanel top_panel = new JPanel(); //panel that you set on main frame
		top_panel.setLayout(new BorderLayout());
		top_panel.add(simple_widget, BorderLayout.CENTER); //add a UI widget-in this case simple widget SimplePictureViewWidget
		main_frame.setContentPane(top_panel);

		main_frame.pack();
		main_frame.setVisible(true);
	}
}