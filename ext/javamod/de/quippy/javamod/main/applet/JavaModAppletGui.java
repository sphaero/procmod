/*
 * @(#) JavaModAppletGui.java
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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import de.quippy.javamod.main.gui.PlayThread;
import de.quippy.javamod.main.gui.components.LEDScrollPanel;
import de.quippy.javamod.system.Helpers;
import de.quippy.javamod.system.Log;

/**
 * @author Daniel Becker
 * @since 12.07.2009
 */
public class JavaModAppletGui extends JavaModAppletBase
{
	private static final long serialVersionUID = 1908155835317607193L;

	private javax.swing.JButton button_GIF = null;
	private LEDScrollPanel ledScrollPanel = null;
	
	protected URL guiFileName;

	private URL guiUrl = null;
	private Insets dim_Buttons[] = null;
	private Insets dim_LEDScroller = null;
	private Color dim_LEDScroller_light = null;
	private Color dim_LEDScroller_dark = null;
	
	private static final String PROPERTY_UI_URL = "ui.skin";
	private static final String PROPERTY_UI_LED = "ui.led";
	private static final String PROPERTY_UI_LED_LIGHTCOLOR = "ui.led.lightcolor";
	private static final String PROPERTY_UI_LED_DARKCOLOR = "ui.led.darkcolor";
	private static final String PROPERTY_UI_BUTTONS[] = new String []
    {
     	"ui.stop", "ui.pause", "ui.start", "ui.prev", "ui.next" 
	};

	private javax.swing.JButton getButton_GIF()
	{
		if (button_GIF == null)
		{
			ImageIcon icon = new javax.swing.ImageIcon(guiUrl);
			button_GIF = new javax.swing.JButton();
			button_GIF.setName("button_GIF");
			button_GIF.setText("");
			button_GIF.setFocusPainted(false);
			button_GIF.setBorderPainted(false);
			button_GIF.setIcon(icon);
			button_GIF.setBounds(0,0, icon.getIconWidth(), icon.getIconHeight());
			button_GIF.addMouseMotionListener(new MouseMotionListener()
			{

				/**
				 * @param e
				 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
				 */
				public void mouseDragged(MouseEvent e)
				{
				}

				/**
				 * @param e
				 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
				 */
				public void mouseMoved(MouseEvent e)
				{
					boolean set = false;
					Point position = e.getPoint();
					for (int i=0; i<PROPERTY_UI_BUTTONS.length; i++)
					{
						if (position.x>=dim_Buttons[i].left && position.x<=dim_Buttons[i].right &&
							position.y>=dim_Buttons[i].top && position.y<=dim_Buttons[i].bottom)
						{
							button_GIF.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							set=true;
							break;
						}
					}
					if (!set) button_GIF.setCursor(Cursor.getDefaultCursor());
				}
			});
			button_GIF.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					Point position = e.getPoint();
					for (int i=0; i<PROPERTY_UI_BUTTONS.length; i++)
					{
						if (position.x>=dim_Buttons[i].left && position.x<=dim_Buttons[i].right &&
							position.y>=dim_Buttons[i].top && position.y<=dim_Buttons[i].bottom)
						{
							switch (i)
							{
								case 0: doStopPlaying(); break;
								case 1: doPausePlaying(); break;
								case 2: doStartPlaying(); break;
								case 3: doPrevPlayListEntry(); break;
								case 4: doNextPlayListEntry(); break;
							}
							break;
						}
					}
				}
			});
		}
		return button_GIF;
	}
	public LEDScrollPanel getLEDScrollPanel()
	{
		if (ledScrollPanel==null)
		{
			Dimension d = new Dimension(dim_LEDScroller.right-dim_LEDScroller.left, dim_LEDScroller.bottom-dim_LEDScroller.top);
			int chars = (int)(d.getWidth() / 6.0);
			ledScrollPanel = new LEDScrollPanel(30, Helpers.FULLVERSION + ' ' + Helpers.COPYRIGHT + "                  ", chars, dim_LEDScroller_light, dim_LEDScroller_dark);
			ledScrollPanel.setBorder(BorderFactory.createEmptyBorder());
			ledScrollPanel.setBounds(dim_LEDScroller.left, dim_LEDScroller.top, dim_LEDScroller.right-dim_LEDScroller.left, dim_LEDScroller.bottom-dim_LEDScroller.top);
		}
		return ledScrollPanel;
	}

	/**
	 * Constructor for JavaModAppletGui
	 * @throws HeadlessException
	 */
	public JavaModAppletGui() throws HeadlessException
	{
		super();
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#parseParameters()
	 */
	@Override
	protected void parseParameters()
	{
		super.parseParameters();
		String value = getParameter("ui-properties");
		try
		{
			if (value!=null) guiFileName = new URL(value);
		}
		catch (MalformedURLException ex)
		{
			Log.error("[Applet::parseParameters]", ex);
		}
	}
	public void initGui()
	{
		parseGuiFile(guiFileName);
		this.setLayout(null);
		add(getButton_GIF());
		add(getLEDScrollPanel());
	}

	private void parseGuiFile(URL guiFile)
	{
		try
		{
			java.util.Properties props = new java.util.Properties();
			props.load(guiFile.openStream());
			guiUrl = new URL(props.getProperty(PROPERTY_UI_URL));
			dim_LEDScroller = Helpers.getInsetsFromString(props.getProperty(PROPERTY_UI_LED, "0,0,0,0"));
			dim_LEDScroller_light = Helpers.getColorFromString(props.getProperty(PROPERTY_UI_LED_LIGHTCOLOR, "0,0,0"));
			dim_LEDScroller_dark = Helpers.getColorFromString(props.getProperty(PROPERTY_UI_LED_DARKCOLOR, "0,0,0"));
			dim_Buttons = new Insets[PROPERTY_UI_BUTTONS.length];
			for (int i=0; i<PROPERTY_UI_BUTTONS.length; i++)
			{
				dim_Buttons[i] = Helpers.getInsetsFromString(props.getProperty(PROPERTY_UI_BUTTONS[i], "0,0,0,0"));
			}
		}
		catch (Throwable ex)
		{
			Log.error("[JavaModAppletGui::parseGuiFile]", ex);
		}
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#doPausePlaying()
	 */
	@Override
	public void doPausePlaying()
	{
		if (playerThread!=null)
		{
			playerThread.pausePlay();
		}
		else
			doStartPlaying();
	}
	/**
	 * @param thread
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#playThreadEventOccured(de.quippy.javamod.main.gui.PlayThread)
	 */
	@Override
	public void playThreadEventOccured(PlayThread thread)
	{
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#setPlayListIcons()
	 */
	@Override
	protected void setPlayListIcons()
	{
	}
	/**
	 * @param newSongName
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#setNewSongName(java.lang.String)
	 */
	@Override
	protected void setNewSongName(String newSongName)
	{
		getLEDScrollPanel().setScrollTextTo(newSongName + Helpers.SCROLLY_BLANKS);
	}
}