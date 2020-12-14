import java.util.ArrayList;

/* ShipEntity.java
 * Decmeber 12, 2020
 * Represents player's ship
 */
public class ShipEntity extends Entity {

	private Game game; // the game in which the ship exists
	public String sprites [] = {"sprites/reimu.png", "sprites/reimu_jump.png", "sprites/reimu_dash.png",
		  						"sprites/reimu_back_dash.png", "sprites/reimu_fall.png"};
	private String currentFrame;

  /* construct the player's ship
   * input: game - the game in which the ship is being created
   *        ref - a string with the name of the image associated to
   *              the sprite for the ship
   *        x, y - initial location of ship
   */
	public ShipEntity(Game g, int newX, int newY) {
		super("sprites/reimu.png", newX, newY);  // calls the constructor in Entity
		game = g;
		currentFrame = sprites[0];
	} // constructor


  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move ship
   */
	public void move (long delta){
		// stop at left side of screen
		if ((dx < 0) && (x < 25)) {
    	 return;
		} // if
    // stop at right side of screen
		if ((dx > 0) && (x > 1200)) {
			return;
		} // if
    // stop at bottom of screen
		if ((dy > 0) && (y > 890)) {
			return;
		} // if
    // stop at top of screen
		if ((dy < 0) && (y < 10)) {
			return;
		} // if

    // sprite animations
		if(dx == 0 && dy == 0) {
			currentFrame = sprites[0];
		}// if
		if(dx > 0) {
			currentFrame = sprites[2];
		}// if
		if(dx < 0) {
			currentFrame = sprites[3];
		}// if
		if(dy > 0) {
			currentFrame = sprites[4];
		}// if
		if(dy < 0) {
			currentFrame = sprites[1];
		} // if

		super.move(delta);  // calls the move method in Entity
	} // move

  // changes the animation frame
	public void createSprite(String r) {
		r = currentFrame;
	  	super.createSprite(r);
	} // createSprite


  /* collidedWith
   * input: other - the entity with which the ship has collided
   * purpose: notification that the player's ship has collided
   *          with something
   */
	public void collidedWith(Entity other) {
		if (other instanceof AlienEntity || other instanceof SlasherEntity || other instanceof LevelTwoAlien || other instanceof Asteroid || other instanceof BossEntity) {
			game.notifyDeath();
		}else if(other instanceof ItemEntity) {
			game.powerUp(other);
			game.lives++;
		}//elseif
	} // collidedWith

} // ShipEntity class
