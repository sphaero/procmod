/*
 * @(#) JavaModAppletBase.java
 *
 * Created on 12.07.2009 by Daniel Becker
 * 
 *-----------------------------------------------------------------------
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */
package de.quippy.javamod.main.applet;

import java.applet.Applet;
import java.awt.HeadlessException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import de.quippy.javamod.main.gui.PatternRow;
import de.quippy.javamod.main.gui.PlayThread;
import de.quippy.javamod.main.gui.PlayThreadEventListener;
import de.quippy.javamod.main.playlist.PlayList;
import de.quippy.javamod.main.playlist.PlayListEntry;
import de.quippy.javamod.mixer.Mixer;
import de.quippy.javamod.multimedia.MultimediaContainer;
import de.quippy.javamod.multimedia.MultimediaContainerManager;
import de.quippy.javamod.multimedia.mod.ModContainer;
import de.quippy.javamod.multimedia.mod.ModMixer;
import de.quippy.javamod.multimedia.mod.mixer.BasicModMixer;
import de.quippy.javamod.system.Helpers;
import de.quippy.javamod.system.Log;
import de.quippy.javamod.multimedia.mod.loader.pattern.*;
/**
 * @author Daniel Becker
 * @since 12.07.2009
 */
public abstract class JavaModAppletBase extends Applet implements PlayThreadEventListener
{
	private static final long serialVersionUID = 7586730327373678282L;

	protected URL [] modFileName;
	protected boolean shuffle;
	protected boolean repeat;
	protected boolean autostart;
	protected float initialVolume;

	protected MultimediaContainer currentContainer;
	protected PlayThread playerThread = null;
	protected PlayList currentPlayList = null;

	static // THIS IS FROM JAVAMODMAINBASE FROM WICH THIS CLASS DOES NOT EXTEND
	{
		// Now load and initialize all classes, that should not be
		// initialized during play!
		try
		{
			Helpers.registerAllClasses();
		}
		catch (ClassNotFoundException ex)
		{
			Log.error("JavaModMainBase: a class moved?!", ex);
		}
	}

	/**
	 * Constructor for JavaModAppletBase
	 * @throws HeadlessException
	 */
	public JavaModAppletBase() throws HeadlessException
	{
		super();
		modFileName = null;
	}
	public void init()
	{
		try
		{
			parseParameters();
			initGui();
			loadMultimediaOrPlayListFile(modFileName);
			if (autostart) doStartPlaying();
		}
		catch (Exception ex)
		{
			Log.error("Error occured:", ex);
		}
	}
	
	public abstract void playThreadEventOccured(PlayThread thread);
	public void mixerEventOccured(int rowIndex, PatternRow row) {};
	protected abstract void setPlayListIcons();
	protected abstract void setNewSongName(String newSongName);
	protected abstract void initGui();
	
