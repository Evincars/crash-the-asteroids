package Graphics;

import java.util.ArrayList;
import java.util.List;

public class Spaceship {

	private double x, y, spaceWidth, spaceHeight, speedX, speedY, frictionSpeedX, frictionSpeedY;
	private int screenWidth, screenHeight;
	private int lives = 0;
	private int shootsLeft = 0;
	private int specialShootsLeft = 0;
	private int score = 0;
	private List<Bullet>shoots = new ArrayList<Bullet>();
	
	public Spaceship(){
		this(0.0, 0.0, 20.0, 20.0, 5, 50, 500, 550);
	}
	
	public Spaceship(double x, double y, double w, double h, int lives, int shootsLeft, int screenWidth, int screenHeight){
		this.x = x; this.y = y;
		this.lives = lives; this.shootsLeft = shootsLeft;
		this.spaceWidth = w; this.spaceHeight = h;
		this.speedX = 0;
		this.speedY = 0;
		this.frictionSpeedX = 0;
		this.frictionSpeedY = 0;
		this.screenWidth = screenWidth; this.screenHeight = screenHeight;
	}
	
	public void setXY(double x, double y){
		this.x = x; this.y = y;
	}
	
	public void setX(double x){ this.x = x; }	
	public void setY(double y){ this.y = y; }
	public void setScore(int score){ this.score = score; }
	public void setFrictionSpeedX(double frictionSpeedX){ this.frictionSpeedX = frictionSpeedX; }
	public void setFrictionSpeedY(double frictionSpeedY){ this.frictionSpeedX = frictionSpeedY; }
	public void setSpeedX(double speedX){ this.speedX = speedX; }
	public void setSpeedY(double speedY){ this.speedY = speedY; }
	public void setSpaceWidth(double w){ this.spaceWidth = w; }
	public void setSpaceHeight(double h){ this.spaceHeight = h; }
	public void setLives(int lives){ this.lives = lives; }
	public void setShootsLeft(int countOfShoots){ this.shootsLeft = countOfShoots; }
	public void setSpecialShootsLeft(int countOfShoots){ this.specialShootsLeft = countOfShoots; }
	
	public double getX(){ return this.x; }
	public double getY(){ return this.y; }
	public double getScore(){ return this.score; }
	public double getSpeedX(){ return this.speedX; }
	public double getSpeedY(){ return this.speedY; }
	public double getFrictionSpeedX(){ return this.frictionSpeedX; }
	public double getFrictionSpeedY(){ return this.frictionSpeedY; }
	public double getSpaceWidth(){ return this.spaceWidth; }
	public double getSpaceHeight(){ return this.spaceHeight; }
	public int getLives(){ return this.lives; }
	public int getShootsLeft(){ return this.shootsLeft; }
	public int getSpecialShootsLeft(){ return this.specialShootsLeft; }
	
	public List<Bullet> getShoots(){
		return this.shoots;
	}
	
	public void updateBulletsPosition(){
		for (int i = 0; i < this.shoots.size(); i++){
			if (this.shoots.get(i).y < 0 - this.spaceHeight * 2)
				this.shoots.remove(i);
			else
				this.shoots.get(i).y -= 4;
		}
	}
	
	public String asteroidDestroy(List<Asteroid> a, BonusGenerator bonus){
		for (int i = 0; i < a.size(); i++){
			
			if (this.x < (a.get(i).x + a.get(i).width) && (this.x + this.spaceWidth) > a.get(i).x &&
					this.y < (a.get(i).y + a.get(i).height) && (this.y + this.spaceHeight) > a.get(i).y){
				this.lives--;
				if (this.lives <= 0)
					return "gameOver";
				else {
					a.remove(i);
					return null;
				}
			}
			
			for (int j = 0; j < this.shoots.size(); j++){
				
				if ((this.shoots.get(j).x >= a.get(i).x && this.shoots.get(j).x <= (a.get(i).x + a.get(i).width)) &&
					 this.shoots.get(j).y <= (a.get(i).y + a.get(i).height)){
					if (this.shoots.get(j).type == "special")
						a.get(i).lives = 0;
					else 
						a.get(i).lives--;
					if (a.get(i).lives <= 0){
						if (Integer.parseInt(a.get(i).type) == 2){
							this.score += 3;
						} else this.score += 1;
						if (Integer.parseInt(a.get(i).type) == 2 && a.get(i).bonus != null){
							if (a.get(i).bonus == "lives"){
								bonus.addBonus((int)(a.get(i).x + a.get(i).width / 2 - 5), (int)(a.get(i).y + a.get(i).height / 2 - 5), "lives");
							} else if (a.get(i).bonus == "basicBullet"){
								bonus.addBonus((int)(a.get(i).x + a.get(i).width / 2 - 5), (int)(a.get(i).y + a.get(i).height / 2 - 5), "basicBullet");
							} else if (a.get(i).bonus == "specialBullet"){
								bonus.addBonus((int)(a.get(i).x + a.get(i).width / 2 - 5), (int)(a.get(i).y + a.get(i).height / 2 - 5), "specialBullet");
							}
						}
						a.remove(i);
					}
					this.shoots.remove(j);
					return null;
				}
			}
		}
		return null;
	}
	
	public void takeBonus(List<Bonus> b){
		for (int i = 0; i < b.size(); i++){
			if (this.x < b.get(i).x + 5 && (this.x + this.spaceWidth) > b.get(i).x &&
				this.y < (b.get(i).y + 5) && (this.y + this.spaceHeight) > b.get(i).y){
				
				if (b.get(i).type == "lives"){
					this.lives++;
				} else if (b.get(i).type == "basicBullet"){
					this.shootsLeft += 150;
				} else if (b.get(i).type == "specialBullet"){
					this.specialShootsLeft += 10;
				}
				b.remove(i);
				
			}
		}
	}
	
	public void shooting(){
		if (this.specialShootsLeft > 0){
			this.specialShootsLeft--;
			this.shoots.add(new Bullet((int)this.x + (int)(this.spaceWidth / 2), (int)this.y - 20, "special"));
		} else if (this.shootsLeft > 0){
			this.shootsLeft--;
			this.shoots.add(new Bullet((int)this.x + (int)(this.spaceWidth / 2), (int)this.y - 20, "normal"));
		}
	}
	
	public void checkCollisions(){
		if (this.y <= 0){
			this.y = 0;
			this.frictionSpeedY = 0;
			this.speedY = 0;
		}
		if (this.y + this.spaceHeight >= this.screenHeight){
			this.y = this.screenHeight - this.spaceHeight;
			this.frictionSpeedY = 0;
			this.speedY = 0;
		}
		if (this.x <= 0){
			this.x = 0;
			this.frictionSpeedX = 0;
			this.speedX = 0;
		}
		if (this.x + this.spaceWidth >= this.screenWidth){
			this.x = this.screenWidth - this.spaceWidth;
			this.frictionSpeedX = 0;
			this.speedX = 0;
		}
	}
	
	public void moveUp(boolean released){
		if (!released)
			this.frictionSpeedY = -0.3;
		else this.frictionSpeedY = 0.0;
	}
	public void moveDown(boolean released){ 
		if (!released)
			this.frictionSpeedY = 0.3;
		else this.frictionSpeedY = 0.0;
	}
	public void moveLeft(boolean released){
		if (!released)
			this.frictionSpeedX = -0.3;
		else this.frictionSpeedX = 0.0;
	}
	public void moveRight(boolean released){
		if (!released)
			this.frictionSpeedX = 0.3;
		else this.frictionSpeedX = 0.0;
	}
	
}
