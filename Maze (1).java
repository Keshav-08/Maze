import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MazeProgram extends JPanel implements KeyListener {
    private JFrame frame;
    private int playerX, playerY;
    private int playerDirection;
    private int cellSize = 20;
    private String[][] maze;
    private int cellSpacing = 50;
    private BufferedImage rightWalk, rightStand, monster3D, monster2D;
    private int frameCount;
    private ArrayList<Wall> walls;
    private ArrayList<FrontWall> frontWalls;
    private boolean is3DMode = false;
    private Monster monster;
    private Font gameFont = new Font("Comic Sans MS", Font.BOLD, 40);
    private boolean isGameOver = false;

    public MazeProgram() {
        initializeFrame();
        loadImages();
        playerX = 1;
        playerY = 1;
        playerDirection = 0;
        initializeMaze();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame();
        frame.add(this);
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
    }

    private void loadImages() {
        try {
            
            rightWalk = ImageIO.read(new File("rightWalk.png"));
            rightStand = ImageIO.read(new File("rightStand.png"));
            monster3D = ImageIO.read(new File("monster3D.png"));
            monster2D = ImageIO.read(new File("monster2D.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeMaze() {
        try {
            File file = new File("maze.txt");
            BufferedReader input = new BufferedReader(new FileReader(file));
            String st;
            int row = 0;
            maze = new String[41][];
            while ((st = input.readLine()) != null) {
                String[] rowOfWalls = st.split("");
                maze[row] = rowOfWalls;
                setPlayerStartPosition(st, row);
                setMonsterPosition(st, row);
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerStartPosition(String st, int row) {
        if (st.indexOf("N") >= 0) {
            playerDirection = 0;
            playerX = st.indexOf("N");
            playerY = row;
            maze[playerY][playerX] = " ";
        } else if (st.indexOf("E") >= 0) {
            playerDirection = 1;
            playerX = st.indexOf("E");
            playerY = row;
            maze[playerY][playerX] = " ";
        } else if (st.indexOf("W") >= 0) {
            playerDirection = 3;
            playerX = st.indexOf("W");
            playerY = row;
            maze[playerY][playerX] = " ";
        } else if (st.indexOf("S") >= 0) {
            playerDirection = 2;
            playerX = st.indexOf("S");
            playerY = row;
            maze[playerY][playerX] = " ";
        }
    }

    private void setMonsterPosition(String st, int row) {
        if (st.indexOf("M") >= 0) {
            monster = new Monster(st.indexOf("M"), row);
            maze[playerY][playerX] = " ";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g2.setStroke(new BasicStroke(2));
        if (!is3DMode) {
            rightWalk = resizeImage(rightWalk, cellSize, cellSize);
            rightStand = resizeImage(rightStand, cellSize, cellSize);
            monster2D = resizeImage(monster2D, cellSize, cellSize);
            draw2DMaze(g);
        }
        if (is3DMode) {
            monster3D = resizeImage(monster3D, cellSize, cellSize);
            draw3DMaze(g);
        }
        drawGameInformation(g);
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledVersion = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledVersion.createGraphics();
        g2.drawImage(temp, 0, 0, null);
        g2.dispose();
        return scaledVersion;
    }

    private void draw2DMaze(Graphics g) {
        drawMazeWalls(g, cellSize, 40);
        g.drawImage(monster2D, monster.getX() * cellSize + 40, monster.getY() * cellSize + 40, this);
        drawPlayer(g, cellSize, playerX, playerY, 40);
    }

    private void draw3DMaze(Graphics g) {
        drawMazeWalls(g, cellSpacing, 40);
        g.drawImage(monster3D, monster.getX() * cellSpacing + 40, monster.getY() * cellSpacing + 40, this);
    }

    private void drawMazeWalls(Graphics g, int tileSize, int offset) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j].equals("#")) {
                    g.setColor(new Color(startR, startG, startB));
                    g.fillRect(j * tileSize + offset, i * tileSize + offset, tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * tileSize + offset, i * tileSize + offset, tileSize, tileSize);
                }
            }
        }
    }

    private void drawPlayer(Graphics g, int tileSize, int playerX, int playerY, int offset) {
        if (playerDirection == 0) {
            if (frameCount % 2 == 0)
                g.drawImage(rightWalk, playerX * tileSize + offset, playerY * tileSize + offset, this);
            else
                g.drawImage(rightStand, playerX * tileSize + offset, playerY * tileSize + offset, this);
        } else if (playerDirection == 1) {
            if (frameCount % 2 == 0)
                g.drawImage(rightWalk, playerX * tileSize + offset, playerY * tileSize + offset, this);
            else
                g.drawImage(rightStand, playerX * tileSize + offset, playerY * tileSize + offset, this);
        } else if (playerDirection == 2) {
            if (frameCount % 2 == 0)
                g.drawImage(rightWalk, playerX * tileSize + offset, playerY * tileSize + offset, this);
            else
                g.drawImage(rightStand, playerX * tileSize + offset, playerY * tileSize + offset, this);
        } else if (playerDirection == 3) {
           if (frameCount % 2 == 0)
                g.drawImage(rightWalk, playerX * tileSize + offset, playerY * tileSize + offset, this);
            else
                g.drawImage(rightStand, playerX * tileSize + offset, playerY * tileSize + offset, this);
        }
    }

    private void drawGameInformation(Graphics g) {
        g.setFont(gameFont);
        if (isGameOver)
            g.setColor(Color.RED);
        else
            g.setColor(Color.WHITE);
        g.drawString("Maze Game", 40, 900);

        if (!isGameOver)
            g.drawString("Your goal is to reach the end of the maze", 40, 980);

        if (!isGameOver)
            g.drawString("There is a monster in the maze trying to catch you!", 40, 1060);

        if (!isGameOver)
            g.drawString("Avoid the monster and find your way out!", 40, 1140);

        if (isGameOver)
            g.setColor(Color.RED);
        else
            g.setColor(Color.WHITE);
        g.drawString("You Win!", 1800, 900);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int oldPlayerX = playerX;
        int oldPlayerY = playerY;

        if (!isGameOver) {
            handlePlayerMovement(e);
            handleMonsterMovement();
            frameCount++;
        }

        handleGameLogic(e, oldPlayerX, oldPlayerY);
        repaint();
    }

    private void handlePlayerMovement(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && playerY - 1 >= 0 && !maze[playerY - 1][playerX].equals("#")) {
            playerY--;
            playerDirection = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && playerY + 1 < maze.length && !maze[playerY + 1][playerX].equals("#")) {
            playerY++;
            playerDirection = 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX + 1 < maze[0].length && !maze[playerY][playerX + 1].equals("#")) {
            playerX++;
            playerDirection = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX - 1 >= 0 && !maze[playerY][playerX - 1].equals("#")) {
            playerX--;
            playerDirection = 3;
        }
    }

    private void handleMonsterMovement() {
        if (monster.getX() > playerX) {
            if (playerY < monster.getY() + 3 && playerY > monster.getY() - 3) {
                monster.move("left");
            }
        }
        if (monster.getX() < playerX) {
            if (playerY < monster.getY() + 3 && playerY > monster.getY() - 3) {
                monster.move("right");
            }
        }
        if (monster.getY() > playerY) {
            if (playerX < monster.getX() + 3 && playerX > monster.getX() - 3) {
                monster.move("up");
            }
        }
        if (monster.getY() < playerY) {
            if (playerX < monster.getX() + 3 && playerX > monster.getX() - 3) {
                monster.move("down");
            }
        }
    }

    private void handleGameLogic(KeyEvent e, int oldPlayerX, int oldPlayerY) {
        if (monster.getX() > playerX) {
            if (playerY < monster.getY() + 3 && playerY > monster.getY() - 3) {
                monster.move("left");
            }
        }
        if (monster.getX() < playerX) {
            if (playerY < monster.getY() + 3 and playerY > monster.getY() - 3) {
                monster.move("right");
            }
        }
        if (monster.getY() > playerY) {
            if (playerX < monster.getX() + 3 && playerX > monster.getX() - 3) {
                monster.move("up");
            }
        }
        if (monster.getY() < playerY) {
            if (playerX < monster.getX() + 3 && playerX > monster.getX() - 3) {
                monster.move("down");
            }
        }

        if (maze[playerY][playerX].equals("$")) {
            isGameOver = true;
        }
        if (isGameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            playerX = 1;
            playerY = 1;
            playerDirection = 0;
            monster = new Monster(monster.getStartX(), monster.getStartY());
            initializeMaze();
            frameCount = 0;
            isGameOver = false;
        }

        if (oldPlayerX != playerX || oldPlayerY != playerY)
            moveWalls();
    }

    private void moveWalls() {
        for (Wall w : walls) {
            w.move();
        }
        for (FrontWall w : frontWalls) {
            w.move();
        }
    }

    public void paintWall(Wall wall, Graphics g) {
        g.setColor(wall.getColor());
        g.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
    }

    public void paintFrontWall(FrontWall wall, Graphics g) {
        g.setColor(wall.getColor());
        g.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
    }

    public void draw() {
        paintComponent(this.getGraphics());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MazeProgram();
        });
    }
}