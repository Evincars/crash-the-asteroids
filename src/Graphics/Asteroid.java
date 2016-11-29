package Graphics;

public class Asteroid extends MovingObject {

	public Asteroid(int x_, int y_, int width_, int height_, int lives_, String type_, String bonus_) {
		super(x_, y_);
		type = type_;
		width = width_;
		height = height_;
		lives = lives_;
		bonus = bonus_;
	}
	
	public int width = 50, height = 50;
	public String type;
	public String bonus;
	public int lives;

}
