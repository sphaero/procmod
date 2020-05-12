/*
 * @(#) ModInfoPanel.java
 *
 * Created on 13.10.2007 by Daniel Becker
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
package de.quippy.javamod.multimedia.mod;

import java.awt.LayoutManager;
import java.io.File;

import javax.swing.JPanel;

import de.quippy.javamod.multimedia.mod.loader.Module;
import de.quippy.javamod.system.Helpers;

/**
 * @author Daniel Becker
 * @since 13.10.2007
 */
public class ModInfoPanel extends JPanel
{
	private static final long serialVersionUID = 6435757622273854276L;

	private javax.swing.JLabel modInfo_L_Filename = null;
	private javax.swing.JTextField modInfo_Filename = null;
	private javax.swing.JLabel modInfo_L_Modname = null;
	private javax.swing.JTextField modInfo_Modname = null;
	private javax.swing.JLabel modInfo_L_Instruments = null;
	private javax.swing.JScrollPane scrollPane_ModInfo_Instruments = null;
	private javax.swing.JTextArea modInfo_Instruments = null;
	private javax.swing.JLabel modInfo_L_Trackername = null;
	private javax.swing.JTextField modInfo_Trackername = null;

	/**
	 * Constructor for ModInfoPanel
	 */
	public ModInfoPanel()
	{
		super();
		initialize();
	}
	/**
	 * Constructor for ModInfoPanel
	 * @param layout
	 */
	public ModInfoPanel(LayoutManager layout)
	{
		super(layout);
		initialize();
	}
	/**
	 * Constructor for ModInfoPanel
	 * @param isDoubleBuffered
	 */
	public ModInfoPanel(boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
		initialize();
	}
	/**
	 * Constructor for ModInfoPanel
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public ModInfoPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
		initialize();
	}
	private void initialize()
	{
		this.setName("ModInfoPane");
		this.setLayout(new java.awt.GridBagLayout());

		this.add(getModInfo_L_Filename(),				Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getModInfo_Filename(),					Helpers.getGridBagConstraint(1, 0, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		this.add(getModInfo_L_Modname(),				Helpers.getGridBagConstraint(0, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getModInfo_Modname(),					Helpers.getGridBagConstraint(1, 1, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		this.add(getModInfo_L_Instruments(),			Helpers.getGridBagConstraint(0, 2, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getScrollPane_ModInfo_Instruments(),	Helpers.getGridBagConstraint(0, 3, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.WEST, 1.0, 1.0));
		this.add(getModInfo_L_Trackername(),			Helpers.getGridBagConstraint(0, 4, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getModInfo_Trackername(),				Helpers.getGridBagConstraint(1, 4, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
	}
	public javax.swing.JLabel getModInfo_L_Filename()
	{
		if (modInfo_L_Filename==null)
		{
			modInfo_L_Filename = new javax.swing.JLabel();
			modInfo_L_Filename.setName("modInfo_L_Filename");
			modInfo_L_Filename.setText("File");
			modInfo_L_Filename.setFont(Helpers.DIALOG_FONT);
		}
		return modInfo_L_Filename;
	}
	public javax.swing.JTextField getModInfo_Filename()
	{
		if (modInfo_Filename==null)
		{
			modInfo_Filename = new javax.swing.JTextField();
			modInfo_Filename.setName("modInfo_Filename");
			modInfo_Filename.setFont(Helpers.DIALOG_FONT);
			modInfo_Filename.setEditable(false);
		}
		return modInfo_Filename;
	}
	public javax.swing.JLabel getModInfo_L_Instruments()
	{
		if (modInfo_L_Instruments==null)
		{
			modInfo_L_Instruments = new javax.swing.JLabel();
			modInfo_L_Instruments.setName("modInfo_L_Instruments");
			modInfo_L_Instruments.setText("Instruments");
			modInfo_L_Instruments.setFont(Helpers.DIALOG_FONT);
		}
		return modInfo_L_Instruments;
	}
	private javax.swing.JScrollPane getScrollPane_ModInfo_Instruments()
	{
		if (scrollPane_ModInfo_Instruments == null)
		{
			scrollPane_ModInfo_Instruments = new javax.swing.JScrollPane();
			scrollPane_ModInfo_Instruments.setName("scrollPane_ModInfo_Instruments");
			scrollPane_ModInfo_Instruments.setViewportView(getModInfo_Instruments());
		}
		return scrollPane_ModInfo_Instruments;
	}
	public javax.swing.JTextArea getModInfo_Instruments()
	{
		if (modInfo_Instruments==null)
		{
			modInfo_Instruments = new javax.swing.JTextArea();
			modInfo_Instruments.setName("modInfo_Instruments");
			modInfo_Instruments.setEditable(false);
			modInfo_Instruments.setFont(Helpers.TEXTAREA_FONT);
		}
		return modInfo_Instruments;
	}
	public javax.swing.JLabel getModInfo_L_Modname()
	{
		if (modInfo_L_Modname==null)
		{
			modInfo_L_Modname = new javax.swing.JLabel();
			modInfo_L_Modname.setName("modInfo_L_Modname");
			modInfo_L_Modname.setText("Name");
			modInfo_L_Modname.setFont(Helpers.DIALOG_FONT);
		}
		return modInfo_L_Modname;
	}
	public javax.swing.JTextField getModInfo_Modname()
	{
		if (modInfo_Modname==null)
		{
			modInfo_Modname = new javax.swing.JTextField();
			modInfo_Modname.setName("modInfo_Modname");
			modInfo_Modname.setFont(Helpers.DIALOG_FONT);
			modInfo_Modname.setEditable(false);
		}
		return modInfo_Modname;
	}
	public javax.swing.JLabel getModInfo_L_Trackername()
	{
		if (modInfo_L_Trackername==null)
		{
			modInfo_L_Trackername = new javax.swing.JLabel();
			modInfo_L_Trackername.setName("modInfo_L_Trackername");
			modInfo_L_Trackername.setText("Tracker");
			modInfo_L_Trackername.setFont(Helpers.DIALOG_FONT);
		}
		return modInfo_L_Trackername;
	}
	public javax.swing.JTextField getModInfo_Trackername()
	{
		if (modInfo_Trackername==null)
		{
			modInfo_Trackername = new javax.swing.JTextField();
			modInfo_Trackername.setName("modInfo_Trackername");
			modInfo_Trackername.setFont(Helpers.DIALOG_FONT);
			modInfo_Trackername.setEditable(false);
		}
		return modInfo_Trackername;
	}
	public void fillInfoPanelWith(Module currentMod)
	{
		String modFileName = currentMod.getFileName();
		int i = modFileName.lastIndexOf(File.separatorChar);
		if (i==-1) i= modFileName.lastIndexOf('/');
    	getModInfo_Filename().setText(modFileName.substring(i+1));
    	getModInfo_Filename().select(0, 0);
    	getModInfo_Modname().setText(currentMod.getSongName());
    	getModInfo_Modname().select(0, 0);
    	getModInfo_Trackername().setText(currentMod.toShortInfoString());
    	getModInfo_Trackername().select(0, 0);
    	getModInfo_Instruments().setText(currentMod.getInstrumentContainer().toString());
    	getModInfo_Instruments().select(0, 0);
	}
}
