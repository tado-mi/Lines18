package src;

// for the use of random function
import java.util.*;

// for implementing the MouseListener and the KeyListener
import java.awt.*;
import java.awt.event.*;

// for extending JFrame
import javax.swing.*;

public class Lines18 extends JFrame implements MouseListener, KeyListener {

//    GUI
    Lines18GUI GUI;
//    left-most, top-most corner of the GUI
    int xZero, yZero;

    public Lines18 () {

        setSize (1000, 1000);
        setTitle("Lines 2018");

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);
        setFocusable(true);

//        initialise
        init();

    }

//    variables for governing the game
    int gridSize;
    int score, scoreDiff;
	int addCount;

//    for highScoreList
    String username;
    boolean expectUsername;
    HighScoreList highScoreList;

//    the coordinates of the selected ball
    int xCurr, yCurr;
//    current number of balls, maximum number of balls, number of Colors
    int numBalls, capBalls, numCols;

//    the underlying grid and its dimensions
    int grid[][];
    int gridWidth, gridHeight;

//    memory of the last change the user had made
    Point[] lastChange;
//    memory of the last added balls
    Point[] lastBalls;

//    projection of future balls and their colors
    Point[] futureBalls;
      int[] futureCols;

//    initialisation methods
    public void init() {

        xZero = 20;
        yZero = 20;

        GUI = new Lines18GUI();
        GUI.setDim(xZero, yZero);

        add(GUI);
        GUI.addMouseListener(this);

        gridSize = 50;

        score    =  0;
        username = "";
        highScoreList = new HighScoreList();
        GUI.setHighScore(highScoreList);

        xCurr = -1;
        yCurr = -1;

        initGrid();

        lastChange = new Point[2];

        repaint();

    }

    public void initGrid() {

        numBalls = 0;
        numCols  = 5;

        GUI.setNumColor(numCols);

        gridWidth  = 9;
        gridHeight = 9;

        capBalls = gridWidth * gridHeight;

        grid = new int[gridWidth][gridHeight];

//        initialise to -1
        for (int i = 0; i < gridWidth; i = i + 1) {

            for (int j = 0; j < gridHeight; j = j + 1) grid[i][j] = -1;

        }

		addCount = 3;
//        sets 3 balls to be added
        setFuture(addCount);
//        adds 3 balls
        addBalls(addCount);

        GUI.setGrid(grid, gridSize);

    }

    public void restart() {

//        set all the variables to initial values
        numBalls = 0;
        score    = 0;
        username = "";

        lastChange = new Point[2];

        xCurr = -1;
        yCurr = -1;

//        reinitialize grid
        for (int i = 0; i < gridWidth; i = i + 1) {

            for (int j = 0; j < gridHeight; j = j + 1) {

                grid[i][j] = -1;

            }

        }

        setFuture(addCount);
        addBalls(addCount);

//        adjust GUI
        GUI.releaseHighlight();

        GUI.updateScore(score);
        GUI.updateUsername(username);
        GUI.restart();
        GUI.repaint();

    }

    public void recordHighScore() {

        highScoreList.update(score, username);

    }

//    game controls

//    whether or not [x][y] is a spot projected for a future ball
    public boolean isInFuture(int x, int y) {

        if (futureBalls == null) return false;

        for (int i = 0; i < futureBalls.length; i = i + 1) {

            Point P = futureBalls[i];

            if (P == null) continue;

            if (P.x == x && P.y == y) {

                return true;

            }

        }

        return false;

    }

//    a method to set random balls to be added to the grid in next move
    public void setFuture(int n) {

        if (numBalls > capBalls - (n + 1)) {

            gameOver();
            return;

        }

//        initialise
        futureBalls = new Point[n];
        futureCols  = new int  [n];

        Random rand = new Random();

        for (int i = 0; i < n; i = i + 1) {

//            random coordinates
            int x = rand.nextInt() % gridWidth;
            if (x < 0) x = x + gridWidth;

            int y = rand.nextInt() % gridHeight;
            if (y < 0) y = y + gridHeight;

//            if there is a ball in the cell
//            or the cell is already designated
            if ((grid[x][y] != -1) || isInFuture(x, y)) {

//                try again
                i = i - 1;
                continue;

            }

//            a random color
            int c = rand.nextInt() % numCols;
            if (c < 0) c = c + numCols;

//            assign
            futureBalls[i] = new Point(x, y);
            futureCols [i] = c;

        }

        GUI.updateFuture(futureBalls, futureCols);

    }

