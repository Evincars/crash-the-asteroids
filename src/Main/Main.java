package Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Graphics.AsteroidGenerator;
import Graphics.BonusGenerator;
import Graphics.Canvas;
import Graphics.MyColor;
import Graphics.Spaceship;

public class Main {

	private static String actualKey = "";
	private static int FPS = 50;
	private static Canvas cm;
	private static Spaceship ss;
	private static boolean keyPressedOnce = false, keyPressedOnce2 = false, controlsMenu = false, scoreMenu = false, inMenu = true, gameOver = false;
	private static int screenWidth = 0, screenHeight = 0;
	private static int asteroidsSpeed = 1;
	private static boolean asteroidSpeedIncreased1 = false, generateRateIncreased1 = false;
	private static int generateRate = 100, i = 0;
	private static AsteroidGenerator generator;
	private static BonusGenerator bonusGenerator;
	private static int spaceWidth = 60, spaceHeight = 60;
	private static int oldScore;
	
	public static void main(String[] args) {
		
		cm = Canvas.getInstance();
		generator = new AsteroidGenerator(cm.getWidth(), cm.getHeight());
		bonusGenerator = new BonusGenerator();
		ss = new Spaceship(cm.getWidth() / 2 - (spaceWidth / 2), // x 
						   cm.getHeight() - spaceHeight - 50,    // y
						   spaceWidth, spaceHeight, // width and height of space
						   5, 300, cm.getWidth(), cm.getHeight());
		screenWidth = cm.getWidth();
		screenHeight = cm.getHeight();
		long t = (long) (1.0 / FPS * 1000);
		cm.addKeyListener(new P_KeyListener());
	    new Timer(true).schedule(new P_Task(), t, t);
	    
	    return;
		
	}
	
	private static class P_Task extends TimerTask {

