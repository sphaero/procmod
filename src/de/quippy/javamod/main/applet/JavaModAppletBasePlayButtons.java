/*
 * @(#) JavaModAppletBasePlayButtons.java
 *
 * Created on 06.05.2011 by Daniel Becker
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

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import de.quippy.javamod.main.gui.PlayThread;
import de.quippy.javamod.mixer.Mixer;
import de.quippy.javamod.system.Helpers;

/**
 * @author Daniel Becker
 *
 */
public abstract class JavaModAppletBasePlayButtons extends JavaModAppletBase
{
	private static final long serialVersionUID = 2861808830341641387L;

	public static final String BUTTONPLAY_INACTIVE = "/de/quippy/javamod/main/gui/ressources/play.gif";
	public static final String BUTTONPLAY_ACTIVE = "/de/quippy/javamod/main/gui/ressources/play_aktiv.gif";
	public static final String BUTTONPLAY_NORMAL = "/de/quippy/javamod/main/gui/ressources/play_normal.gif";
	public static final String BUTTONPAUSE_INACTIVE = "/de/quippy/javamod/main/gui/ressources/pause.gif";
	public static final String BUTTONPAUSE_ACTIVE = "/de/quippy/javamod/main/gui/ressources/pause_aktiv.gif";
	public static final String BUTTONPAUSE_NORMAL = "/de/quippy/javamod/main/gui/ressources/pause_normal.gif";
	public static final String BUTTONSTOP_INACTIVE = "/de/quippy/javamod/main/gui/ressources/stop.gif";
	public static final String BUTTONSTOP_ACTIVE = "/de/quippy/javamod/main/gui/ressources/stop_aktiv.gif";
	public static final String BUTTONSTOP_NORMAL = "/de/quippy/javamod/main/gui/ressources/stop_normal.gif";
	public static final String BUTTONPREV_INACTIVE = "/de/quippy/javamod/main/gui/ressources/prev.gif";
	public static final String BUTTONPREV_ACTIVE = "/de/quippy/javamod/main/gui/ressources/prev_aktiv.gif";
	public static final String BUTTONPREV_NORMAL = "/de/quippy/javamod/main/gui/ressources/prev_normal.gif";
	public static final String BUTTONNEXT_INACTIVE = "/de/quippy/javamod/main/gui/ressources/next.gif";
	public static final String BUTTONNEXT_ACTIVE = "/de/quippy/javamod/main/gui/ressources/next_aktiv.gif";
	public static final String BUTTONNEXT_NORMAL = "/de/quippy/javamod/main/gui/ressources/next_normal.gif";

	private javax.swing.ImageIcon buttonPlay_Active = null;
	private javax.swing.ImageIcon buttonPlay_Inactive = null;
	private javax.swing.ImageIcon buttonPlay_normal = null;
	private javax.swing.ImageIcon buttonPause_Active = null;
	private javax.swing.ImageIcon buttonPause_Inactive = null;
	private javax.swing.ImageIcon buttonPause_normal = null;
	private javax.swing.ImageIcon buttonStop_Active = null;
	private javax.swing.ImageIcon buttonStop_Inactive = null;
	private javax.swing.ImageIcon buttonStop_normal = null;
	private javax.swing.ImageIcon buttonPrev_Active = null;
	private javax.swing.ImageIcon buttonPrev_Inactive = null;
	private javax.swing.ImageIcon buttonPrev_normal = null;
	private javax.swing.ImageIcon buttonNext_Active = null;
	private javax.swing.ImageIcon buttonNext_Inactive = null;
	private javax.swing.ImageIcon buttonNext_normal = null;

	private javax.swing.JButton button_Play = null;
	private javax.swing.JButton button_Pause = null;
	private javax.swing.JButton button_Stop = null;
	private javax.swing.JButton button_Prev = null;
	private javax.swing.JButton button_Next = null;

	private JPanel playerControlPanel = null;

