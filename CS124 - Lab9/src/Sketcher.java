import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Sketcher extends Application {

	private GraphicsContext g;
	private double prevX, prevY; // location of previous mouse event

	private final static int LINE = 0;
	private final static int SQUARE = 1;
	private final static int DISK = 2;

	private int currentTool = LINE;
	private Color currentColor = Color.RED;

	private void doMouseDown(double x, double y) {
		prevX = x; // Just save the starting position.
		prevY = y;

		if (currentTool != LINE) {
			putShape(x, y);
		}
	}

	private MenuBar makeMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu controlMenu = new Menu("Control");
		MenuItem quitItem = new MenuItem("Quit");
		quitItem.setOnAction(e -> System.exit(0));
		controlMenu.getItems().add(quitItem);

		Menu colorMenu = new Menu("Color");
		MenuItem colorItem;
		colorItem = new MenuItem("Red");
		colorItem.setOnAction(e -> currentColor = Color.RED);
		colorMenu.getItems().add(colorItem);
		colorItem = new MenuItem("Green");
		colorItem.setOnAction(e -> currentColor = Color.GREEN);
		colorMenu.getItems().add(colorItem);
		colorItem = new MenuItem("Blue");
		colorItem.setOnAction(e -> currentColor = Color.BLUE);
		colorMenu.getItems().add(colorItem);
		colorItem = new MenuItem("Random");
		colorItem.setOnAction(e -> currentColor = null);
		colorMenu.getItems().add(colorItem);

		menuBar.getMenus().addAll(controlMenu, colorMenu);
		return menuBar;
	}

	private TilePane makeButtonBar() {
		ToggleGroup grp = new ToggleGroup();

		RadioButton lineBtn = new RadioButton("Line");
		lineBtn.setOnAction(e -> currentTool = LINE);
		lineBtn.setToggleGroup(grp);
		lineBtn.setSelected(true); // Make this one the selected radiobutton

		RadioButton squareBtn = new RadioButton("Square");
		squareBtn.setOnAction(e -> currentTool = SQUARE);
		squareBtn.setToggleGroup(grp);

		RadioButton diskBtn = new RadioButton("Disk");
		diskBtn.setOnAction(e -> currentTool = DISK);
		diskBtn.setToggleGroup(grp);

		TilePane bottom = new TilePane(5, 5);
		bottom.getChildren().add(new Label("Draw using: "));
		bottom.getChildren().addAll(lineBtn, squareBtn, diskBtn);
		bottom.setStyle("-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2px 0 0 0");
		return bottom;
	}

	private void putShape(double x, double y) {
		if (currentColor == null) {
			g.setFill(Color.hsb(360 * Math.random(), 0.8, 1));
		} else {
			g.setFill(currentColor);
		}
		g.setStroke(Color.BLACK);
		g.setLineWidth(1);
		if (currentTool == SQUARE) {
			g.fillRect(x - 15, y - 15, 30, 30);
			g.strokeRect(x - 15, y - 15, 30, 30);
		} else if (currentTool == DISK) {
			g.fillOval(x - 15, y - 15, 30, 30);
			g.strokeOval(x - 15, y - 15, 30, 30);
		}
	}

	private void doMouseDrag(double x, double y) {
		double offset = Math.sqrt((x - prevX) * (x - prevX) + (y - prevY) * (y - prevY));
		if (offset < 5) {
			// Don't draw anything until mouse has moved a bit.
			return;
		}

		if (currentColor == null)
			g.setStroke(Color.hsb(360 * Math.random(), 0.8, 1));
		else
			g.setStroke(currentColor);

		if (currentTool == LINE) {
			g.setLineWidth(2);
			g.strokeLine(prevX, prevY, x, y);
			prevX = x;
			prevY = y;
		}
		else {
			putShape(x, y);
		}
	}

	public void start(Stage stage) {
		Canvas canvas = new Canvas(800, 600);
		g = canvas.getGraphicsContext2D();
		BorderPane mainPane = new BorderPane(canvas);
		mainPane.setTop(makeMenuBar());
		mainPane.setBottom(makeButtonBar());
		Scene scene = new Scene(mainPane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Simple Sketcher");
		stage.show();

		canvas.setOnMousePressed(e -> doMouseDown(e.getX(), e.getY()));
		canvas.setOnMouseDragged(e -> doMouseDrag(e.getX(), e.getY()));
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, 800, 600);
	}

	public static void main(String[] args) {
		launch();
	}

}
