import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
public class FirstProject extends JPanel 
{
   //VARIABLES
   String file = "GameFile.txt";
   int [][]GameBoard;
   int StartX;
   int StartY;
   int NumRows;
   int NumColumns;
   int PointX;
   int PointY;
   int PlayerPosY;
   int PlayerPosX;
   int gravityforce = 1;
   int jumpforce = 7;
   int timerticks = 1;
   
   boolean up;
   boolean down;
   boolean left;
   boolean right;
   boolean jumping = true; //makes it so you can only jump once when you push W
   boolean onground;
   
   //constructor
   public FirstProject()
   {
      filescanner(); //first off, scans file
      Timer time = new Timer(10, new TimeListener()); //creates time
      time.start();
      addKeyListener(new KeyEventDemo()); //adds key listeners
      setPreferredSize(new Dimension(800,600)); //sets size
      setFocusable(true);
   }
   
   //paints gamefile and player
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g.setColor(Color.BLACK);
      g.fillRect(0,0,820,620);
      g.setColor(Color.GRAY);
      g.fillRect(10,10,800,600);
      PointX = 10;
      PointY = 10;
      
      Color BackgroundColor = new Color(215,255,255);
      Color WallColor = new Color(0,0,128);
      Color WinBoxColor = new Color(255,213,0);
      Color PlayerColor = new Color(255,128,128);
      
