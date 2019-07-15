package broadcaster;

import codes.IClientCode;
import codes.IServerCode;
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
     * Socket address of this singleton.
     */
    private SocketAddress serverAddress;


    /**
     *  ServerSocket of {@code ListBroadcaster}
     */
    private ServerSocket server;


    /**
     * Executor pool
     */
    private ExecutorService pool;


    /**
     * Subject of this {@link NetworkPlayerDataObserver}.
     */
    private PlayerData subject;


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
     * Loads codes found in {@link codes.concreteservercodes concreteservercodes} into {@link #communicationCodes}.<br>
     */
    private void loadCodes() {
        communicationCodes = new HashMap<>();
        BufferedReader bufferedReader;
        try{
            bufferedReader = Files.newBufferedReader(Paths.get("servercodes.txt"));
            List<String> codes = new ArrayList<>();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                codes.add(line);
            }
            String packagePrefix = "codes.concreteservercodes.";
            for(String code : codes) {
                Class<IServerCode> iCommunicationCodeClass = (Class<IServerCode>) Class.forName(packagePrefix + code);
                IServerCode iConcrete = iCommunicationCodeClass.getDeclaredConstructor().newInstance();
                communicationCodes.put(iConcrete.getClass().getSimpleName(), iConcrete);
            }
            bufferedReader.close();
            bufferedReader = Files.newBufferedReader(Paths.get("clientcodes.txt"));
            codes.clear();
            while((line = bufferedReader.readLine()) != null) {
                codes.add(line);
            }
            packagePrefix = "codes.concreteclientcodes.";
            for(String code : codes) {
                Class<IClientCode> iClientCodeClass = (Class<IClientCode>) Class.forName(packagePrefix + code);
                IClientCode iConcrete = iClientCodeClass.getDeclaredConstructor().newInstance();
                clientCodeMap.put(iConcrete.getClass().getSimpleName(), iConcrete);
            }
            bufferedReader.close();
        }catch(IOException exception) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Failed to load codes used for communication.\nBroadcaster will not turn on.", ButtonType.CLOSE);
                alert.setTitle("Error loading codes");
                alert.showAndWait();
            });
            throw new RuntimeException("Error loading codes.");
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error in instantaion of concrete codes.\nBroadcaster will not turn on.", ButtonType.CLOSE);
                alert.setTitle("Error loading codes");
                alert.showAndWait();
            });
            throw new RuntimeException("Error in concrete codes instantation");
        }

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
     *
     */
    public void startBroadcast() {
        if(!isRunning) {
            isRunning = true;
            try{
                System.out.println("Server is running at: " + serverAddress);
                while(isRunning) {
                    Socket client = server.accept();
                    ClientWorker worker = new ClientWorker(client);
                    pool.submit(worker);
                }
            }catch(IOException exception) {
                System.out.println(exception);
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

    @Override
    public void update(String code) {
        try {
            communicationCodes.get(code).execute(subject, clientWriters);
        }catch (IOException exception) {
            //TODO REMOVE THIS OR HANDLE IT PROPERLY
            System.out.println("An error occured. YACK." + exception.getMessage());
        }
    }


    /**
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
         * When a client connects to server it will send a code and its UUID.<br>
         * This method saves UUID and appropriate writer to {@link #clientWriters}
         *
         * @throws IOException
         *         if an error occurs while reading from reader
         */
        private void saveWriter() throws IOException{
            bufferedReader.readLine();
            clientWriters.put(bufferedReader.readLine(), bufferedWriter);
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
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream(), Charset.forName("UTF-8")));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), Charset.forName("UTF-8")));
            saveWriter();
        }



        @Override
        public void run() {
            System.out.println("Nalazim se u run metodi lets see what we got here: ");
            try {
                String token = "";
                update(Codes.SERVER_SONG_LIST);
                update(Codes.SERVER_QUEUE_LIST);
              //  update(Codes.SERVER_NOW_PLAYING);
               // update(Codes.SERVER_MY_QUEUED_SONG);
                while(token != null) {
                    token = bufferedReader.readLine();
                    System.out.println(token);
                }
                System.out.println("Kraj !");
            }catch(IOException exception) {
                System.out.println("Greška at: " + exception.getMessage());
            }
        }
    }
}
