/*
 * @(#) MP3Mixer.java
 *
 * Created on 17.10.2007 by Daniel Becker
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
package de.quippy.javamod.multimedia.mp3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.sound.sampled.AudioFormat;

import de.quippy.javamod.io.FileOrPackedInputStream;
import de.quippy.javamod.mixer.BasicMixer;
import de.quippy.javamod.multimedia.mp3.streaming.IcyInputStream;
import de.quippy.javamod.multimedia.mp3.streaming.TagParseListener;
import de.quippy.javamod.system.Log;
import de.quippy.mp3.decoder.Bitstream;
import de.quippy.mp3.decoder.BitstreamException;
import de.quippy.mp3.decoder.Decoder;
import de.quippy.mp3.decoder.Header;
import de.quippy.mp3.decoder.SampleBuffer;

/**
 * @author Daniel Becker
 * @since 17.10.2007
 */
public class MP3Mixer extends BasicMixer
{
	private static final String STREAM_HTTP=" HTTP/1.1\r\nuser-agent: JavaMod\r\nIcy-MetaData: 1\r\nConnection: keep-alive\r\n\r\n";

	private byte [] output;
	
	private Socket socket;
	private InputStream inputStream;
	private Bitstream bitStream;
	private Decoder	decoder; 
	
	private URL mp3FileUrl;
	
	private TagParseListener tagParseListener;
	
	private int played_ms;
	
	private Boolean isStreaming;