//    whether or not the user tried
//    occupy a cell designated for a future ball
    public boolean isFutureConflict() {

        return isInFuture(xCurr, yCurr);

    }

//    handles situation when the user tried to
//    occupy a cell designated for a future ball
    public void futureConflict() {

        if (futureBalls == null) return;

        for (int i = 0; i < futureBalls.length; i = i + 1) {

            Point P = futureBalls[i];
            if (P.x == xCurr && P.y == yCurr) {

                Random rand = new Random();

                int x = rand.nextInt() % gridWidth;
                if (x < 0) x = x + gridWidth;

                int y = rand.nextInt() % gridHeight;
                if (y < 0) y = y + gridHeight;

//                selects a spot unoccupied by an existing ball
//                or by a projected ball
                while (grid[x][y] != -1 || isInFuture(x, y)) {

                    x = rand.nextInt() % gridWidth;
                    if (x < 0) x = x + gridWidth;

                    y = rand.nextInt() % gridHeight;
                    if (y < 0) y = y + gridHeight;

                }

                futureBalls[i] = new Point(x, y);

                break;

            }

        }

    }

//    adds n balls in random colors and random free locations
    public void addBalls(int n) {

        if (futureBalls == null || futureCols == null) return;

//        damage control
        if (futureBalls.length  != futureCols.length)  return;

//        save information about added balls
        lastBalls = new Point[n];

        for (int i = 0; i < n; i = i + 1) {

            Point P = futureBalls[i];

            int x = P.x;
            int y = P.y;

            int c = futureCols[i];

//            assign
            grid[x][y] = c;
            lastBalls[i] = new Point(x, y);

//            updateScore
//            save current coordinates
            int xTemp = xCurr;
            int yTemp = yCurr;

//            since updateScore() is written to
//            work around global variables [xCurr][yCurr]
            xCurr = x;
            yCurr = y;

//            if the hit had occured at futureBalls[0 ... (n - 2)]
//            by the time the addBalls(n) method is complete: updateScore() is called on futureBalls[n - 1] -
//            the scoreDiff will be reassigned to 0: losing the information about the hit having had occured
            int tempScoreDiff = scoreDiff;

            updateScore();

            scoreDiff = scoreDiff + tempScoreDiff;

//            return the value
            xCurr = xTemp;
            yCurr = yTemp;

        }

        numBalls = numBalls + n;

        if (numBalls > capBalls - (n + 1)) {

            gameOver();
            return;

        }

        GUI.updateCurr(-1, -1);
        GUI.updateGrid(grid);

        setFuture(n);

    }

    public void gameOver() {

        GUI.gameOver();
//        prompt for username
        GUI.usernameBar();
        expectUsername = true;

    }

//    performs a DFS
    public boolean isReachable(int xDest, int yDest) {

        Stack<Point> S = new Stack<Point>();
        S.push(new Point(xCurr, yCurr));

        while (!S.isEmpty()) {

            Point temp = S.pop();

            int xTemp = temp.x;
            int yTemp = temp.y;

//            mark as visited
            if (grid[xTemp][yTemp] == -1) grid[xTemp][yTemp] = -2;

            if (xTemp == xDest && yTemp == yDest) {

                unMark();
                return true;

            }

            if (xTemp < gridWidth - 1) {

                if (grid[xTemp + 1][yTemp] == -1) {

                    S.push(new Point(xTemp + 1, yTemp));
//                    mark as visited
                    grid[xTemp + 1][yTemp] = -2;

                }

            }

            if (xTemp > 0) {

                if (grid[xTemp - 1][yTemp] == -1) {

                    S.push(new Point(xTemp - 1, yTemp));
//                    mark as visited
                    grid[xTemp - 1][yTemp] = -2;

                }

            }

            if (yTemp < gridHeight - 1) {

                if (grid[xTemp][yTemp + 1] == -1) {

                    S.push(new Point(xTemp, yTemp + 1));
//                    mark as visited
                    grid[xTemp][yTemp + 1] = -2;

                }

            }

            if (yTemp > 0) {

                if (grid[xTemp][yTemp - 1] == -1) {

                    S.push(new Point(xTemp, yTemp - 1));
//                    mark as visited
                    grid[xTemp][yTemp - 1] = -2;

                }

            }

        }

        unMark();
        return false;

    }

