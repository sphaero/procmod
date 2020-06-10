/*
 * @(#) MainForm.java
 * 
 * Created on 22.06.2006 by Daniel Becker
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
package de.quippy.javamod.main.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import de.quippy.javamod.io.GaplessSoundOutputStreamImpl;
import de.quippy.javamod.io.SoundOutputStream;
import de.quippy.javamod.main.gui.components.LEDScrollPanel;
import de.quippy.javamod.main.gui.components.RoundSlider;
import de.quippy.javamod.main.gui.components.SAMeterPanel;
import de.quippy.javamod.main.gui.components.SeekBarPanel;
import de.quippy.javamod.main.gui.components.SeekBarPanelListener;
import de.quippy.javamod.main.gui.components.SimpleProgessDialog;
import de.quippy.javamod.main.gui.components.VUMeterPanel;
import de.quippy.javamod.main.gui.playlist.PlayListGUI;
import de.quippy.javamod.main.gui.playlist.PlaylistGUIChangeListener;
import de.quippy.javamod.main.gui.tools.FileChooserFilter;
import de.quippy.javamod.main.gui.tools.FileChooserResult;
import de.quippy.javamod.main.gui.tools.PlaylistDropListener;
import de.quippy.javamod.main.gui.tools.PlaylistDropListenerCallBack;
import de.quippy.javamod.main.playlist.PlayList;
import de.quippy.javamod.main.playlist.PlayListEntry;
import de.quippy.javamod.mixer.Mixer;
import de.quippy.javamod.mixer.dsp.AudioProcessor;
import de.quippy.javamod.mixer.dsp.DspProcessorCallBack;
import de.quippy.javamod.mixer.dsp.iir.GraphicEQ;
import de.quippy.javamod.mixer.dsp.iir.GraphicEqGUI;
import de.quippy.javamod.mixer.dsp.pitchshift.PitchShift;
import de.quippy.javamod.mixer.dsp.pitchshift.PitchShiftGUI;
import de.quippy.javamod.multimedia.MultimediaContainer;
import de.quippy.javamod.multimedia.MultimediaContainerEvent;
import de.quippy.javamod.multimedia.MultimediaContainerEventListener;
import de.quippy.javamod.multimedia.MultimediaContainerManager;
import de.quippy.javamod.system.Helpers;
import de.quippy.javamod.system.Log;
import de.quippy.javamod.system.LogMessageCallBack;
import de.quippy.javamod.multimedia.mod.loader.pattern.*;
/**
 * @author Daniel Becker
 * @since 22.06.2006
 */
public class MainForm extends javax.swing.JFrame implements DspProcessorCallBack, PlayThreadEventListener, MultimediaContainerEventListener, PlaylistGUIChangeListener, PlaylistDropListenerCallBack
{
	private static final long serialVersionUID = -2737074464335059959L;

	private static final String DEFAULTICONPATH = "/de/quippy/javamod/main/gui/ressources/quippy_the_kangaroo_icon.gif";
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
	
	private static final String PROPERTYFILENAME = ".javamod.properties";
	private static final String PROPERTY_SEARCHPATH = "javamod.path.loadpath"; 
	private static final String PROPERTY_EXPORTPATH = "javamod.path.exportpath"; 
	private static final String PROPERTY_LOOKANDFEEL = "javamod.lookandfeel.classname"; 
	private static final String PROPERTY_LASTLOADED = "javamod.path.lastloaded";
	private static final String PROPERTY_SYSTEMTRAY = "javamod.systemtray";
	private static final String PROPERTY_MAINDIALOG_POS = "javamod.dialog.position.main";
	private static final String PROPERTY_SETUPDIALOG_POS = "javamod.dialog.position.setup";
	private static final String PROPERTY_PROPERTIESDIALOG_POS = "javamod.dialog.position.properties";
	private static final String PROPERTY_PLAYLISTDIALOG_POS = "javamod.dialog.position.playlist";
	private static final String PROPERTY_EFFECTDIALOG_POS = "javamod.dialog.position.equalizer";
	private static final String PROPERTY_MAINDIALOG_SIZE = "javamod.dialog.size.main";
	private static final String PROPERTY_SETUPDIALOG_SIZE = "javamod.dialog.size.setup";
	private static final String PROPERTY_PROPERTIESDIALOG_SIZE = "javamod.dialog.size.properties";
	private static final String PROPERTY_PLAYLISTDIALOG_SIZE = "javamod.dialog.size.playlist";
	private static final String PROPERTY_EFFECTDIALOG_SIZE = "javamod.dialog.size.equalizer";
	private static final String PROPERTY_VOLUME_VALUE = "javamod.dialog.volume.value";
	private static final String PROPERTY_BALANCE_VALUE = "javamod.dialog.balance.value";
	private static final String PROPERTY_SETUPDIALOG_VISABLE = "javamod.dialog.open.setup";
	private static final String PROPERTY_PROPERTIESDIALOG_VISABLE = "javamod.dialog.open.properties";
	private static final String PROPERTY_PLAYLIST_VISABLE = "javamod.dialog.open.playlist";
	private static final String PROPERTY_EFFECT_VISABLE = "javamod.dialog.open.equalizer";

	private static final String PROPERTY_EFFECTS_PASSTHROUGH = "javamod.player.effects.passthrough";
	private static final String PROPERTY_EQUALIZER_PREAMP = "javamod.player.equalizer.preamp";
	private static final String PROPERTY_EQUALIZER_BAND_PREFIX = "javamod.player.equalizer.band.";
	private static final String PROPERTY_EQUALIZER_ISACTIVE = "javamod.player.equalizer.isactive";
	private static final String PROPERTY_PITCHSHIFT_ISACTIVE = "javamod.player.pitchshift.isactive";
	private static final String PROPERTY_PITCHSHIFT_PITCH = "javamod.player.pitchshift.pitch";
	private static final String PROPERTY_PITCHSHIFT_SAMPLESCALE = "javamod.player.pitchshift.scale";
	private static final String PROPERTY_PITCHSHIFT_FRAMESIZE = "javamod.player.pitchshift.framesize";
	private static final String PROPERTY_PITCHSHIFT_OVERSAMPLING = "javamod.player.pitchshift.oversampling";
	private static final int PROPERTY_LASTLOADED_MAXENTRIES = 10;
	
	private static final String WINDOW_TITLE = Helpers.FULLVERSION;
	private static final String WINDOW_NAME = "JavaMod";
	
	private static FileFilter fileFilterExport[];
	private static FileFilter fileFilterLoad[];
	
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
	
	private RoundSlider volumeSlider = null;
	private javax.swing.JLabel volumeLabel = null;
	private RoundSlider balanceSlider = null;
	private javax.swing.JLabel balanceLabel = null;
	
	private javax.swing.JPanel baseContentPane = null;
	private javax.swing.JPanel mainContentPane = null;
	private javax.swing.JPanel musicDataPane = null;
	private javax.swing.JPanel playerControlPane = null;
	private javax.swing.JPanel playerDataPane = null;
	
	private javax.swing.JDialog modInfoDialog = null;
	private javax.swing.JDialog playerSetUpDialog = null;
	private javax.swing.JDialog playlistDialog = null;
	private javax.swing.JDialog equalizerDialog = null;
	private PlayerConfigPanel playerConfigPanel = null; 
	private javax.swing.JPanel modInfoPane = null;
	private javax.swing.JPanel playerSetUpPane = null;
	private javax.swing.JPanel playlistPane = null;
	private javax.swing.JPanel effectPane = null;
	
	private java.awt.Point mainDialogLocation = null;
	private java.awt.Dimension mainDialogSize = null;
	private java.awt.Point modInfoDialogLocation = null;
	private java.awt.Dimension modInfoDialogSize = null;
	private boolean modInfoDialogVisable = false;
	private java.awt.Point playerSetUpDialogLocation = null;
	private java.awt.Dimension playerSetUpDialogSize = null;
	private boolean playerSetUpDialogVisable = false;
	private java.awt.Point playlistDialogLocation = null;
	private java.awt.Dimension playlistDialogSize = null;
	private boolean playlistDialogVisable = false;
	private java.awt.Point effectsDialogLocation = null;
	private java.awt.Dimension effectsDialogSize = null;
	private boolean effectDialogVisable = false;

	private SimpleProgessDialog downloadDialog = null;

	private VUMeterPanel vuLMeterPanel = null;
	private VUMeterPanel vuRMeterPanel = null;
	private SAMeterPanel saLMeterPanel = null;
	private SAMeterPanel saRMeterPanel = null;
	private LEDScrollPanel ledScrollPanel = null;
	
	private SeekBarPanel seekBarPanel = null;

	private javax.swing.JTextField messages = null;

	private javax.swing.JMenuBar baseMenuBar = null;
	private javax.swing.JMenu menu_File = null;
	private javax.swing.JMenu menu_View = null;
	private javax.swing.JMenu menu_LookAndFeel = null;
	private javax.swing.JMenu menu_Help = null;
	private javax.swing.JMenu menu_File_RecentFiles = null;
	private javax.swing.JMenuItem menu_File_openMod = null;
	private javax.swing.JMenuItem menu_File_openURL = null;
	private javax.swing.JMenuItem menu_File_exportWave = null;
	private javax.swing.JMenuItem menu_File_Close = null;
	private javax.swing.JMenuItem menu_View_Info = null;
	private javax.swing.JMenuItem menu_View_Setup = null;
	private javax.swing.JMenuItem menu_View_Playlist = null;
	private javax.swing.JMenuItem menu_View_GraphicEQ = null;
	private javax.swing.JCheckBoxMenuItem menu_View_UseSystemTray = null;
	private javax.swing.JMenuItem menu_Help_CheckUpdate = null;
	private javax.swing.JMenuItem menu_Help_ShowVersionHistory = null;
	private javax.swing.JMenuItem menu_Help_About = null;
	private javax.swing.JCheckBoxMenuItem [] menu_LookAndFeel_Items = null;
	
	private MenuItem aboutItem = null;
	private MenuItem playItem = null;
	private MenuItem pauseItem = null;
	private MenuItem stopItem = null;
	private MenuItem prevItem = null;
	private MenuItem nextItem = null;
	private MenuItem closeItem = null;
	
	private TrayIcon javaModTrayIcon = null;
	
	private JavaModAbout about = null;
	private UrlDialog urlDialog = null;
	private SimpleTextViewerDialog simpleTextViewerDialog = null;
	private PlayListGUI playlistGUI = null;
	private EffectsPanel effectGUI = null;
	private GraphicEqGUI equalizerGUI = null;
	private PitchShiftGUI pitchShiftGUI = null;
	
	private MultimediaContainer currentContainer;
	private PlayThread playerThread;
	private PlayList currentPlayList;
	private GraphicEQ currentEqualizer;
	private PitchShift currentPitchShift;
	
	private ArrayList<DropTarget> dropTargetList;
	private AudioProcessor audioProcessor;
	private transient SoundOutputStream soundOutputStream;

	private String propertyFilePath;
	private String searchPath;
	private String exportPath;
	private String uiClassName;
	private boolean useSystemTray = false;
	private float currentVolume; /* 0.0 - 1.0 */
	private float currentBalance; /* -1.0 - 1.0 */
	
	private ArrayList<URL> lastLoaded;
	
	private boolean inExportMode;
	
	private final class LookAndFeelChanger implements ActionListener
	{
		private String uiClassName;
		private JCheckBoxMenuItem parent;
		
