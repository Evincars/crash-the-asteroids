package Graphics;

public class Bullet extends MovingObject {
	
	public Bullet(int x_, int y_, String type_) {
		super(x_, y_);
		type = type_;
	}

	public String type;
	
}
