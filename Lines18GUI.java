// for the use of Random, ArrayList
import java.util.*;

// for the use of font, Color, Graphics
import java.awt.*;

// for extending JComponent
import javax.swing.*;

public class Lines18GUI extends JComponent {
    
    Graphics graphics;
//    width, height    
    int w, h;
//    left-most, top-most corner of the GUI
    int xZero, yZero;
//    current cell
    int xCurrCell, yCurrCell;
//    the underlying grid, gridSize, score
    int grid[][];
    int gridSize, score, fontSize;
//    colors
    int numCols;
    ArrayList<Color> colSet;
    Color base, back, white;

// bake
    
    boolean gameOver;
    
//    for asking and taking a username
    String username;
    boolean userBar;
    
//    for HighScoreList
    boolean showHighScore;
    HighScoreList highScoreList;
    
//    future Balls
    boolean showFutureColor, showFutureLocation;
    Point[] futureBalls;
      int[] futureCols;
      
//      highlightinh
    Highlighter highlighter;
    Highlighter lastBlown;

    public Lines18GUI() {
        
        showFutureColor    = false;
        showFutureLocation = false;
        showHighScore      = false;
        
        gameOver = false;
        userBar  = false;
        
        username = "";
        
        xCurrCell = -1;
        yCurrCell = -1;
        
    }

    public void paintComponent(Graphics g) {
        
        graphics = g;
        
        drawBackground();
       
        drawFuture();
       
        drawBalls();
        drawHighlight();
        drawCurr();
        
        drawPanel();        
        
        drawHighScore();
        drawUsernameBar();        
        
    }
   
//    setters    
    public void setDim(int xZero, int yZero) {
          
        this.xZero = xZero;
        this.yZero = yZero;
        
        repaint();
        
    }
    
    public void setGrid(int g[][], int gS) {
        
        grid = g;
        gridSize = gS;
        fontSize = 3 * gridSize / 5;
        
        if (grid == null) return;
        
//        adjust width and height
        w = gridSize * grid.length;
            
        h = gridSize * grid[0].length;
        
        repaint();

    }
    
    public void setNumColor(int n) {
        
        numCols = n;
        initColSet();
        
    }
    
    public void setHighScore(HighScoreList h) {
        
        highScoreList = h;
        
    }
    
//    initialise color set
    public void initColSet() {
        
        int temp = 5;
        base = new Color(temp, temp, temp);
        temp = 90;
        back = new Color(temp, temp, temp);
        temp = 255;
        white = new Color(temp, temp, temp);
        
        colSet = new ArrayList<Color>();
        
        if (numCols == 5) {
            
//            red
            colSet.add(new Color(210, 75, 80));
//            yellow
            colSet.add(new Color(255, 235, 155));
//            green
            colSet.add(new Color(140, 210, 120));
//            blue
            colSet.add(new Color(75, 195, 210));
//            purple
            colSet.add(new Color(200, 155, 255));

            
        } else {
            
//            damage control
            
            Random rand = new Random();
            
            for (int i = 0; i < numCols; i = i + 1) {
            
                int n = 256;
            
                int r = rand.nextInt() % n;
                if (r < 0) r = r + n;
            
                int g = rand.nextInt() % n;
                if (g < 0) g = g + n;
            
                int b = rand.nextInt() % n;
                if (b < 0) b = b + n;
            
                Color color = new Color(r, g, b);
            
                colSet.add(color);
            
            }
        
        }
        
    }

//   drawing methods
    public void drawBackground() {
        
        graphics.setColor(back);
        graphics.fillRect(xZero, yZero, w, h);
        
//        grid
        graphics.setColor(base);
		
		for (int i = 0; i <= w;  i = i + gridSize) {
             
            graphics.drawLine(i + xZero, yZero, i + xZero, h + yZero);
        
        }
		
        for (int i = 0; i <= h; i = i + gridSize) {
        
            graphics.drawLine(xZero, i + yZero, w + xZero, i + yZero);
        
        }
        
    }
    
    public void drawBalls() {
        
        if (grid == null || gridSize == 0) return;
        
        int radius = 4 * gridSize / 5;
        
        if (colSet == null) initColSet();
        
        for (int i = 0; i < grid.length; i = i + 1) {
            
            for (int j = 0; j < grid[i].length; j = j + 1) {
                
                if (grid[i][j] < 0) continue;
                
                int x = i * gridSize;
                int y = j * gridSize;
                
                int c = grid[i][j];
                
                if (c > numCols - 1) {
                    
                    grid[i][j] = -1;
                    continue;
                
                }
                
                graphics.setColor(colSet.get(c));
                graphics.fillOval(xZero + x + ((gridSize - radius) / 2), yZero + y + ((gridSize - radius) / 2), radius, radius);
                graphics.setColor(white);
                graphics.drawOval(xZero + x + ((gridSize - radius) / 2), yZero + y + ((gridSize - radius) / 2), radius, radius);
        
            }
            
        }
        
    }
   
