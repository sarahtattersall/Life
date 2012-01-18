package life;

public class Life {
	
    public static void main(String [] args) {
    	final int size;
    	if( args.length > 0){
    		try{
    			size = Integer.parseInt(args[0]);
    			javax.swing.SwingUtilities.invokeLater(new Runnable() {
    	            public void run() {
    	            	Model model = new Model(size);
    	                View gui = new View(model);
    	                gui.display();
    	            }
    	        });
    		} catch (NumberFormatException e){
    			System.err.println("The first parameter should be a number");
    			System.exit(1);
    		}
    	} else {
    		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	Model model = new Model(30);
	                View gui = new View(model);
	                gui.display();
	            }
	        });
    	}
    }
}