package zenwheelslibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * 
 * @author davide
 */
public class Bluetooth implements DiscoveryListener{
    public static final String MICRO_CAR_ADDRESS = "00066647285B";
    //object used for waiting
    private static Object lock = new Object();  
    //ArrayList containing the devices discovered
    private static ArrayList<RemoteDevice> devices  = new ArrayList<>();
    //connection url
    private static String connectionURL = null;
    
    private static LocalDevice localDevice;
    private static Bluetooth client;
    private static DiscoveryAgent agent;
    private static RemoteDevice remoteDevice;
    private static String uuid = "0000110100001000800000805F9B34FB";
    private static StreamConnection streamConnection;
    private static OutputStream outStream;
    
    
    /**
     * Connessione con MicroCar
     * @return Risultato della connessione (fallita | riuscita)
     */
    public static boolean connectToMicroCar(){
        if(localDevice == null){
            if(!inizializeBluetooth()) return false;
        }
        if(!searchDevices()) return false;
        if (devices.size() <= 0) {
            return false;
        } else {
            for (int i = 0; i < devices.size(); i++) {
                RemoteDevice searchingRemoteDevice = (RemoteDevice) devices.get(i);
                if(searchingRemoteDevice.getBluetoothAddress().equals(MICRO_CAR_ADDRESS)){
                    if(!connectTo(searchingRemoteDevice)) return false;
                    break;
                }
            }
        }
        
        return true;
    }
    
    private static boolean inizializeBluetooth(){
        client = new Bluetooth();
        try {
            localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());
        } catch (BluetoothStateException ex) {
            Logger.getLogger(Bluetooth.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    private static boolean searchDevices() {
        if(agent == null) agent = localDevice.getDiscoveryAgent();
        System.out.println("Starting device inquiry...");//log
        
        try {
            agent.startInquiry(DiscoveryAgent.GIAC, client);
        } catch (BluetoothStateException ex) {
            Logger.getLogger(Bluetooth.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //log
        System.out.println("Device Inquiry Completed. ");
        if (devices.size() <= 0) {
            System.out.println("No Devices Found.");
            return true;
        } else {
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i < devices.size(); i++) {
                RemoteDevice remoteDevice = (RemoteDevice) devices.get(i);
                try {
                    System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(false) + ")");
                } catch (IOException ex) {
                    Logger.getLogger(Bluetooth.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
    private static boolean connectTo(RemoteDevice remoteDevice){
        UUID[] uuidSet = new UUID[1];
        uuidSet[0] = new UUID(uuid, false);
        try {
            agent.searchServices(null, uuidSet, remoteDevice, client);
            Bluetooth.remoteDevice = remoteDevice;
        } catch (BluetoothStateException ex) {
            Logger.getLogger(Bluetooth.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        if (connectionURL == null) {
            return false;
        }
        
        try {
            streamConnection = (StreamConnection) Connector.open(connectionURL);
            outStream = streamConnection.openOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(Bluetooth.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println("Connected");
        return true;
    }
    public static boolean sendCommand(int command){
        byte[] commandCoded;
        commandCoded = ByteBuffer.allocate(4).putInt(command).array();
        try {
            outStream.write(commandCoded);
        } catch (IOException ex) {
            Logger.getLogger(Bluetooth.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    //osservatori e modificatori
    public static RemoteDevice[] getDevices(){
        RemoteDevice[] devices = new RemoteDevice[Bluetooth.devices.size()];
        for(int i=0; i<Bluetooth.devices.size(); i++){
            devices[i] = Bluetooth.devices.get(i);
        }
        return devices;
    }
    public static String getUuid(){
        return uuid;
    }
    public static void setUuid(String newUuid){
        uuid = newUuid;
    }
    
    //METHODS OF DiscoveryListener INTERFACE
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        //add the device to the ArrayList
        if (!devices.contains(btDevice)) {
            devices.add(btDevice);
        }
    }
    //implement this method since services are not being discovered
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (servRecord != null && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }
        synchronized (lock) {
            lock.notify();
        }
    }
    //implement this method since services are not being discovered
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (lock) {
            lock.notify();
        }
    }
    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }

    }
}
