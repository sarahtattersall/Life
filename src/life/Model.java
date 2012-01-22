package life;

public class Model {
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
	// Here it is simpler to have the cell contain it's next state and loop through 
	// updating the next state and then commit all the states at the end rather than
	// having a new grid. It is also more efficient as there is not duplicate data.
	// The grid iterator calculates the neighbour count and majority color.
	public void takeTurn(){
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