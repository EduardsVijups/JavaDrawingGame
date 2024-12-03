import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
   private static final long serialVersionUID = 3876286449627408974L;
   static final int WIDTH = 600;
   static final int HEIGHT = 600;
   static final int UNIT_SIZE = 25;
   static final int GAME_UNITS = 14400;
   static final int DELAY = 75;
   int x = 0;
   int y = 0;
   long[][] blockPos;
   Random random = new Random();
   Timer timer;
   int blocks = 0;
   int rows = 24;
   int colls = 24;
   int selected = 1;
   String command;
   String clear = "clear";
   final JFrame parent = new JFrame();
   JButton button = new JButton();

   GamePanel() {
      this.setPreferredSize(new Dimension(600, 600));
      this.setBackground(Color.black);
      this.setFocusable(true);
      this.addKeyListener(new GamePanel.MyKeyAdapter());
      this.startGame();
   }

   public static void main(String[] args) {
   }

   public void startGame() {
      this.timer = new Timer(75, this);
      this.timer.start();
      this.blockPos = new long[this.rows][this.colls];
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      this.draw(g);
   }

   public void draw(Graphics g) {
      int i;
      for(i = 0; i < this.rows; ++i) {
         g.drawLine(i * 25, 0, i * 25, 600);
         g.drawLine(0, i * 25, 600, i * 25);
      }

      for(i = 0; i < this.rows; ++i) {
         for(int j = 0; j < this.colls; ++j) {
            if (this.blockPos[i][j] == 1L) {
               g.setColor(Color.red);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 2L) {
               g.setColor(Color.orange);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 3L) {
               g.setColor(Color.yellow);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 4L) {
               g.setColor(Color.green);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 5L) {
               g.setColor(Color.blue);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 6L) {
               g.setColor(Color.magenta);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 7L) {
               g.setColor(Color.white);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 8L) {
               g.setColor(Color.gray);
               g.fillRect(i * 25, j * 25, 25, 25);
            }

            if (this.blockPos[i][j] == 9L) {
               g.setColor(Color.black);
               g.fillRect(i * 25, j * 25, 25, 25);
            }
         }
      }

      this.parseCommand();
      g.setColor(Color.DARK_GRAY);
      g.fillRect(this.x * 25, this.y * 25, 25, 25);
   }

   public void paintBlock(int block, int row, int coll) {
      if (this.blockPos[row][coll] != (long)block) {
         this.blockPos[row][coll] = (long)block;
      } else if (this.blockPos[row][coll] == (long)block) {
         this.blockPos[row][coll] = 0L;
      }

   }

   public void parseCommand() {
      if (this.command != null) {
         int i;
         if (this.command.equals("clear") || this.command.equals("clr")) {
            int i = 0;

            while(true) {
               if (i >= this.rows) {
                  this.command = null;
                  break;
               }

               for(i = 0; i < this.colls; ++i) {
                  this.blockPos[i][i] = 0L;
               }

               ++i;
            }
         }

         int i;
         if (this.command.equals("save")) {
            try {
               FileWriter saveFile = new FileWriter("save.txt");

               for(i = 0; i < this.rows; ++i) {
                  for(i = 0; i < this.colls; ++i) {
                     saveFile.write(Long.toString(this.blockPos[i][i]));
                  }

                  saveFile.write("\n");
               }

               saveFile.close();
               System.out.println("Successfully saved map to the file.");
            } catch (IOException var7) {
               System.out.println("An error occurred.");
               var7.printStackTrace();
            }

            this.command = null;
         }

         if (this.command.equals("load")) {
            try {
               File loadFile = new File("load.txt");
               Scanner myReader = new Scanner(loadFile);

               while(myReader.hasNextLine()) {
                  for(i = 0; i < this.rows; ++i) {
                     String data = myReader.nextLine();

                     for(int j = 0; j < this.colls; ++j) {
                        this.blockPos[j][i] = Long.parseLong(data.substring(j, j + 1));
                     }
                  }
               }
            } catch (FileNotFoundException var6) {
               var6.printStackTrace();
            }

            this.command = null;
            System.out.println("Printed file data");
         }

         this.command = null;
      }

   }

   public void actionPerformed(ActionEvent e) {
      this.repaint();
   }

   public class MyKeyAdapter extends KeyAdapter {
      public void keyPressed(KeyEvent e) {
         int i;
         int j;
         switch(e.getKeyCode()) {
         case 10:
            for(i = 0; i < GamePanel.this.rows; ++i) {
               for(j = 0; j < GamePanel.this.colls; ++j) {
                  System.out.print(GamePanel.this.blockPos[j][i]);
               }

               System.out.println();
            }

            System.out.println("\n");
            break;
         case 17:
            GamePanel.this.command = JOptionPane.showInputDialog(GamePanel.this.parent, "Enter Command", (Object)null);
            break;
         case 32:
            for(i = 0; i < GamePanel.this.rows; ++i) {
               if (i == GamePanel.this.x) {
                  for(j = 0; j < GamePanel.this.colls; ++j) {
                     if (j == GamePanel.this.y) {
                        GamePanel.this.paintBlock(GamePanel.this.selected, i, j);
                     }
                  }
               }
            }

            return;
         case 37:
            if (GamePanel.this.x > 0) {
               --GamePanel.this.x;
            }
            break;
         case 38:
            if (GamePanel.this.y > 0) {
               --GamePanel.this.y;
            }
            break;
         case 39:
            if (GamePanel.this.x < GamePanel.this.colls - 1) {
               ++GamePanel.this.x;
            }
            break;
         case 40:
            if (GamePanel.this.y < GamePanel.this.rows - 1) {
               ++GamePanel.this.y;
            }
            break;
         case 49:
            GamePanel.this.selected = 1;
            System.out.println(GamePanel.this.command);
            break;
         case 50:
            GamePanel.this.selected = 2;
            break;
         case 51:
            GamePanel.this.selected = 3;
            break;
         case 52:
            GamePanel.this.selected = 4;
            break;
         case 53:
            GamePanel.this.selected = 5;
            break;
         case 54:
            GamePanel.this.selected = 6;
            break;
         case 55:
            GamePanel.this.selected = 7;
            break;
         case 56:
            GamePanel.this.selected = 8;
            break;
         case 57:
            GamePanel.this.selected = 9;
            break;
         case 65:
            if (GamePanel.this.x > 0) {
               --GamePanel.this.x;
            }
            break;
         case 68:
            if (GamePanel.this.x < GamePanel.this.colls - 1) {
               ++GamePanel.this.x;
            }
            break;
         case 83:
            if (GamePanel.this.y < GamePanel.this.rows - 1) {
               ++GamePanel.this.y;
            }
            break;
         case 87:
            if (GamePanel.this.y > 0) {
               --GamePanel.this.y;
            }
         }

      }
   }
}