	protected PlayList getCurrentPlayList()
	{
		return currentPlayList;
	}
	public URL[] getModFileNames()
	{
		return modFileName;
	}
	public MultimediaContainer getCurrentContainer()
	{
		return currentContainer;
	}
	protected void parseParameters()
	{
		Properties props = new Properties();

		String value = getParameter("i");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_ISP, Integer.toString(Integer.parseInt(value)));
		value = getParameter("s");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_STEREO, (value.charAt(0)=='+')?"2":"1");
		value = getParameter("w");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_WIDESTEREOMIX, (value.charAt(0)=='+')?"TRUE":"FALSE");
		value = getParameter("n");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_NOISEREDUCTION, (value.charAt(0)=='+')?"TRUE":"FALSE");
		value = getParameter("l");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_NOLOOPS, Integer.toString(Integer.parseInt(value)));
		value = getParameter("m");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_MEGABASS, (value.charAt(0)=='+')?"TRUE":"FALSE");
		value = getParameter("r");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_FREQUENCY, Integer.toString(Integer.parseInt(value)));
		value = getParameter("t");
		if (value!=null) props.setProperty(ModContainer.PROPERTY_PLAYER_MSBUFFERSIZE, Integer.toString(Integer.parseInt(value)));
		value = getParameter("h");
		if (value!=null) shuffle = value.charAt(0)=='+'; 
		value = getParameter("j");
		if (value!=null) repeat = value.charAt(0)=='+'; 
		value = getParameter("a");
		if (value!=null) autostart = value.charAt(0)=='+'; else autostart = true;
		value = getParameter("v");
		if (value!=null) initialVolume = Float.parseFloat(value); else initialVolume = 1.0f;
		value = getParameter("b");
		if (value!=null)
		{
			int sampleSizeInBits = Integer.parseInt(value);
			if (sampleSizeInBits!=8 && sampleSizeInBits!=16 && sampleSizeInBits!=24)
				throw new RuntimeException("samplesize of " + sampleSizeInBits + " is not supported");
			props.setProperty(ModContainer.PROPERTY_PLAYER_BITSPERSAMPLE, Integer.toString(sampleSizeInBits));			
		}
		
		MultimediaContainerManager.configureContainer(props);
		
		value = getParameter("file");
		try
		{
			if (value!=null)
			{
				ArrayList<URL> files = new ArrayList<URL>();
				StringTokenizer tok = new StringTokenizer(value, ",");
				while (tok.hasMoreTokens())
				{
					URL url = Helpers.createURLfromString(tok.nextToken().trim());
					if (url!=null) files.add(url);
				}
				modFileName = files.toArray(new URL[files.size()]);
			}
		}
		catch (Throwable ex)
		{
			Log.error("[Applet::parseParameters]", ex);
		}
	}
	protected Mixer createNewMixer()
	{
		Mixer mixer = currentContainer.createNewMixer();
		if (mixer!=null)
		{
			mixer.setVolume(initialVolume);
		}
		return mixer;
	}
	protected void removeMixer()
	{
	}
	protected boolean loadMultimediaOrPlayListFile(URL[] mediaPLSFileURL)
	{
		currentPlayList = null;
    	try
    	{
    		currentPlayList = PlayList.createNewListWithFiles(mediaPLSFileURL, shuffle, repeat);
			return doNextPlayListEntry();
    	}
    	catch (Throwable ex)
    	{
			Log.error("[Applet::loadMultimediaOrPlayListFile]", ex);
			currentPlayList = null;
    	}
    	return false;
	}
	/**
	 * load a mod file and display it
	 * @since 01.07.2006
	 * @param modFileName
	 */
	protected boolean loadMultimediaFile(URL mediaFileURL)
	{
    	try
    	{
    		if (mediaFileURL!=null)
    		{
    			MultimediaContainer newContainer = MultimediaContainerManager.getMultimediaContainer(mediaFileURL);
    			if (newContainer!=null)
    			{
    				currentContainer = newContainer;
    				setNewSongName(currentContainer.getSongName());
    			}
    		}
    	}
    	catch (Throwable ex)
    	{
			Log.error("[Applet::loadMultimediaFile] Loading of " + mediaFileURL + " failed!", ex);
			return false;
    	}
		setPlayListIcons();
		// if we are currently playing, start the current piece:
		if (playerThread!=null) doStartPlaying();
		return true;
	}
	// PLAYER INFO METHOD FOR JAVASCRIPT
	public int getPlayingPosition()
	{
		if (playerThread!=null)
		{
			final Mixer mixer = playerThread.getCurrentMixer();
			if (mixer!=null && mixer instanceof ModMixer)
			{
				final BasicModMixer modMixer = ((ModMixer)mixer).getModMixer();
				if (modMixer!=null) return modMixer.getCurrentPatternPosition();
			}
		}
		return -1;
	}
	// PLAYER CONTROL METHODS ARE PUBLIC TO GET CALLED VIA JAVASCRIPT
	public boolean doNextPlayListEntry()
	{
		boolean ok = false;
		while (currentPlayList!=null && currentPlayList.hasNext() && !ok)
		{
			currentPlayList.next();
			ok = loadMultimediaFile(currentPlayList.getCurrentEntry().getFile());
		}
		return ok;
	}
	public boolean doPrevPlayListEntry()
	{
		boolean ok = false;
		while (currentPlayList!=null && currentPlayList.hasPrevious() && !ok)
		{
			currentPlayList.previous();
			ok = loadMultimediaFile(currentPlayList.getCurrentEntry().getFile());
		}
		return ok;
	}
	public void doStartPlaying()
	{
		if (currentContainer!=null)
		{
			doStopPlaying();
			playerThread = new PlayThread(createNewMixer(), this);
			playerThread.start();
		}
	}
	public void doStopPlaying()
	{
		if (playerThread!=null)
		{
			playerThread.stopMod();
			playerThread = null;
			removeMixer();
		}
	}
	public void doPausePlaying()
	{
		if (playerThread!=null)
		{
			playerThread.pausePlay();
		}
	}
	// PLAYLIST CONTROL METHODS FOR JAVASCRIPT CONTROL
	public void clearPlaylist()
	{
		currentPlayList = null;
		setPlayListIcons();
	}
	public void addFileToPlayList(String urlString)
	{
		URL url = Helpers.createURLfromString(urlString);
		if (url!=null)
		{
			if (currentPlayList == null)
			{
				loadMultimediaOrPlayListFile(new URL [] { url });
			}
			else
			{
				currentPlayList.addEntry(new PlayListEntry(url, currentPlayList));
			}
		}
	}
	public void playFile(String urlString)
	{
		URL url = Helpers.createURLfromString(urlString);
		if (url!=null) loadMultimediaOrPlayListFile(new URL [] { url });
	}
}