	/**
	 * @throws HeadlessException
	 * @since 06.05.2011
	 */
	public JavaModAppletBasePlayButtons() throws HeadlessException
	{
		super();
	}
	protected javax.swing.JButton getButton_Play()
	{
		if (button_Play == null)
		{
			buttonPlay_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_NORMAL));
			buttonPlay_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_INACTIVE));
			buttonPlay_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_ACTIVE));

			button_Play = new javax.swing.JButton();
			button_Play.setName("button_Play");
			button_Play.setText("");
			button_Play.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Play.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Play.setIcon(buttonPlay_normal);
			button_Play.setDisabledIcon(buttonPlay_Inactive);
			button_Play.setPressedIcon(buttonPlay_Active);
			button_Play.setMargin(new java.awt.Insets(0, 2, 0, 2));
			button_Play.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doStartPlaying();
				}
			});
		}
		return button_Play;
	}
	protected javax.swing.JButton getButton_Pause()
	{
		if (button_Pause == null)
		{
			buttonPause_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_NORMAL));
			buttonPause_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_INACTIVE));
			buttonPause_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_ACTIVE));

			button_Pause = new javax.swing.JButton();
			button_Pause.setName("button_Pause");
			button_Pause.setText("");
			button_Pause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Pause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Pause.setIcon(buttonPause_normal);
			button_Pause.setDisabledIcon(buttonPause_Inactive);
			button_Pause.setPressedIcon(buttonPause_Active);
			button_Pause.setMargin(new java.awt.Insets(0, 2, 0, 2));
			button_Pause.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doPausePlaying();
				}
			});
		}
		return button_Pause;
	}
	protected javax.swing.JButton getButton_Stop()
	{
		if (button_Stop == null)
		{
			buttonStop_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_NORMAL));
			buttonStop_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_INACTIVE));
			buttonStop_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_ACTIVE));

			button_Stop = new javax.swing.JButton();
			button_Stop.setName("button_Stop");
			button_Stop.setText("");
			button_Stop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Stop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Stop.setIcon(buttonStop_normal);
			button_Stop.setDisabledIcon(buttonStop_Inactive);
			button_Stop.setPressedIcon(buttonStop_Active);
			button_Stop.setMargin(new java.awt.Insets(0, 2, 0, 2));
			button_Stop.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doStopPlaying();
				}
			});
		}
		return button_Stop;
	}
	protected javax.swing.JButton getButton_Prev()
	{
		if (button_Prev == null)
		{
			buttonPrev_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPREV_NORMAL));
			buttonPrev_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPREV_INACTIVE));
			buttonPrev_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPREV_ACTIVE));

			button_Prev = new javax.swing.JButton();
			button_Prev.setName("button_Prev");
			button_Prev.setText("");
			button_Prev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Prev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Prev.setIcon(buttonPrev_normal);
			button_Prev.setDisabledIcon(buttonPrev_Inactive);
			button_Prev.setPressedIcon(buttonPrev_Active);
			button_Prev.setMargin(new java.awt.Insets(0, 2, 0, 2));
			button_Prev.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doPrevPlayListEntry();
				}
			});
		}
		return button_Prev;
	}
	protected javax.swing.JButton getButton_Next()
	{
		if (button_Next == null)
		{
			buttonNext_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONNEXT_NORMAL));
			buttonNext_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONNEXT_INACTIVE));
			buttonNext_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONNEXT_ACTIVE));

			button_Next = new javax.swing.JButton();
			button_Next.setName("button_Next");
			button_Next.setText("");
			button_Next.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Next.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Next.setIcon(buttonNext_normal);
			button_Next.setDisabledIcon(buttonNext_Inactive);
			button_Next.setPressedIcon(buttonNext_Active);
			button_Next.setMargin(new java.awt.Insets(0, 2, 0, 2));
			button_Next.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doNextPlayListEntry();
				}
			});
		}
		return button_Next;
	}
	protected JPanel getPlayerControlPanel()
	{
		if (playerControlPanel==null)
		{
			playerControlPanel = new JPanel();
			playerControlPanel.setName("playerControlPanel");
			playerControlPanel.setLayout(new java.awt.GridBagLayout());
			playerControlPanel.add(getButton_Prev(), 		Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			playerControlPanel.add(getButton_Play(), 		Helpers.getGridBagConstraint(1, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			playerControlPanel.add(getButton_Next(), 		Helpers.getGridBagConstraint(2, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			playerControlPanel.add(getButton_Pause(), 		Helpers.getGridBagConstraint(3, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			playerControlPanel.add(getButton_Stop(), 		Helpers.getGridBagConstraint(4, 0, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
			playerControlPanel.setOpaque(false);
		}
		return playerControlPanel;
	}
	/**
	 * @param thread
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#playThreadEventOccured(de.quippy.javamod.main.gui.PlayThread)
	 * @since 06.05.2011
	 */
	@Override
	public void playThreadEventOccured(PlayThread thread)
	{
		if (thread.isRunning())
		{
			getButton_Play().setIcon(buttonPlay_Active);
		}
		else
		{
			getButton_Play().setIcon(buttonPlay_normal);
			if (thread.getHasFinishedNormaly())
			{
				boolean ok = doNextPlayListEntry();
				if (!ok) doStopPlaying();
			}
		}
		
		Mixer mixer = thread.getCurrentMixer();
		if (mixer!=null)
		{
			if (mixer.isPaused())
				getButton_Pause().setIcon(buttonPause_Active);
			else
				getButton_Pause().setIcon(buttonPause_normal);
		}
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#setPlayListIcons()
	 * @since 06.05.2011
	 */
	@Override
	protected void setPlayListIcons()
	{
		if (getCurrentPlayList()==null)
		{
			getButton_Prev().setEnabled(false);
			getButton_Next().setEnabled(false);
		}
		else
		{
			getButton_Prev().setEnabled(getCurrentPlayList().hasPrevious());
			getButton_Next().setEnabled(getCurrentPlayList().hasNext());
		}
	}
}
