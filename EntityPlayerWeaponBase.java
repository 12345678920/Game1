package com.game;

import java.awt.Color;

import com.game.GameCanvas.Entity;

public class EntityPlayerWeaponBase {
	
	public int disappear;
	
	@SuppressWarnings("serial")
	public class EntityPlayerWeapon1 extends Entity {
		public Entity noTouch;
		public EntityPlayerWeapon1(GameCanvas gameCanvas, float x, float y, Entity en) {
			gameCanvas.super(x, y, 10, 5, "weapon");
			this.theColor = new Color(0, 0, 255);
			noTouch = en;
			disappear = GameCanvas.frameNo + GameCanvas.rangeLevel1;
		}
	@Override
	public void mainLoop() {
		this.setXSpeed(1);
		for(int i = 0; i < GameCanvas.gameEntities.size(); i++) {
			if(GameCanvas.gameEntities.get(i).equals(noTouch) || GameCanvas.gameEntities.get(i) instanceof EntityPlayerWeapon1) {
				continue;
			}
			Entity e = GameCanvas.gameEntities.get(i);
			if(this.overlaps(e) && !e.isDead && !e.desc.equals("noKill") && e.acceptable()) {
					this.setDead();
					System.out.println(e.desc);
					if(e instanceof GameCanvas.AbstractEnemy) {
						e.getHurt(5);
					}
			}
		}
		if(noTouch.speedingX > 0) {
		for(int i = 0; i < 6; i++) {
			this.newPos();
		}
	} else if(noTouch.speedingX < 0) {
		for(int i = 0; i < 12; i++) {
			this.newPos();
		}
	}
	else {
		for(int i = 0; i < 9; i++) {
			this.newPos();
		}
	}
	if(GameCanvas.frameNo == disappear) {
		this.setDead();
		//GameCanvas.range1LevelUp(2);
	}
	}
	}
}
