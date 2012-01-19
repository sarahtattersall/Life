package life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.OverlayLayout;


public class View extends JFrame{
	// Keeps track of the steps taken to be displayed on screen.
	private int count;
	// Label to display count
	private JLabel label = new JLabel("0", SwingConstants.CENTER);
	// A glass panel to sit over the grid to disable mouse events whilst
	// the game of life is running
	private JPanel glass;
	
	Cell[][] grid;
	private JButton runButton;
	private JButton stepButton;
	private Timer timer;
	private JSlider slider;
	private Model lifeModel;

	View(Model model, int gridSize){
		lifeModel = model;
		count = 0;
		setTitle("Game of life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel gamePane = new JPanel();
        gamePane.setLayout(new BoxLayout(gamePane, BoxLayout.X_AXIS));
        addGrid(gridSize, gridSize, gamePane);
        addSlider(JSlider.VERTICAL, 1, 10, 1, 2, 1, gamePane, new ChangeValue());
        
        int delay = 2000/slider.getValue();
        timer = new Timer(delay, new TimerListener());
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

        // Add buttons:
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ClearListener());
		buttonPane.add(clearButton);
		
		stepButton = new JButton("Step");
		stepButton.addActionListener(new StepListener());
		buttonPane.add(stepButton);
		
		runButton = new JButton("Run");
		runButton.addActionListener(new RunListener());
		buttonPane.add(runButton);
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new QuitListener());
		buttonPane.add(quitButton);
		
        Container contentPane = getContentPane();
        contentPane.add(label, BorderLayout.NORTH);
        contentPane.add(gamePane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        pack();
	}
	
	public void display(){
        setVisible(true);
	}
	
	// Updates the grid to be a replica of the lifeModel grid
	private void updateGrid(){
		for( int row = 0; row < grid.length; ++row ){
			for( int col = 0; col < grid[0].length; ++col ){
				LifeCell cell = lifeModel.get(row, col);
				if ( cell.getColor() == LifeCell.Color.GREEN ){
					grid[row][col].setBackground(Color.GREEN);
				} else if ( cell.getColor() == LifeCell.Color.RED ){
					grid[row][col].setBackground(Color.RED);
				} else{
					assert(cell.getState() == LifeCell.State.DEAD);
					grid[row][col].setBackground(Color.GRAY);
				}
			}
		}
	}
	
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
	
	// Creates a grid with a 'glass' panel in front of it that can be enabled when
	// the game of life is running so that none of the Cells in the grid can be
	// altered.
	private void addGrid(int rows, int cols, Container container) {
		grid = new Cell[rows][cols];
		JPanel panel =  new JPanel();
		panel.setLayout(new OverlayLayout(panel));
		JPanel gridPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(rows, cols);
		gridPanel.setLayout(gridLayout);
		for( int row = 0; row < rows; ++row){
			for ( int col = 0; col < cols; ++col){
				Cell cell = new Cell(row,col,lifeModel);
				grid[row][col] = cell;
				gridPanel.add(cell);
			}
		}
		glass = new JPanel();
		glass.setOpaque(false);
		glass.setVisible(false);
		glass.addMouseListener(new IgnoreMousePressAdapter());
		panel.add(glass);
		panel.add(gridPanel);
		container.add(panel);
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
	
	class ClearListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			timer.stop();
			glass.setVisible(false);
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
	
	// Changes the timers speed.
	class ChangeValue implements ChangeListener {
        public void stateChanged(final ChangeEvent expn) {
            final JSlider source = (JSlider)expn.getSource();
            if (!source.getValueIsAdjusting()) {
            	int delay = 2000/source.getValue();
            	timer.setDelay(delay);
            }
        }
    }
	
	// When clicked changes run button text to pause, sets the
	// timer and makes the glass pane over the grid panel visible
	// so that you cannot click on any of the grid cells.
	// Does the reverse when you click on pause.
	class RunListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			JButton button = (JButton)event.getSource();
			if (button.getText().equals("Run")){
				timer.start();
				button.setText("Pause");
				glass.setVisible(true);
				stepButton.setEnabled(false);
			} else if ( button.getText().equals("Pause")){
				timer.stop();
				button.setText("Run");
				glass.setVisible(false);
				stepButton.setEnabled(true);
			}
		}
	}
	
	class IgnoreMousePressAdapter extends MouseAdapter{
		public void mouseClicked(MouseEvent event) {
	    }
	}
	
	class QuitListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
}
