/* Astroid.java
 * December 12, 2020
 * Represents the rock obsticals 
 */

//alien class
public class Asteroid extends Entity {

  private double moveSpeed = 75; // horizontal speed
  private long lastAlienFire = 0;
	int firingIntervalAlien = (int)(Math.random( ) * 500 + 400);
  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public Asteroid(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g; // sets game
    dx = -moveSpeed;  // start off moving left
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){
    // if we reach left side of screen and are moving left
    // request logic update
    if ((dx < 0) && (x < 10)) {
      game.updateLogic();   // logic deals with moving entities
                            // in other direction and down screen
    } // if

 // if shot moves off left side of the screen, remove it from entity list
    if (x < -50) {
      game.removeEntity(this);
    } // if

    // if we reach right side of screen and are moving right
    // request logic update
    if ((dx > 0) && (x > 950)) {
      game.updateLogic();
    } // if

    // proceed with normal move
    super.move(delta);
  } // move

  /* collidedWith
   * input: other - the entity with which the alien has collided
   * purpose: notification that the alien has collided
   *          with something
   */
   public void collidedWith(Entity other) {
     // collisions with aliens are handled in ShotEntity and ShipEntity
   } // collidedWith

} // Asteroid class
