import javax.swing.*; 
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class World extends JPanel
{
    // 10 x 10 grid
    public static final int ROWS = 10;
    public static final int COLS = 10;

    // Variables to indicate direction moving
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;

    // Keep track of the number of frogs
    private int numFrogs = 0;

    // To display a grid
    private GridBagConstraints gbc;

    private Fly fly;
    private Frog [] frogs;
    private Square [][] squares = new Square [ROWS][COLS];

    // Next several variables handle user pressing
    // The arrow keys
    // DO NOT MODIFY
    private Action right = new AbstractAction("RIGHT")
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		updateWorld(EAST);
	    }
	};

    private Action left = new AbstractAction("LEFT")
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		updateWorld(WEST);
	    }
	};

    private Action up = new AbstractAction("UP")
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		updateWorld(NORTH);
	    }
	};

    private Action down = new AbstractAction("DOWN")
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		updateWorld(SOUTH);
	    }
	};
    
    public World(String fin)
    {
	// Associates key press handlers with the world
	getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
	getActionMap().put("RIGHT", right);
	
	getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");
	getActionMap().put("LEFT", left);
	
	getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
	getActionMap().put("UP", up);
	
	getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
	getActionMap().put("DOWN", down);

	// Initializes the GUI
	gbc = new GridBagConstraints();
	setLayout(new GridBagLayout());

	// Initialize variables for file processing
	File inFile = new File(fin);
	Scanner input = null;

	/**
	 *
	 * Open file for reading
	 *
	 */
	try 
	    {    
		input = new Scanner(inFile); } 
	catch (FileNotFoundException fnf)
	    {
		System.err.println("Input file not found");     
		System.exit(1); }
	/**
	 *
	 * Read first line and save value
	 * Initialize frogs array
	 *
	 */                
	String numbF = input.nextLine();
        int numFrogs = Integer.parseInt(numbF);
	System.out.println(numFrogs);
        frogs = new Frog [numFrogs];
	int countFrog = 0;

	for (int row = 0; row < ROWS; ++row)
	{
	    String line = input.nextLine();
	    System.out.println(line);
	    for (int col = 0; col < COLS; ++col)
	    {
		gbc.gridx = col;
		gbc.gridy = row;
		char symbol = line.charAt(col);
		Square sqr;
		
		if (symbol == 'h')
		    {sqr = new HomeSquare();}
		else if (symbol == '*')
		    {sqr = new Square();
			fly = new Fly(row,col);
			sqr.addFly(fly);
		    }
		else if (symbol == 'f')
		    {sqr = new Square();
		        frogs [countFrog]=new Frog(row,col);
			sqr.addFrog(frogs[countFrog]);
			//System.out.println(frogs[countFrog]); 
			countFrog ++;
			
		    }
		else if (symbol == 'd')
		    {sqr = new Square();
			frogs [countFrog] = new DartFrog(row,col);
			sqr.addFrog(new DartFrog(row,col));
			countFrog ++;
		       	//System.out.println(frogs[countFrog]);
		    }
		else
		    {sqr = new Square();}

	        // DO NOT MODIFY NEXT TWO LINES
		// Adds square to the GUI and
		// Adds square to the double array of squares
		// which you can use later on for moving fly/frogs
		add(sqr, gbc);
		squares[row][col] = sqr;
	    }
	}
	this.frogs = frogs;
	input.close();
    }

    public void updateWorld(int direction)
    {
	int[] flyLoc = fly.getLocation();
	int a = flyLoc[0];
	int b = flyLoc[1];//get location
 	Square sqr = squares [a][b];//find the square
	sqr.removeFly();
	fly.move(direction);
	flyLoc = fly.getLocation();
	sqr = squares [flyLoc[0]][flyLoc[1]];//find new square
	sqr.addFly(fly);
	if (sqr.flyIsHome())
	  {
		gameOver(false);
		System.exit(1);
	  }	

	/**
	 *
	 * FILL IN HERE
	 * Move the frogs part I
	 * 1. One-by-one get frog's location
	 * 2. Check if the frog can eat the fly.  If so call gameOverMethod
	 *    and System.exit
	 * 3. Otherwise remove frog from the square it is on
	 *
	 */

	for (int i =0;i<frogs.length;i++)
	    {int[] oldLoc =frogs[i].getLocation();
		Square sqrF = squares [oldLoc[0]][oldLoc[1]];
		if (frogs[i].eatsFly(fly.getLocation()))
		    {
			gameOver(true);
			System.exit(0);}
		else
		    {
			sqrF.removeFrog();}
	    }

	
	/**
	 *
	 * FILL IN HERE
	 * Move the frogs part II
	 * 1. Get frog's current location
	 * 2. Call the frog's move method and get new location
	 * 3. Try to add frog to square at new location
	 *    If successful check if frog can eat fly again
	 *    calling gameOver/System.exit if it can.
	 *    If unsuccesssful call frog's resetPosition and put
	 *    frog back on square it had been on
	 *
	 */

	for (int i =0;i<frogs.length;i++)
	    { 
		int[] oldLoc =frogs[i].getLocation();
		frogs[i].move();
		int []newLoc = frogs[i].getLocation();
		Square NsqrF = squares [newLoc[0]][newLoc[1]];
		if (NsqrF.addFrog(frogs[i])==false)
			{
			    frogs[i].resetPosition(oldLoc);
			    squares[oldLoc[0]][oldLoc[1]].addFrog(frogs[i]);
		    }
		else
		    {
			if (frogs[i].eatsFly(fly.getLocation()))
			    {
				gameOver(true);
				System.exit(0);
			    }
			else
			    {
				NsqrF.addFrog(frogs[i]);
			    }
		    }
	    }
    }
    

    // Method display a window to show player they won or lost
    // DO NOT MODIFY
    private void gameOver(boolean eaten)
    {
	JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
	
	if (eaten)
	    JOptionPane.showMessageDialog(frame,
					  "Oh no Mosca was eaten",
					  "Too bad. Try again",
					  JOptionPane.ERROR_MESSAGE);
	else
	    JOptionPane.showMessageDialog(frame,
					  "Yeah, Mosca made it home safely",
					  "Hooray",
					  JOptionPane.PLAIN_MESSAGE);
    }

    // DO NOT MODIFY
    public static void main(String [] args)
    {
	JFrame frame = new JFrame("Fly Away Home");
	World world = new World(args[0]);
	
	frame.getContentPane().add("Center", world);

	world.setFocusable(true);
	world.requestFocusInWindow();

	frame.setSize(600, 600);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}
