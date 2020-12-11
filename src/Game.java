
/* Game.java
 * Space Invaders Main Program
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Game extends Canvas {


      	private BufferStrategy strategy;   // take advantage of accelerated graphics
        private boolean waitingForKeyPress = true;  // true if game held up until
                                                    // a key is pressed
        private boolean leftPressed = false;  // true if left arrow key currently pressed
        private boolean rightPressed = false; // true if right arrow key currently pressed
        private boolean firePressed = false; // true if firing
        private boolean upPressed = false;  // true if left arrow key currently pressed
        private boolean downPressed = false; 

	private int width = 1000;
        private int height = 1000;
        private boolean gameRunning = true;
        private ArrayList entities = new ArrayList(); // list of entities
                                                      // in game
        private ArrayList removeEntities = new ArrayList(); // list of entities
                                                           // to remove this loop
        private Entity ship;  // the ship
        private Entity background;
        private Entity backgroundRepeat;
        private int lives = 3;
        private double moveSpeed = 300; // hor. vel. of ship (px/s)
        private long lastFire = 0; // time last shot fired
        private long alienFire = 0;
        private long lastAlien = 0;
        private long lastAlienFireTwo = 0;
        private long lastAlienFire = 0;
        private long alienSpawnInterval = 2000;
        private long firingInterval = 300; // interval between shots (ms)
        private long firingIntervalAlien = 700; // interval between shots (ms)
        private long firingIntervalAlienTwo = 300;
        private long deathInterval = 300;
        private long jumpInterval = 20;
        private int alienCount = 0; // # of aliens left on screen
        private int alienScore = 0;
        private String message = ""; // message to display while waiting
                                     // for a key press
        private long lastDeath = 0;
        private boolean logicRequiredThisLoop = false; // true if logic
                                                       // needs to be 
                                                       // applied this loop

    	/*
    	 * Construct our game and set it running.
    	 */
    	public Game() {
    		// create a frame to contain game
    		JFrame container = new JFrame("Space Invaders!");
    
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
    
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(1000,1000));
    		panel.setLayout(null);
    
    		// set up canvas size (this) and add to frame
    		setBounds(0,0,1000,1000);
    		panel.add(this);
    
    		// Tell AWT not to bother repainting canvas since that will
            // be done using graphics acceleration
    		setIgnoreRepaint(true);
    
    		// make the window visible
    		container.pack();
    		container.setResizable(false);
    		container.setVisible(true);
    
    
            // if user closes window, shutdown game and jre
    		container.addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				System.exit(0);
    			} // windowClosing
    		});
    
    		// add key listener to this canvas
    		addKeyListener(new KeyInputHandler());
    
    		// request focus so key events are handled by this canvas
    		requestFocus();

    		// create buffer strategy to take advantage of accelerated graphics
    		createBufferStrategy(2);
    		strategy = getBufferStrategy();
    
    		// initialize entities
    		initEntities();
    
    		// start the game
    		gameLoop();
        } // constructor
    
    
        /* initEntities
         * input: none
         * output: none
         * purpose: Initialise the starting state of the ship and alien entities.
         *          Each entity will be added to the array of entities in the game.
    	 */
    	private void initEntities() {
		background = new BackgroundEntity(this, "sprites/city.png",0,0);
    		entities.add(background);
              // create the ship and put in center of screen
              ship = new ShipEntity(this, 0, 500);
              entities.add(ship);
              
              //spawns first alien
  
                  Entity alien = new AlienEntity(this, "sprites/tounge.png", 
                      500 + (10 * 40),
                      50 + (10 * 30));
                  entities.add(alien);
                  alienCount++;
              
           
    	} // initEntities

        /* Notification from a game entity that the logic of the game
         * should be run at the next opportunity 
         */
         public void updateLogic() {
           logicRequiredThisLoop = true;
         } // updateLogic

         /* Remove an entity from the game.  It will no longer be
          * moved or drawn.
          */
         public void removeEntity(Entity entity) {
           removeEntities.add(entity);
         } // removeEntity

         /* Notification that the player has died.
          */
         public void notifyDeath() {
        	 if(lives > 1) { 
        		 lives--;
        		 entities.remove(ship);
        		 ship = new ShipEntity(this, ship.getX(), ship.getY());
                 entities.add(ship);
        	 }else {
           message = "You FAILED!  Would you like to try again?";
           waitingForKeyPress = true;
        	 }
         } // notifyDeath


         /* Notification that the play has killed all aliens
          */
         public void notifyWin(){
           message = "Well done! You win!";
           waitingForKeyPress = true;
         } // notifyWin

        /* Notification than an alien has been killed
         */
        public void notifyAlienKilled(int x, int y) {
           int dropChance = 0;
           alienCount--;
           alienScore++;
           
           dropChance = (int) (Math.random()*30) + 1;
           
           if(dropChance == 10) {
        	   
           } else if (dropChance == 20){
        	   
           } else if (dropChance == 30) {
        	   
           }
         
           Entity alien = new DeathEntity(this, "sprites/death.png", x, y);
                  entities.add(alien);
                  if ((System.currentTimeMillis() - lastDeath) < deathInterval){
                       return;
                     } else {
                       lastDeath = System.currentTimeMillis();
                       entities.remove(alien);
                     }
             
           
         } // notifyAlienKilled
         
         public void notifyPowerUp() {
        	 
         }
   
        /* Attempt to fire.*/
        public void tryToFire() {
          // check that we've waited long enough to fire
          if ((System.currentTimeMillis() - lastFire) < firingInterval){
            return;
          } // if

          // otherwise add a shot
          lastFire = System.currentTimeMillis();
          ShotEntity shot = new ShotEntity(this, "sprites/redShot.png", 
                            ship.getX(), ship.getY());
          entities.add(shot);
        } // tryToFire
 	
	   public void alienSpawn() {
        	if ((System.currentTimeMillis() - lastAlien) < alienSpawnInterval){
                return;
        	}else {
        	lastAlien = System.currentTimeMillis();
        	int en = (int)(Math.random( ) * 5 + 1);
        	int y = (int)(Math.random( ) * 900 + 200);
        	
           
          	if(en == 1) {
          		System.out.println("1");
                Entity alien = new AlienEntity(this, "sprites/tounge.png", 1000, y);
                entities.add(alien);
               
                alienCount++;
          	}else if(en == 2 && alienScore > 1){
        	   System.out.println("2");
          	  Entity alien = new LevelTwoAlien(this, "sprites/snake.png", 1000, y); 
                      
                    entities.add(alien);
                 
                    alienCount++;
            }else if(en == 3 && alienScore > 4) {
          	  System.out.println("3");
          	  Entity alien = new AlienEntity(this, "sprites/greenFace.png", 1000, y); 
                     
                    entities.add(alien);
                  
                    alienCount++;
            }else if(en == 4 && alienScore > 4 ) {
            	int num = (int)(Math.random( ) * 3 + 1);
            	if(num == 1) {
            	  Entity alien = new Asteroid(this, "sprites/bigAsteroid.png", 1000, y);
                    entities.add(alien);
                    alienCount++;
            	}else if(num == 2) {
            		Entity alien = new Asteroid(this, "sprites/mediumAsteroid.png", 1000, y);
                    entities.add(alien);
                    alienCount++;
            	}else {
            		Entity alien = new Asteroid(this, "sprites/smallAsteroid.png", 1000, y);
                    entities.add(alien);
                    alienCount++;
            	}
            }else if(en == 5 && alienScore > 10) {
          	  System.out.println("5");
          	  Entity alien = new AlienEntity(this, "sprites/creep.png", 1000, y);
                    
                    entities.add(alien);
                    alienCount++;
            }else {
            	System.out.println("last");
                Entity alien = new AlienEntity(this, "sprites/blueEnemy.png", 1000, y); 
                 
                entities.add(alien);
            
                alienCount++;
            }//else
	}
        } // if

	/*
	 * gameLoop
         * input: none
         * output: none
         * purpose: Main game loop. Runs throughout game play.
         *          Responsible for the following activities:
	 *           - calculates speed of the game loop to update moves
	 *           - moves the game entities
	 *           - draws the screen contents (entities, text)
	 *           - updates game events
	 *           - checks input
	 */
	public void gameLoop() {
          long lastLoopTime = System.currentTimeMillis();

          // keep loop running until game ends
          while (gameRunning) {
            
            // calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // get graphics context for the accelerated surface and make it black
           Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(new Color(0,0,0));
            g.fillRect(0,0,1600,1200);
            g.setColor(new Color(128,128,128));
            g.fillRect(0,0,1000,100);
            g.setColor(Color.white);
            String str3 = String.valueOf(alienScore); 
            g.drawString(str3, 50, 50);
            g.drawString("Project 42!", (1000 - g.getFontMetrics().stringWidth("Project 42!"))/2, 50);
            String str4 = String.valueOf(lives); 
            g.drawString("You have " + str4 + " lives", 750, 50);
            // move each entity
            if (!waitingForKeyPress) {
              for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.move(delta);
            	alienSpawn();
            	
              } // for
             for (int i=0; i < entities.size(); i++) {
                  Entity entity = (Entity) entities.get(i);
                  if( entity instanceof LevelTwoAlien) {
                
                  	if(entity.tryToFire() == true) {
                  		
                  		AlienShotDefault shot = new AlienShotDefault(this, "sprites/shot.gif", 
                                entity.getX(), entity.getY());
                  		entities.add(shot);
                  	}
                  }
                if (entity instanceof AlienEntity ) {
                	if(entity.tryToFire() == true) {

                		AlienShotDefault shot = new AlienShotDefault(this, "sprites/blueShot.png", 
                              entity.getX(), entity.getY());
                		entities.add(shot);
                }
                
       	   }//for
              }
            } // if
	
            // draw all entities
            for (int i = 0; i < entities.size(); i++) {
               Entity entity = (Entity) entities.get(i);
	       if(entity instanceof ShipEntity) {
            	   entity.createSprite(null);
               }
               entity.draw(g);
            } // for

            // brute force collisions, compare every entity
            // against every other entity.  If any collisions
            // are detected notify both entities that it has
            // occurred
           for (int i = 0; i < entities.size(); i++) {
             for (int j = i + 1; j < entities.size(); j++) {
                Entity me = (Entity)entities.get(i);
                Entity him = (Entity)entities.get(j);

                if (me.collidesWith(him)) {
                  me.collidedWith(him);
                  him.collidedWith(me);
                } // if
             } // inner for
           } // outer for

           // remove dead entities
           entities.removeAll(removeEntities);
           removeEntities.clear();

           // run logic if required
           if (logicRequiredThisLoop) {
             for (int i = 0; i < entities.size(); i++) {
               Entity entity = (Entity) entities.get(i);
               entity.doLogic();
             } // for
             logicRequiredThisLoop = false;
           } // if

           // if waiting for "any key press", draw message
           if (waitingForKeyPress) {
             g.setColor(Color.white);
             g.drawString(message, (1000 - g.getFontMetrics().stringWidth(message))/2, 250);
             g.drawString("Project 42!", (1000 - g.getFontMetrics().stringWidth("Project 42!"))/2, 300);
           }  // if

            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // ship should not move without user input
            ship.setHorizontalMovement(0);
            ship.setVerticalMovement(0);

            // respond to user moving ship
            if ((leftPressed) && (!rightPressed)) {
              ship.setHorizontalMovement(-moveSpeed);
            }
            if ((rightPressed) && (!leftPressed)) {
              ship.setHorizontalMovement(moveSpeed);
            } 
            
            if ((upPressed) && (!downPressed)) {
                ship.setVerticalMovement(-moveSpeed);
            }
            if ((downPressed) && (!upPressed)) {
                ship.setVerticalMovement(moveSpeed);
            } 
           
            // if spacebar pressed, try to fire
            if (firePressed) {
              tryToFire();
            } // if
		  
		  if((background.getX() + background.getWidth()) < width) {
            	
            	backgroundRepeat = new BackgroundEntity(this, "sprites/city.png",background.getX() + background.getWidth(), 0);
            	entities.add(1,backgroundRepeat);
            	//System.out.println("background's X-coordinate is " + background.getX() );
            	//System.out.println("backgroundRepeat's X-coordinate is " + backgroundRepeat.getX() );
            }
            
            if(backgroundRepeat != null && (backgroundRepeat.getX() + backgroundRepeat.getWidth()) < width) {
            	//System.out.println("background is at " + background.getX() + " and is moving at a speed of " + background.getHorizontalMovement());
            	
            	background.setX(0);
            	entities.remove(backgroundRepeat);
            	backgroundRepeat = null;
            }
           
            // pause
            try { Thread.sleep(100); } catch (Exception e) {}
            
          } // while

	} // gameLoop


        /* startGame
         * input: none
         * output: none
         * purpose: start a fresh game, clear old data
         */
         private void startGame() {
            // clear out any existing entities and initalize a new set
            entities.clear();
            
            initEntities();
            
            // blank out any keyboard settings that might exist
            leftPressed = false;
            rightPressed = false;
            upPressed = false;
           downPressed = false;
            firePressed = false;
            alienScore = 0;
            lives = 3;

         } // startGame


        /* inner class KeyInputHandler
         * handles keyboard input from the user
         */
	private class KeyInputHandler extends KeyAdapter {
                 
                 private int pressCount = 1;  // the number of key presses since
                                              // waiting for 'any' key press

                /* The following methods are required
                 * for any class that extends the abstract
                 * class KeyAdapter.  They handle keyPressed,
                 * keyReleased and keyTyped events.
                 */
		public void keyPressed(KeyEvent e) {

                  // if waiting for keypress to start game, do nothing
                  if (waitingForKeyPress) {
                    return;
                  } // if
                  
                  // respond to move left, right or fire
                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = true;
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = true;
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    firePressed = true;
                  } // if
                  
                  if(e.getKeyCode() == KeyEvent.VK_UP) {
                	 upPressed = true;
                  }
                  if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                	 downPressed = true;
                  }

		} // keyPressed

		public void keyReleased(KeyEvent e) {
                  // if waiting for keypress to start game, do nothing
                  if (waitingForKeyPress) {
                    return;
                  } // if
                  
                  // respond to move left, right or fire
                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = false;
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = false;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_UP) {
                      upPressed = false;
                    } // if

                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                      downPressed = false;
                    } // if

                  if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    firePressed = false;
                  } // if
             

		} // keyReleased

 	        public void keyTyped(KeyEvent e) {

                   // if waiting for key press to start game
 	           if (waitingForKeyPress) {
                     if (pressCount == 1) {
                       waitingForKeyPress = false;
                       startGame();
                       pressCount = 0;
                     } else {
                       pressCount++;
                     } // else
                   } // if waitingForKeyPress

                   // if escape is pressed, end game
                   if (e.getKeyChar() == 27) {
                     System.exit(0);
                   } // if escape pressed

		} // keyTyped

	} // class KeyInputHandler


	/**
	 * Main Program
	 */
	public static void main(String [] args) {
        // instantiate this object
		new Game();
	} // main
} // Game
