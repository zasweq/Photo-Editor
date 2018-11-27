package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class FramePuzzleWidget extends JPanel implements MouseListener, KeyListener{
	private Picture picture;
	private JPanel panel;
	private PictureView[][] frame_puzzle;
	//these will represent the x and y coordiates of
	//the white tile on the 5x5 grid of pictureview objects
	private int whiteX;
	private int whiteY;

	public FramePuzzleWidget(Picture picture) {
		this.picture = picture;
		whiteX = 4;
		whiteY = 4;


		panel = new JPanel();
		panel.addMouseListener(this);
		panel.addKeyListener(this);
		panel.setLayout(new GridLayout(5, 5));

		//"Grid" of picture view objects
		frame_puzzle = new PictureView[5][5];

		makeSubPictures(picture);

	}

	//create the "frames that are nailed on wall"
	//Will do this through a 2d array of PictureView objects

	public void makeSubPictures(Picture original) {
		int tileWidth = picture.getWidth()/5;
		int tileHeight = picture.getHeight()/5;

		//this represents the 5x5 grid of pictureview "frames"
		for(int x=0; x<5; x++) {
			for(int y=0; y<5; y++) {

				//this is the bottom right corner, fill it with a blank white picture
				if(x==4 && y==4) {

					Pixel[][] whitePicturePixels = new Pixel[tileWidth][tileHeight];
					for (int i = 0; i < tileWidth; i++) {
						for (int j = 0; j < tileHeight; j++) {
							whitePicturePixels[i][j] = new ColorPixel(1, 1, 1);
						}
					}

					MutablePixelArrayPicture whitePicture = new MutablePixelArrayPicture(whitePicturePixels, "White Picture");

					PictureView whitePictureView = new PictureView(whitePicture.createObservable());
					frame_puzzle[x][y] = whitePictureView;
					frame_puzzle[x][y].addMouseListener(this);
					frame_puzzle[x][y].addKeyListener(this);
				}
				//the rest of the picture
				else {
					SubPictureImpl subpicture = new SubPictureImpl(picture, x*tileWidth, y*tileHeight, tileWidth, tileHeight);
					PictureView subpictureview = new PictureView(subpicture.createObservable());
					frame_puzzle[x][y] = subpictureview;
					frame_puzzle[x][y].addMouseListener(this);
					frame_puzzle[x][y].addKeyListener(this);
					frame_puzzle[x][y].setFocusable(true); //need to be focusable for a keypress to affect component
				}
			}
		}

		//fill up the panel that actually displays puzzle
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				//need to do j, then i as you go through x values left to right
				panel.add(frame_puzzle[j][i]);
			}
		}
		add(panel, BorderLayout.CENTER);
		//Note: Once you set this grid up, do not need to set up again
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		//four possible, up, left, down, right
		switch(key) {

		//All four cases are nested in a try catch block because if a user presses an arrow
		//with no effect, it will throw an array index exception, but the program should
		//continue running. Each of these cases switches two pictures on the 5x5 grid.
		case KeyEvent.VK_UP:
			try {
				Picture temporary = frame_puzzle[whiteX][whiteY-1].getPicture();
				Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
				frame_puzzle[whiteX][whiteY - 1].setPicture(whitePicture.createObservable());
				frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());
				whiteY--; //the white picture moves up one on the 5x5 grid
			}
			catch(ArrayIndexOutOfBoundsException a) {

			}
			break;

		case KeyEvent.VK_DOWN:
			try {
				Picture temporary = frame_puzzle[whiteX][whiteY+1].getPicture();
				Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
				frame_puzzle[whiteX][whiteY + 1].setPicture(whitePicture.createObservable());
				frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());
				whiteY++; //the white picture moves down one on the 5x5 grid
			}
			catch(ArrayIndexOutOfBoundsException a) {

			}
			break;

		case KeyEvent.VK_LEFT:
			try {
				Picture temporary = frame_puzzle[whiteX-1][whiteY].getPicture();
				Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
				frame_puzzle[whiteX-1][whiteY].setPicture(whitePicture.createObservable());
				frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());
				whiteX--; //the white picture moves left one on the 5x5 grid
			}
			catch(ArrayIndexOutOfBoundsException a) {

			}
			break;

		case KeyEvent.VK_RIGHT:
			try {
				Picture temporary = frame_puzzle[whiteX+1][whiteY].getPicture();
				Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
				frame_puzzle[whiteX+1][whiteY].setPicture(whitePicture.createObservable());
				frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());
				whiteX++; //the white picture moves right one on the 5x5 grid
			}
			catch(ArrayIndexOutOfBoundsException a) {

			}	
			break;
		}

		setFocusable(false);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mousePressed(MouseEvent e) {
		int mouseX=-1;
		int mouseY=-1;

		//These for loops are checking where the click occurred at
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (e.getSource() == frame_puzzle[i][j]) {
					mouseX = i;
					mouseY = j;
				}
			}
		}

		//These for loops move the pictures based on conditions of where
		//click occurred at
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				
				//This condition means the same Column
				if (mouseX == whiteX) {
					
					//Checks if click was above or below of the white tile
					if (mouseY > whiteY) {
						
						//Steps represents how many times the picture should switch with the white tile
						int steps = mouseY - whiteY;
						
						//Move the white picture down
						for (int i = 0; i < steps; i++) {
							Picture temporary = frame_puzzle[whiteX][whiteY + 1].getPicture();
							Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
							frame_puzzle[whiteX][whiteY + 1].setPicture(whitePicture.createObservable());
							frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());

							whiteY++;


						}

					} else if (mouseY < whiteY) {
						//Steps represents how many times the picture should switch with the white tile
						int steps = whiteY - mouseY;
						//Move the white picture up
						for (int i = 0; i < steps; i++) {
							Picture temporary = frame_puzzle[whiteX][whiteY - 1].getPicture();
							Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
							frame_puzzle[whiteX][whiteY - 1].setPicture(whitePicture.createObservable());
							frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());

							whiteY--;

						}
					}
				}
				
				//This condition means the same row
				if (mouseY == whiteY) {
					//Checks if click was left or right of the white tile
					if (mouseX > whiteX) {
						//Steps represents how many times the picture should switch with the white tile
						int steps = mouseX - whiteX;
						//Move the white picture right
						for (int i = 0; i < steps; i++) {
							Picture temporary = frame_puzzle[whiteX+1][whiteY].getPicture();
							Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
							frame_puzzle[whiteX+1][whiteY].setPicture(whitePicture.createObservable());
							frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());
							whiteX++;
						}
						} 
					else if (mouseX < whiteX) {
						//Steps represents how many times the picture should switch with the white tile
						int steps = whiteX - mouseX;
						//Move the white picture left
						for (int i = 0; i < steps; i++) {
							Picture temporary = frame_puzzle[whiteX-1][whiteY].getPicture();
							Picture whitePicture = frame_puzzle[whiteX][whiteY].getPicture();
							frame_puzzle[whiteX-1][whiteY].setPicture(whitePicture.createObservable());
							frame_puzzle[whiteX][whiteY].setPicture(temporary.createObservable());
							whiteX--;

						}
					}
				}
			}
		}


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
