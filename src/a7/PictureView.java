package a7;
import java.awt.Canvas; //blank rectangular area
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
//will need to add mouse listeners and key listeners
//Place this object into a UI container, attach listeners to automatically respond to UI events

//Picture view paints the encapsualted picture onto screen, it registers itself as an observer and if the picture notifies update it will repaint

public class PictureView extends Canvas implements ROIObserver {

	private ObservablePicture picture;
	private BufferedImage buffered_image; //what is a buffered image?
	
	public PictureView(ObservablePicture p) {
		setPicture(p);
	}

	public void setPicture(ObservablePicture p) {
		if (picture == p) { //if same picture
			return;
		}
		
		if (picture != null) { //if different picture unregister observer
			picture.unregisterROIObserver(this);
		}
		
		picture = p;
		picture.registerROIObserver(this);
		buffered_image = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.setPreferredSize(new Dimension(p.getWidth(), p.getHeight()));
		this.setSize(new Dimension(p.getWidth(), p.getHeight()));
		notify(picture, new RegionImpl(0,0,p.getWidth()-1,p.getHeight()-1));		
	}
	
	public ObservablePicture getPicture() {
		return picture;
	}
	
	public void paint(Graphics g) {
		g.drawImage(buffered_image, 0, 0, this);
	}

	public void notify(ObservablePicture picture, Region area) {
		for (int x=area.getLeft(); x<=area.getRight(); x++) {
			for (int y=area.getTop(); y<=area.getBottom(); y++) {
				buffered_image.setRGB(x, y, A7Helper.pixelToRGB(picture.getPixel(x, y)));
			}
		}
		repaint();
	}
}
