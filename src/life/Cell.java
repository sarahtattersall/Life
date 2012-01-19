package life;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

class Cell extends JButton{
		int row;
		int col;
		Model lifeModel;
		Cell(int x, int y, Model lifeModel){
			this.row = x;
			this.col = y;
			this.lifeModel = lifeModel;
			setBackground(Color.GRAY);
			//setOpaque(true);
			//setBorderPainted(false);
			setPreferredSize(new Dimension(20, 20));
			addMouseListener(new MouseQuitAdapter());
		}
		
		
		public void die(){
			setBackground(Color.GRAY);
		}
		
		class MouseQuitAdapter extends MouseAdapter {
	        public void mouseClicked(MouseEvent event) {
	            if (SwingUtilities.isLeftMouseButton(event)) {
	            	setBackground(Color.RED);
	            	lifeModel.changeCellColor(row, col, LifeCell.Color.RED);
	            	lifeModel.changeCellState(row, col, LifeCell.State.ALIVE);
	            }else if (SwingUtilities.isRightMouseButton(event)){
	            	setBackground(Color.GREEN);
	            	lifeModel.changeCellColor(row, col, LifeCell.Color.GREEN);
	            	lifeModel.changeCellState(row, col, LifeCell.State.ALIVE);
	            }else if (SwingUtilities.isMiddleMouseButton(event)){
	            	setBackground(Color.GRAY);
	            	lifeModel.changeCellColor(row, col, LifeCell.Color.NONE);
	            	lifeModel.changeCellState(row, col, LifeCell.State.DEAD);
	            }
        	}
	    }
	}