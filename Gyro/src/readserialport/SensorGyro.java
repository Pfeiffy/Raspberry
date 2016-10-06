package readserialport;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.*;

import java.net.*;
import java.io.*;

public class SensorGyro {
    static I2CDevice sensor;
    //static I2CBus bus;
    static byte[] accelData, gyroData;
    static long accelCalib[] = new long[3];
    static long gyroCalib[] = new long[3];

    static double gyroX = 0;
    static double gyroY = 0;
    static double gyroZ = 0;

    static double accelX;
    static double accelY;
    static double accelZ;

    static double angleX;
    static double angleY;
    static double angleZ;

    public SensorGyro() {
        System.out.println("Hello, Raspberry Pi!");
        try {
        	I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

            sensor = bus.getDevice(0x68);
//            sensor = bus.getDevice(0x20);

            sensor.write(0x6B, (byte) 0x0);
            sensor.write(0x6C, (byte) 0x0);
            System.out.println("Calibrating...");

            //calibrate();

            Thread sensors = new Thread(){
                    public void run(){
                        try {
                            readSensors();
                        } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    }
            };
            sensors.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void readSensors() throws IOException {
        long time = System.currentTimeMillis();
        long sendTime = System.currentTimeMillis();

        while (true) {
            accelData = new byte[6];
            gyroData = new byte[6];
            int r = sensor.read(0x3B, accelData, 0, 6);
            accelX = (((accelData[0] << 8)+accelData[1]-accelCalib[0])/16384.0)*9.8;
            accelY = (((accelData[2] << 8)+accelData[3]-accelCalib[1])/16384.0)*9.8;
            accelZ = ((((accelData[4] << 8)+accelData[5]-accelCalib[2])/16384.0)*9.8)+9.8;
            accelZ = 9.8-Math.abs(accelZ-9.8);

            double hypotX = Math.sqrt(Math.pow(accelX, 2)+Math.pow(accelZ, 2));
            double hypotY = Math.sqrt(Math.pow(accelY, 2)+Math.pow(accelZ, 2));


            double accelAngleX = Math.toDegrees(Math.asin(accelY/hypotY));
            double accelAngleY = Math.toDegrees(Math.asin(accelX/hypotX));
//            System.out.println("accelAngleX: " + accelAngleX + " /// " +"accelAngleY: " + accelAngleY );
            
//            System.out.println((int)gyroX+", "+(int)gyroY);
//
//            System.out.println("accelX: " + accelX+" accelY: " + accelY+" accelZ: " + accelZ);

            r = sensor.read(0x43, gyroData, 0, 6);
            if(System.currentTimeMillis()-time >= 5)
            {
                gyroX = (((gyroData[0] << 8)+gyroData[1]-gyroCalib[0])/131.0);
                gyroY = (((gyroData[2] << 8)+gyroData[3]-gyroCalib[1])/131.0);
                gyroZ = (((gyroData[4] << 8)+gyroData[5]-gyroCalib[2])/131.0);

                angleX += gyroX*(System.currentTimeMillis()-time)/1000;
                angleY += gyroY*(System.currentTimeMillis()-time)/1000;
                angleZ += gyroZ;

                angleX = 0.95*angleX + 0.05*accelAngleX;
                angleY = 0.95*angleY + 0.05*accelAngleY;

                time = System.currentTimeMillis();
            }
            System.out.println((int)angleX+", "+(int)angleY);
            System.out.println((int)accelAngleX+", "+(int)accelAngleY);
        }
    }

    public static void calibrate() throws IOException {
        int i;
        for(i = 0; i < 300; i++)
        {
            accelData = new byte[6];
            gyroData = new byte[6];
            int r = sensor.read(0x3B, accelData, 0, 6);
            accelCalib[0] += (accelData[0] << 8)+accelData[1];
            accelCalib[1] += (accelData[2] << 8)+accelData[3];
            accelCalib[2] += (accelData[4] << 8)+accelData[5];

            r = sensor.read(0x43, gyroData, 0, 6);
            gyroCalib[0] += (gyroData[0] << 8)+gyroData[1];
            gyroCalib[1] += (gyroData[2] << 8)+gyroData[3];
            gyroCalib[2] += (gyroData[4] << 8)+gyroData[5];
            try {
                Thread.sleep(1);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        gyroCalib[0] /= i;
        gyroCalib[1] /= i;
        gyroCalib[2] /= i;

        accelCalib[0] /= i;
        accelCalib[1] /= i;
        accelCalib[2] /= i;
        System.out.println(gyroCalib[0]+", "+gyroCalib[1]+", "+gyroCalib[2]);
    }

    public double readAngle(int axis) //Winkel
    {
        switch (axis)
        {
            case 0:
                return angleX;
            case 1:
                return angleY;
            case 2:
                return angleZ;
        }

        return 0;
    }

    public double readGyro(int axis) //Umdrehung
    {
        switch (axis)
        {
            case 0:
                return gyroX;
            case 1:
                return gyroY;
            case 2:
                return gyroZ;
        }

        return 0;
    }

    public double readAccel(int axis) //Beschleunigung
    {
        switch (axis)
        {
            case 0:
                return accelX;
            case 1:
                return accelY;
            case 2:
                return accelZ;
        }

        return 0;
    }
}