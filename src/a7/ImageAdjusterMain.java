package a7;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageAdjusterMain {
	public static void main(String[] args) throws IOException {
		Picture p = A7Helper.readFromURL("http://www.cs.unc.edu/~kmp/kmp-in-namibia.jpg");
		p.setCaption("KMP in Namibia");
		ImageAdjusterWidget simple_widget = new ImageAdjusterWidget(p);
		
		JFrame main_frame = new JFrame(); //frame surrounds it, have to set this to visible later
		main_frame.setTitle("Assignment 7 Simple Picture View with Pixel Inspector");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel top_panel = new JPanel(); //panel that you set on main frame
		top_panel.setLayout(new BorderLayout());
		top_panel.add(simple_widget, BorderLayout.CENTER); //add a UI widget-in this case ImageAdjusterWidget
		main_frame.setContentPane(top_panel);

		main_frame.pack();
		main_frame.setVisible(true);
	}
}