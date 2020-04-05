package src;

public class BallPath {

//    supports basic functionalities of an array list
    Point[] arr;
    int     color;

    int num, cap;

    public BallPath (int c, int cap) {

      color       =  c ;
      this.cap = cap;

      // initialise the Path
      arr = new Point[cap];
      for (int i = 0; i < cap; i = i + 1) arr[i] = new Point(-1, -1);

    }

    public void increaseCap() {

      cap = 2 * (cap + 1);

      Point[] temp = new Point[cap];

      for (int i = 0;   i < num; i = i + 1) temp[i] = arr[i];
      for (int i = num; i < cap; i = i + 1) temp[i] = new Point(-1, -1);

      arr = temp;

    }

    public void add(Point P) {

    	if (arr == null) return;

    	if (num >= cap) increaseCap();

      arr[num] = P;
      num = num + 1;

    }

//    getters
    public int num() {

        return num;

    }

    public int color() {

        return color;

    }

    public Point get(int index) {

        if (index > num - 1) return null;

        return arr[index];

    }

}
