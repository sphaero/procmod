/*
 * @(#) ModConfigPanel.java
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.quippy.javamod.system.Helpers;

/**
 * @author Daniel Becker
 * @since 13.10.2007
 */
public class ModConfigPanel extends JPanel
{
	private static final long serialVersionUID = -3417460833901933361L;

	private ModContainer parentContainer;
	
	private javax.swing.JCheckBox playerSetUp_WideStereoMix = null;
	private javax.swing.JCheckBox playerSetUp_NoiseReduction = null;
	private javax.swing.JCheckBox playerSetUp_MegaBass = null;
	private javax.swing.JCheckBox playerSetUp_fadeOutLoops = null;
	private javax.swing.JCheckBox playerSetUp_ignoreLoops = null;
	private javax.swing.JLabel playerSetUp_L_Channels = null;
	private javax.swing.JComboBox<String> playerSetUp_Channels = null;
	private javax.swing.JLabel playerSetUp_L_BitsPerSample = null;
	private javax.swing.JComboBox<String> playerSetUp_BitsPerSample = null;
	private javax.swing.JLabel playerSetUp_L_SampleRate = null;
	private javax.swing.JComboBox<String> playerSetUp_SampleRate = null;
	private javax.swing.JLabel playerSetUp_L_Interpolation = null;
	private javax.swing.JComboBox<String> playerSetUp_Interpolation = null;
	private javax.swing.JLabel playerSetUp_L_BufferSize = null;
	private javax.swing.JComboBox<String> playerSetUp_BufferSize = null;
	
	//private javax.swing.JSlider volumeSlider = null;

