package yobo.ui;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe Job
 * Date: Feb 23, 2004
 * Time: 7:05:28 PM
 * To change this template use File | Settings | File Templates.
 */

import yobo.engine.Board;
import yobo.engine.Piece;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.*;


    class BoardFrame extends Applet
    {
        public BoardFrame() {};
        JFrame frame;
        Image[] image = new Image[12];
        Board board;
        int firstClick=-1;
        int s;

        public void init()
        {
          try
          {
            jbInit();
          }
          catch(Exception e)
          {
            e.printStackTrace();
          }

        }

        public static void main(String args[])
         {

            BoardFrame applet = new BoardFrame();
            applet.board = new yobo.engine.Board(applet);
            applet.frame = new JFrame();
            applet.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            applet.frame.getContentPane().add(applet, BorderLayout.CENTER);
            applet.frame.setTitle("Yobo Chess");
            applet.init();
            applet.start();
            applet.frame.setSize(600, 600);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = applet.frame.getSize();
            applet.frame.setLocation((d.width - frameSize.width) / 2, (d.height - frameSize.height) / 2);
            applet.frame.setVisible(true);

         }

        private void jbInit() throws Exception
        {
            File[] file = new File[12];

            addMouseListener(new java.awt.event.MouseAdapter()
                 {
                   public void mousePressed(MouseEvent e)
                   {
                       int click = (int)e.getPoint().getX() / s + (int)(e.getPoint().getY() / s) * 8;
                       // System.out.println("Inage Height " + s + "GetX " + e.getPoint().getX() + "GetY " + e.getPoint().getY() + " Clicked " + click);
                       if ((click < 85) && (click >=0))
                       {
                           if (firstClick == -1)
                               firstClick = click;
                           else if (click == firstClick)
                           {
                             firstClick = -1;
                           }
                           else
                           {
                               if (firstClick < 64)
                               {
                                    board.move(firstClick, click);
                                    firstClick = -1;


                                    // Create the thread supplying it with the runnable object
                                    Thread thread = new Thread(board);
                                    thread.setPriority(Thread.NORM_PRIORITY - 1);
                                    // Start the thread
                                    thread.start();
                               } else if (firstClick > 71)
                               {
                                   if (click < 64)
                                        Board.BoardArray[click] = Piece.getPiecebyID(firstClick-72);
                                   else
                                       firstClick = click;
                               } else
                                   firstClick = -1;
                           }
                       }
                       else
                       {
                        firstClick = -1;
                       }
                       repaint();
                   }
                 });


            Action action = new AbstractAction("Computer play") {
                 // This method is called when the button is pressed
                 public void actionPerformed(ActionEvent evt) {
                     board.run();
                     repaint();
                 }
             };

             // Create the button

             JButton button = new JButton(action);
             JTextArea nodes = new JTextArea();
             frame.getContentPane().add(button, BorderLayout.SOUTH);

                try {
                    // Read from a file
                    for (int i =0 ; i < 12; i++)
                    {
                          file[i] = new File( i + ".gif");
                          image[i] = ImageIO.read(file[i]);
                    }
                } catch(IOException e) {
                    System.out.println(e.toString());
                };

        }

        public void paint(Graphics g)
        {
            Graphics2D g2d = (Graphics2D)g;
            s = image[0].getHeight(null);

            for (int i = 0 ; i < 8 ; i++ )
                for (int j = 0 ; j < 8 ; j++ )
                {
                    if ((i*8+j)== firstClick)
                        g2d.setColor(Color.RED);
                    else {
                        if ( ((i +j) & 1) !=0 )
                            g2d.setColor(Color.GRAY);
                        else
                            g2d.setColor(Color.PINK);
                    }
                    g2d.fillRect(j*s, i*s, s, s);
                    if (board.BoardArray[i*8+j]!=null)
                        g.drawImage(image[board.BoardArray[i*8+j].imageIndex],j*s,i*s,this );
                }

            for (int i = 0; i < 13; i++)
            {
                if (i == firstClick - 72)
                    g2d.setColor(Color.RED);
                else {
                    if ( (i & 1) !=0 )
                        g2d.setColor(Color.GRAY);
                    else
                        g2d.setColor(Color.PINK);
                }
                g2d.fillRect(i*s, 9*s, s, s);
                if (i < 12)
                    g.drawImage(image[i],i*s,9*s,this );
            }
        }

   }