		public LookAndFeelChanger(JCheckBoxMenuItem parent, String uiClassName)
		{
			this.uiClassName = uiClassName;
			this.parent = parent;
		}
		private void setSelection()
		{
			for (int i=0; i<menu_LookAndFeel_Items.length; i++)
			{
				if (menu_LookAndFeel_Items[i]==parent)
					menu_LookAndFeel_Items[i].setSelected(true);
				else
					menu_LookAndFeel_Items[i].setSelected(false);
			}
		}
		public void actionPerformed(ActionEvent event)
		{
			setSelection();
			MainForm.this.uiClassName = uiClassName;
			MainForm.this.updateLookAndFeel(uiClassName);
		}
	}
	private final class MouseWheelVolumeControl implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			if (!e.isConsumed() && e.getScrollType()==MouseWheelEvent.WHEEL_UNIT_SCROLL)
			{
				final RoundSlider volSlider = getVolumeSlider();
				volSlider.setValue(volSlider.getValue() + ((float)e.getWheelRotation() / 100f));
				e.consume();
			}
		}
	}
	private final class MakeMainWindowVisible implements WindowFocusListener
	{
		public void windowLostFocus(WindowEvent e) { /*NOOP*/ }
		public void windowGainedFocus(WindowEvent e)
		{
			MainForm.this.setFocusableWindowState(false);
			MainForm.this.toFront();
			MainForm.this.setFocusableWindowState(true);
		}
	}

	private transient final MakeMainWindowVisible makeMainWindowVisiable = new MakeMainWindowVisible();
	/**
	 * Constructor for MainForm
	 * @param title
	 * @throws HeadlessException
	 */
	public MainForm() throws HeadlessException
	{
		super();
		propertyFilePath = Helpers.HOMEDIR;
		currentPlayList = null;
		currentEqualizer = new GraphicEQ();
		currentPitchShift = new PitchShift();
	    audioProcessor = new AudioProcessor(2048, 70);
	    audioProcessor.addListener(this);
	    audioProcessor.addEffectListener(currentEqualizer);
	    audioProcessor.addEffectListener(currentPitchShift);
		inExportMode = false;
		initialize();
	}
	/**
	 * Read the properties from file. Use default values, if not set or file not available
	 * @since 01.07.2006
	 */
	private void readPropertyFile()
	{
		java.util.Properties props = new java.util.Properties();
	    try
	    {
	        File propertyFile = new File(propertyFilePath + File.separator + PROPERTYFILENAME);
	        if (propertyFile.exists())
	        {
	        	java.io.FileInputStream fis = null;
	        	try
	        	{
			    	fis = new java.io.FileInputStream(propertyFile);
			        props.load(fis);
	        	}
	        	finally
	        	{
	        		if (fis!=null) try { fis.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
	        	}
	        }

	        searchPath = props.getProperty(PROPERTY_SEARCHPATH, Helpers.HOMEDIR);
			exportPath = props.getProperty(PROPERTY_EXPORTPATH, Helpers.HOMEDIR);
			uiClassName = props.getProperty(PROPERTY_LOOKANDFEEL, javax.swing.UIManager.getSystemLookAndFeelClassName());
			useSystemTray = Boolean.parseBoolean(props.getProperty(PROPERTY_SYSTEMTRAY, "FALSE"));
			currentVolume = Float.parseFloat(props.getProperty(PROPERTY_VOLUME_VALUE, "1.0"));
			currentBalance = Float.parseFloat(props.getProperty(PROPERTY_BALANCE_VALUE, "0.0"));
			lastLoaded = new ArrayList<URL>(PROPERTY_LASTLOADED_MAXENTRIES);
			for (int i=0; i<PROPERTY_LASTLOADED_MAXENTRIES; i++)
			{
				String url = props.getProperty(PROPERTY_LASTLOADED+'.'+i, null);
				if (url!=null) lastLoaded.add(new URL(url)); else lastLoaded.add(null);
			}
			setDSPEnabled(Boolean.parseBoolean(props.getProperty(PROPERTY_EFFECTS_PASSTHROUGH, "FALSE")));
			mainDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_MAINDIALOG_POS, "-1x-1"));
			mainDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_MAINDIALOG_SIZE, "320x410"));
			playerSetUpDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_SETUPDIALOG_POS, "-1x-1"));
			playerSetUpDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_SETUPDIALOG_SIZE, "720x230"));
			playerSetUpDialogVisable = Boolean.parseBoolean(props.getProperty(PROPERTY_SETUPDIALOG_VISABLE, "false"));
			modInfoDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_PROPERTIESDIALOG_POS, "-1x-1"));
			modInfoDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_PROPERTIESDIALOG_SIZE, "520x630"));
			modInfoDialogVisable = Boolean.parseBoolean(props.getProperty(PROPERTY_PROPERTIESDIALOG_VISABLE, "false"));
			playlistDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_PLAYLISTDIALOG_POS, "-1x-1"));
			playlistDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_PLAYLISTDIALOG_SIZE, "400x400"));
			playlistDialogVisable = Boolean.parseBoolean(props.getProperty(PROPERTY_PLAYLIST_VISABLE, "false"));
			effectsDialogLocation = Helpers.getPointFromString(props.getProperty(PROPERTY_EFFECTDIALOG_POS, "-1x-1"));
			effectsDialogSize = Helpers.getDimensionFromString(props.getProperty(PROPERTY_EFFECTDIALOG_SIZE, "560x470"));
			effectDialogVisable = Boolean.parseBoolean(props.getProperty(PROPERTY_EFFECT_VISABLE, "false"));
			
			if (currentEqualizer!=null)
			{
				boolean isActive = Boolean.parseBoolean(props.getProperty(PROPERTY_EQUALIZER_ISACTIVE, "FALSE"));
				currentEqualizer.setIsActive(isActive);
				float preAmpValueDB = Float.parseFloat(props.getProperty(PROPERTY_EQUALIZER_PREAMP, "0.0"));
				currentEqualizer.setPreAmp(preAmpValueDB);
				for (int i=0; i<currentEqualizer.getBandCount(); i++)
				{
					float bandValueDB = Float.parseFloat(props.getProperty(PROPERTY_EQUALIZER_BAND_PREFIX + Integer.toString(i), "0.0"));
					currentEqualizer.setBand(i, bandValueDB);
				}
			}
			if (currentPitchShift!=null)
			{
				boolean isActive = Boolean.parseBoolean(props.getProperty(PROPERTY_PITCHSHIFT_ISACTIVE, "FALSE"));
				currentPitchShift.setIsActive(isActive);
				float pitchValue = Float.parseFloat(props.getProperty(PROPERTY_PITCHSHIFT_PITCH, "1.0"));
				currentPitchShift.setPitchScale(pitchValue);
				float scaleValue = Float.parseFloat(props.getProperty(PROPERTY_PITCHSHIFT_SAMPLESCALE, "1.0"));
				currentPitchShift.setSampleScale(scaleValue);
				int overSampling = Integer.parseInt(props.getProperty(PROPERTY_PITCHSHIFT_OVERSAMPLING, "32"));
				currentPitchShift.setFFTOversampling(overSampling);
				int frameSize = Integer.parseInt(props.getProperty(PROPERTY_PITCHSHIFT_FRAMESIZE, "8192"));
				currentPitchShift.setFFTFrameSize(frameSize);
			}

			MultimediaContainerManager.configureContainer(props);
	    }
	    catch (Throwable ex)
	    {
			Log.error("[MainForm]", ex);
	    }
	}
	/**
	 * Write back to a File
	 * @since 01.07.2006
	 */
	private void writePropertyFile()
	{
	    try
	    {
	    	java.util.Properties props = new java.util.Properties();
			
	    	MultimediaContainerManager.getContainerConfigs(props);
			props.setProperty(PROPERTY_SEARCHPATH, searchPath);
			props.setProperty(PROPERTY_EXPORTPATH, exportPath);
			props.setProperty(PROPERTY_LOOKANDFEEL, uiClassName);
			props.setProperty(PROPERTY_SYSTEMTRAY, Boolean.toString(useSystemTray));
			props.setProperty(PROPERTY_VOLUME_VALUE, Float.toString(currentVolume));
			props.setProperty(PROPERTY_BALANCE_VALUE, Float.toString(currentBalance));
			for (int i=0; i<PROPERTY_LASTLOADED_MAXENTRIES; i++)
			{
				URL element = lastLoaded.get(i);
				if (element!=null)
					props.setProperty(PROPERTY_LASTLOADED+'.'+i, element.toString());
			}
			props.setProperty(PROPERTY_EFFECTS_PASSTHROUGH, Boolean.toString(isDSPEnabled()));

			props.setProperty(PROPERTY_MAINDIALOG_POS, Helpers.getStringFromPoint(getLocation()));
			props.setProperty(PROPERTY_MAINDIALOG_SIZE, Helpers.getStringFromDimension(getSize()));
			props.setProperty(PROPERTY_SETUPDIALOG_POS, Helpers.getStringFromPoint(getPlayerSetUpDialog().getLocation()));
			props.setProperty(PROPERTY_SETUPDIALOG_SIZE, Helpers.getStringFromDimension(getPlayerSetUpDialog().getSize()));
			props.setProperty(PROPERTY_SETUPDIALOG_VISABLE, Boolean.toString(getPlayerSetUpDialog().isVisible()));
			props.setProperty(PROPERTY_PROPERTIESDIALOG_POS, Helpers.getStringFromPoint(getModInfoDialog().getLocation()));
			props.setProperty(PROPERTY_PROPERTIESDIALOG_SIZE, Helpers.getStringFromDimension(getModInfoDialog().getSize()));
			props.setProperty(PROPERTY_PROPERTIESDIALOG_VISABLE, Boolean.toString(getModInfoDialog().isVisible()));
			props.setProperty(PROPERTY_PLAYLISTDIALOG_POS, Helpers.getStringFromPoint(getPlaylistDialog().getLocation()));
			props.setProperty(PROPERTY_PLAYLISTDIALOG_SIZE, Helpers.getStringFromDimension(getPlaylistDialog().getSize()));
			props.setProperty(PROPERTY_PLAYLIST_VISABLE, Boolean.toString(getPlaylistDialog().isVisible()));
			props.setProperty(PROPERTY_EFFECTDIALOG_POS, Helpers.getStringFromPoint(getEffectDialog().getLocation()));
			props.setProperty(PROPERTY_EFFECTDIALOG_SIZE, Helpers.getStringFromDimension(getEffectDialog().getSize()));
			props.setProperty(PROPERTY_EFFECT_VISABLE, Boolean.toString(getEffectDialog().isVisible()));
			if (currentEqualizer!=null)
			{
				props.setProperty(PROPERTY_EQUALIZER_ISACTIVE, Boolean.toString(currentEqualizer.isActive()));
				props.setProperty(PROPERTY_EQUALIZER_PREAMP, Float.toString(currentEqualizer.getPreAmpDB()));
				for (int i=0; i<currentEqualizer.getBandCount(); i++)
				{
					props.setProperty(PROPERTY_EQUALIZER_BAND_PREFIX + Integer.toString(i), Float.toString(currentEqualizer.getBand(i)));
				}
			}
			if (currentPitchShift!=null)
			{
				props.setProperty(PROPERTY_PITCHSHIFT_ISACTIVE, Boolean.toString(currentPitchShift.isActive()));
				props.setProperty(PROPERTY_PITCHSHIFT_PITCH, Float.toString(currentPitchShift.getPitchScale()));
				props.setProperty(PROPERTY_PITCHSHIFT_SAMPLESCALE, Float.toString(currentPitchShift.getSampleScale()));
				props.setProperty(PROPERTY_PITCHSHIFT_FRAMESIZE, Integer.toString(currentPitchShift.getFftFrameSize()));
				props.setProperty(PROPERTY_PITCHSHIFT_OVERSAMPLING, Integer.toString(currentPitchShift.getFFTOversampling()));
			}

			File propertyFile = new File(propertyFilePath + File.separator + PROPERTYFILENAME);
	        if (propertyFile.exists())
	        {
	        	boolean ok = propertyFile.delete();
	        	if (ok) ok = propertyFile.createNewFile();
	        	if (!ok) Log.error("Could not create property file: " + propertyFile.getCanonicalPath());
	        }
	        java.io.FileOutputStream fos = null;
	        try
	        {
		    	fos = new java.io.FileOutputStream(propertyFile);
			    props.store(fos, WINDOW_TITLE);
	        }
	        finally
	        {
	        	if (fos!=null) try { fos.close(); } catch (IOException ex) { Log.error("IGNORED", ex); }
	        }
	    }
	    catch (Throwable ex)
	    {
			Log.error("MainForm]", ex);
	    }
	}
	private javax.swing.UIManager.LookAndFeelInfo [] getInstalledLookAndFeels()
	{
//		java.util.ArrayList<UIManager.LookAndFeelInfo> allLAFs = new java.util.ArrayList<UIManager.LookAndFeelInfo>();
//		allLAFs.add(new UIManager.LookAndFeelInfo("Kunststoff", "com.incors.plaf.kunststoff.KunststoffLookAndFeel"));
//		allLAFs.add(new UIManager.LookAndFeelInfo("Oyoaha", "com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel"));
//		allLAFs.add(new UIManager.LookAndFeelInfo("MacOS", "it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel"));
//		allLAFs.add(new UIManager.LookAndFeelInfo("GTK", "org.gtk.java.swing.plaf.gtk.GtkLookAndFeel"));
//		javax.swing.UIManager.LookAndFeelInfo [] installedLAFs = javax.swing.UIManager.getInstalledLookAndFeels();
//		for (int i=0; i<installedLAFs.length; i++)
//		{
//			allLAFs.add(installedLAFs[i]);
//		}
//		return allLAFs.toArray(new javax.swing.UIManager.LookAndFeelInfo[allLAFs.size()]);
		return javax.swing.UIManager.getInstalledLookAndFeels();
	}
	/**
	 * Create the file filters so that we do have them for
	 * the dialogs
	 * @since 05.01.2008
	 */
	private void createFileFilter()
	{
		HashMap<String, String[]> extensionMap = MultimediaContainerManager.getSupportedFileExtensionsPerContainer();
		
		ArrayList<FileFilter> chooserFilterArray = new ArrayList<FileFilter>(extensionMap.size() + 1);

		// Add playlist files to full list of supported file extensions
		String [] containerExtensions = MultimediaContainerManager.getSupportedFileExtensions();
		String [] fullSupportedExtensions = new String[containerExtensions.length + PlayList.SUPPORTEDPLAYLISTS.length];
		System.arraycopy(PlayList.SUPPORTEDPLAYLISTS, 0, fullSupportedExtensions, 0, PlayList.SUPPORTEDPLAYLISTS.length);
		System.arraycopy(containerExtensions, 0, fullSupportedExtensions, PlayList.SUPPORTEDPLAYLISTS.length, containerExtensions.length);
		chooserFilterArray.add(new FileChooserFilter(fullSupportedExtensions, "All playable files"));
//		chooserFilterArray.add(new FileChooserFilter("*", "All files"));
		
		// add all single file extensions grouped by container
		Set<String> containerNameSet = extensionMap.keySet();
		Iterator<String> containerNameIterator = containerNameSet.iterator();
		while (containerNameIterator.hasNext())
		{
			String containerName = containerNameIterator.next();
			String [] extensions = extensionMap.get(containerName);
			StringBuilder fileText = new StringBuilder(containerName);
			fileText.append(" (");
			int ende = extensions.length-1;
			for (int i=0; i<=ende; i++)
			{
				fileText.append("*.").append(extensions[i]);
				if (i<ende) fileText.append(", ");
			}
			fileText.append(')');
			chooserFilterArray.add(new FileChooserFilter(extensions, fileText.toString()));
		}
		// now add playlist as group of files
		chooserFilterArray.add(PlayList.PLAYLIST_FILE_FILTER);
		
		fileFilterLoad = new FileFilter[chooserFilterArray.size()];
		chooserFilterArray.toArray(fileFilterLoad);

		fileFilterExport = new FileFilter[1];
		fileFilterExport[0] = new FileChooserFilter(javax.sound.sampled.AudioFileFormat.Type.WAVE.getExtension(), javax.sound.sampled.AudioFileFormat.Type.WAVE.toString());
	}
	/**
	 * Do main initials
	 * @since 22.06.2006
	 */
	private void initialize()
	{
		Log.addLogListener(new LogMessageCallBack()
		{
			@Override
			public void debug(String message)
			{
				showMessage(message);
			}
			@Override
			public void info(String message)
			{
				showMessage(message);
			}
			@Override
			public void error(String message, Throwable ex)
			{
				if (ex!=null)
					showMessage(message+'|'+ex.toString());
				else
					showMessage(message);
			}
		});
		
		readPropertyFile();
		
		setSystemTray();

		setName(WINDOW_NAME);
		setTitle(WINDOW_TITLE);
		getTrayIcon().setToolTip(WINDOW_TITLE);

		java.net.URL iconURL = MainForm.class.getResource(DEFAULTICONPATH);
	    if (iconURL!=null) 
	    	setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(iconURL));
		
	    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	    addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				doClose();
			}
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowIconified(java.awt.event.WindowEvent)
			 * @since 07.02.2012
			 */
			@Override
			public void windowIconified(WindowEvent e)
			{
				if (useSystemTray) setVisible(false);
			}
			/**
			 * @param e
			 * @see java.awt.event.WindowAdapter#windowDeiconified(java.awt.event.WindowEvent)
			 * @since 07.02.2012
			 */
			@Override
			public void windowDeiconified(WindowEvent e)
			{
				if (useSystemTray) setVisible(true);
			}
		});
	    setSize(mainDialogSize);
		setPreferredSize(mainDialogSize);

		updateLookAndFeel(uiClassName);
		
		setJMenuBar(getBaseMenuBar());
		setContentPane(getBaseContentPane());
	    setPlayListIcons();
		// Volumecontrol by mousewheel:
	    addMouseWheelListener(new MouseWheelVolumeControl());
	    pack();

		if (mainDialogLocation == null || (mainDialogLocation.getX()==-1 || mainDialogLocation.getY()==-1))
			mainDialogLocation = Helpers.getFrameCenteredLocation(this, null); 
	    setLocation(mainDialogLocation);
	    getModInfoDialog().setVisible(modInfoDialogVisable);
		getPlaylistDialog().setVisible(playlistDialogVisable);
		getEffectDialog().setVisible(effectDialogVisable);
		getPlayerSetUpDialog().setVisible(playerSetUpDialogVisable);

		dropTargetList = new ArrayList<DropTarget>();
	    PlaylistDropListener myListener = new PlaylistDropListener(this);
	    Helpers.registerDropListener(dropTargetList, this, myListener);
	    
		MultimediaContainerManager.addMultimediaContainerEventListener(this);

	    createFileFilter();

	    currentContainer = null; //set Back to null!
	    showMessage("Ready...");
	}
	/**
	 * @param dtde
	 * @param dropResult
	 * @param addToLastLoaded
	 * @see de.quippy.javamod.main.gui.tools.PlaylistDropListenerCallBack#playlistRecieved(java.awt.dnd.DropTargetDropEvent, de.quippy.javamod.main.playlist.PlayList, java.net.URL)
	 * @since 08.03.2011
	 */
	public void playlistRecieved(DropTargetDropEvent dtde, PlayList dropResult, URL addToLastLoaded)
	{
		if (addToLastLoaded!=null) addFileToLastLoaded(addToLastLoaded);
		if (dropResult!=null)
		{
			doStopPlaying();
    		getPlaylistGUI().setNewPlaylist(currentPlayList = dropResult);
			boolean ok = doNextPlayListEntry();
			if (playerThread==null && ok) doStartPlaying();
		}
	}
	/**
	 * @see de.quippy.javamod.main.gui.playlist.PlaylistGUIChangeListener#playListChanged()
	 * @since 08.03.2011
	 */
	public void playListChanged(PlayList newPlayList)
	{
		if (newPlayList!=null)
		{
			if (newPlayList!=currentPlayList)
			{
				boolean playListWasEmpty = currentPlayList == null;
				currentPlayList = newPlayList;
				if (playListWasEmpty) doNextPlayListEntry();
			}
			setPlayListIcons();
			//if (playerThread==null) doStartPlaying();
		}
	}
	/**
	 * set the selected look and feel
	 * @since 01.07.2006
	 * @param lookAndFeelClassName
	 * @return
	 */
	private void setLookAndFeel(String lookAndFeelClassName)
	{
		try
		{
	        javax.swing.UIManager.setLookAndFeel(lookAndFeelClassName);
		}
		catch (Throwable e)
		{
			showMessage("The selected Look&Feel is not supported or not reachable through the classpath. Switching to system default...");
	        try
	        {
	        	lookAndFeelClassName = javax.swing.UIManager.getSystemLookAndFeelClassName();
	            javax.swing.UIManager.setLookAndFeel(lookAndFeelClassName);
	        }
	        catch (Throwable e1)
	        {
				Log.error("[MainForm]", e1);
	        }
		}
	}
	/**
	 * Changes the look and feel to the new ClassName
	 * @since 22.06.2006
	 * @param lookAndFeelClassName
	 * @return
	 */
	private void updateLookAndFeel(String lookAndFeelClassName)
	{
	    setLookAndFeel(lookAndFeelClassName);
	    MultimediaContainerManager.updateLookAndFeel();
		javax.swing.SwingUtilities.updateComponentTreeUI(this); pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getJavaModAbout()); getJavaModAbout().pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getModInfoDialog()); getModInfoDialog().pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getPlayerSetUpDialog()); getPlayerSetUpDialog().pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getURLDialog()); getURLDialog().pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getShowVersion_Text()); getShowVersion_Text().pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getPlaylistDialog()); getPlaylistDialog().pack();
	    javax.swing.SwingUtilities.updateComponentTreeUI(getEffectDialog()); getEffectDialog().pack();
	}
	private void changeInfoPane()
	{
		getModInfoPane().removeAll();
		getModInfoPane().add(getCurrentContainer().getInfoPanel(), java.awt.BorderLayout.CENTER);
		getModInfoDialog().pack();
		getModInfoDialog().repaint();
	}
	private void changeConfigPane()
	{
		getPlayerConfigPanel().selectTabForContainer(getCurrentContainer());
//		getPlayerSetUpPane().removeAll();
//		getPlayerSetUpPane().add(getCurrentContainer().getConfigPanel(), java.awt.BorderLayout.CENTER);
//		getPlayerSetUpDialog().pack();
//		getPlayerSetUpDialog().repaint();
	}
	private void changeExportMenu()
	{
		getMenu_File_exportWave().setEnabled(getCurrentContainer().canExport());
	}
	/**
	 * @since 15.01.2012
	 * @return
	 */
	public boolean isDSPEnabled()
	{
		if (audioProcessor!=null) return audioProcessor.isDspEnabled();
		return false;
	}
	/**
	 * @since 15.01.2012
	 * @param dspEnabled
	 */
	public void setDSPEnabled(boolean dspEnabled)
	{
		if (audioProcessor!=null) audioProcessor.setDspEnabled(dspEnabled);
	}
	/* Element Getter Methods ---------------------------------------------- */
	public javax.swing.JMenuBar getBaseMenuBar()
	{
		if (baseMenuBar == null)
		{
			baseMenuBar = new javax.swing.JMenuBar();
			baseMenuBar.setName("baseMenuBar");
			baseMenuBar.add(getMenu_File());
			baseMenuBar.add(getMenu_View());
			baseMenuBar.add(getMenu_LookAndFeel());
			baseMenuBar.add(getMenu_Help());
		}
		return baseMenuBar;
	}
	public javax.swing.JMenu getMenu_File()
	{
		if (menu_File == null)
		{
			menu_File = new javax.swing.JMenu();
			menu_File.setName("menu_File");
			menu_File.setMnemonic('f');
			menu_File.setText("File");
			menu_File.setFont(Helpers.DIALOG_FONT);
			menu_File.add(getMenu_File_openMod());
			menu_File.add(getMenu_File_openURL());
			menu_File.add(getMenu_File_exportWave());
			menu_File.add(new javax.swing.JSeparator());
			menu_File.add(getMenu_File_RecentFiles());
			menu_File.add(new javax.swing.JSeparator());
			menu_File.add(getMenu_File_Close());
		}
		return menu_File;
	}
	public javax.swing.JMenu getMenu_View()
	{
		if (menu_View == null)
		{
			menu_View = new javax.swing.JMenu();
			menu_View.setName("menu_View");
			menu_View.setMnemonic('v');
			menu_View.setText("View");
			menu_View.setFont(Helpers.DIALOG_FONT);
			menu_View.add(getMenu_View_Info());
			menu_View.add(getMenu_View_Setup());
			menu_View.add(getMenu_View_Playlist());
			menu_View.add(getMenu_View_GraphicEQ());
			menu_View.add(new javax.swing.JSeparator());
			menu_View.add(getMenu_View_UseSystemTray());
		}
		return menu_View;
	}
	public javax.swing.JMenu getMenu_LookAndFeel()
	{
		if (menu_LookAndFeel == null)
		{
			menu_LookAndFeel = new javax.swing.JMenu();
			menu_LookAndFeel.setName("menu_LookAndFeel");
			menu_LookAndFeel.setMnemonic('l');
			menu_LookAndFeel.setText("Look&Feel");
			menu_LookAndFeel.setFont(Helpers.DIALOG_FONT);
			
			String currentUIClassName = javax.swing.UIManager.getLookAndFeel().getClass().getName();
			javax.swing.UIManager.LookAndFeelInfo [] lookAndFeels = getInstalledLookAndFeels();
			menu_LookAndFeel_Items = new javax.swing.JCheckBoxMenuItem[lookAndFeels.length];
			for (int i=0; i<lookAndFeels.length; i++)
			{
				menu_LookAndFeel_Items[i] = new javax.swing.JCheckBoxMenuItem();
				menu_LookAndFeel_Items[i].setName("newMenuItem_"+i);
				menu_LookAndFeel_Items[i].setText(lookAndFeels[i].getName());
				menu_LookAndFeel_Items[i].setFont(Helpers.DIALOG_FONT);
				menu_LookAndFeel_Items[i].setToolTipText("Change to " + lookAndFeels[i].getName() + " look and feel");
				String uiClassName = lookAndFeels[i].getClassName();
				if (uiClassName.equals(currentUIClassName)) menu_LookAndFeel_Items[i].setSelected(true);
				menu_LookAndFeel_Items[i].addActionListener(new LookAndFeelChanger(menu_LookAndFeel_Items[i], uiClassName));
				menu_LookAndFeel.add(menu_LookAndFeel_Items[i]);
			}
			
		}
		return menu_LookAndFeel;
	}
	private javax.swing.JMenu getMenu_Help()
	{
		if (menu_Help == null)
		{
			menu_Help = new javax.swing.JMenu();
			menu_Help.setName("menu_Help");
			menu_Help.setMnemonic('h');
			menu_Help.setText("Help");
			menu_Help.setFont(Helpers.DIALOG_FONT);
			menu_Help.add(getMenu_Help_CheckUpdate());
			menu_Help.add(getMenu_Help_ShowVersionHistory());
			menu_Help.add(new javax.swing.JSeparator());
			menu_Help.add(getMenu_Help_About());
		}
		return menu_Help;
	}
	private javax.swing.JMenuItem getMenu_File_openMod()
	{
		if (menu_File_openMod == null)
		{
			menu_File_openMod = new javax.swing.JMenuItem();
			menu_File_openMod.setName("menu_File_openMod");
			menu_File_openMod.setMnemonic('o');
			menu_File_openMod.setText("Open Sound File...");
			menu_File_openMod.setFont(Helpers.DIALOG_FONT);
			menu_File_openMod.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doOpenFile();
				}
			});
		}
		return menu_File_openMod;
	}
	private javax.swing.JMenuItem getMenu_File_openURL()
	{
		if (menu_File_openURL == null)
		{
			menu_File_openURL = new javax.swing.JMenuItem();
			menu_File_openURL.setName("menu_File_openURL");
			menu_File_openURL.setMnemonic('u');
			menu_File_openURL.setText("Open an URL...");
			menu_File_openURL.setFont(Helpers.DIALOG_FONT);
			menu_File_openURL.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doOpenURL();
				}
			});
		}
		return menu_File_openURL;
	}
	private javax.swing.JMenuItem getMenu_File_exportWave()
	{
		if (menu_File_exportWave == null)
		{
			menu_File_exportWave = new javax.swing.JMenuItem();
			menu_File_exportWave.setName("menu_File_exportWave");
			menu_File_exportWave.setMnemonic('e');
			menu_File_exportWave.setText("Export to wave...");
			menu_File_exportWave.setFont(Helpers.DIALOG_FONT);
			menu_File_exportWave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doExportToWave();
				}
			});
		}
		return menu_File_exportWave;
	}
	private javax.swing.JMenu getMenu_File_RecentFiles()
	{
		if (menu_File_RecentFiles == null)
		{
			menu_File_RecentFiles = new javax.swing.JMenu();
			menu_File_RecentFiles.setName("menu_File_RecentFiles");
			menu_File_RecentFiles.setMnemonic('r');
			menu_File_RecentFiles.setText("Recent files");
			menu_File_RecentFiles.setFont(Helpers.DIALOG_FONT);
			
			createRecentFileMenuItems();
		}
		return menu_File_RecentFiles;
	}
	private void createRecentFileMenuItems()
	{
		javax.swing.JMenu recent = getMenu_File_RecentFiles();
		recent.removeAll();
		for (int i=0, index=1; i<PROPERTY_LASTLOADED_MAXENTRIES; i++)
		{
			URL element = lastLoaded.get(i);
			if (element!=null)
			{
				String displayName = null;
				// convert to a local filename if possible (that looks better!)
				if (element.getProtocol().equalsIgnoreCase("file"))
				{
					try
					{
						File f = new File(element.toURI());
						displayName = f.getAbsolutePath();
					}
					catch (URISyntaxException ex)
					{
					}
				}
				
				if (displayName==null) displayName = lastLoaded.get(i).toString();
				javax.swing.JMenuItem lastLoadURL = new javax.swing.JMenuItem();
				lastLoadURL.setName("menu_File_RecentFiles_File"+i);
				lastLoadURL.setText(((index<10)?"  ":"") + (index++) + " " + displayName);
				lastLoadURL.setFont(Helpers.DIALOG_FONT);
				lastLoadURL.setToolTipText(element.toString());
				lastLoadURL.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						try
						{
							URL url = new URL(((javax.swing.JMenuItem)e.getSource()).getToolTipText());
							loadMultimediaOrPlayListFile(url);
						}
						catch (Exception ex)
						{
							Log.error("Load recent error", ex);
						}
					}
				});
				recent.add(lastLoadURL);
			}
		}
	}
	private javax.swing.JMenuItem getMenu_File_Close()
	{
		if (menu_File_Close == null)
		{
			menu_File_Close = new javax.swing.JMenuItem();
			menu_File_Close.setName("menu_File_Close");
			menu_File_Close.setMnemonic('c');
			menu_File_Close.setText("Close");
			menu_File_Close.setFont(Helpers.DIALOG_FONT);
			menu_File_Close.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doClose();
				}
			});
		}
		return menu_File_Close;
	}
	private javax.swing.JMenuItem getMenu_View_Info()
	{
		if (menu_View_Info == null)
		{
			menu_View_Info = new javax.swing.JMenuItem();
			menu_View_Info.setName("menu_View_Info");
			menu_View_Info.setMnemonic('p');
			menu_View_Info.setText("Properties...");
			menu_View_Info.setFont(Helpers.DIALOG_FONT);
			menu_View_Info.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					getModInfoDialog().setVisible(true);
				}
			});
		}
		return menu_View_Info;
	}
	private javax.swing.JMenuItem getMenu_View_Setup()
	{
		if (menu_View_Setup == null)
		{
			menu_View_Setup = new javax.swing.JMenuItem();
			menu_View_Setup.setName("menu_View_Setup");
			menu_View_Setup.setMnemonic('s');
			menu_View_Setup.setText("Setup...");
			menu_View_Setup.setFont(Helpers.DIALOG_FONT);
			menu_View_Setup.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					getPlayerSetUpDialog().setVisible(true);
				}
			});
		}
		return menu_View_Setup;
	}
	private javax.swing.JMenuItem getMenu_View_Playlist()
	{
		if (menu_View_Playlist == null)
		{
			menu_View_Playlist = new javax.swing.JMenuItem();
			menu_View_Playlist.setName("menu_View_Playlist");
			menu_View_Playlist.setMnemonic('p');
			menu_View_Playlist.setText("Playlist...");
			menu_View_Playlist.setFont(Helpers.DIALOG_FONT);
			menu_View_Playlist.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					getPlaylistDialog().setVisible(true);
				}
			});
		}
		return menu_View_Playlist;
	}
	private javax.swing.JMenuItem getMenu_View_GraphicEQ()
	{
		if (menu_View_GraphicEQ == null)
		{
			menu_View_GraphicEQ = new javax.swing.JMenuItem();
			menu_View_GraphicEQ.setName("menu_View_GraphicEQ");
			menu_View_GraphicEQ.setMnemonic('e');
			menu_View_GraphicEQ.setText("Effect...");
			menu_View_GraphicEQ.setFont(Helpers.DIALOG_FONT);
			menu_View_GraphicEQ.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					getEffectDialog().setVisible(true);
				}
			});
		}
		return menu_View_GraphicEQ;
	}
	private javax.swing.JCheckBoxMenuItem getMenu_View_UseSystemTray()
	{
		if (menu_View_UseSystemTray == null)
		{
			menu_View_UseSystemTray = new javax.swing.JCheckBoxMenuItem();
			menu_View_UseSystemTray.setName("menu_View_UseSystemTray");
			menu_View_UseSystemTray.setMnemonic('t');
			menu_View_UseSystemTray.setText("Use system tray");
			menu_View_UseSystemTray.setFont(Helpers.DIALOG_FONT);
			menu_View_UseSystemTray.setEnabled(SystemTray.isSupported());
			menu_View_UseSystemTray.setSelected(useSystemTray);
			menu_View_UseSystemTray.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					useSystemTray = getMenu_View_UseSystemTray().isSelected();
					setSystemTray();
				}
			});
		}
		return menu_View_UseSystemTray;
	}
	private javax.swing.JMenuItem getMenu_Help_CheckUpdate()
	{
		if (menu_Help_CheckUpdate == null)
		{
			menu_Help_CheckUpdate = new javax.swing.JMenuItem();
			menu_Help_CheckUpdate.setName("menu_Help_CheckUpdate");
			menu_Help_CheckUpdate.setMnemonic('c');
			menu_Help_CheckUpdate.setText("Check for update...");
			menu_Help_CheckUpdate.setFont(Helpers.DIALOG_FONT);
			menu_Help_CheckUpdate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doCheckUpdate();
				}
			});
		}
		return menu_Help_CheckUpdate;
	}
	private javax.swing.JMenuItem getMenu_Help_ShowVersionHistory()
	{
		if (menu_Help_ShowVersionHistory == null)
		{
			menu_Help_ShowVersionHistory = new javax.swing.JMenuItem();
			menu_Help_ShowVersionHistory.setName("menu_Help_showVersionHistory");
			menu_Help_ShowVersionHistory.setMnemonic('s');
			menu_Help_ShowVersionHistory.setText("Show version history...");
			menu_Help_ShowVersionHistory.setFont(Helpers.DIALOG_FONT);
			menu_Help_ShowVersionHistory.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					SimpleTextViewerDialog dialog = getShowVersion_Text();
					dialog.setDisplayTextFromURL(Helpers.VERSION_URL);
					dialog.setVisible(true);
				}
			});
		}
		return menu_Help_ShowVersionHistory;
	}
	private javax.swing.JMenuItem getMenu_Help_About()
	{
		if (menu_Help_About == null)
		{
			menu_Help_About = new javax.swing.JMenuItem();
			menu_Help_About.setName("menu_Help_About");
			menu_Help_About.setMnemonic('a');
			menu_Help_About.setText("About...");
			menu_Help_About.setFont(Helpers.DIALOG_FONT);
			menu_Help_About.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					doShowAbout();
				}
			});
		}
		return menu_Help_About;
	}
	private MenuItem getAboutItem()
	{
		if (aboutItem==null)
		{
			aboutItem = new MenuItem("About");
			aboutItem.setFont(Helpers.DIALOG_FONT);
			aboutItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doShowAbout();
				}
			});
		}
		return aboutItem;
	}
	private MenuItem getPlayItem()
	{
		if (playItem==null)
		{
			playItem = new MenuItem("Play");
			playItem.setFont(Helpers.DIALOG_FONT);
			playItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doStartPlaying();
				}
			});
		}
		return playItem;
	}
	private MenuItem getPauseItem()
	{
		if (pauseItem==null)
		{
			pauseItem = new MenuItem("Pause");
			pauseItem.setFont(Helpers.DIALOG_FONT);
			pauseItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doPausePlaying();
				}
			});
		}
		return pauseItem;
	}
	private MenuItem getStopItem()
	{
		if (stopItem==null)
		{
			stopItem = new MenuItem("Stop");
			stopItem.setFont(Helpers.DIALOG_FONT);
			stopItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doStopPlaying();
				}
			});
		}
		return stopItem;
	}
	private MenuItem getPrevItem()
	{
		if (prevItem==null)
		{
			prevItem = new MenuItem("Previous");
			prevItem.setFont(Helpers.DIALOG_FONT);
			prevItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doPrevPlayListEntry();
				}
			});
		}
		return prevItem;
	}
	private MenuItem getNextItem()
	{
		if (nextItem==null)
		{
			nextItem = new MenuItem("Next");
			nextItem.setFont(Helpers.DIALOG_FONT);
			nextItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doNextPlayListEntry();
				}
			});
		}
		return nextItem;
	}
	private MenuItem getCloseItem()
	{
		if (closeItem==null)
		{
			closeItem = new MenuItem("Close");
			closeItem.setFont(Helpers.DIALOG_FONT);
			closeItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					doClose();
				}
			});
		}
		return closeItem;
	}
	private TrayIcon getTrayIcon()
	{
		if (javaModTrayIcon==null)
		{
			final java.net.URL iconURL = MainForm.class.getResource(DEFAULTICONPATH);
			if (iconURL!=null)
			{
				javaModTrayIcon = new TrayIcon(java.awt.Toolkit.getDefaultToolkit().getImage(iconURL));
				javaModTrayIcon.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if (SwingUtilities.isLeftMouseButton(e))
						{
							final MainForm me = MainForm.this;
							me.setVisible(true);
							me.setExtendedState(me.getExtendedState() & ~ICONIFIED);
						}
					}
				});
				// Add components to pop-up menu
				final PopupMenu popUp = new PopupMenu();
				popUp.add(getAboutItem());
				popUp.addSeparator();
				popUp.add(getPlayItem());
				popUp.add(getPauseItem());
				popUp.add(getStopItem());
				popUp.add(getPrevItem());
				popUp.add(getNextItem());
				popUp.addSeparator();
				popUp.add(getCloseItem());
				javaModTrayIcon.setPopupMenu(popUp);
			}
		}
		return javaModTrayIcon;
	}
	/**
	 * 
	 * @since 07.02.2012
	 */
	private void setSystemTray()
	{
		// Check the SystemTray is supported
		if (SystemTray.isSupported())
		{
			final SystemTray tray = SystemTray.getSystemTray();
			try
			{
				tray.remove(getTrayIcon());
				if (useSystemTray)
				{
					tray.add(getTrayIcon());
				}
			}
			catch (AWTException e)
			{
				Log.error("TrayIcon could not be added.", e);
			}
		}
	}
	public javax.swing.JPanel getBaseContentPane()
	{
		if (baseContentPane==null)
		{
			baseContentPane = new javax.swing.JPanel();
			baseContentPane.setName("baseContentPane");
			baseContentPane.setLayout(new java.awt.BorderLayout());

			baseContentPane.add(getMessages(), java.awt.BorderLayout.SOUTH);
			baseContentPane.add(getMainContentPane(), java.awt.BorderLayout.CENTER);
		}
		return baseContentPane;
	}
	public javax.swing.JTextField getMessages()
	{
		if (messages==null)
		{
			messages = new javax.swing.JTextField();
			messages.setName("messages");
			messages.setEditable(false);
			messages.setFont(Helpers.DIALOG_FONT);
		}
		return messages;
	}
	public javax.swing.JPanel getMainContentPane()
	{
		if (mainContentPane==null)
		{
			mainContentPane = new javax.swing.JPanel();
			mainContentPane.setName("mainContentPane");
			mainContentPane.setLayout(new java.awt.GridBagLayout());

			mainContentPane.add(getMusicDataPane(),		Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
			mainContentPane.add(getPlayerDataPane(),	Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
			mainContentPane.add(getPlayerControlPane(),	Helpers.getGridBagConstraint(0, 2, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
		}
		return mainContentPane;
	}
	private JavaModAbout getJavaModAbout()
	{
		if (about == null)
		{
			about = new JavaModAbout(this, true);
			about.addWindowFocusListener(makeMainWindowVisiable);
		}
		else
			about.setLocation(Helpers.getFrameCenteredLocation(about, this));
		return about;
	}
	private UrlDialog getURLDialog()
	{
		if (urlDialog == null)
		{
			urlDialog = new UrlDialog(this, true, "");
			urlDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		else
			urlDialog.setLocation(Helpers.getFrameCenteredLocation(urlDialog, this));
		return urlDialog;
	}
	public javax.swing.JDialog getEffectDialog()
	{
		if (equalizerDialog==null)
		{
			equalizerDialog = new JDialog(this, "Effect", false);
			equalizerDialog.setName("equalizerDialog");
			equalizerDialog.setSize(effectsDialogSize);
			equalizerDialog.setPreferredSize(effectsDialogSize);
			equalizerDialog.setContentPane(getEffectPane());
			if (effectsDialogLocation == null || (effectsDialogLocation.getX()==-1 || effectsDialogLocation.getY()==-1))
				effectsDialogLocation = Helpers.getFrameCenteredLocation(equalizerDialog, null); 
			equalizerDialog.setLocation(effectsDialogLocation);
			equalizerDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		return equalizerDialog;
	}
	public javax.swing.JDialog getPlayerSetUpDialog()
	{
		if (playerSetUpDialog==null)
		{
			playerSetUpDialog = new JDialog(this, "Configuration", false);
			playerSetUpDialog.setName("playerSetUpDialog");
			playerSetUpDialog.setSize(playerSetUpDialogSize);
			playerSetUpDialog.setPreferredSize(playerSetUpDialogSize);
			playerSetUpDialog.setContentPane(getPlayerSetUpPane());
			if (playerSetUpDialogLocation == null || (playerSetUpDialogLocation.getX()==-1 || playerSetUpDialogLocation.getY()==-1))
				playerSetUpDialogLocation = Helpers.getFrameCenteredLocation(playerSetUpDialog, null); 
			playerSetUpDialog.setLocation(playerSetUpDialogLocation);
			playerSetUpDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		return playerSetUpDialog;
	}
	public javax.swing.JDialog getModInfoDialog()
	{
		if (modInfoDialog==null)
		{
			modInfoDialog = new JDialog(this, "File properties", false);
			modInfoDialog.setName("modInfoDialog");
			modInfoDialog.setSize(modInfoDialogSize);
			modInfoDialog.setPreferredSize(modInfoDialogSize);
			modInfoDialog.setContentPane(getModInfoPane());
			if (modInfoDialogLocation == null || (modInfoDialogLocation.getX()==-1 || modInfoDialogLocation.getY()==-1))
				modInfoDialogLocation = Helpers.getFrameCenteredLocation(modInfoDialog, null); 
		    modInfoDialog.setLocation(modInfoDialogLocation);
		    modInfoDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		return modInfoDialog;
	}
	public javax.swing.JPanel getModInfoPane()
	{
		if (modInfoPane==null)
		{
			modInfoPane = new javax.swing.JPanel();
			modInfoPane.setName("ModInfoPane");
			modInfoPane.setLayout(new java.awt.BorderLayout());
			modInfoPane.setBorder(new TitledBorder(null, "Multimedia File Info", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));
			changeInfoPane();
		}
		return modInfoPane;
	}
	public javax.swing.JDialog getPlaylistDialog()
	{
		if (playlistDialog==null)
		{
			playlistDialog = new JDialog(this, "Playlist", false);
			playlistDialog.setName("playlistDialog");
			playlistDialog.setSize(playlistDialogSize);
			playlistDialog.setPreferredSize(playlistDialogSize);
			playlistDialog.setContentPane(getPlaylistPane());
			if (playlistDialogLocation == null || (playlistDialogLocation.getX()==-1 || playlistDialogLocation.getY()==-1))
				playlistDialogLocation = Helpers.getFrameCenteredLocation(playlistDialog, null); 
			playlistDialog.setLocation(playlistDialogLocation);
			playlistDialog.addWindowFocusListener(makeMainWindowVisiable);
		}
		return playlistDialog;
	}
	public javax.swing.JPanel getPlaylistPane()
	{
		if (playlistPane==null)
		{
			playlistPane = new javax.swing.JPanel();
			playlistPane.setName("playlistPane");
			playlistPane.setLayout(new java.awt.BorderLayout());
			playlistPane.setBorder(new TitledBorder(null, "Playlist", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));
			playlistPane.add(getPlaylistGUI());
		}
		return playlistPane;
	}
	public PlayListGUI getPlaylistGUI()
	{
		if (playlistGUI==null)
		{
			playlistGUI = new PlayListGUI(getPlaylistDialog());
			playlistGUI.addPlaylistGUIChangeListener(this);
		}
		return playlistGUI;
	}
	public javax.swing.JPanel getEffectPane()
	{
		if (effectPane==null)
		{
			effectPane = new javax.swing.JPanel();
			effectPane.setName("effectPane");
			effectPane.setLayout(new java.awt.BorderLayout());
			effectPane.setBorder(new TitledBorder(null, "Effects", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));
			effectPane.add(getEffectsPanel());
		}
		return effectPane;
	}
	private GraphicEqGUI getEqualizerGui()
	{
		if (equalizerGUI==null)
		{
			equalizerGUI = new GraphicEqGUI(currentEqualizer);
		}
		return equalizerGUI;
	}
	private PitchShiftGUI getPitchShiftGui()
	{
		if (pitchShiftGUI==null)
		{
			pitchShiftGUI = new PitchShiftGUI(currentPitchShift);
		}
		return pitchShiftGUI;
	}
	public EffectsPanel getEffectsPanel()
	{
		if (effectGUI==null)
		{
			javax.swing.JPanel [] effectPanels = 
			{
			 	getEqualizerGui(),
			 	getPitchShiftGui()
			};
			effectGUI = new EffectsPanel(effectPanels, audioProcessor);
		}
		return effectGUI;
	}
	public javax.swing.JPanel getPlayerSetUpPane()
	{
		if (playerSetUpPane==null)
		{
			playerSetUpPane = new javax.swing.JPanel();
			playerSetUpPane.setName("playerSetUpPane");
			playerSetUpPane.setLayout(new java.awt.BorderLayout());
			playerSetUpPane.setBorder(new TitledBorder(null, "Mixer Control", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));
			playerSetUpPane.add(getPlayerConfigPanel());
			changeConfigPane();
		}
		return playerSetUpPane;
	}
	/**
	 * @since 10.12.2011
	 * @return
	 */
	public PlayerConfigPanel getPlayerConfigPanel()
	{
		if (playerConfigPanel==null)
		{
			playerConfigPanel = new PlayerConfigPanel();
		}
		return playerConfigPanel;
	}
	public SimpleProgessDialog getDownloadDialog()
	{
		if (downloadDialog==null)
		{
			downloadDialog = new SimpleProgessDialog(this, "Download progress");
			downloadDialog.setSize(350, 90);
			downloadDialog.setPreferredSize(downloadDialog.getSize());
			downloadDialog.pack();
		}
		return downloadDialog;
	}
	public SAMeterPanel getSALMeterPanel()
	{
		if (saLMeterPanel==null)
		{
			saLMeterPanel = new SAMeterPanel(50, 25);
			Dimension d = new Dimension(104, 60);
			saLMeterPanel.setSize(d);
			saLMeterPanel.setMaximumSize(d);
			saLMeterPanel.setMinimumSize(d);
			saLMeterPanel.setPreferredSize(d);
			saLMeterPanel.setDoubleBuffered(true);
			saLMeterPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return saLMeterPanel;
	}
	public SAMeterPanel getSARMeterPanel()
	{
		if (saRMeterPanel==null)
		{
			saRMeterPanel = new SAMeterPanel(50, 25);
			Dimension d = new Dimension(104, 60);
			saRMeterPanel.setSize(d);
			saRMeterPanel.setMaximumSize(d);
			saRMeterPanel.setMinimumSize(d);
			saRMeterPanel.setPreferredSize(d);
			saRMeterPanel.setDoubleBuffered(true);
			saRMeterPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return saRMeterPanel;
	}
	public VUMeterPanel getVULMeterPanel()
	{
		if (vuLMeterPanel==null)
		{
			vuLMeterPanel = new VUMeterPanel(50);
			Dimension d = new Dimension(20, 100);
			vuLMeterPanel.setSize(d);
			vuLMeterPanel.setMaximumSize(d);
			vuLMeterPanel.setMinimumSize(d);
			vuLMeterPanel.setPreferredSize(d);
			vuLMeterPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return vuLMeterPanel;
	}
	public VUMeterPanel getVURMeterPanel()
	{
		if (vuRMeterPanel==null)
		{
			vuRMeterPanel = new VUMeterPanel(50);
			Dimension d = new Dimension(20, 100);
			vuRMeterPanel.setSize(d);
			vuRMeterPanel.setMaximumSize(d);
			vuRMeterPanel.setMinimumSize(d);
			vuRMeterPanel.setPreferredSize(d);
			vuRMeterPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		return vuRMeterPanel;
	}
	public javax.swing.JPanel getMusicDataPane()
	{
		if (musicDataPane==null)
		{
			musicDataPane = new javax.swing.JPanel();
			musicDataPane.setName("musicDataPane");
			musicDataPane.setLayout(new java.awt.GridBagLayout());
			musicDataPane.setBorder(new TitledBorder(null, "Name", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));
			
			musicDataPane.add(getLEDScrollPanel(), Helpers.getGridBagConstraint(0, 0, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
		}
		return musicDataPane;
	}
	public LEDScrollPanel getLEDScrollPanel()
	{
		final int chars = 15; // show 15 chars
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
	public javax.swing.JPanel getPlayerDataPane()
	{
		if (playerDataPane==null)
		{
			playerDataPane = new javax.swing.JPanel();
			playerDataPane.setName("playerDataPane");
			playerDataPane.setLayout(new java.awt.GridBagLayout());
			playerDataPane.setBorder(new TitledBorder(null, "Player Data", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));
			
			playerDataPane.add(getVULMeterPanel(), Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerDataPane.add(getSALMeterPanel(), Helpers.getGridBagConstraint(1, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerDataPane.add(getSARMeterPanel(), Helpers.getGridBagConstraint(2, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerDataPane.add(getVURMeterPanel(), Helpers.getGridBagConstraint(3, 0, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
		}
		return playerDataPane;
	}
	public javax.swing.JPanel getPlayerControlPane()
	{
		if (playerControlPane==null)
		{
			playerControlPane = new javax.swing.JPanel();
			playerControlPane.setName("playerControlPane");
			playerControlPane.setLayout(new java.awt.GridBagLayout());
			playerControlPane.setBorder(new TitledBorder(null, "Player Control", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, Helpers.DIALOG_FONT, null));

			playerControlPane.add(getButton_Prev(),		Helpers.getGridBagConstraint(0, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getButton_Play(),		Helpers.getGridBagConstraint(1, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getButton_Next(),		Helpers.getGridBagConstraint(2, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getButton_Pause(),	Helpers.getGridBagConstraint(3, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getButton_Stop(),		Helpers.getGridBagConstraint(4, 0, 2, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getVolumeSlider(),	Helpers.getGridBagConstraint(5, 0, 1, 1, java.awt.GridBagConstraints.VERTICAL, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
			playerControlPane.add(getBalanceSlider(),	Helpers.getGridBagConstraint(6, 0, 1, 0, java.awt.GridBagConstraints.VERTICAL, java.awt.GridBagConstraints.CENTER, 0.0, 1.0));
			playerControlPane.add(getVolumeLabel(),		Helpers.getGridBagConstraint(5, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getBalanceLabel(),	Helpers.getGridBagConstraint(6, 1, 1, 0, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 0.0, 0.0));
			playerControlPane.add(getSeekBarPanel(),	Helpers.getGridBagConstraint(0, 2, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.CENTER, 1.0, 1.0));
		}
		return playerControlPane;
	}
	private SeekBarPanel getSeekBarPanel()
	{
		if (seekBarPanel==null)
		{
			seekBarPanel = new SeekBarPanel(30, false);
			seekBarPanel.setName("SeekBarPanel");
			seekBarPanel.addListener(new SeekBarPanelListener()
			{
				@Override
				public void valuesChanged(long milliseconds)
				{
					if (currentPlayList!=null && playerThread!=null && playerThread.isRunning()) 
						currentPlayList.setCurrentElementByTimeIndex(milliseconds);
				}
			});
		}
		return seekBarPanel;
	}
	private javax.swing.JButton getButton_Play()
	{
		if (button_Play == null)
		{
			buttonPlay_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_NORMAL));
			buttonPlay_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_INACTIVE));
			buttonPlay_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPLAY_ACTIVE));

			button_Play = new javax.swing.JButton();
			button_Play.setName("button_Play");
			button_Play.setText("");
			button_Play.setToolTipText("play");
			button_Play.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Play.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Play.setIcon(buttonPlay_normal);
			button_Play.setDisabledIcon(buttonPlay_Inactive);
			button_Play.setPressedIcon(buttonPlay_Active);
			button_Play.setMargin(new java.awt.Insets(4, 6, 4, 6));
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
	private javax.swing.JButton getButton_Pause()
	{
		if (button_Pause == null)
		{
			buttonPause_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_NORMAL));
			buttonPause_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_INACTIVE));
			buttonPause_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPAUSE_ACTIVE));

			button_Pause = new javax.swing.JButton();
			button_Pause.setName("button_Pause");
			button_Pause.setText("");
			button_Pause.setToolTipText("pause");
			button_Pause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Pause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Pause.setIcon(buttonPause_normal);
			button_Pause.setDisabledIcon(buttonPause_Inactive);
			button_Pause.setPressedIcon(buttonPause_Active);
			button_Pause.setMargin(new java.awt.Insets(4, 6, 4, 6));
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
	private javax.swing.JButton getButton_Stop()
	{
		if (button_Stop == null)
		{
			buttonStop_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_NORMAL));
			buttonStop_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_INACTIVE));
			buttonStop_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONSTOP_ACTIVE));

			button_Stop = new javax.swing.JButton();
			button_Stop.setName("button_Stop");
			button_Stop.setText("");
			button_Stop.setToolTipText("stop");
			button_Stop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Stop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Stop.setIcon(buttonStop_normal);
			button_Stop.setDisabledIcon(buttonStop_Inactive);
			button_Stop.setPressedIcon(buttonStop_Active);
			button_Stop.setMargin(new java.awt.Insets(4, 6, 4, 6));
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
	private javax.swing.JButton getButton_Prev()
	{
		if (button_Prev == null)
		{
			buttonPrev_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONPREV_NORMAL));
			buttonPrev_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONPREV_INACTIVE));
			buttonPrev_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONPREV_ACTIVE));

			button_Prev = new javax.swing.JButton();
			button_Prev.setName("button_Prev");
			button_Prev.setText("");
			button_Prev.setToolTipText("previous");
			button_Prev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Prev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Prev.setIcon(buttonPrev_normal);
			button_Prev.setDisabledIcon(buttonPrev_Inactive);
			button_Prev.setPressedIcon(buttonPrev_Active);
			button_Prev.setMargin(new java.awt.Insets(4, 6, 4, 6));
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
	private javax.swing.JButton getButton_Next()
	{
		if (button_Next == null)
		{
			buttonNext_normal = new javax.swing.ImageIcon(getClass().getResource(BUTTONNEXT_NORMAL));
			buttonNext_Inactive = new javax.swing.ImageIcon(getClass().getResource(BUTTONNEXT_INACTIVE));
			buttonNext_Active = new javax.swing.ImageIcon(getClass().getResource(BUTTONNEXT_ACTIVE));

			button_Next = new javax.swing.JButton();
			button_Next.setName("button_Next");
			button_Next.setText("");
			button_Next.setToolTipText("next");
			button_Next.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			button_Next.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			button_Next.setIcon(buttonNext_normal);
			button_Next.setDisabledIcon(buttonNext_Inactive);
			button_Next.setPressedIcon(buttonNext_Active);
			button_Next.setMargin(new java.awt.Insets(4, 6, 4, 6));
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
	public javax.swing.JLabel getVolumeLabel()
	{
		if (volumeLabel==null)
		{
			volumeLabel = new JLabel("Volume");
			volumeLabel.setFont(Helpers.DIALOG_FONT);
		}
		return volumeLabel;
	}
	public RoundSlider getVolumeSlider()
	{
		if (volumeSlider==null)
		{
			volumeSlider = new RoundSlider();
			volumeSlider.setSize(new Dimension(20,20));
			volumeSlider.setMinimumSize(new Dimension(20,20));
			volumeSlider.setMaximumSize(new Dimension(20,20));
			volumeSlider.setPreferredSize(new Dimension(20,20));
			volumeSlider.setValue(currentVolume);
			volumeSlider.setToolTipText(Float.toString(currentVolume*100f) + '%');
			volumeSlider.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					RoundSlider slider = (RoundSlider) e.getSource();
					if (e.getClickCount()>1)
					{
						slider.setValue(0.5f);
						e.consume();
					}
				}
			});
			volumeSlider.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					RoundSlider slider = (RoundSlider) e.getSource();
					currentVolume = slider.getValue();
					if (currentVolume<0) currentVolume=0;
					else
					if (currentVolume>1) currentVolume=1;
					slider.setToolTipText(Float.toString(currentVolume*100f) + '%');
					doSetVoumeValue();
				}
			});
		}
		return volumeSlider;
	}
	public javax.swing.JLabel getBalanceLabel()
	{
		if (balanceLabel==null)
		{
			balanceLabel = new JLabel("Balance");
			balanceLabel.setFont(Helpers.DIALOG_FONT);
		}
		return balanceLabel;
	}
	public RoundSlider getBalanceSlider()
	{
		if (balanceSlider==null)
		{
			balanceSlider = new RoundSlider();
			balanceSlider.setSize(new Dimension(20,20));
			balanceSlider.setMinimumSize(new Dimension(20,20));
			balanceSlider.setMaximumSize(new Dimension(20,20));
			balanceSlider.setPreferredSize(new Dimension(20,20));
			balanceSlider.setValue((currentBalance + 1f)/2f);
			balanceSlider.setToolTipText(Float.toString(currentBalance*100f) + '%');
			balanceSlider.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					RoundSlider slider = (RoundSlider) e.getSource();
					if (e.getClickCount()>1)
					{
						slider.setValue(0.5f);
						e.consume();
					}
				}
			});
			balanceSlider.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					RoundSlider slider = (RoundSlider) e.getSource();
					currentBalance = (slider.getValue()*2f)-1f;
					slider.setToolTipText(Float.toString(currentBalance*100f) + '%');
					doSetBalanceValue();
				}
			});
		}
		return balanceSlider;
	}
	/* DspAudioProcessor CallBack -------------------------------------------*/
	public void currentSampleChanged(float [] leftSample, float [] rightSample)
	{
		getVULMeterPanel().setVUMeter(leftSample);
		getVURMeterPanel().setVUMeter(rightSample);

		getSALMeterPanel().setMeter(leftSample);
		getSARMeterPanel().setMeter(rightSample);
	}
	public void multimediaContainerEventOccured(MultimediaContainerEvent event)
	{
		if (event.getType() == MultimediaContainerEvent.SONG_NAME_CHANGED)
			getLEDScrollPanel().addScrollText(event.getEvent().toString() + Helpers.SCROLLY_BLANKS);
		else
		if (event.getType() == MultimediaContainerEvent.SONG_NAME_CHANGED_OLD_INVALID)
			getLEDScrollPanel().setScrollTextTo(event.getEvent().toString() + Helpers.SCROLLY_BLANKS);
		getTrayIcon().setToolTip(event.getEvent().toString());
	}
	/**
	 * @param thread
	 * @see de.quippy.javamod.main.gui.PlayThreadEventListener#playThreadEventOccured(de.quippy.javamod.main.gui.PlayThread)
	 */
	public void playThreadEventOccured(PlayThread thread)
	{
		if (thread.isRunning())
		{
			getButton_Play().setIcon(buttonPlay_Active);
		}
		else // Signaling: not running-->Piece finished...
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
	private void setPlayListIcons()
	{
		if (currentPlayList==null)
		{
			getButton_Prev().setEnabled(false);
			getButton_Next().setEnabled(false);
		}
		else
		{
			getButton_Prev().setEnabled(currentPlayList.hasPrevious());
			getButton_Next().setEnabled(currentPlayList.hasNext());
		}
		getPrevItem().setEnabled(getButton_Prev().isEnabled());
		getNextItem().setEnabled(getButton_Next().isEnabled());
	}
	/* EVENT METHODS --------------------------------------------------------*/
	/**
	 * Default Close Operation
	 * @since 22.06.2006
	 */
	private void doClose()
	{
		// set visible, if system tray active and frame is iconified
		if (useSystemTray && (getExtendedState()&ICONIFIED)!=0) setVisible(true);
		
		doStopPlaying();
		getSeekBarPanel().pauseThread();
		getVULMeterPanel().pauseThread();
		getVURMeterPanel().pauseThread();
		getSALMeterPanel().pauseThread();
		getSARMeterPanel().pauseThread();
		getLEDScrollPanel().pauseThread();
		writePropertyFile();
		if (audioProcessor!=null) audioProcessor.removeListener(this);
		
		MultimediaContainerManager.removeMultimediaContainerEventListener(this);

		useSystemTray = false; setSystemTray();

		getJavaModAbout().setVisible(false);
	    getModInfoDialog().setVisible(false);
		getPlayerSetUpDialog().setVisible(false);
		getURLDialog().setVisible(false);
		getShowVersion_Text().setVisible(false);
		getPlaylistDialog().setVisible(false);
		getEffectDialog().setVisible(false);
		setVisible(false);

		getJavaModAbout().dispose();
		getModInfoDialog().dispose();
		getPlayerSetUpDialog().dispose();
		getURLDialog().dispose();
		getShowVersion_Text().dispose();
		getPlaylistDialog().dispose();
		getEffectDialog().dispose();
		dispose();
		
		System.exit(0); // this should not be needed! 
	}
	/**
	 * Open a new ModFile
	 * @since 22.06.2006
	 */
	private void doOpenFile()
	{
		FileChooserResult selectedFile = Helpers.selectFileNameFor(this, searchPath, "Load a Sound-File", fileFilterLoad, 0, true);
		if (selectedFile!=null) 
			doOpenFile(selectedFile.getSelectedFiles());
	}
	/**
	 * Open a new File
	 * @since 22.06.2006
	 */
	public void doOpenFile(File[] files)
	{
	    if (files!=null)
	    {
	    	if (files.length==1)
	    	{
	    		File f = files[0];
		    	if (f.isFile())
		    	{
			    	String modFileName = f.getAbsolutePath();
			    	int i = modFileName.lastIndexOf(File.separatorChar);
			    	searchPath = modFileName.substring(0, i);
		    		loadMultimediaOrPlayListFile(Helpers.createURLfromFile(f)); 
		    	}
		    	else
		    	if (f.isDirectory())
		    	{
			    	searchPath = f.getAbsolutePath();
		    	}
	    	}
	    	else
	    	{
	    		playlistRecieved(null, PlayList.createNewListWithFiles(files, false, false), null);
	    	}
	    }
	}
	/**
	 * Open a new ModFile
	 * @since 17.10.2007
	 */
	private void doOpenURL()
	{
		getURLDialog().setVisible(true);
		String url = getURLDialog().getURL();
		if (url!=null && url.length()!=0) doOpenURL(url);
	}
	/**
	 * Open a new File
	 * @since 22.06.2006
	 */
	public void doOpenURL(String surl)
	{
	    if (surl!=null)
	    {
	    	loadMultimediaOrPlayListFile(Helpers.createURLfromString(surl));
	    }
	}
	/**
	 * Exports to a Wavefile
	 * @since 01.07.2006
	 */
	private void doExportToWave()
	{
		doStopPlaying();
		
		if (currentContainer==null)
	    {
	    	JOptionPane.showMessageDialog(this, "You need to load a file first!", "Ups!", JOptionPane.ERROR_MESSAGE);
	    }
	    else
	    {
	    	do
		    {
				String fileName = Helpers.createLocalFileStringFromURL(currentContainer.getFileURL(), true);
				fileName = fileName.substring(fileName.lastIndexOf(File.separatorChar)+1);
				String exportToWav = exportPath + File.separatorChar + fileName + ".WAV";
				FileChooserResult selectedFile = Helpers.selectFileNameFor(this, exportToWav, "Export to wave", fileFilterExport, 1, false);
				if (selectedFile!=null)
				{
					File f = selectedFile.getSelectedFile();
				    if (f!=null)
				    {
				    	if (f.exists())
				    	{
				    		int result = JOptionPane.showConfirmDialog(this, "File already exists! Overwrite?", "Overwrite confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				    		if (result==JOptionPane.CANCEL_OPTION) return;
				    		if (result==JOptionPane.NO_OPTION) continue; // Reselect
				    		boolean ok = f.delete();
				    		if (!ok)
				    		{
		        		    	JOptionPane.showMessageDialog(MainForm.this, "Overwrite failed. Is file write protected or in use?", "Failed", JOptionPane.ERROR_MESSAGE);
		        		    	return;
				    		}
				    	}
				    	// get Export Type from selected filechooser index (find the index ;) )
				    	String modFileName = f.getAbsolutePath();
				    	int i = modFileName.lastIndexOf(File.separatorChar);
				    	exportPath = modFileName.substring(0, i);
			    		int result = JOptionPane.showConfirmDialog(this, "Continue playback while exporting?", "Playback?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			    		Mixer mixer = createNewMixer();
				    	mixer.setPlayDuringExport(result==JOptionPane.YES_OPTION);
				    	mixer.setExportFile(f);
				    	playerThread = new PlayThread(mixer, this);
				    	playerThread.start();
			    		
				    	inExportMode = true; // Signal, that we are exporting right now...
				    }
				}
		    	return;
		    }
		    while (true);
	    }
	}
	private SimpleTextViewerDialog getShowVersion_Text()
	{
		if (simpleTextViewerDialog==null)
		{
			simpleTextViewerDialog = new SimpleTextViewerDialog(this, true);
		}
		simpleTextViewerDialog.setLocation(Helpers.getFrameCenteredLocation(simpleTextViewerDialog, this));
		return simpleTextViewerDialog;
	}
	private void doCheckUpdate()
	{
		// This is necessary - to make the progress indicator work
		new Thread( new Runnable()
		{
			public void run()
			{
				String serverVersion = Helpers.getCurrentServerVersion();
				int compareResult = Helpers.compareVersions(Helpers.VERSION, serverVersion);
				if (compareResult<0)
				{
					File f = new File(".");
		    		String programmDestination = f.getAbsolutePath();
		    		// Show Version History
					int resultHistory = JOptionPane.showConfirmDialog(MainForm.this, "There is a new version available!\n\nYour version: "+Helpers.VERSION+" - online verison: " + serverVersion + "\n\nWatch version history?\n\n", "New Version", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resultHistory == JOptionPane.YES_OPTION)
					{
						SimpleTextViewerDialog dialog = getShowVersion_Text();
						dialog.setDisplayTextFromURL(Helpers.VERSION_URL);
						dialog.setVisible(true);
					}
		    		// Ask for download
					int result = JOptionPane.showConfirmDialog(MainForm.this, "Your version: "+Helpers.VERSION+" - online verison: " + serverVersion + "\n\nShould I start the download?\n\n", "New Version", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    		if (result == JOptionPane.YES_OPTION)
		    		{
		    			JFileChooser chooser = new JFileChooser(); 
		    		    chooser.setDialogTitle("Select download destination");
		    		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    		    chooser.setAcceptAllFileFilterUsed(false);
		    		    chooser.setApproveButtonText("Save here");
		    		    do
		    		    {
		    		    	final int chooser_result = chooser.showOpenDialog(MainForm.this);
		    		    	if (chooser_result == JFileChooser.CANCEL_OPTION) return;
			    		    if (chooser_result == JFileChooser.APPROVE_OPTION)
			    		    { 
			        		    File destinationDir = chooser.getSelectedFile();
			        			File destination = new File(destinationDir.getAbsolutePath() + File.separatorChar + "javamod.jar");
						    	if (destination.exists())
						    	{
						    		int owresult = JOptionPane.showConfirmDialog(MainForm.this, "File already exists! Overwrite?", "Overwrite confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						    		if (owresult==JOptionPane.CANCEL_OPTION) return;
						    		if (owresult==JOptionPane.NO_OPTION) continue; // Reselect
						    		boolean ok = destination.delete();
						    		if (!ok && destination.exists())
						    		{
				        		    	JOptionPane.showMessageDialog(MainForm.this, "Overwrite failed. Is file write protected or in use?", "Failed", JOptionPane.ERROR_MESSAGE);
				        		    	return;
						    		}
						    	}
			        			
						    	getDownloadDialog().setLocation(Helpers.getFrameCenteredLocation(getDownloadDialog(), MainForm.this));
						    	getDownloadDialog().setCurrentFileName(Helpers.JAVAMOD_URL);
						    	getDownloadDialog().setVisible(true);
						    	int copied = Helpers.downloadJavaMod(destination, getDownloadDialog());
			        		    getDownloadDialog().setVisible(false);
			        		    if (copied==-1)
			        		    	JOptionPane.showMessageDialog(MainForm.this, "Download failed!\n"+destination, "Failed", JOptionPane.ERROR_MESSAGE);
			        		    else
			        		    	JOptionPane.showMessageDialog(MainForm.this, "Saved "+copied+" bytes successfully to\n"+destination+"\n\nNow exit JavaMod, move the downloaded file to\n" + programmDestination + "\nand restart javamod.\n\n", "Success", JOptionPane.INFORMATION_MESSAGE);
			        		    return;
			    			}
		    		    }
		    		    while (true);
		    		}
				}
				else
				if (compareResult>0)
				{
					JOptionPane.showMessageDialog(MainForm.this, "Your version of JavaMod is newer!", "Newer version", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(MainForm.this, "Your version of JavaMod is up-to-date.", "Up-To-Date", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}).start();
	}
	/**
	 * Display About-Dialog
	 * @since 22.06.2006
	 */
	private void doShowAbout()
	{
		getJavaModAbout().setVisible(true);
	}
	/**
	 * start playback of a audio file
	 * @since 01.07.2006
	 */
	public void doStartPlaying()
	{
		doStartPlaying(false, 0);
	}
	/**
	 * @param initialSeek
	 * @since 13.02.2012
	 */
	public void doStartPlaying(boolean reuseMixer, long initialSeek)
	{
		if (currentContainer!=null)
		{
			if (playerThread!=null && !reuseMixer)
			{
				playerThread.stopMod();
				playerThread = null;
				removeMixer();
			}
			if (inExportMode)
			{
				inExportMode = false;
				doExportToWave();
			}
			if (playerThread == null)
			{
				Mixer mixer = createNewMixer();
				if (mixer!=null)
				{
					if (initialSeek>0) mixer.setMillisecondPosition(initialSeek);
					playerThread = new PlayThread(mixer, this);
					playerThread.start();
				}
			}
			else
			{
				playerThread.getCurrentMixer().setMillisecondPosition(initialSeek);
			}
		}
	}
	/**
	 * stop playback of a mod
	 * @since 01.07.2006
	 */
	private void doStopPlaying()
	{
		if (playerThread!=null)
		{
			playerThread.stopMod();
			getSoundOutputStream().closeAllDevices();
			playerThread = null;
			removeMixer();
		}
	}
	/**
	 * pause the playing of a mod
	 * @since 01.07.2006
	 */
	private void doPausePlaying()
	{
		if (playerThread!=null)
		{
			playerThread.pausePlay();
		}
	}
	private boolean doNextPlayListEntry()
	{
		boolean ok = false;
		while (currentPlayList!=null && currentPlayList.hasNext() && !ok)
		{
			currentPlayList.next();
			ok = loadMultimediaFile(currentPlayList.getCurrentEntry());
		}
		return ok;
	}
	private boolean doPrevPlayListEntry()
	{
		boolean ok = false;
		while (currentPlayList!=null && currentPlayList.hasPrevious() && !ok)
		{
			currentPlayList.previous();
			ok = loadMultimediaFile(currentPlayList.getCurrentEntry());
		}
		return ok;
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.gui.playlist.PlaylistGUIChangeListener#userSelectedPlaylistEntry()
	 * @since 13.02.2012
	 */
	public void userSelectedPlaylistEntry()
	{
		boolean ok = false;
		while (currentPlayList!=null && !ok)
		{
			final PlayListEntry entry = currentPlayList.getCurrentEntry();
			ok = loadMultimediaFile(entry);
			if (!ok) currentPlayList.next();
			else
			if (playerThread==null) doStartPlaying(true, entry.getTimeIndex());

		}
	}
	private void doSetVoumeValue()
	{
		if (playerThread!=null)
		{
			Mixer currentMixer = playerThread.getCurrentMixer();
			currentMixer.setVolume(currentVolume);
		}
	}
	private void doSetBalanceValue()
	{
		if (playerThread!=null)
		{
			Mixer currentMixer = playerThread.getCurrentMixer();
			currentMixer.setBalance(currentBalance);
		}
	}
	private SoundOutputStream getSoundOutputStream()
	{
		if (soundOutputStream==null)
		{
			soundOutputStream = new GaplessSoundOutputStreamImpl();
		}
		return soundOutputStream;
	}
	/**
	 * Creates a new Mixer for playback
	 * @since 01.07.2006
	 * @return
	 */
	private Mixer createNewMixer()
	{
		Mixer mixer = getCurrentContainer().createNewMixer();
		if (mixer!=null)
		{
			mixer.setAudioProcessor(audioProcessor);
			mixer.setVolume(currentVolume);
			mixer.setBalance(currentBalance);
			mixer.setSoundOutputStream(getSoundOutputStream());
			getSeekBarPanel().setCurrentMixer(mixer);
		}
		return mixer;
	}
	private void removeMixer()
	{
		getSeekBarPanel().setCurrentMixer(null);
	}
	/**
	 * @since 14.09.2008
	 * @param mediaPLSFileURL
	 */
	private boolean loadMultimediaOrPlayListFile(URL mediaPLSFileURL)
	{
		Log.info("");
		addFileToLastLoaded(mediaPLSFileURL);
		currentPlayList = null;
    	try
    	{
   			currentPlayList = PlayList.createFromFile(mediaPLSFileURL, false, false);
   			if (currentPlayList!=null)
   			{	
   				getPlaylistGUI().setNewPlaylist(currentPlayList);
   				return doNextPlayListEntry();
   			}
    	}
    	catch (Throwable ex)
    	{
			Log.error("[MainForm::loadMultimediaOrPlayListFile]", ex);
			currentPlayList = null;
    	}
    	return false;
	}
	/**
	 * load a mod file and display it
	 * @since 01.07.2006
	 * @param modFileName
	 * @return boolean if loading succeeded
	 */
	private boolean loadMultimediaFile(PlayListEntry playListEntry)
	{
		final URL mediaFileURL = playListEntry.getFile();
		final boolean reuseMixer = (currentContainer!=null &&
									Helpers.isEqualURL(currentContainer.getFileURL(), mediaFileURL) &&
									playerThread!=null && playerThread.isRunning());
		if (!reuseMixer)
		{
	    	try
	    	{
	    		if (mediaFileURL!=null)
	    		{
	    			MultimediaContainer newContainer = MultimediaContainerManager.getMultimediaContainer(mediaFileURL);
	    			if (newContainer!=null)
	    			{
	    				currentContainer = newContainer;
	        			getLEDScrollPanel().setScrollTextTo(currentContainer.getSongName() + Helpers.SCROLLY_BLANKS);
	        			getTrayIcon().setToolTip(currentContainer.getSongName());
	    			}
	    		}
	    	}
	    	catch (Throwable ex)
	    	{
				Log.error("[MainForm::loadMultimediaFile] Loading of " + mediaFileURL + " failed!", ex);
				return false;
	    	}
			changeInfoPane();
			changeConfigPane();
			changeExportMenu();
		}
		setPlayListIcons();
		// if we are currently playing, start the current piece:
		if (playerThread!=null) doStartPlaying(reuseMixer, playListEntry.getTimeIndex());
		return true;
	}
	/**
	 * @since 14.09.2008
	 * @param url
	 */
	private void addFileToLastLoaded(URL url)
	{
		if (lastLoaded.contains(url)) lastLoaded.remove(url);
		lastLoaded.add(0, url);
		createRecentFileMenuItems();
	}
	/**
	 * @since 14.09.2008
	 * @return
	 */
	private MultimediaContainer getCurrentContainer()
	{
		if (currentContainer == null)
		{
			try
			{
				currentContainer = MultimediaContainerManager.getMultimediaContainerForType("mod");
			}
			catch (Exception ex)
			{
				Log.error("getCurrentContainer()", ex);
			}
		}
		return currentContainer;
	}
	/**
	 * Shows the given Message
	 * @since 22.06.2006
	 * @param msg
	 */
	private synchronized void showMessage(final String msg)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				getMessages().setText(msg);
			}
		});
	}
	/**
	 * Shows the errormessage
	 * 
	 * @since 22.06.2006
	 * @param error
	 */
//	private void showMessage(Throwable ex)
//	{
//		showMessage(ex.toString());
//		ex.printStackTrace(System.err);
//	}
	public void mixerEventOccured(int rowIndex, PatternRow row) {};
	public void patternEventOccured(int pattern, int position) {};

}