	/**
	 * Constructor for ModConfigPanel
	 */
	public ModConfigPanel()
	{
		super();
		initialize();
	}
	/**
	 * Constructor for ModConfigPanel
	 * @param layout
	 */
	public ModConfigPanel(LayoutManager layout)
	{
		super(layout);
		initialize();
	}
	/**
	 * Constructor for ModConfigPanel
	 * @param isDoubleBuffered
	 */
	public ModConfigPanel(boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
		initialize();
	}
	/**
	 * Constructor for ModConfigPanel
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public ModConfigPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
		initialize();
	}
	/**
	 * @return the parent
	 */
	public ModContainer getParentContainer()
	{
		return parentContainer;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParentContainer(ModContainer parent)
	{
		this.parentContainer = parent;
	}
	private void initialize()
	{
		this.setName("ModConfigPane");
		this.setLayout(new java.awt.GridBagLayout());
		
		this.add(getPlayerSetUp_WideStereoMix(),	Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_NoiseReduction(),	Helpers.getGridBagConstraint(1, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_MegaBass(),			Helpers.getGridBagConstraint(2, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_fadeOutLoops(),		Helpers.getGridBagConstraint(3, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_ignoreLoops(),		Helpers.getGridBagConstraint(4, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));

		this.add(getPlayerSetUp_L_SampleRate(),		Helpers.getGridBagConstraint(0, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_L_Channels(),		Helpers.getGridBagConstraint(1, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_L_BitsPerSample(),	Helpers.getGridBagConstraint(2, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_L_BufferSize(),		Helpers.getGridBagConstraint(3, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		this.add(getPlayerSetUp_L_Interpolation(),	Helpers.getGridBagConstraint(4, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));

		this.add(getPlayerSetUp_SampleRate(),		Helpers.getGridBagConstraint(0, 2, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		this.add(getPlayerSetUp_Channels(),			Helpers.getGridBagConstraint(1, 2, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		this.add(getPlayerSetUp_BitsPerSample(),	Helpers.getGridBagConstraint(2, 2, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		this.add(getPlayerSetUp_BufferSize(),		Helpers.getGridBagConstraint(3, 2, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		this.add(getPlayerSetUp_Interpolation(),	Helpers.getGridBagConstraint(4, 2, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));

		//this.add(getVolumeSlider(),					Helpers.getGridBagConstraint(5, 0, 3, 0, java.awt.GridBagConstraints.VERTICAL, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
	}
	public javax.swing.JCheckBox getPlayerSetUp_WideStereoMix()
	{
		if (playerSetUp_WideStereoMix == null)
		{
			playerSetUp_WideStereoMix = new javax.swing.JCheckBox();
			playerSetUp_WideStereoMix.setName("playerSetUp_WideStereoMix");
			playerSetUp_WideStereoMix.setText("Wide Stereo Mix");
			playerSetUp_WideStereoMix.setFont(Helpers.DIALOG_FONT);
			playerSetUp_WideStereoMix.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setDoWideStereoMix(getPlayerSetUp_WideStereoMix().isSelected());
						}
					}
				}
			});
		}
		return playerSetUp_WideStereoMix;
	}
	public javax.swing.JCheckBox getPlayerSetUp_NoiseReduction()
	{
		if (playerSetUp_NoiseReduction==null)
		{
			playerSetUp_NoiseReduction = new javax.swing.JCheckBox();
			playerSetUp_NoiseReduction.setName("playerSetUp_NoiseReduction");
			playerSetUp_NoiseReduction.setText("Noise Reduction");
			playerSetUp_NoiseReduction.setFont(Helpers.DIALOG_FONT);
			playerSetUp_NoiseReduction.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setDoNoiseReduction(getPlayerSetUp_NoiseReduction().isSelected());
						}
					}
				}
			});
		}
		return playerSetUp_NoiseReduction;
	}
	public javax.swing.JCheckBox getPlayerSetUp_MegaBass()
	{
		if (playerSetUp_MegaBass==null)
		{
			playerSetUp_MegaBass = new javax.swing.JCheckBox();
			playerSetUp_MegaBass.setName("playerSetUp_MegaBass");
			playerSetUp_MegaBass.setText("Bass Boost");
			playerSetUp_MegaBass.setFont(Helpers.DIALOG_FONT);
			playerSetUp_MegaBass.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setDoMegaBass(getPlayerSetUp_MegaBass().isSelected());
						}
					}
				}
			});
		}
		return playerSetUp_MegaBass;
	}
	public int getLoopValue()
	{
		return	 getPlayerSetUp_fadeOutLoops().isSelected() ? Helpers.PLAYER_LOOP_FADEOUT :
				(getPlayerSetUp_ignoreLoops().isSelected() ? Helpers.PLAYER_LOOP_IGNORE : Helpers.PLAYER_LOOP_DEACTIVATED);
	}
	/**
	 * Never ever call this Method from an event handler for those
	 * two buttons - endless loop!
	 * @param newLoopValue
	 * @since 11.01.2012
	 */
	public void setLoopValue(int newLoopValue)
	{
		if (newLoopValue==Helpers.PLAYER_LOOP_DEACTIVATED)
		{
			getPlayerSetUp_fadeOutLoops().setSelected(false);
			getPlayerSetUp_ignoreLoops().setSelected(false);
		}
		else
		if (newLoopValue==Helpers.PLAYER_LOOP_FADEOUT)
		{
			getPlayerSetUp_fadeOutLoops().setSelected(true);
			getPlayerSetUp_ignoreLoops().setSelected(false);
		}
		else
		if (newLoopValue==Helpers.PLAYER_LOOP_IGNORE)
		{
			getPlayerSetUp_fadeOutLoops().setSelected(false);
			getPlayerSetUp_ignoreLoops().setSelected(true);
		}
	}
	private void configMixerWithLoopValue()
	{
		ModContainer parent = getParentContainer();
		if (parent!=null)
		{
			ModMixer currentMixer = parent.getCurrentMixer();
			if (currentMixer!=null)
				currentMixer.setDoNoLoops(getLoopValue());
		}
			
	}
	public javax.swing.JCheckBox getPlayerSetUp_fadeOutLoops()
	{
		if (playerSetUp_fadeOutLoops==null)
		{
			playerSetUp_fadeOutLoops = new javax.swing.JCheckBox();
			playerSetUp_fadeOutLoops.setName("playerSetUp_fadeOutLoops");
			playerSetUp_fadeOutLoops.setText("Fade out infinit loops");
			playerSetUp_fadeOutLoops.setFont(Helpers.DIALOG_FONT);
			playerSetUp_fadeOutLoops.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						if (getPlayerSetUp_fadeOutLoops().isSelected()) 
							getPlayerSetUp_ignoreLoops().setSelected(false);
						configMixerWithLoopValue();
					}
				}
			});
		}
		return playerSetUp_fadeOutLoops;
	}
	public javax.swing.JCheckBox getPlayerSetUp_ignoreLoops()
	{
		if (playerSetUp_ignoreLoops==null)
		{
			playerSetUp_ignoreLoops = new javax.swing.JCheckBox();
			playerSetUp_ignoreLoops.setName("playerSetUp_ignoreLoops");
			playerSetUp_ignoreLoops.setText("Ignore infinit loops");
			playerSetUp_ignoreLoops.setFont(Helpers.DIALOG_FONT);
			playerSetUp_ignoreLoops.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						if (getPlayerSetUp_ignoreLoops().isSelected()) 
							getPlayerSetUp_fadeOutLoops().setSelected(false);
						configMixerWithLoopValue();
					}
				}
			});
		}
		return playerSetUp_ignoreLoops;
	}
	public javax.swing.JLabel getPlayerSetUp_L_BitsPerSample()
	{
		if (playerSetUp_L_BitsPerSample==null)
		{
			playerSetUp_L_BitsPerSample = new javax.swing.JLabel();
			playerSetUp_L_BitsPerSample.setName("playerSetUp_L_BitsPerSample");
			playerSetUp_L_BitsPerSample.setText("Resolution");
			playerSetUp_L_BitsPerSample.setFont(Helpers.DIALOG_FONT);
		}
		return playerSetUp_L_BitsPerSample;
	}
	public javax.swing.JComboBox<String> getPlayerSetUp_BitsPerSample()
	{
		if (playerSetUp_BitsPerSample == null)
		{
			playerSetUp_BitsPerSample = new JComboBox<String>();
			playerSetUp_BitsPerSample.setName("playerSetUp_BitsPerSample");

			javax.swing.DefaultComboBoxModel<String> theModel = new javax.swing.DefaultComboBoxModel<String>(ModContainer.BITSPERSAMPLE);
			playerSetUp_BitsPerSample.setModel(theModel);
			playerSetUp_BitsPerSample.setFont(Helpers.DIALOG_FONT);
			playerSetUp_BitsPerSample.setEnabled(true);
			playerSetUp_BitsPerSample.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setSampleSizeInBits(Integer.parseInt(getPlayerSetUp_BitsPerSample().getSelectedItem().toString()));
						}
					}
				}
			});
		}
		return playerSetUp_BitsPerSample;
	}
	public javax.swing.JLabel getPlayerSetUp_L_Channels()
	{
		if (playerSetUp_L_Channels==null)
		{
			playerSetUp_L_Channels = new javax.swing.JLabel();
			playerSetUp_L_Channels.setName("playerSetUp_L_Channels");
			playerSetUp_L_Channels.setText("Channels");
			playerSetUp_L_Channels.setFont(Helpers.DIALOG_FONT);
		}
		return playerSetUp_L_Channels;
	}
	public javax.swing.JComboBox<String> getPlayerSetUp_Channels()
	{
		if (playerSetUp_Channels==null)
		{
			playerSetUp_Channels = new JComboBox<String>();
			playerSetUp_Channels.setName("playerSetUp_Channels");
			
			javax.swing.DefaultComboBoxModel<String> theModel = new javax.swing.DefaultComboBoxModel<String>(ModContainer.CHANNELS);
			playerSetUp_Channels.setModel(theModel);
			playerSetUp_Channels.setFont(Helpers.DIALOG_FONT);
			playerSetUp_Channels.setEnabled(true);
			playerSetUp_Channels.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setChannels(Integer.parseInt(getPlayerSetUp_Channels().getSelectedItem().toString()));
						}
					}
				}
			});
		}
		return playerSetUp_Channels;
	}
	public javax.swing.JLabel getPlayerSetUp_L_SampleRate()
	{
		if (playerSetUp_L_SampleRate==null)
		{
			playerSetUp_L_SampleRate = new javax.swing.JLabel();
			playerSetUp_L_SampleRate.setName("playerSetUp_L_SampleRate");
			playerSetUp_L_SampleRate.setText("Frequency");
			playerSetUp_L_SampleRate.setFont(Helpers.DIALOG_FONT);
		}
		return playerSetUp_L_SampleRate;
	}
	public javax.swing.JComboBox<String> getPlayerSetUp_SampleRate()
	{
		if (playerSetUp_SampleRate==null)
		{
			playerSetUp_SampleRate = new JComboBox<String>();
			playerSetUp_SampleRate.setName("playerSetUp_SampleRate");
			
			javax.swing.DefaultComboBoxModel<String> theModel = new javax.swing.DefaultComboBoxModel<String>(ModContainer.SAMPLERATE);
			playerSetUp_SampleRate.setModel(theModel);
			playerSetUp_SampleRate.setFont(Helpers.DIALOG_FONT);
			playerSetUp_SampleRate.setEnabled(true);
			playerSetUp_SampleRate.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setSampleRate(Integer.parseInt(getPlayerSetUp_SampleRate().getSelectedItem().toString()));
						}
					}
				}
			});
		}
		return playerSetUp_SampleRate;
	}
	public javax.swing.JLabel getPlayerSetUp_L_Interpolation()
	{
		if (playerSetUp_L_Interpolation==null)
		{
			playerSetUp_L_Interpolation = new javax.swing.JLabel();
			playerSetUp_L_Interpolation.setName("playerSetUp_L_Interpolation");
			playerSetUp_L_Interpolation.setText("Interpolation");
			playerSetUp_L_Interpolation.setFont(Helpers.DIALOG_FONT);
		}
		return playerSetUp_L_Interpolation;
	}
	public javax.swing.JComboBox<String> getPlayerSetUp_Interpolation()
	{
		if (playerSetUp_Interpolation==null)
		{
			playerSetUp_Interpolation = new JComboBox<String>();
			playerSetUp_Interpolation.setName("playerSetUp_Interpolation");
			
			javax.swing.DefaultComboBoxModel<String> theModel = new javax.swing.DefaultComboBoxModel<String>(ModContainer.INTERPOLATION);
			playerSetUp_Interpolation.setModel(theModel);
			playerSetUp_Interpolation.setFont(Helpers.DIALOG_FONT);
			playerSetUp_Interpolation.setEnabled(true);
			playerSetUp_Interpolation.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setDoISP(getPlayerSetUp_Interpolation().getSelectedIndex());
						}
					}
				}
			});
		}
		return playerSetUp_Interpolation;
	}
	public javax.swing.JLabel getPlayerSetUp_L_BufferSize()
	{
		if (playerSetUp_L_BufferSize==null)
		{
			playerSetUp_L_BufferSize = new javax.swing.JLabel();
			playerSetUp_L_BufferSize.setName("playerSetUp_BufferSize");
			playerSetUp_L_BufferSize.setText("ms buffer size");
			playerSetUp_L_BufferSize.setFont(Helpers.DIALOG_FONT);
		}
		return playerSetUp_L_BufferSize;
	}
	public javax.swing.JComboBox<String> getPlayerSetUp_BufferSize()
	{
		if (playerSetUp_BufferSize==null)
		{
			playerSetUp_BufferSize = new JComboBox<String>();
			playerSetUp_BufferSize.setName("playerSetUp_BufferSize");
			
			javax.swing.DefaultComboBoxModel<String> theModel = new javax.swing.DefaultComboBoxModel<String>(ModContainer.BUFFERSIZE);
			playerSetUp_BufferSize.setModel(theModel);
			playerSetUp_BufferSize.setFont(Helpers.DIALOG_FONT);
			playerSetUp_BufferSize.setEnabled(true);
			playerSetUp_BufferSize.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						ModContainer parent = getParentContainer();
						if (parent!=null)
						{
							ModMixer currentMixer = parent.getCurrentMixer();
							if (currentMixer!=null)
								currentMixer.setBufferSize(Integer.parseInt(getPlayerSetUp_BufferSize().getSelectedItem().toString()));
						}
					}
				}
			});
		}
		return playerSetUp_BufferSize;
	}
//	public javax.swing.JSlider getVolumeSlider()
//	{
//		if (volumeSlider==null)
//		{
//			volumeSlider = new javax.swing.JSlider(JSlider.VERTICAL, 0, 10, 0);
//			volumeSlider.setPaintTicks(true);
//			volumeSlider.setMajorTickSpacing(1);
//			volumeSlider.setPaintLabels(false);
//			volumeSlider.setValue(5);
//			volumeSlider.addMouseListener(new MouseAdapter()
//			{
//				public void mouseClicked(MouseEvent e)
//				{
//					JSlider slider = (JSlider) e.getSource();
//					if (e.getClickCount()>1)
//					{
//						slider.setValue(5);
//						e.consume();
//					}
//				}
//			});
//			volumeSlider.addChangeListener(new ChangeListener()
//			{
//				public void stateChanged(ChangeEvent e)
//				{
//					JSlider slider = (JSlider) e.getSource();
//					int value = slider.getValue();
//				}
//			});
//		}
//		return volumeSlider;
//	}
}
