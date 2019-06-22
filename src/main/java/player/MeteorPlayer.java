package player;

import commands.OpenCommand;
import gui.PlayerDisplay;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.PlayerData;
import commands.Command;

public class MeteorPlayer extends Application {
    private static final String STYLE_SHEET_MATRIX = "matrix.css";

    private static final int WIDTH_CONSTRAINT = 960;

    private static final int BUTTON_BOX_PREFERED_WIDTH = 1920;

    private static final int BUTTON_PREFERED_WIDTH = 50;

    private Stage window;

    private MenuBar menuBar;

    private Menu fileMenu;

    private Menu networkMenu;

    private BorderPane rootLayout;

    private GridPane gridLayout;

    private PlayerData playerData;

    private PlayerDisplay playerDisplay;

    private Command openCommand;

    private Command startServerCommand;

    private Command stopServerCommand;

    private Button previousButton;

    private Button playButton;

    private Button nextButton;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Meteor");
        rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("rootPane");
        gridLayout = new GridPane();
        rootLayout.setCenter(gridLayout);
        Scene scene = new Scene(rootLayout,800,800);
        scene.getStylesheets().add(STYLE_SHEET_MATRIX);
        playerData = new PlayerData();
        playerDisplay = new PlayerDisplay(playerData);
        initGUI();
        window.setScene(scene);
        window.show();
    }

    private void initGUI() {
        initMenu();
        gridLayout.getColumnConstraints().addAll(new ColumnConstraints(WIDTH_CONSTRAINT),new ColumnConstraints(WIDTH_CONSTRAINT));
        gridLayout.setVgap(15);
        HBox labelHBox = new HBox();
        labelHBox.getChildren().addAll(new Label(), playerDisplay.getNowPlaying());
        labelHBox.getStyleClass().add("labelHBox");
        gridLayout.add(labelHBox,0,0);
        gridLayout.add(playerDisplay.getloadedSongsView(),0 ,2);
        gridLayout.add(playerDisplay.getNextInQueue(),1,0);
        gridLayout.add(playerDisplay.getQueuedSongsView(),1,2);
        previousButton = new Button("<<");
        playButton = new Button("|>");
        nextButton = new Button(">>");
        previousButton.setPrefSize(BUTTON_PREFERED_WIDTH, BUTTON_PREFERED_WIDTH);
        playButton.setPrefSize(BUTTON_PREFERED_WIDTH, BUTTON_PREFERED_WIDTH);
        nextButton.setPrefSize(BUTTON_PREFERED_WIDTH, BUTTON_PREFERED_WIDTH);
        previousButton.setOnAction(l->playerData.playPreviousLoaded());
        nextButton.setOnAction(l->playerData.playNextSong());
        HBox hb = new HBox();
        hb.setPrefWidth(BUTTON_BOX_PREFERED_WIDTH);
        hb.getChildren().addAll(previousButton,playButton,nextButton);
        hb.getStyleClass().add("hbox");
        GridPane twoGridder = new GridPane();
        twoGridder.add(hb, 0, 0);
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
        initFileMenu();
       // initNetworkMenu();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(networkMenu);
        rootLayout.setTop(menuBar);
    }


    /**
     * Initializes {@link #fileMenu {@code fileMenu}} with {@link Command Commands}.
     *
     * This method will assign appropriate {@code Commands} to {@code fileMenu}
     * set style class of {@code fileMenu} and styles of added {@code Commands}.
     */
    private void initFileMenu() {
        openCommand = new OpenCommand(window,playerData,"Open...");
        fileMenu.getItems().add(openCommand);
        fileMenu.getStyleClass().add("menu");
        openCommand.getStyleClass().add("menuitem");
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