	    @Override
	    public void run() {
	    	cm.clearCanvas();
	    	cm.drawBackground();
	    	
	    	if (!controlsMenu && !scoreMenu && inMenu)
	    		cm.drawMenu();
	    	else if (controlsMenu && !scoreMenu && inMenu)
	    		cm.drawControlsMenu();
	    	else if (!controlsMenu && scoreMenu && inMenu)
	    		cm.drawScoreMenu();
	    	
	    	if (inMenu && !gameOver){
		    	if (actualKey == "up" && keyPressedOnce2){
		    		cm.drawPointer("up");
		    		keyPressedOnce2 = false;
		    		controlsMenu = false;
		    		scoreMenu = false;
		    	} else if (actualKey == "down" && keyPressedOnce2){
		    		cm.drawPointer("down");
		    		keyPressedOnce2 = false;
		    		controlsMenu = false;
		    		scoreMenu = false;
		    	} else if ((actualKey == "enter" || actualKey == "space") && keyPressedOnce2 && cm.getCursorPosition() == 0){
		    		inMenu = false;
		    		keyPressedOnce2 = false;
		    		cm = Canvas.getInstance();
	    			generator = new AsteroidGenerator(cm.getWidth(), cm.getHeight());
	    			bonusGenerator = new BonusGenerator();
	    			ss = new Spaceship(cm.getWidth() / 2 - (spaceWidth / 2), // x 
	    							   cm.getHeight() - spaceHeight - 50,    // y
	    							   spaceWidth, spaceHeight, // width and height of space
	    							   5, 300, cm.getWidth(), cm.getHeight());
	    			
		    	} else if ((actualKey == "enter" || actualKey == "space") && keyPressedOnce2 && cm.getCursorPosition() == 1){
		    		scoreMenu = true;
		    		controlsMenu = false;
		    		keyPressedOnce2 = false;
		    	} else if ((actualKey == "enter" || actualKey == "space") && keyPressedOnce2 && cm.getCursorPosition() == 2){
		    		controlsMenu = true;
		    		scoreMenu = false;
		    		keyPressedOnce2 = false;
		    	} else if ((actualKey == "enter" || actualKey == "space") && keyPressedOnce2 && cm.getCursorPosition() == 3){
		    		if (cm.endGame())
		    			System.exit(1);
		    	} else if(!controlsMenu && !scoreMenu) cm.drawPointer(null);
	    	} else if (!inMenu && !gameOver){ // GAME LOOP
	    		
	    		i++;
	    		if (i > generateRate){
	    			generator.generateAsteroid();
	    			i = 0;
	    		}
	    		
	    		if (ss.getScore() > 70 && !generateRateIncreased1){
	    			generateRate -= 40;
	    			generateRateIncreased1 = true;
	    		}
	    		if (ss.getScore() > 180 && !asteroidSpeedIncreased1){
	    			asteroidsSpeed++;
	    			generateRate -= 15;
	    			asteroidSpeedIncreased1 = true;
	    		}
	    		
	    		ss.checkCollisions();
	    		if (actualKey == "space" && keyPressedOnce2){ 
	    			ss.shooting();
	    			keyPressedOnce2 = false;
	    		}
	    		if (actualKey == "up"){ ss.moveUp(false); }
	    		if (actualKey == "up_released"){ ss.moveUp(true); }
	    		if (actualKey == "down"){ ss.moveDown(false); }
	    		if (actualKey == "down_released"){ ss.moveDown(true); }
	    		if (actualKey == "left"){ ss.moveLeft(false); }
	    		if (actualKey == "left_released"){ ss.moveLeft(true); }
	    		if (actualKey == "right"){ ss.moveRight(false); }
	    		if (actualKey == "right_released"){ ss.moveRight(true); }
	    		if (actualKey == "escape"){ inMenu = true; }
	    		
	    		ss.updateBulletsPosition();
	    		generator.updateAsteroidsPosition(asteroidsSpeed);
	    		ss.takeBonus(bonusGenerator.getBonuses());
	    		
	    		if (ss.asteroidDestroy(generator.getAsteroids(), bonusGenerator) == "gameOver"){ // reset all
	    			gameOver = true;
	    			oldScore = (int)ss.getScore();
	    		}
	    		
	    		cm.drawShoots(ss.getShoots());
	    		cm.drawAsteroids(generator.getAsteroids());
	    		cm.drawBonuses(bonusGenerator.getBonuses());
	    		
	    		ss.setSpeedX(ss.getSpeedX() + ss.getFrictionSpeedX());
	    		ss.setSpeedY(ss.getSpeedY() + ss.getFrictionSpeedY());
	    		ss.setX(ss.getX() + ss.getSpeedX());
	    		ss.setY(ss.getY() + ss.getSpeedY());
	    		ss.setSpeedX(ss.getSpeedX() * 0.95);
	    		ss.setSpeedY(ss.getSpeedY() * 0.95);
	    		
	    		cm.drawSpaceship(ss.getX(), ss.getY(), ss.getSpaceWidth(), ss.getSpaceHeight());
	    		
	    		cm.drawString("❤ " + ss.getLives(), screenWidth - 70, screenHeight - 15, MyColor.RED);
	    		cm.drawString("⦿ " + ss.getSpecialShootsLeft(), screenWidth - 150, screenHeight - 15, MyColor.GOLDEN);
	    		cm.drawString("⦾ " + ss.getShootsLeft(), screenWidth - 250, screenHeight - 15, MyColor.WHITE);
	    		cm.drawString("" + (int)ss.getScore(), 20, screenHeight - 15, MyColor.WHITE);
	    	} else if (gameOver){
	    		
	    		cm.drawEndGame(oldScore);
	    		
	    		if (actualKey == "enter" && keyPressedOnce2){
	    			keyPressedOnce2 = false;
	    			gameOver = false;
	    			inMenu = true;
	    			
	    			Date date = new Date();
	    			int year = date.getYear() + 1900;
	    		    String sDate = date.getDay() + "." + date.getMonth() + ". " + year + " " + date.getHours() + ":" + date.getMinutes();
	    		    String finalString = sDate+ ";" + (int)ss.getScore() + "\n";
		    		try {
		    		    Files.write(Paths.get("score.csv"), finalString.getBytes(), StandardOpenOption.APPEND);
		    		}catch (IOException e) {
		    		    System.out.println("Error: to appending score.csv file.");
		    		}
		    		
	    		}
	    	}
	    	cm.CanvasRepaint();
	    }
	}
	
	private static class P_KeyListener extends KeyAdapter {

	    @Override
	    public void keyPressed(KeyEvent e) {
	    	if (!keyPressedOnce){
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) actualKey = "left";
				if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) actualKey = "right";			
				if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) actualKey = "up";	    	  
				if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) actualKey = "down";
				if (keyCode == KeyEvent.VK_ENTER) actualKey = "enter";	    	  
				if (keyCode == KeyEvent.VK_SPACE) actualKey = "space";
				if (keyCode == KeyEvent.VK_ESCAPE) actualKey = "escape";
				if (inMenu){
					keyPressedOnce = true;
				}
				keyPressedOnce2 = true;
				
	    	}
	    }
	    
	    @Override
	    public void keyReleased(KeyEvent e) {
	    	keyPressedOnce = false;
	    	int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) actualKey = "left_released";
			if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) actualKey = "right_released";			
			if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) actualKey = "up_released";	    	  
			if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) actualKey = "down_released";	    	  
			if (keyCode == KeyEvent.VK_ENTER) actualKey = "enter_released";	    	  
			if (keyCode == KeyEvent.VK_SPACE) actualKey = "space_released";	
	    }
	    
	  }

}
