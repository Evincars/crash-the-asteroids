package Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AsteroidGenerator {
	
	private List<Asteroid> asteroids = new ArrayList<Asteroid>();
	private int screenWidth, screenHeight;
	
	public AsteroidGenerator(int screenWidth, int screenHeight){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public List<Asteroid> getAsteroids(){
		return this.asteroids;
	}
	
	public void generateAsteroid(){
		int witchOne = ThreadLocalRandom.current().nextInt(1, 5);
		int x = ThreadLocalRandom.current().nextInt(0, this.screenWidth - 90);
		int w = 70, h = 70, lives = 5;
		String bonus = null;
		int chanceBonus = 0;
		switch(witchOne){
		case 1:
			w = 70;
			h = 60;
			lives = 5;
			break;
		case 2:
			w = 90;
			h = 80;
			lives = 7;
			chanceBonus = ThreadLocalRandom.current().nextInt(1, 5);
			switch(chanceBonus){
			case 1:
				bonus = "lives";
				break;
			case 2:
				bonus = "basicBullet";
				break;
			case 3:
				bonus = "specialBullet";
				break;
			default: bonus = null; break;
			}
			break;
		case 3:
			w = 70;
			h = 60;
			lives = 4;
			break;
		case 4:
			w = 70;
			h = 60;
			lives = 5;
			break;
		default:
			w = 70;
			h = 60;
			lives = 4;
			break;
		}
		asteroids.add(new Asteroid(x, -100, w, h, lives, ""+witchOne, bonus));
	}
	
	public void updateAsteroidsPosition(int speed){
		for (int i = 0; i < this.asteroids.size(); i++){
			if (this.asteroids.get(i).y > screenHeight + 100)
				this.asteroids.remove(i);
			else
				this.asteroids.get(i).y += speed;
		}
	}

}
