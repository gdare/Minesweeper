import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    buttons = new MSButton [NUM_ROWS] [NUM_COLS];
    // make the manager
    Interactive.make( this );
    
    for (int i = 0; i < NUM_ROWS; i++){
        for (int k = 0; k < NUM_COLS; k++){
            buttons[i][k] = new MSButton(i,k);
        }
    }
    bombs = new ArrayList <MSButton> ();
    //declare and initialize buttons
    for (int i = 0; i < 20; i++){
        setBombs();
    }
    
}
public void setBombs()
{
    int randomRow = (int)(Math.random() * 20);
    int randomCol = (int)(Math.random() * 20);
    if (!bombs.contains(buttons[randomRow][randomCol])){
        bombs.add(buttons[randomRow][randomCol]);
    }
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    int count = 0;
    for (int i=0; i<bombs.size (); i++){
        if (bombs.get(i).isMarked()){
        count++;
        }
    }
    if (count == bombs.size()){
        return true;
    }
    return false;
}
public void displayLosingMessage()
{
  for (int i=0; i<bombs.size (); i++)
  {
    bombs.get(i).isClicked();
  }
  buttons[7][6].setLabel("Y");
  buttons[7][7].setLabel("O");
  buttons[7][8].setLabel("U");
  buttons[7][9].setLabel(" ");
  buttons[7][10].setLabel("L");
  buttons[7][11].setLabel("O");
  buttons[7][12].setLabel("S");
  buttons[7][13].setLabel("E");
}
public void displayWinningMessage()
{
  buttons[7][6].setLabel("Y");
  buttons[7][7].setLabel("O");
  buttons[7][8].setLabel("U");
  buttons[7][9].setLabel(" ");
  buttons[7][10].setLabel("W");
  buttons[7][11].setLabel("I");
  buttons[7][12].setLabel("N");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
            
        clicked = true;
        
        if (mouseButton == RIGHT){
            marked = true;
        }
        else if (bombs.contains(this)){
            displayLosingMessage();
        }
        else if (countBombs(r,c) > 0){
            label = str(countBombs(r,c));
        }
        else{
            if (isValid(r,c-1) && buttons[r][c-1].clicked == false){
                buttons[r][c-1].mousePressed();
            }
            if (isValid(r,c+1) && buttons[r][c+1].clicked == false){
                buttons[r][c+1].mousePressed();
            }
            if (isValid(r-1,c) && buttons[r-1][c].clicked == false){
                buttons[r-1][c].mousePressed();
            }
            if (isValid(r+1,c) && buttons[r+1][c].clicked == false){
                buttons[r+1][c].mousePressed();
            }



            if (isValid(r-1,c-1) && buttons[r-1][c-1].clicked == false){
                buttons[r-1][c-1].mousePressed();
            }
            if (isValid(r+1,c-1) && buttons[r+1][c-1].clicked == false){
                buttons[r+1][c-1].mousePressed();
            }
            if (isValid(r-1,c+1) && buttons[r-1][c+1].clicked == false){
                buttons[r-1][c+1].mousePressed();
            }
            if (isValid(r+1,c+1) && buttons[r+1][c+1].clicked == false){
                buttons[r+1][c+1].mousePressed();
            }
        }
        
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
            fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        return (r>=0 && r<NUM_ROWS) && (c>=0 && c<NUM_COLS);
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        for (int grid1 = -1; grid1 < 2; grid1++){
            for (int grid2 = -1; grid2 < 2; grid2++){
                if(isValid(row + grid1, col + grid2) == true){
                    if(bombs.contains(buttons[row + grid1][col + grid2])){
                        numBombs++;
                    }
                }
            }
        }
        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
