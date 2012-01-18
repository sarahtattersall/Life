package life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class View extends JFrame{
	Cell[][] grid;
	private int count;
	private JLabel label = new JLabel("0", SwingConstants.CENTER);
	private JButton runButton;
	private Timer timer;
	private int timerDelay;
	private JSlider slider;
	private Model lifeModel;
	private boolean running;
	View(Model model, int gridSize){
		lifeModel = model;
		count = 0;
		running = false;
		setTitle("Game of life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        
        JPanel gamePane = new JPanel();
        gamePane.setLayout(new BoxLayout(gamePane, BoxLayout.X_AXIS));
        addGrid(gridSize, gridSize, gamePane);
        addSlider(JSlider.VERTICAL, 1, 10, 1, 2, 1, gamePane, new ChangeValue());
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

        // Add buttons:y
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ClearListener());
		buttonPane.add(clearButton);
		
		JButton stepButton = new JButton("Step");
		stepButton.addActionListener(new StepListener());
		buttonPane.add(stepButton);
		
		runButton = new JButton("Run");
		runButton.addActionListener(new RunListner());
		buttonPane.add(runButton);
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new QuitListener());
		buttonPane.add(quitButton);
		
        //addButton("Clear", buttonPane, new ClearListener());
        //addButton("Step", buttonPane, new StepListener());
        //addButton("Run", buttonPane, new RunListner());
        //addButton("Quit", buttonPane, new QuitListener());
		
        Container contentPane = getContentPane();
        contentPane.add(label, BorderLayout.NORTH);
        contentPane.add(gamePane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        timerDelay = 2000/slider.getValue();
        timer = new Timer(timerDelay, new TimerListener());
        pack();
	}
	
	public void display(){
        setVisible(true);
	}
	
//	TODO: else followed by assert instead of if. 
	private void updateGrid(){
		for( int row = 0; row < grid.length; ++row ){
			for( int col = 0; col < grid[0].length; ++col ){
				LifeCell cell = lifeModel.get(row, col);
				if ( cell.getColor() == LifeCell.Color.GREEN ){
					grid[row][col].setBackground(Color.GREEN);
				} else if ( cell.getColor() == LifeCell.Color.RED ){
					grid[row][col].setBackground(Color.RED);
				} else if ( cell.getState() == LifeCell.State.DEAD ){
					grid[row][col].setBackground(Color.GRAY);
				}
			}
		}
	}
	
	//private void addButton(String text, Container container, ActionListener adapter){
	//	JButton button = new JButton(text);
	//	button.addActionListener(adapter);
	//	container.add(button);
	//}
	
	private void addSlider(int orientation, int min, int max, int value, int majorTicks,
			int minorTicks, Container container, ChangeListener listener){
		slider = new JSlider(orientation, min, max, value);
		slider.addChangeListener(listener);
        slider.setMajorTickSpacing(majorTicks);
        slider.setMinorTickSpacing(minorTicks);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
		container.add(slider);	
	}
	
	private void addGrid(int rows, int cols, Container container) {
		grid = new Cell[rows][cols];
		JPanel gridPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(rows, cols);
		gridPanel.setLayout(gridLayout);
		for( int row = 0; row < rows; ++row){
			for ( int col = 0; col < cols; ++col){
				Cell cell = new Cell(row,col);
				grid[row][col] = cell;
				gridPanel.add(cell);
			}
		}
		container.add(gridPanel);
	}
	
	private void incrementCounter(){
		++count;
		label.setText(Integer.toString(count));
	}
	
	private void takeTurn(){
		lifeModel.takeTurn();
		updateGrid();
		incrementCounter();
	}
	
	class QuitListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	class RunListner implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			JButton button = (JButton)event.getSource();
			if (button.getText().equals("Run")){
				timer.start();
				button.setText("Pause");
				running = true;
			} else if ( button.getText().equals("Pause")){
				timer.stop();
				button.setText("Run");
				running = false;
			}
		}
	}
	
	class ClearListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			timer.stop();
			running = false;
			count = 0;
			label.setText(Integer.toString(count));
			runButton.setText("Run");
			for(int row = 0; row < grid.length; ++row){
				for(int col = 0; col < grid[0].length; ++col){
					grid[row][col].die();
					lifeModel.changeCellState(row, col, LifeCell.State.DEAD);
					lifeModel.changeCellColor(row, col, LifeCell.Color.NONE);
				}
			}
		}
	}
	
	class StepListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			takeTurn();
		}
	}

	class TimerListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			takeTurn();
		}
	}
	
	class MouseQuitAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if (SwingUtilities.isLeftMouseButton(event)) {
            	System.exit(0);
            }
        }
    }
	
	class ChangeValue implements ChangeListener {
        public void stateChanged(final ChangeEvent expn) {
            final JSlider source = (JSlider)expn.getSource();
            if (!source.getValueIsAdjusting()) {
            	timerDelay = 2000/source.getValue();
            	timer.setDelay(timerDelay);
            }
        }
    }
	class Cell extends JButton{
		int row;
		int col;
		Cell(int x, int y){
			this.row = x;
			this.col = y;
			setBackground(Color.GRAY);
			//setOpaque(true);
			//setBorderPainted(false);
			setPreferredSize(new Dimension(20, 20));
			addMouseListener(new MouseQuitAdapter());
		}
		
		class MouseQuitAdapter extends MouseAdapter {
	        public void mouseClicked(MouseEvent event) {
	        	if (!running){
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
		
		public void die(){
			setBackground(Color.GRAY);
		}
	}
	
}
