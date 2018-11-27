package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorWidget extends JPanel implements MouseListener{

	private PictureView picture_view;
	private Picture picture;
	private JLabel x;
	private JLabel y;
	private JLabel red;
	private JLabel green;
	private JLabel blue;
	private JLabel brightness;

	public PixelInspectorWidget(Picture picture) {
		this.picture = picture;

		setLayout(new BorderLayout());

		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);


		JPanel panel = new JPanel(new GridLayout(6, 1)); //what will appear on left of the screen: Pixel Information
		add(panel, BorderLayout.WEST);

		x = new JLabel("X: ");
		panel.add(x);

		y = new JLabel("Y: ");
		panel.add(y);

		red = new JLabel("Red: ");
		panel.add(red);

		green = new JLabel("Green: ");
		panel.add(green);

		blue = new JLabel("Blue: ");
		panel.add(blue);

		brightness = new JLabel("Brightness: ");
		panel.add(brightness);


	}

	public void mouseClicked(MouseEvent e) {

	}


	@Override
	public void mousePressed(MouseEvent e) {
		//getting the x and y representing pixel whose information 
		//will be displayed
		int x = e.getX();
		int y = e.getY();

		double red = Math.round(picture.getPixel(e.getX(), e.getY()).getRed()*100.0)/100.0;
		double green = Math.round(picture.getPixel(e.getX(), e.getY()).getGreen()*100.0)/100.0;
		double blue = Math.round(picture.getPixel(e.getX(), e.getY()).getBlue()*100.0)/100.0;
		double brightness = Math.round(picture.getPixel(e.getX(), e.getY()).getIntensity()*100.0)/100.0;

		//update label
		this.x.setText("X: " + x);
		this.y.setText("Y: " + y);
		this.red.setText("Red: " + red);
		this.green.setText("Green: " + green);
		this.blue.setText("Blue: " + blue);
		this.brightness.setText("Brightness: " + brightness);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