//    a method to change all -2 from DFS/isReachable() to -1
    public void unMark() {

        for (int i = 0; i < gridWidth; i = i + 1) {

            for (int j = 0; j < gridHeight; j = j + 1) {

                if (grid[i][j] == -2) grid[i][j] = -1;

            }

        }

    }

//    undo the last change the user had made
//    remove last added balls
    public void    unDo() {

        if (lastChange[0] == null || lastChange[1] == null) return;

//        return balls that were erased by the last move
        if (scoreDiff >  0) {

            GUI.releaseHighlight();

//            update the score
            score = score - scoreDiff;
            scoreDiff = 0;
            GUI.updateScore(score);

//            return the balls
            BallPath toRestore = GUI.getLastPath();
//                damage control
            if (toRestore != null) {

                int c = toRestore.color();

                for (int j = 0; j < toRestore.num(); j = j + 1) {

                        Point P = toRestore.get(j);
                        if (P == null) continue;

                        int x = P.x;
                        if (x < 0 || x > gridWidth  - 1) continue;

                        int y = P.y;
                        if (y < 0 || y > gridHeight - 1) continue;

                        grid[x][y] = c;
                        numBalls = numBalls + 1;

                        if (isInFuture(x, y)) {

                            int xTemp = xCurr;
                            int yTemp = yCurr;

                            xCurr = x;
                            yCurr = y;

                            futureConflict();

                            xCurr = xTemp;
                            yCurr = yTemp;

                        }

                    }

                }

        }

//          remove the last added balls
//          note: lastBalls have been updated after the move
        if (lastBalls != null) {

            for (int i = 0; i < lastBalls.length; i = i + 1) {

                Point P = lastBalls[i];

                grid[P.x][P.y] = -1;
                numBalls = numBalls - 1;

            }

        }

//            reverse the move
        Point P1 = lastChange[0];
        Point P2 = lastChange[1];

        int c = grid[P2.x][P2.y];

        grid[P2.x][P2.y] = grid[P1.x][P1.y];
        grid[P1.x][P1.y] = c;

//        note: only one unDo will be possible at a time
        lastChange[0] = null;
        lastChange[1] = null;

        GUI.repaint();

    }

//    '_'
    public boolean horizontal () {

		int c = grid[xCurr][yCurr];

        int st = xCurr;
        if (st > 0) {

            while (grid[st - 1][yCurr] == c) {

                st = st - 1;
                if (st == 0) break;

            }

        }

        int end = xCurr;
        if (end < gridWidth - 1) {

            while (grid[end + 1][yCurr] == c) {

                end = end + 1;
                if (end == gridWidth - 1) break;

            }

        }

        if (end - st > 3) {

            int temp = end - st + 1;

            score = score + temp;
            scoreDiff = scoreDiff + temp;

            BallPath P = new BallPath(c, temp);

//			  remove the balls
			for (int i = 0; i < temp; i = i + 1) {

				grid[st + i][yCurr] = -1;
				numBalls = numBalls - 1;

                P.add(new Point(st + i, yCurr));

			}

//            highlight
            GUI.addHighlight(P);

//            return the current ball
            numBalls = numBalls + 1;
            grid[xCurr][yCurr] = c;
            return true;

        }

        return false;

	}

