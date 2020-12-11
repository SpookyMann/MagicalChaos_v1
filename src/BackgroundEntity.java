import java.awt.Graphics;

/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class BackgroundEntity extends Entity {

  private double moveSpeed = 75; // horizontal speed

  private Game game; // the game in which the alien exists
  private int width = 1000;
  private int height = 1000;

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  
  
  public BackgroundEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
    dx = -moveSpeed;  // start off moving left
  } // constructor
  
  /*public int getArrayRowPos() {
	  return this.arrayRowPos;
  }
  
  public int getArrayColPos() {
	  return this.arrayColPos;
  }*/

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){

    super.move(delta);
  } // move


  /* doLogic
   * Updates the game logic related to the aliens,
   * ie. move it down the screen and change direction
   */
  public void doLogic() {
    // swap horizontal direction and move down screen 10 pixels
    /*dx *= -1;
    y += 10;

    // if bottom of screen reached, player dies
    if (y > 1000) {
      game.notifyDeath();
    } // if*/
  } // doLogic

@Override
public void collidedWith(Entity other) {
	// TODO Auto-generated method stub
	
}

public int getWidth() {
	return this.width;
}
	
public void setX(int i){
	this.x = i;
}
public void repeatImage() {
	if((x + width) < width) {
		x = 0;
	}else {
		x += dx;
		
	}
}
  
 

  /* collidedWith
   * input: other - the entity with which the alien has collided
   * purpose: notification that the alien has collided
   *          with something
   */
   /*public void collidedWith(Entity other) {
     // collisions with aliens are handled in ShotEntity and ShipEntity
   } // collidedWith
  
   public void collidedWith(Entity other, Entity[][] alienArray) {}*/

} // AlienEntity class


