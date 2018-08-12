import java.io.*;

public class HighScoreList {
    
    int[]    scoreList;
    String[] userList;
    
    int num, maxCap;
    
    String filename;
    
//    constructor
    public HighScoreList() {
        
        filename = "HighScoreList.txt";
        
        maxCap = 6;
        
        scoreList = new int[maxCap];
        for (int i = 0; i < maxCap; i = i + 1) scoreList[i] = -1;
        userList  = new String[maxCap];
        
        num = 0;
        
        initialise(filename);
        
    }
    
    public void initialise(String name) {
        
        filename = name;
//        read from filename and save in scoreLise and userList
        try {
            
            FileReader     temp   = new FileReader(filename);
            BufferedReader reader = new BufferedReader(temp);
            
            while (true) {
                
                String str = reader.readLine();
                
                if (str == null) break;
                
                char[] arr = str.toCharArray();
                int ind = 0;
                
                String scoreString = "";
                while (arr[ind] != ' ') {
                    
                    scoreString = scoreString + arr[ind];
                    ind = ind + 1;
                    
                }
                   
                int scre = Integer.parseInt(scoreString);
                
//                covering for ' - '
                ind = ind + 3;
                
                String usrname = "";
                
                while (ind < arr.length) {
                    
                    usrname = usrname + arr[ind];
                    ind = ind + 1;
                    
                }
                
//                add into the array
                update(scre, usrname);
                
                
            }
            
            if (temp   != null) temp.close();
            if (reader != null) reader.close();
            
            
        } catch (IOException e) {
            
            
            
        }
        
        
    }
    
//    getters
    public int getScore(int ind) {
        
        if (ind > num - 1) return -1;
        
        return scoreList[ind];
        
    }
    
    public String getUser(int ind) {
        
        if (ind > num - 1) return null;
        
        return userList[ind];
        
    }
    
    public int num() {
        
        return num;
        
    }
    
//    modifiers
    public void swap(int i, int j) {
        
        if (i > maxCap - 1 || j > maxCap - 1) return;
        
        int temp = scoreList[i];
        scoreList[i] = scoreList[j];
        scoreList[j] = temp;
        
        String str  = userList[i];
        userList[i] = userList[j];
        userList[j] = str;
        
    }

    public void update(int score, String username) { 
                             
        if (num < maxCap) {
            
//            add current score to the next available spot
            scoreList[num] = score;
            userList [num] = username;
            num = num + 1;        

            int spotGuess = num - 1;
            if (spotGuess != 0) {
                
                while (scoreList[spotGuess] > scoreList[spotGuess - 1]) {
                
                    swap(spotGuess, spotGuess - 1);
                    spotGuess = spotGuess - 1;
                    if (spotGuess == 0) break;
                    
                
                }
                
            }
            
//        write back to file
          write();
        
        } else if (score > scoreList[num - 1]) {
                
//           erase the last entry of highScoreList   
            scoreList[num - 1] = -1;
            num = num - 1;
            
//            call recursively
            update(score, username);
        
        }
        
    }
    
    public void write() {
        
        if (scoreList == null) return;
        
        try {
            
            FileWriter writer = new FileWriter(filename);
            
            for (int i = 0; i < num; i = i + 1) {
            
                String str = scoreList[i] + "";
                
                str = str + " - " + userList[i] + "\n";
                writer.write(str);
                
            }
            
            if (writer != null) writer.close();
            
        } catch (IOException e) {
            
            
            
        }
        
       
    }
    
    
}
