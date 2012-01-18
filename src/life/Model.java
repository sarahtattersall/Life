package life;

public class Model {
//	TODO: This should be edgeless grid class!!!
//	Have edgeless grid implement Iterable - make an iterator that remembers the 
//	surrounding cells. It's more efficient than the for loops below!
//	For the 6 it already has can remember count.
//	Iterator gives you the flexability to add more efficiency in later.
//	Holds the neighbours, might have count Fns in.
	private LifeCell[][] grid;
	
	Model(int gridSize){
		grid = new LifeCell[gridSize][gridSize];
		for( int i = 0; i < gridSize; ++i){
			for( int j = 0; j < gridSize; ++j){
				grid[i][j] = new LifeCell();
			}
		}
	}
	
	// Takes a turn following the rules given in the specification of life.
	// Any live cell with fewer than two live neighbours dies
	// Any live cell with two or three live neighbours lives onto the next generation
	// Any live cell with more than three live neighbours dies
	// Any dead cell with exactly three live neighbours becomes a live cell.
	// If a new cell is born it inherits the majority color of it's surrounding cells.	
	public void takeTurn(){
//		TODO: (1) look at state and update newState
//			  (2) commit newStates to state.
		int gridHeight = grid.length;
		int gridWidth = grid[0].length;
		LifeCell[][] newGrid = new LifeCell[gridHeight][gridWidth];
		for( int row = 0; row < gridHeight; ++row ){
			for( int col = 0; col < gridWidth; ++col ){
				int liveNeighbours = liveNeighbourCount(row, col);
				if (aliveCell(row, col)){
					if (liveNeighbours < 2 || liveNeighbours > 3){
						newGrid[row][col] = new LifeCell();
					} else{
						newGrid[row][col] = grid[row][col];
					}
				} else{
					if( liveNeighbours == 3){
						LifeCell.Color newColor = calculateMajorityColor(row, col);
						newGrid[row][col] = new LifeCell(newColor);
					} else {
						newGrid[row][col] = grid[row][col];
					}
				}
			}
		}
		grid = newGrid;
	}
	
	public int gridRows(){
		return grid.length;
	}
	
	public int gridCols(){
		return grid[0].length;
	}
	
	public LifeCell get(int x, int y){
		return grid[x][y];
	}
	
	public void changeCellColor(int x, int y, LifeCell.Color color ){
		 grid[x][y].changeColor(color);
	}
	
	public void changeCellState(int x, int y, LifeCell.State state ){
		grid[x][y].changeState(state);
	}
	
	// Determines if a cell at grid location x y is alive.
	private boolean aliveCell( int x, int y ){
		return grid[x][y].getState() == LifeCell.State.ALIVE;
	}
	
	// Counts the number of live neighbours to the cell located at x, y.
	private int liveNeighbourCount(int x, int y){
		int count = 0;
		for( int i = -1; i < 2; ++i){
			for( int j = -1; j < 2; ++j){
				if ( i != 0 || j != 0){
					int neighbourX = neighbourCoord(x, i, grid.length);
					int neighbourY = neighbourCoord(y, j, grid[0].length);
					if (aliveCell(neighbourX, neighbourY)){
						++count;
					}
				}
				
			}
		}
		return count;	
	}
	
	// Calculates the majority color surrounding the cell at x, y.
	private LifeCell.Color calculateMajorityColor(int x, int y){
		int redCount = 0;
		int greenCount = 0;
		for( int i = -1; i < 2; ++i){
			for( int j = -1; j < 2; ++j){
				if ( i != 0 || j != 0){
					int neighbourX = neighbourCoord(x, i, grid.length);
					int neighbourY = neighbourCoord(y, j, grid[0].length);
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
	
//	TODO: Add to edgeless grid
	// A smart mod for wrapping around if numbers are < 0.
	private int mod( int value, int mod_value ){
		value %= mod_value;
		return value < 0 ? value + mod_value : value; 
	}
	
	// Calculates the neighbouring x/y coordinate given an offset.
	// Uses mod for "wrapping around" the board.
	private int neighbourCoord(int value, int offset, int length){
		return mod(value + offset, length);
	}
}
