/*
 * @(#) APEMixer.java
 *
 * Created on 22.12.2010 by Daniel Becker
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
package de.quippy.javamod.multimedia.ape;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;

import de.quippy.javamod.mixer.BasicMixer;
import de.quippy.javamod.system.Log;
import de.quippy.jmac.decoder.IAPEDecompress;
import de.quippy.jmac.tools.File;

/**
 * @author Daniel Becker
 * @since 22.12.2010
 */
public class APEMixer extends BasicMixer
{
	private URL apeFileURL;
	private File apeFile;
	private IAPEDecompress spAPEDecompress;

    private int blocksPerDecode;
	private int blockAlign;
	private int channels;
	private int bitsPerSample;
	private int sampleRate;
	
	private byte[] output;

	/**
	 * Constructor for APEMixer
	 */
	public APEMixer(URL apeFileURL)
	{
		super();
		this.apeFileURL = apeFileURL;
		initialize();
	}
	private void initialize()
	{
		try
		{
			apeFile = File.createFile(apeFileURL, "r");
			spAPEDecompress = IAPEDecompress.CreateIAPEDecompress(apeFile);
			blockAlign = spAPEDecompress.getApeInfoBlockAlign();
			channels = spAPEDecompress.getApeInfoChannels();
			bitsPerSample = spAPEDecompress.getApeInfoBitsPerSample();
			sampleRate = spAPEDecompress.getApeInfoSampleRate();
			blocksPerDecode = 250 * sampleRate / 1000;
            // allocate space for decompression
			
			int bufferSize = blockAlign * blocksPerDecode;
            output = new byte[bufferSize];

            AudioFormat audioFormat = new AudioFormat(sampleRate, bitsPerSample, channels, true, false);  
    		setAudioFormat(audioFormat);
    		openAudioDevice();
		}
		catch (Exception ex)
		{
			if (apeFile!=null) try { apeFile.close(); apeFile = null; } catch (IOException e) { Log.error("IGNORED", e); }
			Log.error("[APEMixer]", ex);
		}
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getChannelCount()
	 */
	@Override
	public int getChannelCount()
	{
		if (spAPEDecompress==null)
			return 0;
		else
			return spAPEDecompress.getApeInfoChannels();
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getCurrentKBperSecond()
	 */
	@Override
	public int getCurrentKBperSecond()
	{
		if (spAPEDecompress==null)
			return 0;
		else
		{
			try
			{
				return spAPEDecompress.getApeInfoDecompressCurrentBitRate();
			}
			catch (IOException ex)
			{
				return spAPEDecompress.getApeInfoAverageBitrate();
			}
		}
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getCurrentSampleFrequency()
	 */
	@Override
	public int getCurrentSampleFrequency()
	{
		if (spAPEDecompress==null)
			return 0;
		else
			return spAPEDecompress.getApeInfoSampleRate() / 1000;
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getLengthInMilliseconds()
	 */
	@Override
	public long getLengthInMilliseconds()
	{
		if (spAPEDecompress==null)
			return 0;
		else
			return spAPEDecompress.getApeInfoDecompressLengthMS();
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getMillisecondPosition()
	 */
	@Override
	public long getMillisecondPosition()
	{
		if (spAPEDecompress==null)
			return 0;
		else
			return spAPEDecompress.getApeInfoDecompressCurrentMS();
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#isSeekSupported()
	 */
	@Override
	public boolean isSeekSupported()
	{
		return true;
	}
	/**
	 * @param milliseconds
	 * @see de.quippy.javamod.mixer.BasicMixer#seek(long)
	 * @since 13.02.2012
	 */
	@Override
	protected void seek(long milliseconds)
	{
		try
		{
			spAPEDecompress.Seek((int)(milliseconds * (long)sampleRate / 1000L));
		}
		catch (Exception ex)
		{
			Log.error("[APEMixer]", ex);
		}
	}
	/**
	 * @since 22.12.2010
	 */
	private void cleanUp()
	{
		if (spAPEDecompress!=null) spAPEDecompress = null;
		if (apeFile!=null) try { apeFile.close(); apeFile = null; } catch (IOException e) { Log.error("IGNORED", e); }
	}
	/**
	 * 
	 * @see de.quippy.javamod.mixer.Mixer#startPlayback()
	 */
	@Override
	public void startPlayback()
	{
		initialize();
		setIsPlaying();
		
		if (getSeekPosition()>0) seek(getSeekPosition());

		try
		{
			int nBlocksDecoded = 0;
			
			do
			{
				nBlocksDecoded = spAPEDecompress.GetData(output, blocksPerDecode);
				if (nBlocksDecoded>0 && isInitialized())
				{
					int byteCount = nBlocksDecoded * blockAlign;
					writeSampleDataToLine(output, 0, byteCount);
					
					if (isStopping())
					{
						setIsStopped();
						break;
					}
					if (isPausing())
					{
						setIsPaused();
						while (isPaused())
						{
							try { Thread.sleep(1); } catch (InterruptedException ex) { /* noop */ }
						}
					}
					if (isInSeeking())
					{
						setIsSeeking();
						while (isInSeeking())
						{
							try { Thread.sleep(1); } catch (InterruptedException ex) { /*noop*/ }
						}
					}
				}
			}
			while (nBlocksDecoded>0);
			if (nBlocksDecoded<=0) setHasFinished(); // Signals piece was fully played
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			setIsStopped();
			closeAudioDevice();
			cleanUp();
		}
	}
}