//    '|'
	public boolean vertical () {

		int c = grid[xCurr][yCurr];

        int st = yCurr;
        if (st > 0) {

            while (grid[xCurr][st - 1] == c) {

                st  = st - 1;
                if (st == 0) break;

            }

        }

        int end = yCurr;
        if (end < gridHeight - 1) {

            while (grid[xCurr][end + 1] == c) {

                end = end + 1;
                if (end == gridHeight - 1) break;

            }

        }

        if (end - st > 3) {

            int temp = end - st + 1;

            score = score + temp;
            scoreDiff = scoreDiff + temp;

            BallPath P = new BallPath(c, temp);

//			  remove the balls
			for (int i = 0; i < temp; i = i + 1) {

				grid[xCurr][st + i] = -1;
				numBalls = numBalls - 1;

                P.add(new Point(xCurr, st + i));

			}

//            highlight
            GUI.addHighlight(P);

//            return the current ball
            numBalls = numBalls + 1;
            grid[xCurr][yCurr] = c;
            return true;

        }

        return false;

	}

//    '\' or '/'
    public boolean diagonal() {

        boolean D = diagonalDown();
        boolean U = diagonalUp();

        return D || U;

    }

//    '\'
    public boolean diagonalDown () {

        int c = grid[xCurr][yCurr];

        int xSt = xCurr;
        int ySt = yCurr;

        if (xSt > 0  && ySt > 0) {

            while (grid[xSt - 1][ySt - 1] == c) {

                xSt = xSt - 1;
                ySt = ySt - 1;

                if (xSt == 0 || ySt == 0) break;

            }

        }

        int xEnd = xCurr;
        int yEnd = yCurr;

        if (xEnd < gridWidth - 1 && yEnd < gridHeight - 1) {

            while (grid[xEnd + 1][yEnd + 1] == c) {

                xEnd = xEnd + 1;
                yEnd = yEnd + 1;

                if (xEnd == gridWidth - 1 || yEnd == gridHeight - 1) break;

            }

        }

        if (xEnd - xSt > 3) {

            int temp = xEnd - xSt + 1;

            score = score + temp;
            scoreDiff = scoreDiff + temp;

            BallPath P = new BallPath(c, temp);

//			  remove the balls
			for (int i = 0; i < temp; i = i + 1) {

				grid[xSt + i][ySt + i] = -1;
				numBalls = numBalls - 1;

			    P.add(new Point(xSt + i, ySt + i));

			}

//            highlight
            GUI.addHighlight(P);

//            return the current ball
            numBalls = numBalls + 1;
            grid[xCurr][yCurr] = c;

            return true;

        }

        return false;

	}

//    '/'
    public boolean diagonalUp() {

        int c = grid[xCurr][yCurr];

        int xSt = xCurr;
        int ySt = yCurr;

        if (xSt < gridWidth - 1  && ySt > 0) {

            while (grid[xSt + 1][ySt - 1] == c) {

                xSt = xSt + 1;
                ySt = ySt - 1;

                if (xSt == gridWidth - 1 || ySt == 0) break;

            }

        }

        int xEnd = xCurr;
        int yEnd = yCurr;

        if (xEnd > 0 && yEnd < gridHeight - 1) {

            while (grid[xEnd - 1][yEnd + 1] == c) {

                xEnd = xEnd - 1;
                yEnd = yEnd + 1;

                if (xEnd == 0 || yEnd == gridHeight - 1) break;

            }

        }

        if (yEnd - ySt > 3) {

            int temp = yEnd - ySt + 1;

            score = score + temp;
            scoreDiff = scoreDiff + temp;

            BallPath P = new BallPath(c, temp);

//			  remove the balls
			for (int i = 0; i < temp; i = i + 1) {

				grid[xSt - i][ySt + i] = -1;
				numBalls = numBalls - 1;

                P.add(new Point(xSt - i, ySt + i));

			}

//            highlight
            GUI.addHighlight(P);

//            return the current ball
            numBalls = numBalls + 1;
            grid[xCurr][yCurr] = c;
            return true;

        }

        return false;

    }

    public boolean updateScore() {

        scoreDiff = 0;

        boolean h = horizontal();

        boolean v = vertical();

        boolean d = diagonal();

        if ( h || v || d ) {

            grid[xCurr][yCurr] = -1;
            numBalls = numBalls - 1;
            GUI.updateScore(score);

            /*int factor = 1000;
            int temp = score / factor;


             if (temp > 0) {

                if (addCount - temp < 3) addCount = 3 + temp;
                setFuture(addCount);

            } */

            return true;


        }

        return false;

    }

