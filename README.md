# Assignment 7

In this assignment you will build one or more UI-based mini-applications that displays and manipulates ObservablePicture objects. These little mini-applications are simple enough that can be implemented failry easily as a single self-contained UI widget. In order to display ObservablePicture objects on to the screen, we need to make use of some built-in Java library classes for images. In order to shield you from the details of these classes, I have create a subclass of the Java Canvas class called PictureView that is capable of displaying an ObservablePicture object which you should use in your solutions. You don't really need to understand how it works internally to use it, but in order to support this object, we need a few supporting functions. Peruse the code here make note in particular of the following:

* The Picture interface now includes a createObservable() method.
  This method basically converts a Picture object into an ObservablePicture object. It is implemented as a default method in the Picture interface.
  
* An overloaded version of registerROIObserver has been added to ObservablePicture that simply takes the observer and assumes that you are interested in the entire picture.

* A helper class called A7Helper has been added.
  This class contains three static methods that are needed to support our new PictureView class. Thes methods are:
  
  * public static Picture readFromURL(String url)
    This method takes a URL string (i.e., a web address) for a picture and , if successful, constructs a Picture object that represents the picture. It may throw an IOException if something goes wrong.
  * public static int pixelToRGB(Pixel p)
    Java's classes for dealing with images represent pixels as integers with different bits within the integer representing the red, green, and blue components. This function converts our Pixel objects into the appropriate corresponding packed integer.
  * public static Pixel rgbToPixel(int rgb)
    This function does the conversion between a packed RGB integer color and our Pixel representation.

* A new class called PictureView has been added.
  This class is a subclass of the Java Canvas user-interface component. It accepts an ObservablePicture object as a parameter to its constructor. A PictureView object simply paints the encapsulated ObservablePicture object on the screen. You can use the setPicture() method to change what ObservablePicture object it is painting. It implements ROIObserver and attaches itself to the ObservablePicture object that it encapsulates so that if the ObservablePicture object changes, it can repaint the picture automatically. You use it like any other user interface object (i.e., place this object into a UI container, attach listeners in order to respond to user interface events, etc.). As a subclass of Canvas, it supports all of the same event listeners that Canvas does. You will mostly just need to be able to add mouse listeners and key listeners for this assignment.

* A UI widget called SimplePictureViewWidget has been provided.
  This widget is provided a Picture object to its constructor. The widget creates a user-interface comprising of a PictureView object to display the Picture object and a JLabel object to display the picture's caption. It also handles mouse click events on the PictureView object by printing the x and y coordinates to the console whenever a click occurs. You can use this object as a basic pattern for using PictureView in your own widgets.
  
The class A7SetupTest is a simple test application that creates a Picture object from a URL using the A7Helper.readFromURL() method, creates a new top-level user interface window with a SimplePictureViewWidget to display the Picture object along with a title.

If your setup is working correctly, you should be able to run A7SetupTest and see me appear on the screen along with the caption "KMP in Namibia". Mouse clicks on the image should print coordinates of the click to the console. If all is well, it will look like this:

<img src="http://www.cs.unc.edu/~kmp/comp401fall18/assignments/a7/a7-setup.png" width="500"</img>

Follow the pattern above to create one or more of the following mini-applications. You are free to use a different image than the one of me used in the test application, but please don't use anything too large, copyrighted, or NSFW. Also, because we are loading these images by URL, they need to be web accessible. 

**IMPORTANT: In the descriptions below, the name of the application should be the name of the main class (i.e., the one with main() to run).**

These are listed in the order of increasing complexity (and thus point value). For each, there may be many different ways to approach and architect your solution. We will be grading both for basic functionality as well as the design and readability of your code.

## PixelInspector
(5 points)

This application should load an image and when clicked display information about the pixel where the click occurs. Here is what my solution for this application looks like:
