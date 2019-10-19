package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

	   @FXML
	  public Button resetbutton;
	   @FXML
	public   TextField widthbox;
	   @FXML
	 public  TextField heightbox;
	   @FXML
	   public TextField minesbox;
	   @FXML

	public Button getResetbutton() { //returns reset button
		return resetbutton;
	}

	public int getHeight() { //returns the int value of the text in heightbox
		int ret;
		try {
			ret = Integer.parseInt(heightbox.getText());

			return ret > 0 ? ret : 0; //ignore negative numbers
		}
		catch(Exception e) // if letter then return 0
		{
			return 0;
		}
	}
	public int getWidth() { //same for width
		int ret;
		try {
			ret = Integer.parseInt(widthbox.getText());
			return ret > 0 ? ret : 0;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	public int getMines() { //same for mines
		int ret;
		try {
			ret = Integer.parseInt(minesbox.getText());
			return ret > 0 ? ret : 0;
		}
		catch(Exception e)
		{
			return 0;
		}
	}


}
