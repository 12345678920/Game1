package com.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.beans.beancontext.BeanContext;
import java.io.File;
import java.net.URL;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import com.game.EntityPlayerWeaponBase.*;
/**
 * This is the panel of the game
 * @author Danny
 */
@SuppressWarnings({"serial", "unused"})
public class GameCanvas extends JPanel implements Runnable {
	

	Thread playing;
	public boolean shot = false;
	public static ArrayList<Entity> gameEntities = new ArrayList<>();
	public static ArrayList<Level> storyLevels = new ArrayList<>();
	public static boolean movable;
	public static int levelId = 0;
	static Level level;
	public Entity player = new Entity(30, 290, 30, 30, "player");
	public final Level home = new HomeLevel(1,0);
	public final Level one = new FirstLevel();
	public boolean wPress;
	public static int frameNo = 0;
	//public static int jump = 0;
	public boolean weapon1 = true;
	public boolean weapon2 = false;
	public Class<?>[] weapons = {EntityPlayerWeapon1.class}; 
	public static int rangeLevel1 = 5;
	public static int money = 10;
	public static int rangeLevel1$see = 1;
	
	/**
	 * This function adds a level to this game
	 * @param l is the level to be added
	 */
	public Info info  = new Info();
	public static void addLevel(Level l) {
		storyLevels.add(l);
	}
	public void earn(int money) {
		GameCanvas.money += money;
		info.updateAll();
	}
	public void spend(int money) {
		GameCanvas.money -= money;
		info.updateAll();
	}
	public GameCanvas getMe() {
		return this;
	}
	public void range1LevelUp(int plus) {
		rangeLevel1 += plus;
		++rangeLevel1$see;
		info.updateAll();
	}
	/**
	 * Advances a level, if one is present after the current one
	 */
	public static void nextLevel() {
		try {
		if(storyLevels.get(levelId + 1) != null) {
			setLevel(storyLevels.get(++levelId));
		}
		} catch(Exception e) {
			
		}
	}
	
	public GameCanvas() {
		File file = new File("Adventure.data");
		if(!file.exists()) {
			file.mkdir();
		}
		else if(!file.isDirectory()) {
			file.mkdir();
		}
		storyLevels.add(home);
		storyLevels.add(one);
		//setTitle("Game");
		setSize(450, 350);
		setPreferredSize(new Dimension(450, 350));
		//setResizable(false);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		this.setDoubleBuffered(true);
		setLevel(storyLevels.get(1));
		playing = new Thread(this);
		playing.start();
		this.addKeyListener(l);
		player.setLives(15);
		info.updateAll();
		//System.out.println(home);
	}
	
	public ArrayList<Entity> entityList() {
		return gameEntities;
	}
	
	public static Level getLevel() {
		return level;
	}
	public static void setLevel(Level l) {
		level = l;
		l.start();
	}
	
	@Override
	public void run() {
		Thread curr = Thread.currentThread();
		while(playing == curr) {
			frameNo++;
			//doNothing("yo");
			 //Graphics g = this.getGraphics();
	         //g.clearRect(0, 0, this.getWidth(), this.getHeight());
			level.scroll();
			doNothing(level.scrollX);
			if(level.scrollX >= level.end) {
				doNothing("DoneZ");
				nextLevel();
				documentAll();
			}
			//player.newPos();
			for(int i = 0; i < gameEntities.size(); i++) {
				Entity e = gameEntities.get(i);
				if(e.acceptable()) {
					doNothing(e.desc);
					try {
					e.mainLoop();
					} catch(Exception exc) {
						
					}
					//e.newPos();
				}
				
			}
			if(player.getLives() <= 0) {
				playing = null;
			}
			else if(!(player.y < 350)) {
				playing = null;
			}
			/*if(wPress) {
				for(int i = 0; i < 10; i++) {
					player.setYSpeed(-1);
					player.newPos();
				}
				
			}*/
			repaint();
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				
			}
		}
		
	}
