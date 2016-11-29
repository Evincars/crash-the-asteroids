package Graphics;

import java.util.ArrayList;
import java.util.List;

public class BonusGenerator {

	private List<Bonus> bonuses = new ArrayList<Bonus>();
	
	public List<Bonus> getBonuses(){
		return this.bonuses;
	}
	
	public void addBonus(int x, int y, String type){
		this.bonuses.add(new Bonus(x, y, type));
	}
	
}