    public void drawFuture() {
        
        if (!showFutureLocation) return;
        
        if (futureBalls == null || futureCols == null) return;
        
        if (futureBalls.length  != futureCols.length)  return;
        
        int radius = gridSize / 4;
        
        for (int i = 0; i < futureBalls.length; i = i + 1) {
            
            Point P = futureBalls[i];
            
            int x = xZero + (P.x * gridSize) + gridSize / 2;
            x = x - radius / 2;
            int y = yZero + (P.y * gridSize) + gridSize / 2;
            y = y - radius / 2;
            
            int c = futureCols[i];
            
            if (showFutureColor) graphics.setColor(colSet.get(c));
            else graphics.setColor(back);
            
            graphics.fillOval(x, y, radius, radius);
            graphics.setColor(white);
            graphics.drawOval(x, y, radius, radius);
            
        }
        
    }
   
    public void drawPanel() {
        
        int xTop = xZero + w + gridSize;
        int yTop = yZero;
        
        int width  = 4 * gridSize;
        int height = h;
        
//        background
        graphics.setColor(back);
        graphics.fillRect(xTop, yTop, width, height);
        
//        frame
        graphics.setColor(base);
        graphics.drawRect(xTop, yTop, width, height);
        
//        drawScore
        fontSize = 2 * gridSize / 5;
        xTop = xTop + fontSize / 4;
        graphics.setColor(white);
        graphics.setFont(new Font("Ubuntu", Font.PLAIN, fontSize));
        graphics.drawString("Score: " + score, xTop, yTop + fontSize);
        
        yTop = yTop + fontSize;
        
//        instructions for gameOver
        if (gameOver) {
            
            graphics.setColor(new Color(255, 75, 80));
            graphics.drawString("Game is Over!", xTop, yTop + 2 * fontSize);
            
            return;
            
        }
        
//      draw next 3 colors
        if (showFutureColor) {
            
            if (futureBalls == null || futureCols == null) return;
        
                if (futureBalls.length  != futureCols.length)  return;
        
                int radius = 2 * gridSize / 3;
            
                yTop = yTop + fontSize;
        
                for (int i = 0; i < futureBalls.length; i = i + 1) {
                
                    Point P = futureBalls[i];
                
                    int x = xTop + i * (3 * radius / 2);
                
                    int y = yTop;
                
                    int c = futureCols[i];
                
                    graphics.setColor(colSet.get(c));
                
                    graphics.fillOval(x, y, radius, radius);
                    graphics.setColor(white);
                    graphics.drawOval(x, y, radius, radius);
                
                }
            
                yTop = yTop + radius;
            
        }
        
//        instructions for user
        graphics.drawString("Shift + C:", xTop, yTop + 2 * fontSize);
        yTop = yTop + fontSize;
        if (!showFutureColor) graphics.drawString("show colors", xTop, yTop + 2 * fontSize);
        else                  graphics.drawString("hide colors", xTop, yTop + 2 * fontSize);
        
        yTop = yTop + 2 * fontSize;
          
        graphics.drawString("Shift + S:", xTop, yTop + 2 * fontSize);
        yTop = yTop + fontSize;
        if (!showFutureLocation) graphics.drawString("show locations", xTop, yTop + 2 * fontSize);
        else                     graphics.drawString("hide locations", xTop, yTop + 2 * fontSize);
        
        yTop = yTop + 2 * fontSize;
          
        graphics.drawString("Shift + Z:", xTop, yTop + 2 * fontSize);
        yTop = yTop + fontSize;
        graphics.drawString("cancel last move", xTop - fontSize / 8, yTop + 2 * fontSize);
          
        yTop = yTop + 2 * fontSize;
          
        graphics.drawString("Shift + R:", xTop, yTop + 2 * fontSize);
        yTop = yTop + fontSize;
        graphics.drawString("restart", xTop, yTop + 2 * fontSize);
        
        yTop = yTop + 2 * fontSize;
          
        graphics.drawString("Shift + H:", xTop, yTop + 2 * fontSize);
        yTop = yTop + fontSize;
        if (!showHighScore) graphics.drawString("show highscore list", xTop, yTop + 2 * fontSize);
        else                graphics.drawString("hide highscore list", xTop, yTop + 2 * fontSize);
        
        yTop = yTop + 2 * fontSize;
        
        graphics.drawString("Shift + M:", xTop, yTop + 2 * fontSize);
        yTop = yTop + fontSize;
        graphics.drawString("add more balls", xTop, yTop + 2 * fontSize);
        
    }

    public void drawHighlight() {
        
        if (highlighter == null) return;
        
        highlighter.draw(graphics, xZero, yZero, gridSize);
        
    }
    
