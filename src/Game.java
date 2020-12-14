

/****************************************************************************
* Name:        Magical Chaos!
* Author:      Anika Rao, Kaia Skuter, and Rex Dong
* Date:        December 14, 2020
* Assignment # : Final Java project
* Purpose: An anime-inspired shooter game featuring different enemies and a multitude of powerups. 
* Culminates in a boss fight.
*****************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Canvas {


      	private BufferStrategy strategy;   // take advantage of accelerated graphics
        private boolean waitingForKeyPress = true;  // true if game held up until
                                                    // a key is pressed
        private boolean leftPressed = false;  // true if left arrow key currently pressed
        private boolean rightPressed = false; // true if right arrow key currently pressed
        private boolean firePressed = false; // true if firing
        private boolean upPressed = false;  // true if left arrow key currently pressed
        private boolean downPressed = false; 
        private boolean isBoss = false;
        private boolean stopGame = false;
        private boolean changeSprite =  false;
        private boolean isDeath = false;
	    private boolean isLaser = false;
	    private boolean isBall = false;
	    private boolean logicRequiredThisLoop = false; // true if logic
        private boolean secondShot = false;                                               
                                             
	    private boolean gameRunning = true;
        
        private int chooseFire;
        public int lives = 3;
        private int width = 1280;
        private int height = 1024;
  
        private ArrayList entities = new ArrayList(); // list of entities                             
        private ArrayList removeEntities = new ArrayList(); // list of entities to remove this loop
        
        private Entity boss;
        private Entity heart;
        private Entity laser;
        private Entity alien;
        private Entity ship;  // the ship
        private Entity background;
        private Entity backgroundRepeat;
	    private DeathEntity explosion;
      
        
        private double moveSpeed = 300; // hor. vel. of ship (px/s)
        private long lastFire = 0; // time last shot fired
        private long alienFire = 0;
        private long lastAlien = 0;
        private long lastAlienFireTwo = 0;
        private long lastAlienFire = 0;
        private long alienSpawnInterval = 2000;
	    private long lastFirePower = 0;
        private long firingInterval = 300; // interval between shots (ms)
        private long firingIntervalAlien = 700; // interval between shots (ms)
        private long firingIntervalAlienTwo = 300;
        private long deathInterval = 300;
        private long jumpInterval = 20;
        private int alienCount = 0; // # of aliens left on screen
        private int alienScore = 0;
        private String message = ""; // message to display while waiting
        private int currentPowerUp = 0;               
        private long lastDeath = 0;
                

    	/*
    	 * Construct our game and set it running.
    	 */
    	public Game() {
    		// create a frame to contain game
    		JFrame container = new JFrame("Space Invaders!");
    
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
    
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(1280,1024));
    		panel.setLayout(null);
    
    		// set up canvas size (this) and add to frame
    		setBounds(0,0,1280,1024);
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
    		background = new BackgroundEntity(this, "sprites/midnightForest.png",0,0);
    		entities.add(background);
			// create the ship and put in center of screen
			ship = new ShipEntity(this, 0, 500);
			entities.add(ship);
			  
			//spawns first alien  
			Entity alien = new AlienEntity(this, "sprites/cirno.png", 
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

         /* Notification that the player has died.*/
        public void notifyDeath() {
        	if(lives >= 1) { 
        		 lives--;
        		 entities.remove(ship);
        		 ship = new ShipEntity(this, ship.getX(), ship.getY());
                 entities.add(ship);
        	}//if
			if(lives == 0){
	           message = "You FAILED!  Would you like to try again?";
	          	 
	           waitingForKeyPress = true;
	           boss = null;
	           stopGame = false;
	        }//if
	    } // notifyDeath


        /* Notification that the play has killed all aliens*/
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
           
            dropChance = (int) (Math.random()*40) + 1;
            
           	  /*depending on the number, the player will have access to three different powerups: an extra life,
             a temporary shield or double shots*/
            if(dropChance == 10 && currentPowerUp == 0) {
            	heart = new ItemEntity(this, "sprites/1up.png", x, y);
         	    currentPowerUp = 1;
                entities.add(heart);
            } else if (dropChance == 20 && currentPowerUp == 0){
         	   Entity shield = new ItemEntity(this, "sprites/shield.png", x, y);
         	   entities.add(shield);
         	   currentPowerUp = 2;
            } else if (dropChance == 30 && currentPowerUp == 0) {
         	   currentPowerUp = 3;
         	   Entity twoShot = new ItemEntity(this, "sprites/doubleShot.png", x, y);
         	   entities.add(twoShot);
         	   secondShot = true;         	   
            }//ifElse
            
            //once a alien has been killed, draw the explosion sprite at its coordinates
            Entity alien = new DeathEntity(this, "sprites/death.png", x, y);
            entities.add(alien);
            if ((System.currentTimeMillis() - lastDeath) < deathInterval) {
            	return;
            }else{
			    lastDeath = System.currentTimeMillis();
			    entities.remove(alien);
            }//ifElse
            
            //if the player has killed 50 aliens, make the boss boolean true
            if(alienScore == 20) {
                isBoss = true;
            }//ifElse
        }//NotifyAlienKilled
        
        //runs the code for whichever power up the character obtained
        public void powerUp(Entity alien) {
        	 entities.remove(alien);
        	 
        	 //adds an extra life
        	 if(currentPowerUp == 1) {
        		 lives++;
        		 currentPowerUp = 0;
        		 
        		 //generates a shield for a certain amount of time
        	 }else if(currentPowerUp == 2) {
        		 int two = lives;
        		 lives = 999;		  
        		 currentPowerUp = 0;
        		 givenUsingTimer_whenSchedulingTaskOnce_thenCorrect(two);
        		 
        		 //makes SecondShot true, which allows the player to fire doubleshots
        	  }else if(currentPowerUp == 3) {
        		 secondShot = true;
        		 currentPowerUp = 0;
        	  }//ifElse        
         } // powerUp
   
        //selects which attack the boss will use
          public void chooseFire() {
        	 int choose = (int)(Math.random( ) * 6 + 1);
        	 int ballY = (int)(Math.random( ) * 900 + 1);
        	
        	 if(choose == 1 || choose == 3 || choose == 5) {
        		 Entity redBall = new BallEntity(this, "sprites/redBall.png", boss.getX(), ballY);
        		 entities.add(redBall);
        		 isBall = true;
        	 }else if(choose == 2 || choose == 4 || choose == 6) {
        		laser = new LaserEntity(this, "sprites/laser.png", 0, 279);
    			entities.add(laser);
    			isLaser = true;
        	 }//ifElse
         }//chooseFire
	
	
         //creates and uses a timer
	     public void givenUsingTimer_whenSchedulingTaskOnce_thenCorrect(int two) {
	    	 TimerTask task = new TimerTask() {
    
	         public void run() {
	        	 lives = two;
	         }//run
	     };
    	 Timer timer = new Timer("Timer");	    
        	 long delay = 6000L;
        	 timer.schedule(task, delay);
         }//timer	

         /* Attempt to fire.*/
         public void tryToFire() {
             // check that we've waited long enough to fire
        	 if ((System.currentTimeMillis() - lastFire) < firingInterval)
        		 return;

        	 // otherwise add a shot
        	 lastFire = System.currentTimeMillis();
        	 ShotEntity shot = new ShotEntity(this, "sprites/redShot.png", 
                            ship.getX(), ship.getY());
        	 entities.add(shot);
         } // tryToFire
	
         public void tryToFire2() {
             // check that we've waited long enough to fire
             if ((System.currentTimeMillis() - lastFirePower) < firingInterval)
            	 return;
             // otherwise add a shot
             lastFirePower = System.currentTimeMillis();
             ShotEntity shot = new ShotEntity(this, "sprites/redShot.png", 
                              ship.getX()+45, ship.getY()+45);
             entities.add(shot);
         } // tryToFire2
        
         
         //spawns aliens from off the screen
         public void alienSpawn() {
	    	 if ((System.currentTimeMillis() - lastAlien) < alienSpawnInterval)
                return;
        	 else {
	        	 lastAlien = System.currentTimeMillis();
	        	 int en = (int)(Math.random( ) * 5 + 1);
	        	 int y = (int)((Math.random( ) * 880) + 40);
        
	        	 if(en == 1) {
	        		 //easiest level alien
	        		 Entity alien = new AlienEntity(this, "sprites/cirno.png", 1300, y);
	        		 entities.add(alien);
	        		 alienCount++;
	        	 }else if(en == 2 && alienScore > 1){
	        		 //second easiest level alien
	        		 Entity alien = new AlienEntity(this, "sprites/alice.png", 1300, y); 
	        		 entities.add(alien);
	        		 alienCount++;
        
	        	 }else if(en == 3 && alienScore > 4) {
	        		 //third easiest level alien
	        		 Entity alien = new SlasherEntity  (this, "sprites/sakuya.png", 1300, ship.getY()); 
                     entities.add(alien);
                     alienCount++;
	        	 }else if(en == 4 && alienScore > 4 ) {
	        		 //asteroids
	        		 int num = (int)(Math.random( ) * 3 + 1);
	        		 //choses which size asteroid to spawn
	        		 if(num == 1) {
	        			 Entity alien = new Asteroid(this, "sprites/largeRock.png", 1300, y);
	        			 entities.add(alien);
	        			 alienCount++;
	        		 }else if(num == 2) {
	        			 Entity alien = new Asteroid(this, "sprites/mediumRock.png", 1300, y);
	        			 entities.add(alien);
	        			 alienCount++;
	        		 }else {
	        			 Entity alien = new Asteroid(this, "sprites/smallRock.png", 1300, y);
	        			 entities.add(alien);
	        			 alienCount++;
	        		 }//ifElse
	        	 }else if(en == 5 && alienScore > 10) {
	        		 //hardest alien
	        		 Entity alien = new LevelTwoAlien(this, "sprites/aya.png", 1300, y);
	        		 entities.add(alien);
	        		 alienCount++;
	        	 }else {
	        		
	        		 Entity alien = new AlienEntity(this, "sprites/cirno.png", 1300, y); 
	        		 entities.add(alien);
	        		 alienCount++;
	        	 }//else
        	 }//ifElse
	    	 
        }//alienSpawn

		/*
		 * gameLoop
	         * input: none
	         * output : none
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

			    // move each entity
			    if (!waitingForKeyPress) {
			    	for (int i = 0; i < entities.size(); i++) {
			    		Entity entity = (Entity) entities.get(i);
			    		if(entity instanceof BackgroundEntity && stopGame)
			    			entity.setHorizontalMovement(0);
			    		entity.move(delta);
			    		alienSpawn();
			    	} // for
			    	for (int i=0; i < entities.size(); i++) {
			    		Entity entity = (Entity) entities.get(i);
			    		if( entity instanceof LevelTwoAlien) {
			    			if(entity.tryToFire() == true) {
			    				AlienShotDefault shot = new AlienShotDefault(this, "sprites/energyblast.png", 
                                entity.getX(), entity.getY());
			    				entities.add(shot);
			    			}//if
			    		}//if
			    		if (entity instanceof AlienEntity) {
			    			if(entity.tryToFire() == true) {
			    				AlienShotDefault shot = new AlienShotDefault(this, "sprites/iceblade.png", 
			    			    entity.getX(), entity.getY());
			    				entities.add(shot);
			    			}//if
			    		}//if
			    	}//for
			    } // if
	
			    // draw all entities
			    for (int i = 0; i < entities.size(); i++) {
			    	Entity entity = (Entity) entities.get(i);
			    	if(entity instanceof ShipEntity)
			    		entity.createSprite(null);
			    	entity.draw(g);
			    } // for
			    
			    //if isBoss == true, draw the boss and stop the game. Then make the boss boolean false so that it doesn't keep spawning
			    if(isBoss) {
			    	boss = new BossEntity(this, "sprites/tenshi.png", 1280, 180);
			    	entities.add(boss);
			    	isBoss = false;
			    }//if
			    
			    //stop the game
			    if(boss != null && boss.getX() <= 900) {
			    	stopGame = true;
			    }//if
           
			    //remove DeatheEntity
			    if(isDeath) {
			    	
					entities.remove(explosion);
					removeEntities.add(explosion);
					isDeath = false;
	
			    }//if

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
		                	if((me instanceof AlienEntity && him instanceof ShotEntity && !(him instanceof BossEntity))) {
		                		
		                		//draw the deathEntity
		                		explosion = new DeathEntity(this, "sprites/death.png", me.getX(), me.getY());
		                		entities.add(explosion);
		                		isDeath = true; 
		                	}//if
		                	if((him instanceof AlienEntity && me instanceof ShotEntity)&&!(him instanceof BossEntity)) {
		                		//draw the deathEntity
		                		explosion = new DeathEntity(this, "sprites/death.png", him.getX(),him.getY());
		          
		                		entities.add(explosion);
		                		isDeath = true; 
		                	}//if
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

			    // draw message
			    g.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
			    g.setColor(Color.white);
			    g.drawString(message, (1280 - g.getFontMetrics().stringWidth(message))/2, 250);
			    g.drawString("Magical Chaos! You have " + lives + " lives left!  Your score is " + alienScore, (1280 - g.getFontMetrics().stringWidth("Magical Chaos! You have " + lives +  " lives left! " + " You've killed " + alienScore + " Aliens!"))/2, 50);

			    // clear graphics and flip buffer
			    g.dispose();
			    strategy.show();

	            // ship should not move without user input
	            ship.setHorizontalMovement(0);
	            ship.setVerticalMovement(0);
	
	            // respond to user moving ship
	            if ((leftPressed) && (!rightPressed)) {
	            	ship.setHorizontalMovement(-moveSpeed);
	            }//if
	            if ((rightPressed) && (!leftPressed)) {
	            	ship.setHorizontalMovement(moveSpeed);
	            } //if
	            
	            if ((upPressed) && (!downPressed)) {
	                ship.setVerticalMovement(-moveSpeed);
	            }//if
	            if ((downPressed) && (!upPressed)) {
	                ship.setVerticalMovement(moveSpeed);
	            }//if
	           
	            // if spacebar pressed, try to fire
	            if (firePressed) {
	            	tryToFire();
	            	if(secondShot == true) {
	            		tryToFire2();
	            	}//if
	            } // if
	           /*if stopgame isn't true, keep scrolling the background*/
	            if(!stopGame) {
	            	
	            	/*if the background moves off the screen, spawn another backgroundEntity*/
	            	if((background.getX() + background.getWidth()) < width) {
	            		backgroundRepeat = new BackgroundEntity(this, "sprites/midnightForest.png",background.getX() + background.getWidth(), 0);
	            		entities.add(1,backgroundRepeat);
	            	}//if
	            
	            	if(backgroundRepeat != null && (backgroundRepeat.getX() + backgroundRepeat.getWidth()) < width) {
	            		background.setX(0);
	            		entities.remove(backgroundRepeat);
	            		backgroundRepeat = null;
	            	}//if
	            	
	            	/*otherwise, remove all of the other AlienEntities, make the boss stop moving, and make it fire*/
	            }else {
	            	for(int i = 0; i < entities.size(); i++) {
	            		Entity entity = (Entity) entities.get(i);
	            		if(entity instanceof AlienEntity || entity instanceof Asteroid
	            				|| entity instanceof ShotAlien || entity instanceof AlienShotDefault 
	            				|| entity instanceof DeathEntity || entity instanceof SlasherEntity);
	            			removeEntities.add(entity);
	            		}//ifElse
	            	}//for
				
	            	boss.setHorizontalMovement(0);			
	            	if(boss.tryToFire()) {
	            		chooseFire();
	            	}//if
	            }//ifElse
		
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
            isBoss = false;
            stopGame = false;
            boss = null;
            isDeath = false;
            isLaser = false;
            isBall = false;
            firePressed = false;
            alienScore = 0;
            lives = 3;
            message = "";
            currentPowerUp = 0;
            
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
