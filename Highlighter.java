import java.awt.*;
import java.util.*;

public class Highlighter {
    
//    supports basic functionalities of an array list
    BallPath[] arr;
    int num, cap;

    ArrayList<Color> colSet;
    
    public Highlighter (ArrayList<Color> colSet) {
        
        this.cap = 5;
        this.colSet = colSet;
        
//            initialise the arr
        arr = new BallPath[cap];
        for (int i = 0; i < cap; i = i + 1) arr[i] = null;
      
    }
    
    public void increaseCap() {
        
        cap = 2 * (cap + 1);
        
        BallPath[] temp = new BallPath[cap];
        
        for (int i = 0;   i < num; i = i + 1) temp[i] = arr[i];
        for (int i = num; i < cap; i = i + 1) temp[i] = new BallPath(-1, 0);
        
        arr = temp;
        
    }
    
    public void add(BallPath P) {
        
        if (arr == null) return;
        
        if (num == cap)  increaseCap();
            
        arr[num] = P;
        num = num + 1;
    
    }
    
//    clearup
    public void release() {
        
        arr = new BallPath[cap];
        for (int i = 0; i < cap; i = i + 1) arr[i] = null;
        
        num = 0;
        
    }

//    getters
    public int num() {
        
        return num;
        
    }
    
    public BallPath   get(int index) {
        
        if (index > num - 1) return null;
        
        return arr[index];
        
    }
    
//	returns last non-null element if such exists
    public BallPath set() {
        
        if (num == 0) return null;
        
        int temp = num - 1;
        while (arr[temp] == null) {
            
            temp = temp - 1;
            if (temp == -1) return null;
        }
        
        return arr[temp];
        
    }
    
//    painting methods
    public void draw(Graphics graphics, int xZero, int yZero, int gridSize) {
        
        for (int i = 0; i < num; i = i + 1) {
            
            if (arr[i] == null) return;
            
            BallPath focus = arr[i];
            
            int c = focus.color();
            graphics.setColor(colSet.get(c));
            
            int diff = 10;
            int r = gridSize - diff;
            
            for (int j = 0; j < focus.num(); j = j + 1) {
                                
                Point P = focus.get(j);
                if (P == null) continue;
                
                int x = P.x;
                x = xZero + x * gridSize;
                x = x + diff / 2;
                
                int y = P.y;
                y = yZero + y * gridSize;
                y = y + diff / 2;
                
                graphics.drawRect(x, y, r, r);
               
            }
            
        }
        
    }
    
}
