package player;

import commands.*;
import gui.PlayerDisplay;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.PlayerData;
import observers.PlayerDisplayObserver;

public class MeteorPlayer extends Application implements PlayerDisplayObserver {
    private static final String STYLE_SHEET_MATRIX = "matrix.css";

    private static final int WIDTH_CONSTRAINT = 960;

    private static final int BUTTON_BOX_PREFERRED_WIDTH = 1920;

    private static final int BUTTON_PREFERRED_WIDTH = 50;

    private static final int BUTTON_PREFERRED_HEIGHT = 50;

    private Stage window;

    private MenuBar menuBar;

    private Menu fileMenu;

    private Menu networkMenu;

    private Menu skinMenu;

    private BorderPane rootLayout;

    private GridPane gridLayout;

    private Scene scene;

    private PlayerData playerData;

    private PlayerDisplay playerDisplay;

    private ModelCommand openCommand;

    private SceneCommand lightSkinCommand;

    private SceneCommand darkSkinCommand;

    private ModelCommand startServerCommand;

    private Button previousButton;

    private Button playButton;

    private Button nextButton;

    private CheckBox playingRandom;

    private ProgressBar bar;

    private Label progress;


    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Meteor");
        window.getIcons().add(new Image("icon.png"));
        rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("rootPane");
        gridLayout = new GridPane();
        rootLayout.setCenter(gridLayout);
        scene = new Scene(rootLayout,800,800);
        scene.getStylesheets().add(STYLE_SHEET_MATRIX);
        playerData = new PlayerData();
        playerDisplay = new PlayerDisplay(playerData);
        playerDisplay.addPlayerDisplayObserver(this);
        initGUI();
        window.setOnCloseRequest(e -> handleClose());
        window.setScene(scene);
        window.show();
    }


    /**
     * Initializes GUI of {@code MeteorPlayer}.
     * This includes layout, buttons, labels and functionality of those.
     */
    private void initGUI() {
        initMenu();
        gridLayout.getColumnConstraints().addAll(new ColumnConstraints(WIDTH_CONSTRAINT),new ColumnConstraints(WIDTH_CONSTRAINT));
        gridLayout.setVgap(15);

        //Get left side: Search bar, currently playing, loaded songs, information
        HBox labelHBox = new HBox();
        labelHBox.getChildren().addAll(new Label(), playerDisplay.getNowPlaying());
        labelHBox.getStyleClass().add("labelHBox");
        gridLayout.add(labelHBox,0,0);
        gridLayout.add(playerDisplay.getSearchBar(),0,1);
        gridLayout.add(playerDisplay.getLoadedSongsView(),0 ,2);
        gridLayout.add(playerDisplay.getSongInformation(),0,3);
        gridLayout.add(playerDisplay.getServerInfo(),0,4);


        //Get right side: next in queue, queued songs
        HBox labelHBox2 = new HBox();
        labelHBox2.getChildren().addAll(new Label(), playerDisplay.getNextInQueue());
        labelHBox2.getStyleClass().add("labelHBox");
        gridLayout.add(labelHBox2,1,0);
        gridLayout.add(playerDisplay.getQueuedSongsView(),1,2);

        //Bottom of display, buttons and seek bar.
        previousButton = new Button("<<");
        playButton = new Button("|>");
        nextButton = new Button(">>");
        playingRandom = new CheckBox("Random");
        playingRandom.setAllowIndeterminate(false);
        playingRandom.setSelected(false);
        previousButton.setPrefSize(BUTTON_PREFERRED_WIDTH, BUTTON_PREFERRED_HEIGHT);
        playButton.setPrefSize(BUTTON_PREFERRED_WIDTH, BUTTON_PREFERRED_HEIGHT);
        nextButton.setPrefSize(BUTTON_PREFERRED_WIDTH, BUTTON_PREFERRED_HEIGHT);


        previousButton.setOnAction(l->playerData.playPreviousLoaded());
        playButton.setOnAction(l->playerData.togglePlay());
        nextButton.setOnAction(l->playerData.playNextSong());
        playingRandom.setOnAction(event -> {
            if(playingRandom.isSelected()) {
                playerData.setRandomSong(true);
            }
            else {
                playerData.setRandomSong(false);
            }
        });
        progress = new Label();
        progress.getStyleClass().add("timeLabel");

        HBox hb = new HBox();
        hb.setPrefWidth(BUTTON_BOX_PREFERRED_WIDTH);
        hb.getChildren().addAll(previousButton,playButton,nextButton,playingRandom,progress);
        hb.getStyleClass().add("hbox");
        GridPane twoGridder = new GridPane();
        bar = new ProgressBar(0);
        bar.setPrefWidth(BUTTON_BOX_PREFERRED_WIDTH);
        bar.setOnMouseClicked(value -> {
            double percentage = value.getX() / BUTTON_BOX_PREFERRED_WIDTH;
            if(playerData.getMediaPlayer() != null) {
                Duration total = playerData.getMediaPlayer().getMedia().getDuration();
                playerData.getMediaPlayer().seek(total.multiply(percentage));
            }
        });
        twoGridder.add(hb, 0, 0);
        twoGridder.add(bar, 0,1);
        rootLayout.setBottom(twoGridder);

    }

    /**
     * Initializes {@link MenuBar} of a player.
     * This method will add {@link Menu} items to {@code MenuBar} and set
     * style class of {@code menuBar}<br>
     *
     * For more information on menu initialization see:
     * {@link #initFileMenu()}

     */
    private void initMenu() {
        menuBar = new MenuBar();
        menuBar.getStyleClass().add("menu-bar");
        fileMenu = new Menu("_File");
        networkMenu = new Menu("_Network");
        skinMenu = new Menu("_Skin");
        initFileMenu();
        initNetworkMenu();
        initSkinMenu();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(networkMenu);
        menuBar.getMenus().add(skinMenu);
        rootLayout.setTop(menuBar);
    }


    /**
     * Initializes {@link #fileMenu {@code fileMenu}} with {@link ModelCommand ModelCommands}.
     *
     * This method will assign appropriate {@code ModelCommands} to {@code fileMenu},
     * set style class of {@code fileMenu} and styles of added {@code ModelCommands}.
     */
    private void initFileMenu() {
        openCommand = new OpenCommand(window,playerData,"Open...");
        fileMenu.getItems().add(openCommand);
        fileMenu.getStyleClass().add("menu");
        openCommand.getStyleClass().add("menuitem");
    }


    /**
     * Initializes {@link #networkMenu {@code networkMenu}} with {@link ModelCommand ModelCommands}.
     *
     * This method will assign appropriate {@code ModelCommands} to {@code networkMenu},
     * set style class of {@code networkMenu} and styles of added {@code ModelCommands}.
     */
    private void initNetworkMenu() {
        startServerCommand = new NetworkCommand(playerData, "Start Server");
        networkMenu.getItems().add(startServerCommand);
        networkMenu.getStyleClass().add("menu");
        startServerCommand.getStyleClass().add("menuitem");
    }


    /**
     * Initializes {@link #skinMenu {@code skinMenu}} with {@link SceneCommand SceneCommands}.
     *
     * This method will assign appropriate {@code SkinCommands} to {@code skinMenu}
     * set style class of {@code skinMenu}  and styles of added {@code SceneCommands}.
     */
    private void initSkinMenu() {
        lightSkinCommand = new LightSkinCommand(scene,"Light skin");
        darkSkinCommand = new DarkSkinCommand(scene,"Dark skin");
        skinMenu.getItems().add(darkSkinCommand);
        skinMenu.getItems().add(lightSkinCommand);
        skinMenu.getStyleClass().add("menu");
        lightSkinCommand.getStyleClass().add("menuitem");
        darkSkinCommand.getStyleClass().add("menuitem");
    }


    /**
     * Closes MeteorPlayer.<br>
     * Also shuts down any active pool threads.
     */
    private void handleClose() {
        playerData.closePlayerData();
    }


    @Override
    public void setQueriedList() {
        gridLayout.getChildren().remove(playerDisplay.getLoadedSongsView());
        gridLayout.add(playerDisplay.getQueriedSongsView(),0,2);
    }

    @Override
    public void restoreToLoadedList() {
        if(gridLayout.getChildren().contains(playerDisplay.getQueriedSongsView())) {
            gridLayout.getChildren().remove(playerDisplay.getQueriedSongsView());
        }
        if(!gridLayout.getChildren().contains(playerDisplay.getLoadedSongsView())) {
            gridLayout.add(playerDisplay.getLoadedSongsView(), 0, 2);
        }
    }

    @Override
    public void updateTimeProperty(Duration currentTime, Duration totalTime) {
        double currentSeconds = currentTime.toSeconds();
        double totalSeconds = totalTime.toSeconds();
        int cm = (int) (currentSeconds / 60);
        int tm = (int) (totalSeconds / 60);
        int lcm = (int) (currentSeconds % 60);
        int ltm = (int) (totalSeconds % 60);
        String current = String.format("%02d:%02d",cm,lcm);
        String next = String.format("%02d:%02d",tm,ltm);
        progress.setText(current + " / " + next);
        bar.setProgress(currentSeconds/totalSeconds);
    }


    /**
     * Launches application
     *
     * @param args
     *        not used
     */
    public static void main(String[] args) {
        launch(args);
    }
}
