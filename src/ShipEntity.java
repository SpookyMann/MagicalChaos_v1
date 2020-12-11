import java.util.ArrayList;

/* ShipEntity.java
 * March 27, 2006
 * Represents player's ship
 */
public class ShipEntity extends Entity {
	
  private Game game; // the game in which the ship exists
  public String sprites [] = {"sprites/reimu_jump.png", "sprites/reimu_dash.png", 
		  						"sprites/reimu_back_dash.png", "sprites/reimu_falling.png"};
  private String currentFrame;
 
  /* construct the player's ship
   * input: game - the game in which the ship is being created
   *        ref - a string with the name of the image associated to
   *              the sprite for the ship
   *        x, y - initial location of ship
   */
  public ShipEntity(Game g, int newX, int newY) {
    super("sprites/reimu_jump.png", newX, newY);  // calls the constructor in Entity
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
    if ((dx > 0) && (x > 930)) {
      return;
    } // if
    // stop at bottom of screen
    if ((dy > 0) && (y > 930)) {
        return;
    } // if
    // stop at top of screen
    if ((dy < 0) && (y < 25)) {
        return;
    } // if
    
    if(dx == 0 && dy == 0) {
    	currentFrame = sprites[0];
    }
    if(dx > 0) {
    	currentFrame = sprites[1];
    }
    if(dx < 0) {
    	currentFrame = sprites[2];
    }
    if(dy > 0) {
    	currentFrame = sprites[3];
    }
      
    super.move(delta);  // calls the move method in Entity
  } // move
  
  public void createSprite(String r) {
		r = currentFrame;
	  	super.createSprite(r);
  }
  

  /* collidedWith
   * input: other - the entity with which the ship has collided
   * purpose: notification that the player's ship has collided
   *          with something
   */
   public void collidedWith(Entity other) {
     if (other instanceof AlienEntity) {
        game.notifyDeath();
     } // if
   } // collidedWith    

} // ShipEntity class