    public void drawUsernameBar() {
        
        if (!userBar) return;
        
        if (showHighScore) {
            
            showHighScore = false;
            repaint();
            
        }
        
//        draw background
       int xTop = xZero + gridSize / 2;
       int yTop = yZero + h / 3;
       
       int width  = w + 4 * gridSize;
       int height = 5 * gridSize / 2;
       
       graphics.setColor(back);
       graphics.fillRect(xTop, yTop, width, height);
       graphics.setColor(base);
       graphics.drawRect(xTop, yTop, width, height);
       
//        draw field for the string
        fontSize = 3 * gridSize / 5;
        width  = width - gridSize;
        height = 3 * fontSize;
        
        xTop = xTop + gridSize / 2;    
        yTop = yTop + fontSize / 2;
        
        graphics.setColor(white);
        graphics.fillRect(xTop, yTop, width, height);
        graphics.setColor(base);
        graphics.drawRect(xTop, yTop, width, height);
        
//        draw the string
        
        yTop = yTop + fontSize + 5;
        xTop = xTop + 10;
        
        graphics.setFont(new Font("Ubuntu", Font.PLAIN, fontSize));
        graphics.setColor(colSet.get(numCols - 1));
        graphics.drawString(username, xTop, yTop);
        
//        write information
        yTop = yTop + fontSize;
        fontSize = fontSize - 10;
        
        graphics.setFont(new Font("Ubuntu", Font.PLAIN, fontSize));
        graphics.setColor(base);
        graphics.drawString("[insert name] [ENTER to continue]", xTop + 3 * gridSize, yTop);
        
    }
    
    public void drawHighScore() {
    
        if (!showHighScore || userBar) return;
            
        int xTop = xZero + gridSize / 2;
        int yTop = yZero + gridSize / 2;
        
        int width  = w + 4 * gridSize;
        int height = h - gridSize;
        
        graphics.setColor(white);
        graphics.fillRect(xTop, yTop, width, height);
        graphics.setColor(base);
        graphics.drawRect(xTop, yTop, width, height);
        
        if (highScoreList == null) return;
        
        fontSize = 3 * gridSize / 5;
        graphics.setFont(new Font("Ubuntu", Font.PLAIN, fontSize));
        
        xTop = xTop + 10;
        yTop = yTop + 10 + fontSize;
        
        int xEnd = xTop + (2 * width) / 3;
        
        for (int i = 0; i < highScoreList.num(); i = i + 1) {
            
            graphics.setColor(colSet.get(i % numCols));
            
            String str = highScoreList.getUser(i);
            graphics.drawString(str, xTop, yTop);
            
            String scr = highScoreList.getScore(i) + "";
            graphics.drawString(scr, xEnd, yTop);
            
            yTop = yTop + 2 * fontSize;
            
        }
        
        fontSize = 2 * gridSize / 5;
        graphics.setFont(new Font("Ubuntu", Font.PLAIN, fontSize));
        graphics.setColor(base);
        graphics.drawString("[SHIFT + H] to hide", xTop + width / 3, yTop);    
        
    }

    public void drawCurr() {
        
        if (xCurrCell == -1 || yCurrCell == -1) return;
        
        int diff = 4;
        int size = gridSize - diff;
        
        int xTop = xZero + (xCurrCell * gridSize) + diff / 2;
        int yTop = yZero + (yCurrCell * gridSize) + diff / 2;
        
        if (grid[xCurrCell][yCurrCell] == -1) return;
        
        graphics.setColor(colSet.get(grid[xCurrCell][yCurrCell]));
        graphics.drawOval(xTop, yTop, size, size);
        
    }
    
//    update
    public void updateGrid(int g[][]) {
        
        grid = g;
        repaint();
        
    }
    
    public void updateScore(int s) {
    
        score = s;
        repaint();
        
    }
   
    public void updateFuture(Point[] P, int[] C) {
        
        futureBalls = P;
        futureCols  = C;
        if (showFutureColor || showFutureLocation) repaint();
        
    }
   
    public void showLocation() {
        
        showFutureLocation = !showFutureLocation;
        repaint();
        
    }
    
    public void showColor() {
        
        showFutureColor = !showFutureColor;
        repaint();
        
    }
   
    public void updateCurr(int x, int y) {
        
        xCurrCell = x;
        yCurrCell = y;
        
        repaint();
        
    }
   
//   add a path to highlight
    public void addHighlight(BallPath P) {

        if (highlighter == null) highlighter = new Highlighter(colSet);
        if (lastBlown   == null) lastBlown   = new Highlighter(colSet);
    
        if (highlighter.num() < lastBlown.num()) lastBlown = new Highlighter(colSet);
        
        highlighter.add(P);
        lastBlown.add(P);
        
        repaint();   
        
    }
    
    public void releaseHighlight() {
        
        if (highlighter == null) return;
        
        highlighter.release();
        repaint();
        
    }
    
    public BallPath getLastPath() {
        
        return lastBlown.set();
        
    }
   
    
//    GUI controls
    public void gameOver() {
        
        gameOver = true;
        repaint();
        
    }
        
    public void restart() {
        
        xCurrCell = -1;
        yCurrCell = -1;
        
        gameOver = false;
        repaint();
        
    }
   
    public void updateUsername(String str) {
        
        username = str;
        repaint();
        
    }
    
    public void usernameBar() {
        
        userBar = true;
        repaint();
        
    }
    
    public void hideUsernameBar() {
        
        userBar = false;
        repaint();
        
    }
    
    public void showHighScore() {
    
        showHighScore = !showHighScore;
        repaint();
        
    }
   
}
