/*
 * @(#) JavaModApplet.java
 *
 * Created on 26.12.2007 by Daniel Becker
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
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.border.BevelBorder;

import de.quippy.javamod.main.gui.components.LEDScrollPanel;
import de.quippy.javamod.main.gui.components.SeekBarPanel;
import de.quippy.javamod.mixer.Mixer;
import de.quippy.javamod.system.Helpers;

/**
 * @author Daniel Becker
 * @since 26.12.2007
 */
public class JavaModApplet extends JavaModAppletBasePlayButtons
{
	private static final long serialVersionUID = 2434056226963914737L;
	
	private LEDScrollPanel ledScrollPanel = null;
	private SeekBarPanel seekBarPanel = null;

	/**
	 * Constructor for JavaModApplet
	 * @throws HeadlessException
	 */
	public JavaModApplet() throws HeadlessException
	{
		super();
	}
	public LEDScrollPanel getLEDScrollPanel()
	{
		final int chars = 10; // show 9 chars
		final int brick = 3;  // one brick is 3x3 pixel
		if (ledScrollPanel==null)
		{
			ledScrollPanel = new LEDScrollPanel(30, Helpers.FULLVERSION + ' ' + Helpers.COPYRIGHT + "                  ", chars, Color.GREEN, Color.GRAY);
			Dimension d = new Dimension((chars*brick*6)+4, (brick*8)+4);
			ledScrollPanel.setSize(d);
			ledScrollPanel.setMaximumSize(d);
			ledScrollPanel.setMinimumSize(d);
			ledScrollPanel.setPreferredSize(d);
			ledScrollPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return ledScrollPanel;
	}
	private SeekBarPanel getSeekBarPanel()
	{
		if (seekBarPanel==null)
		{
			seekBarPanel = new SeekBarPanel(30, true);
			seekBarPanel.setBackground(this.getBackground());
			seekBarPanel.setName("SeekBarPanel");
		}
		return seekBarPanel;
	}
	public void initGui()
	{
		this.setLayout(new java.awt.GridBagLayout());
		add(getLEDScrollPanel(),	Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
		add(getSeekBarPanel(),		Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.CENTER, 1.0, 1.0));
		add(getPlayerControlPanel(),Helpers.getGridBagConstraint(0, 2, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.CENTER, 1.0, 1.0));
		getPlayerControlPanel().setBackground(this.getBackground());
	}
	/**
	 * @return
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#createNewMixer()
	 * @since 28.04.2011
	 */
	@Override
	protected Mixer createNewMixer()
	{
		Mixer mixer = super.createNewMixer();
		getSeekBarPanel().setCurrentMixer(mixer);
		return mixer;
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.applet.JavaModAppletBase#removeMixer()
	 * @since 06.05.2011
	 */
	@Override
	protected void removeMixer()
	{
		super.removeMixer();
		getSeekBarPanel().setCurrentMixer(null);
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
