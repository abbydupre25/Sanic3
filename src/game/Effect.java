package game;

// TODO multiplier effect
public class Effect {
	private String stat;
	private float boost;

	public Effect(String stat, float boost) {
		this.stat = stat;
		this.boost = boost;
	}
	
	public String getStat() {
		return stat;
	}
	
	public float getBoost() {
		return boost;
	}
}
