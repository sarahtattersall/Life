package life;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

class RunListener implements ActionListener{
	Timer timer;
	JPanel glass;
	RunListener(Timer timer, JPanel glass){
		this.timer = timer;
		this.glass = glass;
	}
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton)event.getSource();
		if (button.getText().equals("Run")){
			timer.start();
			button.setText("Pause");
			glass.setVisible(true);
		} else if ( button.getText().equals("Pause")){
			timer.stop();
			button.setText("Run");
			glass.setVisible(false);
		}
	}
}