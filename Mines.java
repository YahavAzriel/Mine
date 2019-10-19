package mines;

import java.util.ArrayList;
import java.util.List;

public class Mines {
	private static Position[][] board;
	private static int height;
	private static int width;
	private boolean showAll=false;
	public Mines(int height, int width, int numMines) //constructs a mine board with position matrix
	{
		Mines.height=height;
		Mines.width=width;
		board = new Position[height][width];
		for (int i = 0;i<height;i++)
			for (int j = 0; j<width;j++)
				board[i][j]=new Position(i,j);
	}
	public boolean addMine(int i, int j) //adds a mine to a position and also increases the nearmines for each neighbour
	{
		if (board[i][j].mine==true) return false;
		board[i][j].mine=true;
		for (Position p : Place.neighbours(i, j))
			p.nearMines++;
		return true;
	}



	 public boolean open(int i, int j) //opens a place and recursively opens all of its neighbours
		{

			if (board[i][j].mine) return false; //stopping condition is if the position has a mine in it
			board[i][j].open=true; //setting the place to be opened
			if (board[i][j].nearMines==0) // if there are no mines near the position keep opening
				for (Position p : Place.neighbours(i, j))
					if (!p.open) //if neighbour is not opened
						open(p.x,p.y); //open the neighbour
			return true;
		}
	public void toggleFlag(int x, int y) //setting flag of the position to be the opposite
	{
		board[x][y].flag= (!board[x][y].flag);
	}

	public boolean isDone() { //checks if the game is over
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (!board[i][j].mine && !board[i][j].open) //if theres a place that is not a mine and still closed return false
					return false;
			}
		}
		return true; //if there are no places that has mine and closed return true
	}

	public String get(int i, int j) //returns a string of a given position
	{
		String ret = "";
		if (showAll){ //if showall is set to true, temporary open the position, get its string and close it again
			boolean val=board[i][j].open;
			board[i][j].open=true;
			ret=board[i][j].toString();
			board[i][j].open=val;
			return ret;
		}
		return board[i][j].toString();
	}

	public String toString() //returns string presentation of the board
	{
		StringBuilder b = new StringBuilder();
		for (int i = 0 ; i < height;i++)
		{
			for (int j=0;j<width;j++)
				b.append(this.get(i, j));
			b.append("\n");
		}
		return b.toString();
	}


	public void setShowAll(boolean showAll) //sets showall to given value
	{
		this.showAll=showAll;
	}

	private class Position //this class represents a position on the board
	//each position is holding boolean values
	{
		private boolean flag;
		private boolean mine;
		private boolean open;
		private int nearMines;
		private int x,y;
		public Position(int x,int y) //initializes a position
		{
			flag=false; mine=false; open=false; nearMines=0;
			this.x=x; this.y=y;
		}
		public String toString() //returns string presentation of the place
		{
			if (!flag&&!open) return ".";
			else if (flag&&!open) return "F";
			else if (mine&&open) return "X";
			else if (open&&!mine)
				if (nearMines==0) return " ";
			 return ""+nearMines;

		}
	}

	private static class Place //a static class for a place that is used to get neighbors of the position
	{
		public static List<Position> neighbours(int x,int y) //returns a list of valid neighbors
		{
			List<Position> l = new ArrayList<Position>();
			if (x+1<height) l.add(board[x+1][y]);
			if (x-1>=0) l.add(board[x-1][y]);
			if (y+1<width) l.add(board[x][y+1]);
			if (y-1>=0) l.add(board[x][y-1]);
			if (x+1<height&&y+1<width) l.add(board[x+1][y+1]);
			if (x-1>=0&&y-1>=0) l.add(board[x-1][y-1]);
			if (x-1>=0&&y+1<width) l.add(board[x-1][y+1]);
			if (x+1<height&&y-1>=0) l.add(board[x+1][y-1]);
			return l;
		}

	}

}
