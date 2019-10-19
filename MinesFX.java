package mines;

import java.io.IOException;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MinesFX extends Application {
	private Mines m;
	private Button buttons[][]; //button matrix that is used for the button grid
	private int height, width, minesNumber;
	private GridPane grid;
	private Random r = new Random(); //for random mines placements
	private boolean gridEmpty,gameOver;
	private Controller c;
	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox root;
		try { //loading fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Minesweeper.fxml"));
			root = loader.load();
			c = loader.getController(); //saving controller class
			this.stage=stage; //saving stage (for size changes)
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene s = new Scene(root);
		stage.setScene(s);
		stage.setTitle("Minesweeper");
		stage.show();
		gridEmpty=true;
		Button reset = c.getResetbutton(); //getting the reset button to create eventhandler for it
		reset.setOnAction(new Reset(c, root)); //setting handler for reset button
	}

	public GridPane minegrid(int height,int width) //creates buttons gridpane for mine board
	{
		GridPane gp = new GridPane();
		for (int i = 0 ;i < height;i++)
			for (int j=0;j<width;j++)
			{ buttons[i][j] = new Button(m.get(i, j));
				Button b=buttons[i][j];
				b.setPrefSize(45, 45); //button sizes
				b.setMaxSize(50, 50); //buttons sizes
				b.setMinSize(45, 45);
				b.setOnMouseClicked(new ButtonClick(i,j)); //setting event handler for each button (mouse event)
				b.setFont(new Font("Arial",20));
				gp.add(b, i, j); //adding button to grid
			}
		gp.setPadding(new Insets(50));
		//adding mines
		for (int i = 0 ; i < minesNumber ; i++) m.addMine(r.nextInt(height), r.nextInt(width));
		return gp;

	}

	 class Reset implements EventHandler<ActionEvent> //listener to reset button
	 {
		 private Controller c;
		 private HBox root;
		 public Reset(Controller c,HBox root)
		 {
			 this.c=c;
			 this.root=root;
		 }

		@Override
		public void handle(ActionEvent event) { //will create new board with parameters readen from boxes
			if (!gridEmpty) //if the grid is not empty, remove the old one
				root.getChildren().remove(grid);
			gridEmpty=false;
			gameOver=false;
			height=c.getHeight(); //read height from textfield
			width=c.getWidth(); //read width from textfield
			minesNumber=c.getMines(); //read mines number from textfield, WILL BE 0 IF LETTERS WERE WRITTEN IN TEXT BOX
			if (height==0||width==0) return; //if non number was written in these fields it will be empty
			m = new Mines(height,width,minesNumber); //creating new game from given parameters
			buttons = new Button[height][width]; //allocating space for button matrix
			grid=minegrid(height,width); //creating the button grid
			root.getChildren().add(grid); //adding the buttongrid to the root
			stage.setMaxHeight(1920);
			stage.setMaxWidth(1080);
			stage.setHeight(width*60+100); //adjusting window height (width cause its reversed)
			stage.setWidth(height*65+100); //adjusting window width (height cause its reversed)

		}

	 }

	 class ButtonClick implements EventHandler<MouseEvent> //mouse event handler for clicking on game buttons
	 {
		 private int x,y;
		 public ButtonClick(int x,int y) //sets click position
		 {
			 this.x=x; this.y=y;
		 }

		@Override
		public void handle(MouseEvent click) {
			MouseButton mb = click.getButton(); //getting which mouse button is pressed
			if (mb==MouseButton.PRIMARY && m.get(x, y)!="F") //if leftclick and there is no flag
			{
				if (!m.isDone())
				{
					if(!m.open(x, y)) //if opened a mine, end game and show msg
					{
						m.setShowAll(true); //if hit a mine, setting showall to true
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Lose");
						alert.setHeaderText(null);
						alert.setContentText("BOOM! You hit a mine!");
						if (!gameOver) //not show msg after game over
							alert.show();
						gameOver=true;
					}
				}

				 if (m.isDone()) //if game is over and no mines opened
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Win");
					alert.setHeaderText(null);
					alert.setContentText("You are the CHAMPION!!");
					if (!gameOver) //to not show msg after game over
						alert.show();
					gameOver=true;
				}
				for (int i = 0 ; i <c.getHeight() ;i++) //updating board
					for (int j=0;j<c.getWidth();j++)
						buttons[i][j].setText(m.get(i, j));
			}
			else if (mb==MouseButton.SECONDARY) //if right click then set flag on position
			{
				m.toggleFlag(x, y);
				buttons[x][y].setText(m.get(x, y));
			}
		}

	 }

}



