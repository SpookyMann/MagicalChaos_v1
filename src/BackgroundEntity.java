import java.awt.Graphics;

/* BackgroundEntity.java
 * December 12, 2020
 * Represents the background of the game
 */
public class BackgroundEntity extends Entity {

  private double moveSpeed = 60; // horizontal speed

  private Game game; // the game in which the alien exists
  private int width = 1280; // sets width of background
  private int height = 1024; // sets height of background

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */


  public BackgroundEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g; // sets game
    dx = -moveSpeed;  // start off moving left
  } // constructor


  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){

    super.move(delta);
  } // move


@Override
public void collidedWith(Entity other) {

} // collidedWith

//gets width of image
public int getWidth() {
	return this.width;
} // getWidth

public void setX(int i){
	this.x = i;
} // setX


public void repeatImage() {
	if((x + width) < width) {
		x = 0;
	}else {
		x += dx;

	} // else
} // repeatImage


} // BackgroundEntity class