	/**
	 * Constructor for MP3Mixer
	 */
	public MP3Mixer(URL mp3FileUrl)
	{
		super();
		this.mp3FileUrl = mp3FileUrl;
	}
	/**
	 * @since 27.12.2008
	 * @param tagParseListener
	 */
	public void setTagParserListener(TagParseListener tagParseListener)
	{
		this.tagParseListener = tagParseListener;
	}
	private void initialize()
	{
		try
		{
			if (bitStream!=null) try { bitStream.close(); bitStream = null; } catch (BitstreamException e) { Log.error("IGNORED", e); }
			if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
			if (socket!=null) try { socket.close(); socket = null; } catch (IOException e) { Log.error("IGNORED", e); }
			if (!isStreaming())
			{
				inputStream = new FileOrPackedInputStream(mp3FileUrl);
			}
			else
			{
				int port = mp3FileUrl.getPort();
				if (port<0) port = 80;
				String path = mp3FileUrl.getPath();
				if (path==null || path.length()==0) path = "/";
				
				socket = new Socket(mp3FileUrl.getHost(), port);
				OutputStream os=socket.getOutputStream();
				final String getString = "GET " + path + STREAM_HTTP;
				os.write(getString.getBytes());
				inputStream = new IcyInputStream(new BufferedInputStream(socket.getInputStream()), tagParseListener);
			}
			this.bitStream = new Bitstream(inputStream);
			this.decoder = new Decoder();
			this.played_ms = 0;
			// Setting the AudioFormat is only possible during
			// playback so it is done in startPlayBack
		}
		catch (Exception ex)
		{
			if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
			Log.error("[MP3Mixer]", ex);
		}
	}
	/**
	 * @since 10.04.2010
	 * @return
	 */
	private boolean isStreaming()
	{
		if (isStreaming==null)
		{
			if (mp3FileUrl.getProtocol().equalsIgnoreCase("file")) 
				isStreaming = Boolean.FALSE;
			else
			{
				try
				{
					URLConnection con = mp3FileUrl.openConnection();
					//String contentType = con.getContentType();
					int length = con.getContentLength();
					if (length==-1) 
						isStreaming = Boolean.TRUE;
					else
						isStreaming = Boolean.FALSE;
				}
				catch (Throwable ex)
				{
					Log.error("[MP3Mixer::isStreamaing]", ex);
				}
			}
		}
		return isStreaming.booleanValue();
	}
	/**
	 * 
	 * @see de.quippy.javamod.mixer.Mixer#isSeekSupported()
	 */
	@Override
	public boolean isSeekSupported()
	{
		return !isStreaming();
	}
	/**
	 * 
	 * @see de.quippy.javamod.mixer.Mixer#getMillisecondPosition()
	 */
	@Override
	public long getMillisecondPosition()
	{
		if (!isStreaming())
			return played_ms;
		else
			return 0;
	}
	/**
	 * 
	 * @see de.quippy.javamod.mixer.Mixer#getLengthInMilliseconds()
	 */
	@Override
	public long getLengthInMilliseconds()
	{
		if (!isStreaming())
		{
			try
			{
				initialize();
				Header h = bitStream.readFrame();
				if (h!=null)  
					return (long)(h.total_ms(inputStream.available()) + 0.5);
			}
			catch (Throwable ex)
			{
				Log.error("IGNORED", ex);
			}
		}
		return 0;
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
			if (!isStreaming())
			{
				if (played_ms>milliseconds)
				{
					if (bitStream!=null) try { bitStream.close(); bitStream = null; } catch (BitstreamException e) { Log.error("IGNORED", e); }
					if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
					if (socket!=null) try { socket.close(); socket = null; } catch (IOException e) { Log.error("IGNORED", e); }
					
					inputStream = mp3FileUrl.openStream();
					bitStream = new Bitstream(inputStream);
					this.decoder = new Decoder();
					played_ms = 0;
				}
				
				float f_played_ms = (float)played_ms;
				while (f_played_ms < milliseconds)
				{
					Header h = bitStream.readFrame();
					if (h==null) break;
					f_played_ms += h.ms_per_frame(); 
					bitStream.closeFrame();
				}
				played_ms = (int)(f_played_ms + 0.5);
			}
		}
		catch (Throwable ex)
		{
			Log.error("[MP3Mixer]", ex);
		}
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getChannelCount()
	 */
	@Override
	public int getChannelCount()
	{
		if (decoder != null)
		{
			return decoder.getOutputChannels();
		}
		return 0;
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getCurrentKBperSecond()
	 */
	@Override
	public int getCurrentKBperSecond()
	{
		if (bitStream!=null)
		{
			Header h = bitStream.getHeader();
			if (h!=null) return h.bitrate_instant()/1000;
		}
		return 0;
	}
	/**
	 * @return
	 * @see de.quippy.javamod.mixer.Mixer#getCurrentSampleFrequency()
	 */
	@Override
	public int getCurrentSampleFrequency()
	{
		if (decoder!=null)
			return decoder.getOutputFrequency()/1000;
		else
			return 0;
	}
	/**
	 * @since 30.03.2010
	 * @param length
	 * @return
	 */
	private byte[] getOutputBuffer(int length)
	{
		if (output==null || output.length<length)
			output = new byte[length];
		return output;
	}
	/**
	 * 
	 * @see de.quippy.javamod.mixer.Mixer#startPlayback()
	 */
	@Override
	public void startPlayback()
	{
		initialize();
		if (bitStream==null) return; // something went wrong...
		
		setIsPlaying();
		
		if (getSeekPosition()>0) seek(getSeekPosition());

		try
		{
			boolean isFirstFrame = true;
			Header h = null;
			
			do
			{
				h = bitStream.readFrame();
				if (h!=null)
				{
					final SampleBuffer output = (SampleBuffer)decoder.decodeFrame(h, bitStream);
					bitStream.closeFrame();

					if (isFirstFrame)
					{
						// At this point we know our AudioFormat
						setAudioFormat(new AudioFormat(decoder.getOutputFrequency(), 16, decoder.getOutputChannels(), true, false));
						openAudioDevice();
						if (!isInitialized()) return;
					}
					
					final short[] samples = output.getBuffer();
					int origLen = output.getBufferLength();
					played_ms += ((origLen / decoder.getOutputChannels())*1000)/decoder.getOutputFrequency();
					final int len = origLen<<1;
					byte[] b = getOutputBuffer(len);
					
					int idx = 0;
					int pos = 0;
					short s;
					boolean allZero = true;
					while (origLen-- > 0)
					{
						s = samples[pos++];
						if (allZero && s!=0) allZero = false;
						b[idx++] = (byte)(s&0xFF);
						b[idx++] = (byte)((s>>8)&0xFF);
					}
	
					if (!isFirstFrame || !allZero) writeSampleDataToLine(b, 0, len);
				}

				isFirstFrame = false;

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
						try { Thread.sleep(1); } catch (InterruptedException ex) { /*noop*/ }
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
			while (h!=null);
			if (h==null) setHasFinished(); // piece finished
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			setIsStopped();
			closeAudioDevice();
			if (bitStream!=null) try { bitStream.close(); bitStream = null; } catch (BitstreamException e) { Log.error("IGNORED", e); }
			if (inputStream!=null) try { inputStream.close(); inputStream = null; } catch (IOException e) { Log.error("IGNORED", e); }
			if (socket!=null) try { socket.close(); socket = null; } catch (IOException e) { Log.error("IGNORED", e); }
		}
	}
}