//    mouseListener methods
	public void mouseClicked  (MouseEvent e) {

        int x = (e.getX() - xZero) / gridSize;
		int y = (e.getY() - yZero) / gridSize;

//        if it is off the grid
        if (x >= gridWidth || y >= gridHeight) return;

//        if the user hit a ball, update coordinates
        if (grid[x][y] != -1) {

            xCurr = x;
            yCurr = y;
            GUI.updateCurr(x, y);

        } else if (xCurr != -1 && yCurr != -1) {

            if (!isReachable(x, y)) {

                return;
            }

            int xTemp = xCurr;
            int yTemp = yCurr;

            xCurr = x;
            yCurr = y;

            if (isFutureConflict()) {

                futureConflict();

            }

            xCurr = xTemp;
            yCurr = yTemp;

            lastChange[0] = new Point(xCurr, yCurr);
            lastChange[1] = new Point(x, y);

//            relocate the ball
            int c = grid[xCurr][yCurr];
            grid[xCurr][yCurr] = -1;
            grid[x][y] = c;

            xCurr = x;
            yCurr = y;

            if (!updateScore()) {

                int temp = numBalls;
                addBalls(addCount);

//                the highlight should be released only if the number of balls has increased after addBalls(addCount)
//                ie: there is no  hit (decreasing the number of balls) to be highlighted
                if (temp < numBalls) GUI.releaseHighlight();

            } else {

               lastBalls = null;

            }

            GUI.repaint();

            xCurr = -1;
            yCurr = -1;

        }

	}

	public void mouseEntered  (MouseEvent e) {

	}

	public void mouseExited   (MouseEvent e) {

	}

	public void mousePressed  (MouseEvent e) {

	}

	public void mouseReleased (MouseEvent e) {

	}

//    keyListener methods
    public void keyTyped(KeyEvent e) {

        char focus = e.getKeyChar();

        if (focus == KeyEvent.VK_ENTER) {

            if (!expectUsername) return;

            expectUsername = false;
            GUI.hideUsernameBar();

            recordHighScore();
            GUI.showHighScore();

            username = "";

        }

        if (expectUsername) {

            if (focus == KeyEvent.VK_BACK_SPACE) {

                char[] temp = username.toCharArray();
                username = "";
                for (int i = 0; i < temp.length - 1; i = i + 1) username = username + temp[i];


            } else {

                username = username + focus;

            }

            GUI.updateUsername(username);
            return;

        }

        if (focus == 'Z') unDo();

        if (focus == 'S') GUI.showLocation();

        if (focus == 'C') GUI.showColor();

        if (focus == 'R') restart();

        if (focus == 'H') {

            GUI.showHighScore();
//            if gameOver
            if (numBalls > capBalls - (addCount + 1)) restart();

        }

        if (focus == 'M') {

            GUI.releaseHighlight();

            lastChange[0] = null;
            lastChange[1] = null;

            addBalls(addCount);

        }

//        easter eggs :D
        if (focus == '+') {

            if (gridSize >= 100) return;
            gridSize = gridSize + 5;
            GUI.setGrid(grid, gridSize);
            GUI.repaint();

        }

        if (focus == '-') {

            if (gridSize < 10) return;
            gridSize = gridSize - 5;
            GUI.setGrid(grid, gridSize);
            GUI.repaint();

        }

        if (focus == 'd') {

//            move right
            if (xZero > gridWidth * gridSize) return;
            xZero = xZero + 5;
            GUI.setDim(xZero, yZero);

        }

        if (focus == 'a') {

//            move left
            if (xZero < 5) return;
            xZero = xZero - 5;
            GUI.setDim(xZero, yZero);

        }

        if (focus == 'w') {

//            move up
            if (yZero < 5) return;
            yZero = yZero - 5;
            GUI.setDim(xZero, yZero);

        }

        if (focus == 's') {

//            move down
            if (yZero > gridHeight * gridSize) return;
            yZero = yZero + 5;
            GUI.setDim(xZero, yZero);

        }

    }

    public void keyReleased(KeyEvent e) {



    }

    public void keyPressed(KeyEvent e) {



    }

}
