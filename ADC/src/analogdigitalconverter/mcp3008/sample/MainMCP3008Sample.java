package analogdigitalconverter.mcp3008.sample;

import analogdigitalconverter.mcp3008.MCP3008Reader;
import analogdigitalconverter.mcp3008.MCP3008Reader.MCP3008_input_channels;

public class MainMCP3008Sample
{
  private final static boolean DEBUG = false;
  private static boolean go = true;
  private static int ADC_CHANNEL =
    MCP3008Reader.MCP3008_input_channels.CH0.ch(); // Between 0 and 7, 8 channels on the MCP3008
  
  public static void main(String[] args)
  {
    MCP3008Reader.initMCP3008();
    
    Runtime.getRuntime().addShutdownHook(new Thread()
                                         {
                                           public void run()
                                           {
                                             System.out.println("Shutting down.");
                                             go = false;
                                             synchronized (Thread.currentThread())
                                             {
                                               Thread.currentThread().notify();
                                             }
                                           }
                                         });
    int lastRead  = 0;
    int tolerance = 5;
    while (go)
    {
      //System.exit(0);
      boolean trimPotChanged = false;
      int adc = MCP3008Reader.readMCP3008(ADC_CHANNEL);
      System.out.println("adc:" + adc);
      int postAdjust = Math.abs(adc - lastRead);
      if (postAdjust > tolerance)
      {
        trimPotChanged = true;
        int volume = (int)(adc / 10.23); // [0, 1023] ~ [0x0000, 0x03FF] ~ [0&0, 0&1111111111]
        if (DEBUG)
          System.out.println("readAdc:" + Integer.toString(adc) + 
                                          " (0x" + lpad(Integer.toString(adc, 16).toUpperCase(), "0", 2) + 
                                          ", 0&" + lpad(Integer.toString(adc, 2), "0", 8) + ")");        
        System.out.println("Volume:" + volume + "% (" + adc + ")");
        lastRead = adc;
      }
      try 
      { 
        synchronized (Thread.currentThread()) 
        {
          Thread.currentThread().wait(100L); 
        }
      } catch (InterruptedException ie) { ie.printStackTrace(); }
    }
    System.out.println("Bye, freeing resources.");
    MCP3008Reader.shutdownMCP3008();
  }   
  
  private static String lpad(String str, String with, int len)
  {
    String s = str;
    while (s.length() < len)
      s = with + s;
    return s;
  }
}
