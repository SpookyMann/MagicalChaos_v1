/* SlasherEntity.java
 * December 12, 2020
 * Represents the dash attack enemy
 */
public class SlasherEntity extends Entity {
  private double moveSpeed = 800; // horizontal speed

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public SlasherEntity(Game g, String r, int newX, int newY) {
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

    // if we reach right side of screen and are moving right
    // request logic update
    if ((dx > 0) && (x > 950)) {
      game.updateLogic();
    } // if

 // if shot moves off top of screen, remove it from entity list
    if (x < -50) {
      game.removeEntity(this);
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
     // collisions with aliens are handled in ShipEntity
   } // collidedWith

} // SlasherEntity class
