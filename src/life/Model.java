package life;

import java.util.Iterator;

public class Model {
//	TODO: This should be edgeless grid class!!!
//	Have edgeless grid implement Iterable - make an iterator that remembers the 
//	surrounding cells. It's more efficient than the for loops below!
//	For the 6 it already has can remember count.
//	Iterator gives you the flexability to add more efficiency in later.
//	Holds the neighbours, might have count Fns in.
	private EdgelessGrid grid;
	
	Model(int gridSize){
		grid = new EdgelessGrid(gridSize);
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
		/*int gridHeight = grid.grid.length;
		int gridWidth = grid.grid[0].length;
		LifeCell[][] newGrid = new LifeCell[gridHeight][gridWidth];
		for( int row = 0; row < gridHeight; ++row ){
			for( int col = 0; col < gridWidth; ++col ){
				int liveNeighbours = liveNeighbourCount(row, col);
				if (aliveCell(row, col)){
					if (liveNeighbours < 2 || liveNeighbours > 3){
						//newGrid[row][col] = new LifeCell();
						grid.grid[row][col].setNextState(LifeCell.State.DEAD);
						grid.grid[row][col].setNextColor(LifeCell.Color.NONE);
					} else{
						//newGrid[row][col] = grid.grid[row][col];
						grid.grid[row][col].setNextState(grid.grid[row][col].getState());
						grid.grid[row][col].setNextColor(grid.grid[row][col].getColor());
					}
				} else{
					if( liveNeighbours == 3){
						LifeCell.Color newColor = calculateMajorityColor(row, col);
//						newGrid[row][col] = new LifeCell(newColor);
						grid.grid[row][col].setNextState(LifeCell.State.ALIVE);
						grid.grid[row][col].setNextColor(newColor);
					} else {
						//newGrid[row][col] = grid.grid[row][col];
						grid.grid[row][col].setNextState(grid.grid[row][col].getState());
						grid.grid[row][col].setNextColor(grid.grid[row][col].getColor());
					}
				}
			}
		} 
		for( int row = 0; row < gridHeight; ++row ){
			for( int col = 0; col < gridWidth; ++col ){
				grid.grid[row][col].commitState();
			}
		}*/
		for( EdgelessGrid.GridIterator itr = grid.iterator(); itr.hasNext();){
			LifeCell cell = itr.next();
			int liveNeighbours = itr.liveNeighbourCount();
			if (cell.getState() == LifeCell.State.ALIVE){
				if (liveNeighbours < 2 || liveNeighbours > 3){
					cell.setNextState(LifeCell.State.DEAD);
					cell.setNextColor(LifeCell.Color.NONE);
				} else{
					cell.setNextState(cell.getState());
					cell.setNextColor(cell.getColor());
				}
			} else{
				if( liveNeighbours == 3 ){
					cell.setNextState(LifeCell.State.ALIVE);
					cell.setNextColor(itr.calculateMajorityColor());
				} else {
					cell.setNextState(cell.getState());
					cell.setNextColor(cell.getColor());
				}
			}
		}
		
		for( LifeCell cell : grid ){
			cell.commitState();
		}
	}
	
	public LifeCell get(int x, int y){
		return grid.get(x,y);
	}
	
	public void changeCellColor(int x, int y, LifeCell.Color color ){
		grid.changeColor(x, y, color);
	}
	
	public void changeCellState(int x, int y, LifeCell.State state ){
		grid.changeState(x, y, state); 
	}
}