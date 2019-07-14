package broadcaster;

import codes.ICommunicationCode;
import model.PlayerData;
import observers.NetworkPlayerDataObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * Broadcasts changes to clients.<br>
 *
 * @author Ivica Duspara
 * @version 1.0
 */
public class ListBroadcaster implements NetworkPlayerDataObserver {

    /**
     * Singleton instance.
     */
    private static ListBroadcaster LIST_BROADCASTER_SINGLETON = null;

    /**
     * Port on which server is started.
     */
    private static final int PORT = 13370;

    /**
     * Used for comparing IP addresses.
     * See:
     * 		<a href="https://en.wikipedia.org/wiki/Private_network">Private addresses</a>.
     */
    private static final String LOCAL_IP_24BIT_ALIKE = "10.";

    /**
     * Used for comparing IP addresses.
     * See:
     * 		<a href="https://en.wikipedia.org/wiki/Private_network">Private addresses</a>.
     */
    private static final String LOCAL_IP_16BIT_ALIKE = "192.168.";

    /**
     * Used for comparing IP addresses.
     * See:
     * 		<a href="https://en.wikipedia.org/wiki/Private_network">Private addresses</a>.
     */
    private static final String LOCAL_IP_20BIT_ALIKE = "172.";


    /**
     * Returns a String representing an IP address of this machine which begins with {@link #LOCAL_IP_16BIT_ALIKE} or
     * {@link #LOCAL_IP_24BIT_ALIKE} or {@link #LOCAL_IP_20BIT_ALIKE}.
     *
     * Returned IP address is a private IP address on which server will be run.<br>
     *
     * This method will throw an exception if an I/O error occurs or if platform doesn't have any
     * configured network interfaces
     *
     * @return
     * 		   private IP address on which server will run
     *
     * @throws SocketException
     * 		   This method will throw an exception if an I/O error occurs or if platform doesn't have any
     * 		   configured network interfaces
     */
    private String findLocalIPAddress() throws SocketException {
        String hostAddress = "";
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while(en.hasMoreElements()){
            NetworkInterface ni = en.nextElement();
            Enumeration<InetAddress> ee = ni.getInetAddresses();
            while(ee.hasMoreElements()) {
                InetAddress ia = ee.nextElement();
                if(ia.getHostAddress().contains(LOCAL_IP_16BIT_ALIKE)) {
                    hostAddress = ia.getHostAddress();
                }
                else if(ia.getHostAddress().contains(LOCAL_IP_24BIT_ALIKE)) {
                    hostAddress = ia.getHostAddress();
                }
                else if(ia.getHostAddress().contains(LOCAL_IP_20BIT_ALIKE)) {
                    hostAddress = ia.getHostAddress();
                }
            }

        }
        return hostAddress;
    }

    private Map<String, ICommunicationCode> communicationCodes;

    private ListBroadcaster() {
        communicationCodes = new HashMap<>();
        BufferedReader br;
        try {
            br = Files.newBufferedReader(Paths.get("codes.txt"));
            List<String> codes = new ArrayList<>();
            String line ="";
            while((line = br.readLine()) != null) {
                codes.add(line);
            }
            String packagePrefix = "codes.concretecodes.";
            for(String code : codes) {
                Class<ICommunicationCode> iCommunicationCodeClass = (Class<ICommunicationCode>) Class.forName(packagePrefix + code);
                ICommunicationCode iConcrete = iCommunicationCodeClass.getDeclaredConstructor().newInstance();
                communicationCodes.put(iConcrete.getClass().getSimpleName(), iConcrete);
            }
        }catch(IOException ex){
            throw new RuntimeException("Failed to load codes. Place codes file in source path");
        }catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static ListBroadcaster getInstance() {
        if(LIST_BROADCASTER_SINGLETON == null) {
            LIST_BROADCASTER_SINGLETON = new ListBroadcaster();
        }
        return LIST_BROADCASTER_SINGLETON;
    }


    @Override
    public void update(String code, PlayerData playerData) {

    }
}
