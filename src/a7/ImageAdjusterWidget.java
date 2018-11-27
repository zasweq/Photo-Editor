package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ImageAdjusterWidget extends JPanel implements MouseListener, ChangeListener {
	private PictureView picture_view;
	private Picture picture;
	private JSlider blur;
	private JSlider brightness;
	private JSlider saturation;

	public ImageAdjusterWidget(Picture picture) {
		this.picture = picture;
		setLayout(new BorderLayout());

		picture_view = new PictureView(picture.createObservable()); //makes it observable
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);

		JPanel panel = new JPanel(new GridLayout(3, 0));
		add(panel, BorderLayout.SOUTH);

		blur = new JSlider(0, 5, 0);
		blur.setPaintTicks(true);
		blur.setSnapToTicks(true); //true because blur is discrete (must be an integer) 
		blur.setPaintLabels(true);
		blur.setMajorTickSpacing(1);
		blur.setBorder(BorderFactory.createTitledBorder("Blur: "));

		blur.addChangeListener(this);
		panel.add(blur);

		saturation = new JSlider(-100, 100, 0);
		saturation.setPaintTicks(true);
		saturation.setPaintLabels(true);
		saturation.setMajorTickSpacing(25);
		saturation.setBorder(BorderFactory.createTitledBorder("Saturation: "));

		saturation.addChangeListener(this);
		panel.add(saturation);

		brightness = new JSlider(-100, 100, 0);
		brightness.setPaintTicks(true);
		brightness.setPaintLabels(true);
		brightness.setMajorTickSpacing(25);
		brightness.setBorder(BorderFactory.createTitledBorder("Brightness: "));


		brightness.addChangeListener(this);
		panel.add(brightness);

	}

	@Override
	public void stateChanged(ChangeEvent e) {

		//every time the sliders are updated, creates a picture that goes through all three adjustment methods
		//Then set the picture view to view that object
		MutablePixelArrayPicture picture = blurAdjustment(this.picture, blur.getValue(), saturation.getValue(), brightness.getValue());

		picture_view.setPicture(picture.createObservable());

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

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
	
	//This method takes in the encapsulated picture and three values of blur, saturation, and brightness
	//and creates a pixelarray with those adjustments based off encapsulated picture, then returns a picture
	//based off that picture array.
	private MutablePixelArrayPicture blurAdjustment(Picture picture, int blurValue, double saturationValue, double brightnessValue) {
		Pixel[][] pixelArray = new Pixel[picture.getWidth()][picture.getHeight()];

		//Blur adjustment
		//outer for loop, represents looping through each Pixel
		for(int x = 0; x<picture.getWidth(); x++) {
			for(int y = 0; y<picture.getHeight(); y++) {
				//count represents the total amount of pixels being added up
				//to solve for average, will divide each total color by this 
				//number for average, then can set the pixel to that
				int count = 0;
				double totalRed = 0;
				double totalGreen = 0;
				double totalBlue = 0;
				//these nested for loops represent the square around each pixel
				//that will be averaged for each pixel
				for(int i = x-(1*blurValue); i <= x+blurValue; i++) {
					for(int j = y-(1*blurValue); j <= y+blurValue; j++) {
						// Out of bounds on the picture does not matter, simply blur what is nearest it
						try {
							totalRed += picture.getPixel(i, j).getRed();
							totalGreen += picture.getPixel(i, j).getGreen();
							totalBlue += picture.getPixel(i, j).getBlue();
							count++;
						} catch(RuntimeException e) {

						}

					}
				}
				pixelArray[x][y] = new ColorPixel(totalRed/count, totalGreen/count, totalBlue/count);
			}
		}

		//Brightness adjustment

		//Outer nested for loops, represents looping through each Pixel.
		//Note: Need to use pixel array instead of underlying picture, as
		//underlying picture still has original pixel values before blur
		//adjustment
		for(int x = 0; x<picture.getWidth(); x++) {
			for(int y = 0; y<picture.getHeight(); y++) {

				if(brightnessValue<0) {
					pixelArray[x][y] = pixelArray[x][y].darken((-1)*brightnessValue/100); //need the -1 because otherwise would be passing in a negative
				}
				else {
					pixelArray[x][y] = pixelArray[x][y].lighten(brightnessValue/100);
				}	
			}

		}

		//Saturation adjustment

		//Outer nested for loops, represents looping through each Pixel
		//Note: Need to use pixel array instead of underlying picture, as
		//underlying picture still has original pixel values before blur
		//adjustment
		for(int x = 0; x<picture.getWidth(); x++) {
			for(int y = 0; y<picture.getHeight(); y++) {
				//call out red green blue and brightness values for ease of use
				double red = pixelArray[x][y].getRed();
				double green = pixelArray[x][y].getGreen();
				double blue = pixelArray[x][y].getBlue();
				double brightness = pixelArray[x][y].getIntensity();
				double largestValue = Math.max(red, Math.max(green, blue));

				//if statement for black pixel
				if(largestValue<.01) {
					continue; //nothing happens (i.e. Black Pixel stays the same)
				}

				if(saturationValue<0) {
					//new = old * (1.0 + (f / 100.0) ) - (b * f / 100.0)
					double newRed = red * (1.0 + (saturationValue/100.0)) - (brightness * saturationValue /100.0);
					double newGreen = green * (1.0 + (saturationValue/100.0)) - (brightness * saturationValue /100.0);
					double newBlue = blue * (1.0 + (saturationValue/100.0)) - (brightness * saturationValue /100.0);
					pixelArray[x][y] = new ColorPixel(newRed, newGreen, newBlue);
				}
				else {
					//new = old * ((lc + ((1.0 - lc) * (f / 100.0))) / lc)
					double newRed = red * ((largestValue + ((1.0-largestValue)*(saturationValue/100.0)))/largestValue);
					double newGreen = green * ((largestValue + ((1.0-largestValue)*(saturationValue/100.0)))/largestValue);
					double newBlue = blue * ((largestValue + ((1.0-largestValue)*(saturationValue/100.0)))/largestValue);
					pixelArray[x][y] = new ColorPixel(newRed, newGreen, newBlue);
				}
			}
		}

		//Create a new picture with adjustments
		return new MutablePixelArrayPicture(pixelArray, picture.getCaption());
	}
}
