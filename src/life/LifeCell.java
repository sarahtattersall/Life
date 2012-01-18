package life;

public class LifeCell {
	public enum State { DEAD, ALIVE }
	public enum Color { NONE, RED, GREEN }
//	TODO: Add a previous state
	State state;
	Color color;
	LifeCell(){
		state = State.DEAD;
		color = Color.NONE;
	}
	
	LifeCell(Color color){
		state = State.ALIVE;
		this.color = color;
	}
	
	public State getState(){
		return state;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void changeColor(Color color){
		this.color = color;
	}
	
	public void changeState( State state ){
//		previousState = this.state;
		this.state = state;
	}
	
	
}
