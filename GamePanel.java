import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import java.io.*;

public class GamePanel extends JPanel implements ActionListener{
    @Serial
    private static final long serialVersionUID = 3876286449627408974L;
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (WIDTH*HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    int x = 0;
    int y = 0;
    long[][] blockPos;
    Random random;
    Timer timer;

    int rows = HEIGHT/UNIT_SIZE;
    int colls = WIDTH/UNIT_SIZE;
    int selected = 1;
    String command;

    final JFrame parent = new JFrame();
    //JButton button = new JButton();



    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public static void main(String[] args) {

    }
    public void startGame() {
        timer = new Timer(DELAY,this);
        timer.start();
        blockPos = new long[rows][colls];
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        Color[] blockColors = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta, Color.white, Color.gray, Color.black};
        for(int i = 0; i<rows;i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, WIDTH, i*UNIT_SIZE);
        }
        for(int i = 0; i<rows; i++) {
            for(int j = 0; j<colls; j++) {
                for(int clrCheck = 0; clrCheck<blockColors.length; clrCheck++){
                    if(blockPos[i][j] == clrCheck+1){
                        g.setColor(blockColors[clrCheck]);
                        g.fillRect(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
        }
        parseCommand();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x*UNIT_SIZE, y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }
    public void paintBlock(int block, int row, int coll) {
        if (blockPos[row][coll] != block) {
            blockPos[row][coll] = block;
        } else if (blockPos[row][coll] == block){
            blockPos[row][coll] = 0;
        }
    }
    public void parseCommand() {
        if(command != null) {
            if(Objects.equals(command, "clear") || Objects.equals(command, "clr")) {
                for(int i = 0; i < rows;i++) {
                    for(int j = 0; j < colls; j++) {
                        blockPos[j][i] = 0;
                    }
                }
                command = null;
            }
            if(Objects.equals(command, "save")) {
                try {
                    FileWriter saveFile = new FileWriter("save.txt");
                    for(int i = 0; i < rows;i++) {
                        for(int j = 0; j < colls; j++) {
                            saveFile.write(Long.toString(blockPos[j][i]));
                        }
                        saveFile.write("\n");
                    }
                    saveFile.close();
                    System.out.println("Successfully saved map to the file.");

                } catch (IOException e){
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                command = null;
            }
            if(Objects.equals(command, "load")) {
                try {
                    File loadFile = new File("load.txt");
                    Scanner myReader = new Scanner(loadFile);
                    while (myReader.hasNextLine()) {
                        for(int i = 0; i < rows;i++) {
                            String data = myReader.nextLine();
                            for(int j = 0; j < colls; j++) {
                                blockPos[j][i] = Long.parseLong(data.substring(j, j+1));
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                command = null;
                System.out.println("Printed file data");
            }
            command = null;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                    if (x > 0) {x -= 1;}
                }
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                    if (x < colls - 1) {x += 1;}
                }
                case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                    if (y > 0) {y -= 1;}
                }
                case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                    if (y < rows - 1) {y += 1;}
                }
                case KeyEvent.VK_SPACE -> {
                    for (int i = 0; i < rows; i++) {
                        if (i == x) {
                            for (int j = 0; j < colls; j++) {
                                if (j == y) {
                                    paintBlock(selected, i, j);
                                }
                            }
                        }
                    }
                }
                case KeyEvent.VK_1 -> {
                    selected = 1;
                    System.out.println(command);
                }
                case KeyEvent.VK_2 -> selected = 2;
                case KeyEvent.VK_3 -> selected = 3;
                case KeyEvent.VK_4 -> selected = 4;
                case KeyEvent.VK_5 -> selected = 5;
                case KeyEvent.VK_6 -> selected = 6;
                case KeyEvent.VK_7 -> selected = 7;
                case KeyEvent.VK_8 -> selected = 8;
                case KeyEvent.VK_9 -> selected = 9;
                case KeyEvent.VK_ENTER -> {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < colls; j++) {
                            System.out.print(blockPos[j][i]);
                        }
                        System.out.println();
                    }
                    System.out.println("\n");
                }
                case KeyEvent.VK_CONTROL -> command = JOptionPane.showInputDialog(parent, "Enter Command", null);
            }
        }
    }
}
