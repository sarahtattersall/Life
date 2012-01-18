package life;

public class LifeCell {
	public enum State { DEAD, ALIVE }
	public enum Color { NONE, RED, GREEN }
//	TODO: Add a previous state
	State state;
	State nextState;
	Color color;
	Color nextColor;
	LifeCell(){
		state = State.DEAD;
		color = Color.NONE;
	}
	
	//LifeCell(Color color){
	//	state = State.ALIVE;
	//	this.color = color;
	//}
	
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
		this.state = state;
	}
	
	public void setNextState( State state ){
		nextState = state;
	}
	
	public void setNextColor( Color color ){
		nextColor = color;
	}
	
	public void commitState(){
		state = nextState;
		color = nextColor;
	}
	
}