KeyListener l = new KeyListener() {
	@Override
	public void keyPressed(KeyEvent arg0) {
		
		char key = arg0.getKeyChar();
		if(key == 'd') {
			if(!player.touchingAWallLeft()) {
			movable = true;
			level.speedScrollX = 5;
			player.speedingX = 5;
		}
			//else {
				//level.speedScrollX = 0;
				//player.speedingX = 0;
			//}
		}
		else if(key == 'a') {
			if(!player.touchingAWallRight()) {
			if(level.scrollX <= 0) {
				movable = false;
			}
			level.speedScrollX = -5;
			player.speedingX = -5;
			}
		}
		else if(key == 'w') {
			wPress = true;
		}
		else if(key == ' ') {
			if(!shot) {
			shot = true;
			EntityPlayerWeaponBase b = new EntityPlayerWeaponBase();
			level.spawn(b.new EntityPlayerWeapon1(GameCanvas.this, player.x + player.width, player.y + (player.height / 2), player));
			}
			//doNothing("STRINGZ");
			//GameCanvas.this.range1LevelUp(2);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		char key = e.getKeyChar();
		if(key == 'd' || key == 'a') {
			level.speedScrollX = 0;
			player.speedingX = 0;
		}
		else if(key == 'w') {
			wPress = false;
		}
		else if(key == ' ') {
			shot = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
};
	@Override
	public void paintComponent(Graphics comp) {
		super.paintComponent(comp);
		//Graphics g = this.getGraphics();
        //g.clearRect(0, 0, this.getWidth(), this.getHeight());
		//super.paint(comp);
		Graphics2D comp2D = (Graphics2D) comp;
		comp2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		comp2D.setColor(new Color(0, 150, 255));
		comp2D.fill(player);
		for(int i = 0; i < gameEntities.size(); i++) {
			if(gameEntities.get(i).acceptable()) {
				comp2D.setColor(gameEntities.get(i).theColor);
				comp2D.fill(gameEntities.get(i));
			}
		}
		//gameEntities.get(3).setDead();
		comp2D.setColor(new Color(0,0,0));
		//comp2D.setFont(new Font("Courier", 24, Font.BOLD));
		comp2D.drawString(level.title, 5, 15);
	}
	JFrame myFrame = new JFrame("Adventure");
	public static void main(String[] args) {
		GameCanvas gc = new GameCanvas();
		gc.addKeyListener(null);
		
        gc.myFrame.setSize(300,400);
        gc.myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gc.myFrame.setVisible(true);
        gc.myFrame.addKeyListener(gc.l);
        //gc.myFrame.setResizable(false);
        gc.myFrame.add(gc);
        gc.myFrame.pack();
        gc.info.setLocation(500, 0);
        //gc.new Info();
        //gc.myFrame.setResizable(false);
	}
	public class Entity extends Rectangle2D.Float {
		public float x;
		public float y;
		public float width;
		public float height;
		public float speedX;
		public float speedingX;
		public float speedY;
		private Level appearOn;
		public Color theColor;
		public String desc;
		public int lives = 3;
		public boolean isDead;
		/**
		 * The superclass of every game component
		 * @param x - The x coordinate of the top-left corner
		 * @param y - the y coordinate of the top-left corner
		 * @param width - width of component
		 * @param height - height of component
		 * @param desc - description, used for debugging or conditionals
		 * @see Rectangle2D.Float
		 */
		
		public Entity(float x, float y, float width, float height, String desc) {
			super(x, y, width, height);
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.speedX = 0;
			this.speedY = 0;
			this.speedingX = 0;
			this.desc = desc;
			this.isDead = false;
		}
		public void setLives(int lives) {
			this.lives = lives;
		}
		public int getLives() {
			return lives;
		}
		public boolean touchingAWall() {
			for(Entity e : gameEntities) {
				if(this.overlaps(e)) {
					if(e instanceof Wall) {
						if(!e.isDead && e.acceptable()) {
							return true;
						}
					}
				}
			}
			return false;
		}
		public boolean touchingFloor() {
			for(Entity e : gameEntities) {
				if(this.overlaps(e)) {
					if(e instanceof EntityFloor) {
						if(!e.isDead && e.acceptable() && this.y + this.height < e.y + 4) {
							return true;
						}
					}
				}
			}
			return false;
		}
		public boolean touchingAWallLeft() {
			for(Entity e : gameEntities) {
				if(this.overlaps(e)) {
					if(e instanceof Wall) {
						if(!e.isDead && e.acceptable() && (this.x < e.x)) {
							return true;
						}
					}
				}
			}
			return false;
		}
		public boolean touchingAWallRight() {
			for(Entity e : gameEntities) {
				if(this.overlaps(e)) {
					if(e instanceof Wall) {
						if(!e.isDead && e.acceptable() && ((this.x + this.width) > e.x + e.width)) {
							return true;
						}
					}
				}
			}
			return false;
		}
		public void getHurt(int damage) {
			lives -= damage;
		}
		public void heal(int hp) {
			lives += hp;
		}
		public void setSpawnableLevel(Level level) {
			appearOn = level;
		}
		public Level getSpawnableLevel() {
			return appearOn;
		}
		public boolean acceptable() {
			return appearOn == GameCanvas.level;
		}
		public boolean overlaps(Entity r) {
            if (this.x < r.x + r.width && this.x + this.width > r.x && this.y < r.y + r.height && this.y + this.height > r.y) {
                return true;
            }
            return false;
        }
		public boolean isTop(Entity e) {
			return this.y > e.getY() + e.getHeight() && this.y < e.getY();
		}
		public boolean withinXOf(Entity e) {
			if(this.x + this.width > e.x && e.x + e.width < this.x) {
				doNothing("%%");
				return true;
			}
			return false;
		}
		public boolean bottom(Entity e) {
			return withinXOf(e) && touchesBottomOf(e, 2);
		}
		public boolean touchesBottomOf(Entity e, int limit) {
			if(this.y + this.height <= e.y && this.y + this.height >= e.y + limit) {
				return true;
			}
	        return false;
		}
		public void move(float x, float y) {
			this.x += x;
			this.y += y;
			this.setRect(this.x, this.y, this.width, this.height);
		}
		public void setXSpeed(float x) {
			this.speedX = x;
		}
		public void setYSpeed(float y) {
			this.speedY = y;
		}
		public void newPos() {
			move(this.speedX, this.speedY);
		}
		/**
		 * This function should be overriden, and is what gets declared by the game every frame
		 */
		public void mainLoop() {
			if(lives <= 0) {
				setDead();
			}
		}
		public void setDead() {
			this.setSpawnableLevel(null);
			isDead = true;
		}
		public void checkallX() {
			
		}
		
	}
	public class Level {
		public int scrollX;
		public int scrollY;
		public int speedScrollX;
		public int sppedScrollY;
		public int end;
		public String title;
		public Level(int startX, int startY, int end, String title) {
			scrollX = startX;
			scrollY = startY;
			this.end = end;
			this.title = title;
		}
		/**
		 * Gets declared once current game level is set to this one
		 */
		public void start() {
			spawn(GameCanvas.this.player);
		}
		/**
		 * Adds an Entity to this level, and sets the Entity's spawnable level to this one
		 * @param e is the Entity that is spawned
		 */
		public void spawn(Entity e) {
			if(e != player) {
				GameCanvas.gameEntities.add(e);
			}
			e.setSpawnableLevel(this);
		}
		public void scroll(int x, int y) {
			scrollX += x;
			scrollY += y;
		}
		public void scroll() {
			try {
			if(scrollX != 0) {
				GameCanvas.movable = true;
			}
			else {
				movable = false;
			}
			if(GameCanvas.movable) {
				scroll(speedScrollX, sppedScrollY);
				onScroll();
			}
			} catch(Exception e) {
				
			}
		}
		public void onScroll() {
			for(Entity e : GameCanvas.gameEntities) {
				doNothing(e.desc);
				doNothing(0 - GameCanvas.this.player.speedingX);
				//if(!(e instanceof Enemy))
					e.setXSpeed((float)0 - GameCanvas.this.player.speedingX);
			}
		}
		@Override
		public String toString() {
			return "Level Name: " + title + ", Length: " + end;
		}
	}
	/**
	 * This is the home level
	 * @author Danny
	 *
	 */
	public class HomeLevel extends Level {

		public HomeLevel(int startX, int startY) {
			super(startX, startY, 1200, "Home");
		}
		@Override
		public void start() {
			super.start();
			spawn(new EntityFloor(0, 320, 1500, 10));
			spawn(new EntityFloorDecoration(0, 330, 1500, 20, 0, 120, 255));
			spawn(new EntityFloor(200, 290, 30, 10));
			spawn(new EntityFloorDecoration(200, 300, 30, 20, 0, 120, 255));
			spawn(new EntityFloor(400, 260, 30, 10));
			spawn(new EntityFloorDecoration(400, 270, 30, 50, 0, 120, 255));
			spawn(new EntityFloor(600, 230, 30, 10));
			spawn(new EntityFloorDecoration(600, 240, 30, 80, 0, 120, 255));
			spawn(new EntityFloor(800, 260, 30, 10));
			spawn(new EntityFloorDecoration(800, 270, 30, 50, 0, 120, 255));
			spawn(new EntityFloor(1000, 290, 30, 10));
			spawn(new EntityFloorDecoration(1000, 300, 30, 20, 0, 120, 255));
		}
	}
	public class EntityFloor extends Entity {

		public EntityFloor(float x, float y, float width, float height) {
			super(x, y, width, height, "floor");
			this.setSpawnableLevel(GameCanvas.level);
			this.theColor = new Color(0,0,0);
			//GameCanvas.gameEntities.add(this);
			this.setXSpeed(0);
			this.setYSpeed(0);
		}
		public boolean checkalltb(Entity e) {
			ArrayList<Entity> floors = new ArrayList<>();
			for(int i = 0; i < gameEntities.size(); i++) {
				if(gameEntities.get(i).desc == "floor") {
					floors.add(gameEntities.get(i));
				}
			}
			for(int i = 0; i < floors.size(); i++) {
				if(e.isTop(floors.get(i))) {
					return true;
				}
			}
			return false;
		}
		@Override
		public void mainLoop() {
			//doNothing(this.speedX + "hi");
			boolean ov = false;
			withinXOf(player);
			for(Entity e : gameEntities) {
				if(e.desc.equals("floor")) {
					if(!e.touchesBottomOf(player, 9)) {
						ov = true;
					}
				}
			}
			int times = 0;
			for(int i = 0; i < gameEntities.size(); i++) {
				if(gameEntities.get(i).desc.equals("floor")) {
					if(gameEntities.get(i).acceptable())
					times++;
				}
			}
			//if(!(overlaps(GameCanvas.this.player))) {
			//Make this thing only be touched at top
			if(!player.touchingFloor()) {
				if(player.bottom(this)) {
					doNothing("<html>");
				}
				doNothing(level);
				GameCanvas.this.player.setYSpeed(6.5F / times);
				//synchronized(this) {
					player.newPos();
				//}
			}
			else {
				//if(!this.bottom(player)) {
				if(!wPress) {
					
					doNothing(player);
					GameCanvas.this.player.setYSpeed(0);
					player.newPos();
					
				}
				else {
					for(int i = 0; i < 10; i++) {
						GameCanvas.this.player.setYSpeed(-10);
						GameCanvas.this.player.newPos();
						
					}
				}
				//}
			}
			for(Entity e : gameEntities) {
				if(e instanceof Enemy) {
				if(!e.touchingFloor()) {
					if(e.bottom(this)) {
						doNothing("<html>");
					}
					doNothing(level);
					e.setYSpeed(6.5F / times);
					e.newPos();
					//e.y += e.speedY;
				}
				else {
					//if(!this.bottom(player)) {
					//if(!wPress) {
						
						doNothing(player);
						e.setYSpeed(0);
						e.newPos();
						//e.y += e.speedY;
						
					//}
					//else {
						//for(int i = 0; i < 10; i++) {
							//GameCanvas.this.player.setYSpeed(-10);
							//GameCanvas.this.player.newPos();
							
						//}
					//}
					//}
				}
			}
			}
			this.newPos();
		}
	}
	public class EntityFloorDecoration extends Entity {

		public EntityFloorDecoration(float x, float y, float width, float height, int r, int g, int b) {
			super(x, y, width, height, "decoration");
			this.theColor = new Color(153, 102, 49);
		}
		@Override
		public void mainLoop() {
			newPos();
		}
	}
	/**
	 * Is the function to yes, DO NOTHING!!! The annoying doNothings were spamming the debugging console, so I replaced them all with doNothings
	 * @param str - I needed a parameter, so that I don't have to remove every parameter in the old doNothings
	 */
	public void doNothing(Object str) {
		
	}
	public class Info extends JDialog {
		Integer inte = money;
		Integer up1 = GameCanvas.rangeLevel1$see;
		JLabel $ = new JLabel("Coins: " + inte.toString());
		JLabel up2 = new JLabel("Laser Range Level: " + up1.toString());
		JLabel lives = new JLabel("Lives: " + player.getLives());
		public Info() {
			add($);
			add(up2);
			add(lives);
			setLayout(new GridLayout(3,1));
			setVisible(true);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			pack();
		}
		public void updateAll() {
			$.setText("Coins: " + money);
			up2.setText("Laser Range Level: " + GameCanvas.rangeLevel1$see);
			lives.setText("Lives: " + player.getLives());
			pack();
		}
	}
	public void documentAll() {
		try {
		Integer currLevel = levelId;
		Integer coins = money;
		Integer sp1 = GameCanvas.rangeLevel1;
		Integer lives = player.getLives();
		Integer sp1$ = rangeLevel1$see;
		File36.insert("level", currLevel.toString());
		File36.insert("cash", coins.toString());
		File36.insert("rangeUpdate1", sp1.toString());
		File36.insert("rangeUpdate1Num", sp1$.toString());
		File36.insert("lives", lives.toString());
		File36.writeInserted("Adventure.data\\data");
		} catch(Exception e) {
			
		}
	}
	public HashMap<String,String> gameData() {
		return File36.read("Adventure.data\\data");
	}
	public class FirstLevel extends Level {

		public FirstLevel() {
			super(1, 1, 3000, "Dangerous Plains");
			
		}
		@Override
		public void start() {
			
			spawn(new NewEnemy(500, 200, 30, 30, new Color(255, 0, 0)));
			spawn(new NewEnemy(350, 280, 30, 30, new Color(255, 0, 0)));
			spawn(new NewEnemy(750, 200, 30, 30, new Color(255, 0, 0)));
			spawn(new NewEnemy(1000, 200, 30, 30, new Color(255,0,0)));
			spawn(new NewEnemy(2000, 200, 30, 30, new Color(255,0,0)));
			spawn(new NewEnemy(2300, 280, 30, 30, new Color(255,0,0)));
			//spawn(new Enemy(1450, 200, 30, 30, new Color(255,0,0)));
			EntityFloor ef1 = new EntityFloor(0, 320, 1300, 10);
			ef1.theColor = new Color(50, 255, 0);
			spawn(ef1);
			EntityFloor ef2 = new EntityFloor(1750, 320, 750, 10);
			ef2.theColor = new Color(50, 255, 0);
			spawn(ef2);
			EntityFloor ef3 = new EntityFloor(2775, 320, 1000, 10);
			ef3.theColor = new Color(50, 255, 0);
			spawn(ef3);
			EntityFloor ef4 = new EntityFloor(450, 240, 850, 10);
			ef4.theColor = new Color(50, 255, 0);
			spawn(ef4);
			EntityFloor ef5 = new EntityFloor(1400, 280, 100, 10);
			ef5.theColor = new Color(50, 255, 0);
			spawn(ef5);
			EntityFloor ef6 = new EntityFloor(1555, 300, 100, 10);
			ef6.theColor = new Color(50, 255, 0);
			spawn(ef6);
			EntityFloor ef7 = new EntityFloor(2637 - (150 / 2), 300, 150, 10);
			ef7.theColor = new Color(50, 255, 0);
			spawn(ef7);
			EntityFloorDecoration f = new EntityFloorDecoration(0, 330, 1300, 20, 0, 0, 0);
			spawn(f);
			EntityFloorDecoration f2 = new EntityFloorDecoration(1750, 330, 750, 20, 0, 0, 0);
			spawn(f2);
			EntityFloorDecoration f3 = new EntityFloorDecoration(2775, 330, 1000, 20, 0, 0, 0);
			spawn(f3);
			
			spawn(new Coin(100, 300, 10));
			spawn(new Coin(200, 300, 10));
			spawn(new Coin(300, 300, 10));
			spawn(new Coin(400, 300, 10));
			spawn(new LaserEnemy(850, 200));
			
			spawn(new LaserEnemy(1950, 280));
			spawn(new Wall(450, 250, 850, 70, 103, 52, 0));
			//spawn(new SpawnBase(2000, 100, new NewEnemy(2000, 100, 30, 30, new Color(255,0,0)), 45));
		}
	}
	/**
	 * <h1>THIS ENEMY IS NOT RECOMMENDED TO USE AT ALL, USE <i>NewEnemy</i> INSTEAD</h1>
	 * @deprecated This Enemy is <b>Glitched</b>!!!
	 */
	@Deprecated
	public class Enemy extends Entity {

		public int keepAttacking = 0;
		/**
		 * @deprecated
		 */
		@Deprecated
		public Enemy(float x, float y, float width, float height, Color c) {
			super(x, y, width, height, "enemy");
			theColor = c;
		}
		public boolean inSensibleRangeOfPlayer() {
			if(player.x < (this.x - 451) || player.x > (this.x + this.width + 451)) {
				return false;
			}
			return true;
		}
		@Override
		public void mainLoop() {
			//this.newPos();
			if(overlaps(player) && frameNo > keepAttacking) {
				player.getHurt(damage());
				doNothing(player.getLives());
				keepAttacking = frameNo + 20;
				info.updateAll();
			}
			if(lives <= 0) {
				setDead();
			}
			if(this.inSensibleRangeOfPlayer()) {
			if(player.speedingX < 0) { 
				this.setXSpeed(-0.4f);
			}
			else if(player.speedingX == 0) {
				this.setXSpeed(-0.8f);
			}
			else {
				this.setXSpeed(-1.2f);
			}
			}
			else {
				setXSpeed((float)0 - level.scrollX);
			}
			
		}
		public int damage() {
			return 2;
		}
	}
	public class Coin extends Entity {

		public int earn;
		
		public Coin(float x, float y, int worth) {
			super(x, y, 10, 10, "noKill");
			this.theColor = new Color(255, 255, 0);
			earn = worth;
		}
		@Override
		public void mainLoop() {
			newPos();
			if(this.overlaps(player)) {
				setDead();
				GameCanvas.this.earn(earn);
				playSound("coin.wav");
			}
		}
	}
	public void playSound(String audio) {
		try {
			final URL sound = getClass().getResource(audio);
			AudioInputStream s = AudioSystem.getAudioInputStream(sound);
			Clip clip = AudioSystem.getClip();
			clip.open(s);
			clip.start();
		} catch(Exception e) {
			
		}
	}
	public class Wall extends Entity {

		public Wall(float x, float y, float width, float height, int r, int g, int b) {
			super(x, y, width, height, "wall");
			this.theColor = new Color(r,g,b);
		}
		@Override
		public void mainLoop() {
			newPos();
			doNothing(player.touchingAWallLeft());
			if(this.overlaps(player)) {
				if((player.x) < this.x) {
					//player.speedingX = -5;
					//level.scroll(-5, 0);
					//level.scrollX -= 5;
					//level.onScroll();
					level.speedScrollX = 0;
					player.speedingX = 0;
				}
				else {
					//player.speedingX = 5;
					//level.scroll(5, 0);
					//level.scrollX += 5;
					//level.onScroll();
					level.speedScrollX = 0;
					player.speedingX = 0;
				}
			}
		}
	}
	public class Explosion {
		
	}
	public class EnemyLaser extends Entity {
		
		WhichWay way;
		int damage;
		int disappear;
		int keepAttacking = 0;

		public EnemyLaser(float x, float y, WhichWay w, int damage) {
			super(x, y, 10, 5, "noKill");
			this.theColor = new Color(255,0,0);
			way = w;
			this.damage = damage;
			disappear = frameNo + 100;
		}
		@Override
		public void mainLoop() {
			if(way == WhichWay.LEFT) {
				if(player.speedingX > 0) {
					this.setXSpeed(-12);
				}
				else if(player.speedingX == 0) {
					this.setXSpeed(-9);
				}
				else {
					this.setXSpeed(-6);
				}
			}
			else if(way == WhichWay.RIGHT) {
				if(player.speedingX > 0) {
					this.setXSpeed(6);
				}
				else if(player.speedingX == 0) {
					this.setXSpeed(9);
				}
				else {
					this.setXSpeed(12);
				}
			}
			newPos();
			for(Entity e : gameEntities) {
				if(this.overlaps(e)) {
					if(!(e instanceof Enemy || e instanceof AbstractEnemy || e instanceof EntityPlayerWeaponBase.EntityPlayerWeapon1) && !e.desc.equals("noKill") && !e.isDead && e.acceptable() && e != player) {
						setDead();
					}
				}
			}
			if(this.overlaps(player)) {
				setDead();
				//if(frameNo >= keepAttacking) {
					player.getHurt(damage);
				//}
				info.updateAll();
			}
			if(frameNo >= disappear) {
				setDead();
			}
		}
	}
	public enum WhichWay {
		LEFT, RIGHT;
	}
	public class AbstractEnemy extends Entity {

		public AbstractEnemy(float x, float y, float width, float height, Color c) {
			super(x, y, width, height, "enemy");
			this.theColor = c;
		}
		@Override
		public void mainLoop() {
			super.mainLoop();
			newPos();
		}
		public boolean inSensibleRangeOfPlayer() {
			//doNothing(player.x + ": " + (this.x - 451) + ": " + (this.x + 451));
			if(player.x < (this.x - 451) || player.x > (this.x + this.width + 451)) {
				return false;
			}
			return true;
		}
	}
	public class LaserEnemy extends AbstractEnemy {
		
		public int another = 0;
		public int newShot = 0;

		public LaserEnemy(float x, float y) {
			super(x, y, 30, 30, new Color(200,0,0));
			this.setLives(9);
		}
		@Override
		public void mainLoop() {
			super.mainLoop();
			if(this.inSensibleRangeOfPlayer() && frameNo >= another) {
				if((player.x) < this.x) {
					level.spawn(new EnemyLaser(this.x - (2), this.y + (this.height / 2), WhichWay.LEFT, 2));
				}
				else {
					level.spawn(new EnemyLaser(this.x + this.width + (2), this.y + (this.height / 2), WhichWay.RIGHT, 2));
				}
				if(!theColor.equals(new Color(150,0,45))) {
					another = frameNo + 30;
				}
				else {
					another = frameNo + 25;
				}
			}
			if(this.overlaps(player)) {
				if(frameNo > newShot) {
					//doNothing("yyy");
					newShot = frameNo + 20;
					player.getHurt(1);
					info.updateAll();
				}
			}
			if(this.lives <= 5) {
				theColor = new Color(150,0,45);
			}
		}
		
	}
	public class NewEnemy extends AbstractEnemy {
		
		public int keepAttacking = 0;

		public NewEnemy(float x, float y, float width, float height, Color c) {
			super(x, y, width, height, c);
			
		}
		@Override
		public void mainLoop() {
			if(overlaps(player) && frameNo > keepAttacking) {
				player.getHurt(damage());
				doNothing(player.getLives());
				keepAttacking = frameNo + 20;
				info.updateAll();
			}
			if(lives <= 0) {
				setDead();
			}
			if(!this.touchingFloor()) {
				this.setYSpeed(4F);
			}
			else {
				this.setYSpeed(0);
			}
			if(!this.inSensibleRangeOfPlayer()) {
				
			}
			else if(!this.touchingAWallLeft()){
				if(player.speedingX < 0) { 
					this.setXSpeed(player.speedingX + (0 - player.speedingX / 2) * 2);
				}
				else if(player.speedingX == 0) {
					this.setXSpeed(player.speedingX - 4);
				}
				else {
					this.setXSpeed(0 - player.speedingX + (0 - player.speedingX / 2) * 2);
				}
			}
			newPos();
			//System.out.println(x + ":" + y);
		}
		public int damage() {
			return 2;
		}
	}
	public class SpawnBase extends AbstractEnemy {
		
		public Entity spawn;
		public int duration;
		public int another = 0;

		public SpawnBase(float x, float y, Entity e, int duration) {
			super(x, y, 30, 30, Color.black);
			spawn = e;
			this.duration = duration;
			this.desc = "noKill";
			another = frameNo;
		}
		@Override
		public void mainLoop() {
			newPos();
			if(another <= frameNo) {
				level.spawn(spawn);
				another += duration;
			}
		}
	}
	public class EnemyFlame extends AbstractEnemy {
		
		private int more = 0;

		public EnemyFlame(float x, float y) {
			super(x, y, 17.5F, 17.5F, Color.orange);
			this.desc = "noKill";
		}
		@Override
		public void mainLoop() {
			if(!this.touchingFloor()) {
				this.setXSpeed(7.5F);
				this.setYSpeed(1);
			}
			else {
				this.setDead();
			}
			if(this.overlaps(player)) {
				if(frameNo >= more) {
					more = frameNo + 15;
					player.getHurt(3);
					info.updateAll();
				}
			}
		}
	}
}