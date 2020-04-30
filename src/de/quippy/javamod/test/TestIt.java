/*
 * @(#) TestIt.java
 * 
 * Created on 21.04.2006 by Daniel Becker (quippy@quippy.de)
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
package de.quippy.javamod.test;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import de.quippy.javamod.main.JavaModMainBase;
import de.quippy.javamod.mixer.Mixer;
import de.quippy.javamod.multimedia.MultimediaContainer;
import de.quippy.javamod.multimedia.MultimediaContainerManager;
import de.quippy.javamod.multimedia.mod.ModContainer;
import de.quippy.javamod.system.Log;

/**
 * @author Daniel Becker
 * @since 21.04.2006
 */
public class TestIt extends JavaModMainBase
{
	private static class PlayThread extends Thread
	{
		private Mixer currentMixer;
		
		public PlayThread(URL modUrl)
		{
			super();

			try
			{
				Properties props = new Properties();
				props.setProperty(ModContainer.PROPERTY_PLAYER_ISP, "3");
				props.setProperty(ModContainer.PROPERTY_PLAYER_STEREO, "2");
				props.setProperty(ModContainer.PROPERTY_PLAYER_WIDESTEREOMIX, "FALSE");
				props.setProperty(ModContainer.PROPERTY_PLAYER_NOISEREDUCTION, "FALSE");
				props.setProperty(ModContainer.PROPERTY_PLAYER_NOLOOPS, "1");
				props.setProperty(ModContainer.PROPERTY_PLAYER_MEGABASS, "TRUE");
				props.setProperty(ModContainer.PROPERTY_PLAYER_BITSPERSAMPLE, "16");			
				props.setProperty(ModContainer.PROPERTY_PLAYER_FREQUENCY, "48000");
				props.setProperty(ModContainer.PROPERTY_PLAYER_MSBUFFERSIZE, "250");
				MultimediaContainerManager.configureContainer(props);
				MultimediaContainer multimediaContainer = MultimediaContainerManager.getMultimediaContainer(modUrl);
				
				currentMixer = multimediaContainer.createNewMixer();
			}
			catch (Throwable ex)
			{
				ex.printStackTrace(System.err);
			}
		}
//		public Mixer getCurrentMixer()
//		{
//			return currentMixer;
//		}
		
		@Override
		public void run()
		{
			currentMixer.startPlayback();
		}
	}
	private static PlayThread playerThread;
	
	/**
	 * Constructor for TestIt
	 */
	public TestIt()
	{
		super(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\ENJOYTHE.MOD").toURI().toURL(); // VolumeSlideTest
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Karsten Obarski\\SARCOPHA.MOD").toURI().toURL(); // Arpeggio
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\PLANETS.MOD").toURI().toURL(); // Porta Down / Porta Up
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\SPDEBRIS.MOD").toURI().toURL(); // Porta to Note
			URL modUrl = new File("M:\\Multimedia\\Files MOD\\BEYOND.MOD").toURI().toURL(); // Vibrato, Sample offset, Retrig
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\BOOM.MOD").toURI().toURL(); // NoteDelays?!
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Jogeir Liljedahl\\GSLINGER.MOD").toURI().toURL(); // Finetune
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Games\\Cybernoid\\cybernoid ii.mod").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\COUNTRY.MOD").toURI().toURL(); // Mod is too short for 4 Bytes
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Shining\\NEMESIS.MOD").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Lizardking\\1942.MOD").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\DEFLORAT.MOD").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Chorus-Sid-Razor 1911\\COFEBLU3.MOD").toURI().toURL(); // Pattern Break, Position Jump
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\1989.MOD").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Anarchy\\ACSSHIGH.MOD").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\kermit.mod").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\einstein.mod").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\2010.mod").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\rymix\\synthesis.mod").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Lizardking\\DOSKPOPI.MOD").toURI().toURL(); // Porta To Note without a note
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\THESKYOF.MOD").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\HEAVEN&H.MOD").toURI().toURL(); // Ends with Note Delay

			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Captain Future\\KEN.XM").toURI().toURL(); // NoteDelay
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\THEVOICE.XM").toURI().toURL(); // PanningSlide
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\THEWAIT.XM").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\DOMATRON.XM").toURI().toURL(); // Panning envelope, index 3, instrument 19
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Rure2fly.xm").toURI().toURL(); // Instrument with empty envelope
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Games\\Turrican\\TurricanHS.XM").toURI().toURL(); // Perfomance!!!
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Laxity - Maniac of Noise\\LUCK.XM").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Laxity - Maniac of Noise\\MIXED_F.XM").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\chipboat.xm").toURI().toURL(); // Arpeggio
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Lagoona_-_Land_of_imagination.xm").toURI().toURL(); // Sample Index
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\unforg_p.xm").toURI().toURL(); // Vibrato without noteIndex set
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\TheGoaReligion.xm").toURI().toURL(); // PingPongLoops
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\evening_sun.xm").toURI().toURL(); // Empty pattern
			//URL modUrl = new File("M:\\Multimedia\Files MOD\\ambrozia-album\\MYSTYLES.XM").toURI().toURL(); // Empty pattern
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Games\\Turrican\\TurricanHS.XM").toURI().toURL(); // Empty pattern
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\ambrozia-album\\UNTOUCH.XM").toURI().toURL(); // Empty pattern
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\__testmods__\\Fjant2.xm").toURI().toURL(); // Envelopes
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\__testmods__\\Module2.xm").toURI().toURL();
			
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\airbor.stm").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\at.stm").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\niagra.stm").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\larry.stm").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Karsten Obarski\\amegas.stm").toURI().toURL();
			
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\ATHOUGHT.S3M").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Aftersun.s3m").toURI().toURL(); // s3m VolumeSlide Effekt
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\BEYON_NI.S3M").toURI().toURL(); // Portas
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\BURNINGL.S3M").toURI().toURL(); // NoteDelay
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\DARKTIME.S3M").toURI().toURL(); // Volume Slide fine
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\INFINITY.S3M").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\ARYX.S3M").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\FALLENAN.S3M").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Games\\Unreal\\Unreal2ndpm.s3m").toURI().toURL();
			
			
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\JEFF93.IT").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\AOMMCMIX.IT").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\pandora.it").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\Storm & JJMax - Heaven.it").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\XeeGee Trance\\XeeGee-@Starlight.it").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\XeeGee Trance\\xeegee-atlantislostlongedit.it").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\XeeGee Trance\\xeegee-atlantislostpartii.it").toURI().toURL();
			//URL modUrl = new File("M:\\Multimedia\\Files MOD\\XeeGee Trance\\xeegee-lostinforest.it").toURI().toURL(); // MEMORY!!!
			
			//CommandLine.main(new String[]{"-i3","-s+","-w-","-n+","-m+","-b16","-r96000", "C:\\Download\\JavaMod\\test.m3u"});

			playerThread = new PlayThread(modUrl);
			playerThread.start();
			
//			BasicModMixer modMixer = null;
//			final Mixer mixer = playerThread.getCurrentMixer();
//			if (mixer!=null && mixer instanceof ModMixer)
//				modMixer = ((ModMixer)mixer).getModMixer();
			while (playerThread.isAlive())
			{
//				if (modMixer!=null)
//				{
//					System.out.print(String.format("%08X\r", Integer.valueOf(modMixer.getCurrentPatternPosition())));
//				}
			}
		}
		catch (Exception ex)
		{
			Log.error("MIST", ex);
		}
	}
}
