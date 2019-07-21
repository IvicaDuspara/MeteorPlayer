package broadcaster;

import codes.IClientCode;
import codes.IServerCode;
import codes.concreteclientcodes.ClientQueueCode;
import codes.concreteservercodes.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Codes;
import model.PlayerData;
import observers.NetworkPlayerDataObserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Singleton class used as a broadcaster.<br>
 * This class handles communication between server ({@code MeteorPlayer}) and clients (phones connected to server) by
 * doing 2 jobs:
 * <ol>
 *     <li>It listens to client's request for a song and enqueues it in a queue and sends feedback information</li>
 *     <li>When a change occurs on a server(loading songs, change in a queue) change is broadcast to listening clients </li>
 * </ol>
 *
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
     * {@code Map} for holding {@link IServerCode codes} which
     * perform an operation depending of contents of {@link model.Codes codes} which
     * describe action in {@link model.PlayerData PlayerData model}
     */
    private Map<String, IServerCode> communicationCodes;

    /**
     * {@code Map} for holding {@link IClientCode codes} which
     * describe client's request which changes {@link model.PlayerData PlayerData model}
     */
    private Map<String, IClientCode> clientCodeMap;


    /**
     * Indicates whether this broadcaster is running
     */
    private boolean isRunning;


    /**
     * Socket address of {@code ListBroadcaster}.
     */
    private SocketAddress serverAddress;


    /**
     *  ServerSocket of {@code ListBroadcaster}
     */
    private ServerSocket server;


    /**
     * Executor pool used for serving clients
     */
    private ExecutorService pool;


    /**
     * Subject of this {@link NetworkPlayerDataObserver}.
     */
    private PlayerData subject;


    /**
     * {@code Map} for easy access to clients' writers
     */
    private Map<String, BufferedWriter> clientWriters;


    /**
     * Returns singleton instance of {@code ListBroadcaster}
     *
     * @return
     *        singleton instance of {@code ListBroadcaster}
     */
    public static ListBroadcaster getInstance() {
        if(LIST_BROADCASTER_SINGLETON == null) {
            LIST_BROADCASTER_SINGLETON = new ListBroadcaster();
        }
        return LIST_BROADCASTER_SINGLETON;
    }


    /**
     * Returns address at which singleton {@code ListBroadcaster} is bound.
     *
     * @return
     *         address at which singleton {@code ListBroadcaster} is bound
     */
    public String getServerAddress() {
        return serverAddress.toString();
    }


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


    /**
     * Loads codes found in {@link codes.concreteservercodes concreteservercodes} into {@link codes.concreteclientcodes concreteclientcodes}.<br>
     */
    private void loadCodes() {
        communicationCodes = new HashMap<>();
        clientCodeMap = new HashMap<>();
        IServerCode i1 = new ServerSongListCode();
        IServerCode i2 = new ServerSongPartialListCode();
        IServerCode i3 = new ServerQueueListCode();
        IServerCode i4 = new ServerNowPlayingCode();
        IServerCode i5 = new ServerMoveUpCode();
        IClientCode i6 = new ClientQueueCode();
        communicationCodes.put(i1.getClass().getSimpleName(), i1);
        communicationCodes.put(i2.getClass().getSimpleName(), i2);
        communicationCodes.put(i3.getClass().getSimpleName(), i3);
        communicationCodes.put(i4.getClass().getSimpleName(), i4);
        communicationCodes.put(i5.getClass().getSimpleName(), i5);
        clientCodeMap.put(i6.getClass().getSimpleName(), i6);
    }


    /**
     * Constructs a new {@code ListBroadcaster}
     */
    private ListBroadcaster() {
        isRunning = false;
        try{
            loadCodes();
        }catch(RuntimeException exception) {
            throw new RuntimeException("Exception occurred in construction: " + exception.getMessage());
        }
        try{
            serverAddress = new InetSocketAddress(InetAddress.getByName(findLocalIPAddress()),PORT);
            server = new ServerSocket();
            server.bind(serverAddress);
            pool = Executors.newFixedThreadPool(4);
            clientWriters = new ConcurrentHashMap<>();
        }catch(SocketException | UnknownHostException exception) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error in finding IP address: ", ButtonType.CLOSE);
                alert.setTitle("Address error");
                alert.showAndWait();
            });
            throw new RuntimeException(exception.getMessage());
        }catch(IOException exception) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error in creating a connection: ", ButtonType.CLOSE);
                alert.setTitle("Socket error");
                alert.showAndWait();
            });
            throw new RuntimeException(exception.getMessage());
        }
    }


    /**
     * Sets subject of this {@code ListBroadcaster}
     *
     * @param subject
     *        from which information is pulled when this {@code ListBroadcaster} is notified
     */
    public void setSubject(PlayerData subject) {
        this.subject = subject;
    }


    /**
     * Starts broadcast.<br>
     * Server will listen to connections on {@link #serverAddress}. When a connection is made {@link ClientWorker} will
     * serve client. For more information see {@link ClientWorker}.<br>
     * Server can not be started if it is already started.<br>
     * If a server fails to start an exception will be thrown.
     *
     */
    public void startBroadcast() {
        if(!isRunning) {
            isRunning = true;
            try{
                while(isRunning) {
                    Socket client = server.accept();
                    ClientWorker worker = new ClientWorker(client);
                    pool.submit(worker);
                }
            }catch(IOException exception) {
                isRunning = false;
            }
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Server is already running at: " + serverAddress.toString(), ButtonType.CLOSE);
                alert.setTitle("Broadcast error");
                alert.showAndWait();
            });
        }
    }


    /**
     * Stops broadcast.<br>
     * Server will stop broadcasting and receiving messages from clients.
     *
     */
    public void shutdown() {
        isRunning = false;
        pool.shutdown();
        try{
            server.close();
        }catch(IOException ex) {
            System.out.println("Could not close list broadcaster.");
        }
    }


    @Override
    public void update(String code) {
        try {
            for(BufferedWriter writer : clientWriters.values()) {
                communicationCodes.get(code).execute(subject, writer);
            }
        }catch (IOException exception) {
            //TODO REMOVE THIS OR HANDLE IT PROPERLY
            System.out.println("An error occured. YACK." + exception.getMessage());
        }
    }


    /**
     * Models a job which serves a client. When a client connects it will
     * send it's unique ID which will be saved. Afterwards {@code ClientWorker}
     * sends information on loaded songs, queued songs and currently playing song.<br>
     *
     * After this initial exchange is made, {@code ClientWorker} will be listening to any
     * messages from client or it will send appropriate messages when a change occurs to client.<br>
     * Changes are described in {@link codes.concreteservercodes concreteservercodes}
     *
     *
     * @author Ivica Duspara
     * @version 1.0
     */
    private class ClientWorker implements Runnable {

        /**
         * Client's socket reader.
         */
        private BufferedReader bufferedReader;


        /**
         * Client's socket writer.
         */
        private BufferedWriter bufferedWriter;


        /**
         * Socket representing a client
         */
        private Socket client;


        /**
         * UUID which identifies a device
         */
        private String UUID;


        /**
         * When a client connects to server it will send a code and its UUID.<br>
         * This method saves UUID and appropriate writer to {@link #clientWriters}
         *
         * @throws IOException
         *         if an error occurs while reading from reader
         */
        private void saveWriter() throws IOException{
            bufferedReader.readLine();
            UUID = bufferedReader.readLine();
            clientWriters.put(UUID, bufferedWriter);
        }


        /**
         * Constructs a new {@code ClientWorker} with given parameters.
         *
         * @param client
         *        whose I/O streams are used for communication
         *
         * @throws IOException
         *         if an error occurs while creating {@code ClientWorker}
         */
        ClientWorker(Socket client) throws IOException{
            this.client = client;
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream(), Charset.forName("UTF-8")));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), Charset.forName("UTF-8")));
            saveWriter();
        }



        @Override
        public void run() {
            try {
                String token;
                communicationCodes.get(Codes.SERVER_SONG_LIST).execute(subject, bufferedWriter);
                communicationCodes.get(Codes.SERVER_QUEUE_LIST).execute(subject, bufferedWriter);
                communicationCodes.get(Codes.SERVER_NOW_PLAYING).execute(subject, bufferedWriter);
                while((token = bufferedReader.readLine()) != null) {
                    if(token.equals("CLIENT_QUEUE")) {
                        clientCodeMap.get(Codes.CLIENT_QUEUE).execute(subject,clientWriters,bufferedReader);
                    }
                }

                clientWriters.remove(UUID);
                client.close();
            }catch(IOException exception) {
                System.out.println("Greška at: " + exception.getMessage());
            }
        }
    }
}