      for (int x=0; x<NumRows; x++) //prints out all rows and columns of game file
      {
         for (int y=0; y<NumColumns; y++) 
         {
            if(GameBoard[x][y] == 0) //checks if number in gamefile array is a 1, 2, or 0
            {
               g.setColor(BackgroundColor);
               g.fillRect(PointX,PointY,25,25);
            }
            else if(GameBoard[x][y] == 1)
            {
               g.setColor(WallColor);
               g.fillRect(PointX,PointY,25,25);
            }
            else if(GameBoard[x][y] == 2)
            {
               g.setColor(WinBoxColor);
               g.fillRect(PointX,PointY,25,25);
            }
            PointX += 25;
         }
         PointY += 25;
         PointX = 10;
      }
      g.setColor(PlayerColor);
      g.fillRect(10+(PlayerPosX*1),10+(PlayerPosY*1),25,25); //creates player
      repaint();
   }
      
   public void MovingAndCollide(Integer direction, Integer speed) //checks if the player collides, if not, moves them by "speed" in whatever direction is pressed
   {              
      if(direction==1 ) //Up  
      {
         if (GameBoard[(((PlayerPosY-speed)/25)+0)][PlayerPosX/25] != 1 && GameBoard[(((PlayerPosY-speed)/25)+0)][(PlayerPosX+24)/25] != 1  &&  GameBoard[(((PlayerPosY+24-speed)/25)+0)][PlayerPosX/25] != 1 && GameBoard[(((PlayerPosY+24-speed)/25)+0)][(PlayerPosX+24)/25] != 1)  // checks all directions since if W is held while moving then it won't scan bottom when falling
         {
               PlayerPosY -= speed;
         }
      }
      if(direction==2)  // Down (if down doesn't work, you are on the ground)
      {
         onground = false;
         if (GameBoard[(((PlayerPosY+25+speed-1)/25))][PlayerPosX/25] != 1 && GameBoard[(((PlayerPosY+25+speed-1)/25)+0)][(PlayerPosX+24)/25] != 1  )   // checks both left and right top of player
         {
            PlayerPosY += speed;
         }
         else
         {
            up = false; //sets up to false so it won't repeat
            onground = true; //You are on the ground since you can't go down
         }
      }
      if(direction==3)  // Right 
      {
         if (GameBoard[(PlayerPosY/25)][((((PlayerPosX+25+speed-1)/25)+0))] != 1 && GameBoard[(((PlayerPosY+24))/25)][((((PlayerPosX+25+speed-1)/25)+0))] != 1 )  // check both top and bottom right edge
         {
            PlayerPosX += speed;
         }
      }             
      if(direction==4)  //Left
      {
         if (GameBoard[(PlayerPosY/25)][((((PlayerPosX-speed)/25)+0))] != 1 && GameBoard[((PlayerPosY+24)/25)][((((PlayerPosX-speed)/25)+0))] != 1 )    // checks both top and bottom left edge
         {
            PlayerPosX -= speed;
         }
      }  
      repaint(); //repaints
   }
   
      
   public class KeyEventDemo  implements KeyListener //checks keyboard inputs
   {
      public void keyTyped(KeyEvent e){}
      public void keyReleased(KeyEvent e)
      {
         if(e.getKeyCode() == KeyEvent.VK_W)
         {
            up=false;
            jumping = true;
         }
         if(e.getKeyCode() == KeyEvent.VK_S)
         {
            down =false;
         }
         if(e.getKeyCode() == KeyEvent.VK_A)
         {
            left = false;
         }
         if(e.getKeyCode() == KeyEvent.VK_D)
         {
            right = false;
         }
      }
      public void keyPressed(KeyEvent e) //when a key is pressed
      {
         if(e.getKeyCode() == KeyEvent.VK_W)
         {
            if(onground && jumping) //makes sure it is on the ground / sets jump height / makes sure it can only jump once when W is held
            {
               up=true;
               jumpforce = 10; // how high it can jump
               jumping = false;
            }
         }
         if(e.getKeyCode() == KeyEvent.VK_S)
         {
            down =true;
         }
         if(e.getKeyCode() == KeyEvent.VK_A)
         {
            left = true;
         }
         if(e.getKeyCode() == KeyEvent.VK_D)
         {
            right = true;
         }
      }
   }
   
   
   public class TimeListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)//what happens when a key is pressed
      {
         int speed = 1;    //speed of player
         timerticks +=  1; //adds one to timerticks everytime this is active
         
         if (jumpforce >= 0)                
         { 
            jumpforce -= 1; 
         }
            
         if (timerticks == 10 || timerticks == 20 ) //every 10 timer tics it will increase the rate it falls
         {
            if (onground != true )
            { 
               gravityforce += 1.5; //rate it increases
            }
         }
         
         if (timerticks == 20 ) //every 20 timer tics sets it back to 1
         {
            timerticks = 1; 
         }
               
         if (gravityforce > 7) //max falling velocity of the player = 7
         { 
            gravityforce = 7; 
         }

         if (onground) //if its on the ground then resets gravityforce to 1
         { 
            gravityforce = 1; 
         }            
         
         for (int x=0; x<speed+1; x++) //does keypress/timer events in sets of 1 by "speed" amount of times. Also will assign each direction (up right down left) a number 1-4 that will be called in the MovingAndCollide method
         {
            if (up) 
            {
               MovingAndCollide(1, jumpforce);
            }
//             if (down) //only used for testing. Got rid of it since you should not be able to press S to go down faster
//                {
//                   MovingAndCollide(2, 1);
//                }
            if (right)
               {
                  MovingAndCollide(3, 1);
               }
            if (left)
               {
                  MovingAndCollide(4, 1);
               }
         }              
         
         //This is gravity acting on the player
         MovingAndCollide(2, gravityforce);

         // checks to see if you won
         if (GameBoard[PlayerPosY/25][PlayerPosX/25] == 2 || GameBoard[PlayerPosY/25][(PlayerPosX+24)/25] == 2   ||   GameBoard[(PlayerPosY+24)/25][PlayerPosX/25] == 2 || GameBoard[(PlayerPosY+24)/25][(PlayerPosX+24)/25] == 2) //checks to see if any side of the player hits the win box
         {
            JOptionPane.showMessageDialog(null, "You Win!"); //creates panel saying you win
            System.exit(1);
         }
         repaint(); //repaints
      }
   }
      
   public void filescanner() //Scans the game file
   {
      try //checks if file exists, if not then prints FNFE
      {
         Scanner scan = new Scanner(new File(file));
         StartY = scan.nextInt();//scans first few variables
         StartX = scan.nextInt();
         
         PlayerPosX = StartX*25; //where the player starts
         PlayerPosY = StartY*25;
         
         NumRows = scan.nextInt(); //# of rows and columns in gameboard file
         NumColumns = scan.nextInt();
         
         GameBoard = new int[NumRows][NumColumns];
         for (int x=0; x<NumRows; x++) //puts all variables in an array, uses this array to check if player hit a wall or not. Only checks if the next value hits the wall
         {
            for (int y=0; y<NumColumns; y++)
            {
               GameBoard[x][y] = scan.nextInt();
            }
         }
      }
      catch(FileNotFoundException fnfe) //catches fnfe
      {
         System.out.println("File Not Found");
      }
   }
   
   
   public static void main(String args[]) //main
   {
      JFrame frame = new JFrame("FirstProject");
      frame.setSize(832, 657);
      frame.setContentPane(new FirstProject());  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);    
   }
}