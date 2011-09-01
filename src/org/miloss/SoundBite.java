package org.miloss;

public class SoundBite {

  public final int audioFormat;
  public final byte[] data;
  
  public SoundBite(int audioFormat, byte[] data) {
	  this.audioFormat = audioFormat;
	  this.data = data;
  }
}
