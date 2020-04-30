package template.library;


import processing.core.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

//import de.quippy.*;
import de.quippy.javamod.main.JavaModMainBase;
import de.quippy.javamod.main.gui.PlayThread;
import de.quippy.javamod.main.gui.PlayThreadEventListener;
import de.quippy.javamod.main.playlist.PlayList;
import de.quippy.javamod.mixer.Mixer;
import de.quippy.javamod.multimedia.MultimediaContainer;
import de.quippy.javamod.multimedia.MultimediaContainerManager;
import de.quippy.javamod.multimedia.mod.ModContainer;
import de.quippy.javamod.multimedia.mod.loader.pattern.*;
import de.quippy.javamod.system.Log;
import java.lang.reflect.*;

/*
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class HelloLibrary extends JavaModMainBase implements PlayThreadEventListener
{
	
	// myParent is a reference to the parent sketch
	PApplet myParent;
	
	Method modRowEvent;
	Method modPatternEvent;
	
	private int myVariable = 0;
	private URL modFileName;
	public final static String VERSION = "##library.prettyVersion##";
	public MultimediaContainer currentContainer;
	private File wavFileName;
	private PlayThread playerThread = null;
	private PlayList currentPlayList = null;
	
	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public HelloLibrary(PApplet theParent) {
		super(false);
		myParent = theParent;
		initJavaMod();
		
		// check to see if the host applet implements
	    // public void fancyEvent(FancyLibrary f)
	    try {
	      modRowEvent = myParent.getClass().getMethod("modRowEvent",
	                                    new Class[] { HelloLibrary.class });
	    } catch (Exception e) {
	      // no such method, or an error.. which is fine, just ignore
	    }
	    try {
	      modPatternEvent = myParent.getClass().getMethod("modRowEvent",
	                                    new Class[] { HelloLibrary.class });
	    } catch (Exception e) {
	      // no such method, or an error.. which is fine, just ignore
	    }
	}
	
	
	private void initJavaMod() 
	{
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	
		Properties props = new Properties();
		props.setProperty(ModContainer.PROPERTY_PLAYER_ISP, "3"); // interpolation: 0:none; 1:linear; 2:cubic spline; 3:fir interpolation
		props.setProperty(ModContainer.PROPERTY_PLAYER_STEREO, "2");
		props.setProperty(ModContainer.PROPERTY_PLAYER_WIDESTEREOMIX, "TRUE");
		props.setProperty(ModContainer.PROPERTY_PLAYER_NOISEREDUCTION, "FALSE");
		props.setProperty(ModContainer.PROPERTY_PLAYER_MEGABASS, "FALSE");
		props.setProperty(ModContainer.PROPERTY_PLAYER_NOLOOPS, "0"); //set infinit loop handling: 0:original; 1:fade out; 2:ignore"
		props.setProperty(ModContainer.PROPERTY_PLAYER_MSBUFFERSIZE, "30");
		props.setProperty(ModContainer.PROPERTY_PLAYER_BITSPERSAMPLE, "16");
		props.setProperty(ModContainer.PROPERTY_PLAYER_FREQUENCY, "44100");
		String fileName = "/home/arnaud/81030-psychotech.mod";
		try
		{
			modFileName = new URL(fileName);
		}
		catch (MalformedURLException ex) // This is evil, but I dont want to test on local files myself...
		{
			try
			{
				modFileName = (new File(fileName)).toURI().toURL();
			}
			catch (MalformedURLException exe) // This is even more evil...
			{
				Log.error("This is not parsable: " + fileName, ex);
				System.exit(-1);
			}
		}
		
		MultimediaContainerManager.configureContainer(props);

		try 
		{
			MultimediaContainer newContainer = MultimediaContainerManager.getMultimediaContainer("/home/arnaud/81030-psychotech.mod");
			if (newContainer!=null) currentContainer = newContainer;
		}
		catch (Throwable ex)
    	{
			Log.error("[MainForm::loadMultimediaFile] Loading failure", ex);
			currentContainer = null;
    	}
    	finally
    	{
    		Mixer mixer = createNewMixer();
    		mixer.setExportFile(wavFileName);
    		playerThread = new PlayThread(mixer, this);
    		mixer.setListener(this, playerThread);
    		playerThread.start();
    	}
	}
	
	
	public String sayHello() {
		return "hello library.";
	}
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * 
	 * @param theA the width of test
	 * @param theB the height of test
	 */
	public void setVariable(int theA, int theB) {
		myVariable = theA + theB;
	}

	/**
	 * 
	 * @return int
	 */
	public int getVariable() {
		return myVariable;
	}
	
	private Mixer createNewMixer()
	{
		Mixer mixer = currentContainer.createNewMixer();
		if (mixer!=null)
		{
			mixer.setVolume(1.0f);
		}
		return mixer;
	}
	
	public void playThreadEventOccured(PlayThread thread)
	{
		System.out.println("Blaaaa");
	}
	
	public void mixerEventOccured(int rowIndex, PatternRow row) 
	{
		System.out.println(rowIndex + ":" 
				+ row.toString());
	};
}

