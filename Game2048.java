
package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.*;

public class Game2048 extends Game {
     private static final int SIDE = 4;
     private boolean isGameStopped = false;
     private int score = 0;
     @Override
     public void initialize(){
        setScreenSize(SIDE,SIDE);
        createGame();
        drawScene();
        
     }
     private int[][] gameField;
    
     private void createGame(){
         score = 0;
         setScore(score);
         gameField = new int[SIDE][SIDE];
         createNewNumber();
         createNewNumber();
         
     }
     private void drawScene(){
         for(int x = 0; x < SIDE; x++){
              for(int y = 0; y < SIDE; y++){
                 setCellColoredNumber(x,y, gameField[y][x]);
              }
         }
        
     }
     //creating a new value for two random cells 
     private void createNewNumber() {
         if (getMaxTileValue() == 2048){
            win();
         }
        int x;
        int y;
       do {
            x = getRandomNumber(SIDE);     
            y = getRandomNumber(SIDE);
            
            if (gameField[x][y] == 0){
                if(getRandomNumber(10)==9 ){
                       gameField[x][y]= 4;
                }else{//--if(gameField[x][y]==0){
                    gameField[x][y]= 2;
                }
         }
            
        } while(gameField[x][y] == 0);
        
     }
     
    private Color getColorByValue(int value) {
         Color color = null;
         switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.RED;
                break;
            case 8:
                color = Color.CYAN;
                break;
            case 16:
                color = Color.GREEN;
                break;
            case 32:
                color = Color.YELLOW;
                break;
            case 64:
                color = Color.ORANGE;
                break;
            case 128:
                color = Color.PINK;
                break;
            case 256:
                color = Color.MAGENTA;
                break;
            case 512:
                color = Color.BLACK;
                break;
            case 1024:
                color = Color.PURPLE;
                break;
            case 2048:
                color = Color.GRAY;
                break;
            default:
                color = Color.BROWN;
        }    
            return color;    
         
     }
     private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        //setCellValueEx(int, int, Color, String);
        if (value == 0) {
            setCellValueEx(x,y, color, "");
        } else {
            setCellValueEx(x, y, color, Integer.toString(value));
        }
    }
    private boolean compressRow(int[] row) {
        int temp = 0;
        int[] rowtemp = row.clone();
        boolean isChanged = false;
        for(int i = 0; i < row.length; i++) {
            for(int j = 0; j < row.length-i-1; j++){
                if(row[j] == 0) {
                    temp = row[j];
                    row[j] = row[j+1];
                    row[j+1] = temp;
                }
            }
        }
        if(!Arrays.equals(row,rowtemp)){
            isChanged = true;
        }
        return isChanged;
    }
    private boolean mergeRow(int[] row){
    boolean moved = false;
    for (int i=0; i< row.length-1;i++){
        if ((row[i] == row[i+1])&&(row[i]!=0)){
            row[i] = 2*row[i];
            row[i+1] = 0;
            moved = true;
            score = score + row[i];
            setScore(score);
        }
    }

    return moved;
    }
    @Override
    
    public void onKeyPress(Key key){
      
        if(isGameStopped == true){
            if(key == Key.SPACE){
                isGameStopped = false;
                createGame();
                drawScene();
            }
            
        }else{
            if(canUserMove() == false){
                gameOver();
                
            }
           else if( key == Key.LEFT) {
               moveLeft();
                drawScene();
            
               
           }
           else if(key == Key.RIGHT) {
               moveRight();
                drawScene();
              
           }
           else if(key == Key.UP) {
               moveUp();
                drawScene();
              
           }
           else if(key == Key.DOWN){
                    moveDown();
                    drawScene();
            }
        }
        
    }
     
    private void moveLeft(){
         boolean shiffted = false;
         
         for(int r=0;r<gameField.length; r++){
            if(compressRow(gameField[r]) && shiffted == false){
                shiffted = true;
            }
            if(mergeRow(gameField[r]) && shiffted == false){
                shiffted = true;
            }
            if(compressRow(gameField[r]) && shiffted == false){
                shiffted = true;
            }
         }
         if(shiffted == true){
            createNewNumber();
        }
     }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    private void rotateClockwise() {
        // Traverse each cycle
        for (int i = 0; i < SIDE / 2; i++)
        {
            for (int j = i; j < SIDE - i - 1; j++)
            {
                // Swap elements of each cycle in clockwise direction
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;
            }
        }
    }
    private int getMaxTileValue(){
        int maxValue = gameField[0][0];
        for (int j = 0; j < gameField.length; j++) {
            for (int i = 0; i < gameField[j].length; i++) {
                maxValue = Math.max(maxValue, gameField[j][i]);
            }
        }
        return maxValue;
        
    }
    private void win(){
         isGameStopped = true;
         showMessageDialog(getColorByValue(2048), "You won the game!", getColorByValue(256), 20);
    }
    private boolean canUserMove(){
        for(int i = 0; i<gameField.length; i++){
            for(int j = 0; j<gameField.length; j++){
                if(gameField[i][j] == 0)  {
                    //numZero+=1;
                   return true;
                }
                // horizontal RIGHT
                if(j<SIDE-1 &&gameField[i][j] == gameField[i][j+1]){
                    return true;
                }
                // horizontal left
                if(j>0 && gameField[i][j] == gameField[i][j-1]){
                   return true;
                }
                // veritcal DOWN
                if(i<SIDE-1 && gameField[i][j] == gameField[i+1][j]){
                    return true;
                }
                // veritcal UP
                if(i>0 && gameField[i][j] == gameField[i-1][j]){
                    return true;
                }
                
            }
        }
        return false;
        
     }
     private void gameOver(){
       
             if(isGameStopped = true){
         
        showMessageDialog(getColorByValue(2048), "Game is over !", getColorByValue(256), 20);
         }
         
  }
}
