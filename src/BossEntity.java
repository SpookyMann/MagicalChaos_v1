

/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class BossEntity extends Entity {
	 private long lastBossFire =  System.currentTimeMillis();
	 private int firingIntervalBoss = (int)(Math.random( ) * 1000 + 800);
	 private boolean used = false; // true if shot hits something
	 private Entity lasar;
	 private double moveSpeed = 75; // horizontal speed
	 private int lives = 10;
	  

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public BossEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
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
	    
	    // proceed with normal move
	    super.move(delta);
	  } // move


  /* doLogic
   * Updates the game logic related to the aliens,
   * ie. move it down the screen and change direction
   */
  
  public int getLives() {
	  return this.lives;
  }
  public void doLogic() {
    // swap horizontal direction and move down screen 10 pixels
    if(dx < 0){
    //  game.removeEntities.add(entity)
    }
  } // doLogic
 
  public boolean tryToFire() {
	  int randNum = (int)(Math.random() * 15); 
	  if(randNum == 1 && (System.currentTimeMillis() - lastBossFire) > firingIntervalBoss){
		  lastBossFire = System.currentTimeMillis();

		  return true;
		 
            }//if
   	   return false;
  }//tryToFire

  /* collidedWith
   * input: other - the entity with which the alien has collided
   * purpose: notification that the alien has collided
   *          with something
   */
  
  public int getX() {
      return (int) x;
    } // getX
  

  public void collidedWith(Entity other) {
	     // prevents double kills
	     if (used) {
	       return;
	     } // if
	     
	      // if it has hit an alien, kill it!
	     if (other instanceof ShipEntity) {
	       // remove affect entities from the Entity list
	    	
	       
	       // notify the game that the alien is dead
	       game.notifyDeath();
	       used = true;
	     } // if
	     
	     if(other instanceof ShotEntity) {
	    	 lives--;
	    	 if(lives == 0) {
	    		 game.notifyWin();
	    		 game.removeEntity(this);
	    	 }
	     }

	   } // collidedWith
} // AlienEntity class
