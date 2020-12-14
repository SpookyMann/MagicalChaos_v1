/* ShotEntity.java
 * December 12, 2020
 * The players bullet
 */
public class ShotEntity extends Entity {

  private double moveSpeed = 800; // horizontal speed shot moves
  private boolean used = false; // true if shot hits something
  private Game game; // the game in which the ship exists
  /* construct the shot
   * input: game - the game in which the shot is being created
   *        ref - a string with the name of the image associated to
   *              the sprite for the shot
   *        x, y - initial location of shot
   */
  public ShotEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g; // sets game
    dx = moveSpeed; // sets movement speed
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move shot
   */
  public void move (long delta){
    super.move(delta);  // calls the move method in Entity

    // if shot moves off right side of the screen, remove it from entity list
    if (x > 1200) {
      game.removeEntity(this);
    } // if

  } // move


  /* collidedWith
   * input: other - the entity with which the shot has collided
   * purpose: notification that the shot has collided
   *          with something
   */
   public void collidedWith(Entity other) {
     // prevents double kills
     if (used) {
       return;
     } // if

     // if it has hit an alien, kill it!
     if (other instanceof AlienEntity || other instanceof Asteroid || other instanceof LevelTwoAlien ) {
    	 int x = other.getX();
    	 int y = other.getY();

    	 // remove affect entities from the Entity list
    	 game.removeEntity(this);
    	 game.removeEntity(other);

    	 // notify the game that the alien is dead
    	 game.notifyAlienKilled(x, y);
    	 used = true;
     }else if(other instanceof BossEntity){
    	 int x = other.getX();
    	 int y = other.getY();

    	 // remove affect entities from the Entity list
    	 game.removeEntity(this);

    	 // notify the game that the alien is dead
    	 game.notifyAlienKilled(x, y);
    	 used = true;
     }

   } // collidedWith

} // ShotEntity class
