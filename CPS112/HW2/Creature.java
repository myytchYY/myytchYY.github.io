import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Fly
{
    // Location of the fly
    private int row;
    private int col;

    // The image of the fly
    private BufferedImage image;
    
    public Fly(int r, int c)
    {
	// FILL IN HERE
	// INITIALIZE FIRST TWO INSTANCE VARIABLES
	row = r;
	col = c;

	// Code below initializes image instance variable
	// Do not modify
	try
	{
	    image = ImageIO.read(new File("fly.jpg"));
	}
	catch (IOException ioe)
	{
	    System.out.println("Unable to read file: fly.jpg");
	    System.exit(0);
	}
	
    }

    public BufferedImage getImage()
    {
	// FILL IN HERE
	return image;
    }

    public int [] getLocation()
    {
	// FILL IN HERE
	int[] loc = {row,col};
	return loc;
    }
    
    public void move(int dir)
    {
	// FILL IN HERE
	if (dir == 0&&row!= 0)
	    row -= 1;
	else if (dir == 1&&row!= 9)
	    row+= 1;
	else if (dir == 2&&col!=9)
	    col+=1;
	else if (dir == 3&&col!= 0)
	    col-=1;
    }
}

class Frog
{
    protected Random rand = new Random();

    // Location of the frog
    protected int row;
    protected int col;

    // Direction the frog is heading
    protected int direction;

    // Image of the frog
    protected BufferedImage image;


    
    public Frog(int r, int c)
    {
	// FILL IN HERE
	// INITIALIZE INSTANCE VARIABLES
	// SET DIRECTION RANDOMLY to VALUE 0-3 INCLUSIVE
	row = r;
	col = c;
	direction = rand.nextInt(4);

	try
	    {
		image = ImageIO.read(new File("frog.jpg"));
	    }
	catch (IOException ioe)
	    {
		System.out.println("Unable to read file: frog.jpg");
		System.exit(0);
	    }
    }

    public BufferedImage getImage()
    {
	// FILL IN HERE
	return image;
    }
    
    public int [] getLocation()
    {
	// FILL IN HERE
	int[] loc ={row,col};
	return loc;
    }
    
    private void changeDirection()
    {
	// Helper function to randomly change frog's direction
	int dir = rand.nextInt(4); 
	while (dir == direction)
	    dir = rand.nextInt(4);
	direction = dir;
    }

    public void move()
    {	
	// FILL IN HERE
	if (direction == 0&&row!= 0)
	    {row -= 1;}
	else if (direction == 1&&row!= 9)
	    {row += 1;}
	else if (direction == 2&&col!=9)
	    {col += 1;}
	else if (direction == 3&&col!= 0)
	    {col -= 1;}
	else if (row ==0||row ==9||col==0||col==9)
	    {direction = rand.nextInt(4);
		move();}
    }

    public boolean eatsFly(int [] flyLoc)
    {
	// FILL IN HERE
	if ((Math.abs(flyLoc[0]-row)==1 && flyLoc[1]==col)||(Math.abs(flyLoc[1]-col)==1&&flyLoc[0]==row))
	    {return true;}
	else
	    {return false;}
    }
	
    public void resetPosition(int [] loc)
    {
	// Meant as a helper function to put frog
	// at some specific location
	row = loc[0];
	col = loc[1];	
    }
}

class DartFrog extends Frog
{
    protected int direction = rand.nextInt(4);
    public DartFrog(int r,int c)
    {
	super(r,c);

	try
	    {
		image = ImageIO.read(new File("dartfrog.jpg"));
	    }
	catch (IOException ioe)
	    {
		System.out.println("Unable to read file: dartfrog.jpg");
		System.exit(0);
	    }
    }

    @Override
     public void move()
    {	
	// FILL IN HERE
        row = rand.nextInt(10);
	col = rand.nextInt(10);
    }

    @Override
    public boolean eatsFly(int [] flyLoc)
    {
	// FILL IN HERE
	if (row==flyLoc[0] && col==flyLoc[1])
	    {return true;}
	else
	    {return false;}
    }

}
