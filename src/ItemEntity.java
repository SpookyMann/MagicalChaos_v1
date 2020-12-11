
public class ItemEntity extends Entity{
	
	private Game game; // the game in which the ship exists

	public ItemEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		game = g;
		// TODO Auto-generated constructor stub
	}

	
	public void collidedWith(Entity other) {
		if(other instanceof ShipEntity) {
			game.notifyPowerUp();
		}
	}

}
