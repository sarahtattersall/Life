package life;

import java.util.Iterator;

import life.LifeCell.Color;
import life.LifeCell.State;

public class EdgelessGrid implements Iterable<LifeCell>{
	public LifeCell[][] grid;

	EdgelessGrid(int actualSize){
		grid = new LifeCell[actualSize][actualSize];
		for( int i = 0; i < actualSize; ++i){
			for( int j = 0; j < actualSize; ++j){
				grid[i][j] = new LifeCell();
			}
		}
	}

	public void changeState(int x, int y, State state) {
		grid[x][y].changeState(state);
	}

	public void changeColor(int x, int y, Color color) {
		grid[x][y].changeColor(color);
	}

	public LifeCell get(int x, int y) {
		return grid[x][y];
	}
	
	public GridIterator iterator() {
		return new GridIterator(grid);
	}
	
	public class GridIterator implements Iterator<LifeCell>{
		int x;
		int y;
		int prevx;
		int prevy;
		
		LifeCell[][] grid;
		GridIterator(LifeCell[][] grid){
			this.grid = grid;
		}
		
		public boolean hasNext() {
			return (x + 1) != grid.length || (y + 1) != grid[0].length;
		}

		public LifeCell next() {
			LifeCell cell = grid[x][y];
			prevy = y;
			prevx = x;
			y = mod( y + 1, grid.length );
			if( y == 0 ){
				x = mod( x + 1, grid[0].length );
			}
			return cell;
		}

		public void remove() {
		}
		
		public int liveNeighbourCount(){
			int count = 0;
			for( int i = -1; i < 2; ++i){
				for( int j = -1; j < 2; ++j){
					if ( i != 0 || j != 0){
						int neighbourX = neighbourCoord(prevx, i, grid.length);
						int neighbourY = neighbourCoord(prevy, j, grid[0].length);
						if (aliveCell(neighbourX, neighbourY)){
							++count;
						}
					}
				}
			}
			return count;	
		}
		
		public LifeCell.Color calculateMajorityColor(){
			int redCount = 0;
			int greenCount = 0;
			for( int i = -1; i < 2; ++i){
				for( int j = -1; j < 2; ++j){
					if ( i != 0 || j != 0){
						int neighbourX = neighbourCoord(prevx, i, grid.length);
						int neighbourY = neighbourCoord(prevy, j, grid[0].length);
						if (aliveCell(neighbourX, neighbourY)){
							LifeCell cell = grid[neighbourX][neighbourY];
							if (cell.getColor() == LifeCell.Color.GREEN){
								++greenCount;
							} else if ( cell.getColor() == LifeCell.Color.RED ){
								++redCount;
							}
						}
					}
				}
			}
			return redCount > greenCount ? LifeCell.Color.RED : LifeCell.Color.GREEN;	
		}
		
		// Calculates the neighbouring x/y coordinate given an offset.
		// Uses mod for "wrapping around" the board.
		private int neighbourCoord(int value, int offset, int length){
			return mod(value + offset, length);
		}
		
		private int mod( int value, int mod_value ){
			value %= mod_value;
			return value < 0 ? value + mod_value : value; 
		}
		
		// Determines if a cell at grid location x y is alive.
		public boolean aliveCell( int x, int y ){
			return grid[x][y].getState() == LifeCell.State.ALIVE;
		}
		
	}
}
