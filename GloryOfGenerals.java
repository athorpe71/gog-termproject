import java.util.ArrayList;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.imageio.*;

//Glory Of Generals - Alex Thorpe - Mt. Lebanon AP Computer Science Term Project

//All x and y coordinates are the center x and center y

//Unit, town, and grass tiles from the open source game, freeciv - http://www.freeciv.org/
//Water tile from - http://telepath.wikia.com/wiki/File:WaterTile.png
//Sound files from World Conqueror 3 - https://play.google.com/store/apps/details?id=com.easytech.wc3&hl=en

//Explosion and Smoke effects from:
// -http://www.shutterstock.com/s/animation/search-vectors.html
// -http://www.dreamstime.com/stock-illustration-explode-effect-animation-smoke-cartoon-explosion-frames-image61344941

public class GloryOfGenerals extends JComponentWithEvents{
	public boolean gameIsRunning=true;
	public int endBannerSize = 200;
	public String soundCapture = "sfx_capture.wav";
	public String soundExplode = "sfx_explode.wav";
	public String soundTankFire = "sfx_fire.wav";
	public String soundArtilleryFire = "sfx_fire2.wav";
	public String soundGun = "sfx_infantry.wav";
	public String soundMarch = "sfx_infantrymove.wav";
	public String soundProduce = "sfx_produce.wav";
	public String soundSelect = "sfx_select.wav";
	public String soundMoveHeavy = "sfx_tankmove.wav";


	public int smokeCreateTimer = 0;//triggers at max, then resets
	public int smokeCreateTimerMax = 1;
	boolean actioned = false;//Changes quickly - turn of selection when action is taken
	public int turnNumber = 0;
	public boolean myTurn = true;//if false, do AI
	public double moveFactor = 0.2;

	Color menuColor = new Color(200,209,100);
	public int endTurnButtonX = 30;
	public int endTurnButtonY = 80;
	public int endTurnButtonWidth = 80;
	public int endTurnButtonHeight = 30;
	public int unitButtonImageScale = 2;
	public int infantryButtonX = 180;
	public int infantryButtonY = 60;
	public int artilleryButtonX = 320;
	public int artilleryButtonY = 60;
	public int tankButtonX = 480;
	public int tankButtonY = 60;

	public String germanFactionIcon = "German.png";
	public String frenchFactionIcon = "French.png";
	public final int FRENCH_FACTION = 0;
	public final int GERMAN_FACTION = 1;
	public int switchFButtonX = 560;//switch faction
	public int switchFButtonY = 40;
	public int switchFButtonWidth = 140;
	public int switchFButtonHeight = 50;

	String gameFont = "Arial";
	int tileSize = 40;//20 30 40 60
	int unitSize = 60;
	
	public int menuOffset = 120;//Y displacement down for menu
	public int viewMoveSpeed = tileSize;
	static int windowWidth = 1100;
	static int windowHeigth = 800;
	public int xViewOffset = 0;
	public int yViewOffset = menuOffset;

	public boolean playerHasWon = false;
	public boolean enemyHasWon = false;
	public int playerMoney = 0;
	public int enemyMoney = 0;
	final static int PLAYER_VICTORY = 0;
	final static int ENEMY_VICTORY = 1;
	
	public boolean showProductionMenu = false;

	int warMapWidth = 38;
	int warMapHeight = 30;
	String grassImage = "Grass.png";
	String waterImage = "Water.png";
	final static int GRASS_TILE = 1;
	final static int WATER_TILE = 0;
	int[][] warMap = new int[warMapWidth][warMapHeight];
	//Unit Variables
	final static int UNIT_TYPE = 0;//Index in unit arrays |
	//0-infantry,1-artillery,2-tank//                     |
	final static int UNIT_FACTION = 1;//                 \/
	//0-player,1-enemy
	final static int UNIT_X = 2;
	final static int UNIT_Y = 3;
	final static int UNIT_HEALTH = 4;
	final static int UNIT_ID = 5;//------------------------
	//Important unit and faction stats
	public int healthRegen = 3;
	public int playerFaction = 0;
	public int enemyFaction = 1;
	public int infantryPrice = 70;
	public int artilleryPrice = 100;
	public int tankPrice = 120;
	public int maxInfantryHealth = 40;
	public int maxArtilleryHealth = 30;
	public int maxTankHealth = 60;
	public int infantryDamage = 7;
	public int artilleryDamage = 11;
	public int tankDamage = 10;
	public int infantryMove = 2;
	public int artilleryMove = 1;
	public int tankMove = 3;
	public int infantryAttackRange = 1;
	public int artilleryAttackRange = 2;
	public int tankAttackRange = 1;
	String infantryImage = "Infantry.png";
	String artilleryImage = "Artillery.png";
	String tankImage = "Tank.png";
	int nextUnitID = 1;
	boolean unitSelected = false;
	int unitIDSelected = 0;
	ArrayList<Integer> unitIDsMoved = new ArrayList<Integer>();
	ArrayList<Integer[]> allUnits = new ArrayList<Integer[]>();
	//Town Variables
	final static int TOWN_ID = 0;
	final static int TOWN_FACTION = 1;//Index in town arrays
	final static int TOWN_NAME = 2;
	final static int TOWN_X = 3;
	final static int TOWN_Y = 4;
	final static int TOWN_MONEY = 5;
	String townImage = "Town.png";
	int nextTownID=1;
	int townIDSelected=0;
	boolean townSelected = false;
	ArrayList<String> townNames = new ArrayList<String>();
	ArrayList<Integer[]> allTowns = new ArrayList<Integer[]>();
	ArrayList<Integer> townIDsThatProduced = new ArrayList<Integer>();
	//Move Units Queue
	ArrayList<Integer[]> unitsToMove = new ArrayList<Integer[]>();//ID,targetX,targetY
	//Effects - Smoke, Explosion
	//smoke
	String smokeFrame1 = "smoke1.png";
	String smokeFrame2 = "smoke2.png";
	String smokeFrame3 = "smoke3.png";
	String smokeFrame4 = "smoke4.png";
	ArrayList<Double[]> allSmoke = new ArrayList<Double[]>();//frameFactor - 0,1,2,3,4 - going up by 10
	public int smokeMaxFrame = 4;
	public int effectFrameInterval = 10;
	//explosions
	String explodeFrame1 = "explode1.png";
	String explodeFrame2 = "explode2.png";
	String explodeFrame3 = "explode3.png";
	String explodeFrame4 = "explode4.png";
	String explodeFrame5 = "explode5.png";
	String explodeFrame6 = "explode6.png";
	ArrayList<Integer[]> allExplosions = new ArrayList<Integer[]>();//x,y,frame,damage


	private static int linearInterpolation(int from, int to, double factor){
		return (int)(from+(to-from)*factor);
	}
	public void print(String s){
		System.out.println(s);
	}//Created for debugging purposes
	public int distanceBetween(int x, int y, int xx, int yy){
		return (int)Math.sqrt(Math.pow(Math.abs((int)(x-xx)),2)+Math.pow(Math.abs((int)(y-yy)),2));
	}
	//SPAWN IN EFFECTS
	public void addSmoke(int x, int y){
		Random random = new Random();
		Random randomier = new Random();
		double tempRot = (random.nextDouble()/(6.28))*(6.28);
		double tempSize = ((randomier.nextDouble()/(0.9))*(0.2))+(0.1);
		Double[] temp = {(double)x,(double)y,0.0,tempSize,tempRot};
		allSmoke.add(temp);
	}
	public void addExplosion(int x, int y, int damage){
		Integer[] temp = {x,y,0,damage};
		allExplosions.add(temp);
	}

	//SPAWN UNITS AND BUILDINGS
	public void addInfantry(int faction,int x, int y){
		Integer[] temp = {0,faction,x,y+menuOffset,maxInfantryHealth,nextUnitID};
		nextUnitID++;
		allUnits.add(temp);
	}
	public void addArtillery(int faction,int x, int y){
		Integer[] temp = {1,faction,x,y+menuOffset,maxArtilleryHealth,nextUnitID};
		nextUnitID++;
		allUnits.add(temp);
	}
	public void addTank(int faction,int x, int y){
		Integer[] temp = {2,faction,x,y+menuOffset,maxTankHealth,nextUnitID};
		nextUnitID++;
		allUnits.add(temp);
	}
	public void addTown(int faction,String name,int x, int y,int money){
		townNames.add(name);
		Integer[] temp = {nextTownID,faction,townNames.size()-1,x,y,money};
		nextTownID++;
		allTowns.add(temp);
	}

	//UTILITIES
	public void playMoveSound(int type){
		if(type==0)//infantry
			play(soundMarch);
		else
			play(soundMoveHeavy);
	}
	public void playAttackSound(int type){
		if(type==0)//infantry
			play(soundGun);
	}
	public int centerOnTile(int n){//gets nearest tile center position
		return (tileSize/2)+(n/tileSize)*tileSize;
	}
	public int onTile(int n){//board index --> position
		return (n*tileSize)+(tileSize/2);
	}
	public int asTile(int n){//position --> board index
		return (n-(tileSize/2))/tileSize;
	}
	public boolean isEndTurnButtonClicked(int pointX, int pointY){
		boolean withinX = ((pointX>endTurnButtonX)&&
			(pointX<endTurnButtonX+endTurnButtonWidth));
		boolean withinY = ((pointY>endTurnButtonY)&&
			(pointY<endTurnButtonY+endTurnButtonHeight));
		return (withinX&&withinY);
	}
	public boolean isSwitchFactionButtonClicked(int pointX, int pointY){
		boolean withinX = ((pointX>switchFButtonX)&&
			(pointX<switchFButtonX+switchFButtonWidth));
		boolean withinY = ((pointY>switchFButtonY)&&
			(pointY<switchFButtonY+switchFButtonHeight));
		return (withinX&&withinY);
	}
	public boolean isUnitButtonClicked(int pointX, int pointY, int buttX, int buttY){
		boolean withinX = ((pointX>buttX-(tileSize*unitButtonImageScale)/2)
			&&(pointX<buttX+(tileSize*unitButtonImageScale)/2));
		boolean withinY = ((pointY>buttY-(tileSize*unitButtonImageScale)/2)
			&&(pointY<buttY+(tileSize*unitButtonImageScale)/2));
		if(withinX&&withinY)play(soundProduce);
		return (withinX&&withinY);
	}
	public boolean isPointOnMe(int pointX, int pointY, int myX, int myY){
		boolean withinX = ((pointX>myX-(tileSize/2))&&(pointX<myX+(tileSize/2)));
		boolean withinY = ((pointY>myY-(tileSize/2))&&(pointY<myY+(tileSize/2)));
		return (withinX&&withinY);
	}
	public boolean isFriendlyUnitAtPosition(int x, int y){
		for(Integer[] unit:allUnits){
			if(unit[UNIT_X]==x&&unit[UNIT_Y]==y&&
				unit[UNIT_FACTION]==playerFaction)
				return true;
		}
		return false;
	}
	public boolean isEnemyUnitAtPosition(int x, int y){
		for(Integer[] unit:allUnits){
			if(unit[UNIT_X]==x&&unit[UNIT_Y]==y&&
				unit[UNIT_FACTION]==enemyFaction)
				return true;
		}
		return false;
	}
	public boolean isUnitMovingToPosition(int x, int y){
		for(Integer[] moveData:unitsToMove){
			if(moveData[1]==x&&moveData[2]==y){
				return true;
			}
		}
		return false;
	}
	public void deleteUnitWithID(int id){
		for(int i=0;i<allUnits.size();i++){
			if((allUnits.get(i))[UNIT_ID]==id){
				allUnits.remove(i);
				print("Unit ID "+id+" Deleted");
			}
		}
	}
	public void attackUnitAtPosition(int x, int y, int attackerType){
		for(Integer[] unit:allUnits){
			if(unit[UNIT_X]==x&&unit[UNIT_Y]==y){
				int tempDamage=0;
				if(attackerType==0){
					tempDamage=infantryDamage;
				}
				if(attackerType==1){
					tempDamage=artilleryDamage;
				}
				if(attackerType==2){
					tempDamage=tankDamage;
				}
				Random random = new Random();
				int finalDamage=random.nextInt(tempDamage+1)+(tempDamage-2);
				unit[UNIT_HEALTH]-=finalDamage;
				addExplosion(x,y,finalDamage);//Kaboom
				play(soundExplode);
				print("finalDamage: "+finalDamage);
				if(unit[UNIT_HEALTH]<=0)
					deleteUnitWithID(unit[UNIT_ID]);
			}
		}
	}
	public void trySelectUnit(int pointX, int pointY){//selects or deselects
		for(Integer[] unit:allUnits){
			boolean inViewX = ((unit[UNIT_X]<windowWidth)&&(unit[UNIT_X]>0));
			boolean inViewY = ((unit[UNIT_Y]<windowHeigth)&&(unit[UNIT_Y]>menuOffset));
			if(isPointOnMe(pointX,pointY,unit[UNIT_X],unit[UNIT_Y])
				&&unit[UNIT_FACTION]==playerFaction
			&&inViewX&&inViewY){//last checks if in view
				unitIDSelected=unit[UNIT_ID];
				if(unitSelected){
					play(soundSelect);
					unitSelected=false;
				}
				else{
					play(soundSelect);
					unitSelected=true;
				}
				return;
			}
		}
	}
	public void tryMoveUnit(int pointX, int pointY){//moves or attacks
		for(Integer[] unit:allUnits){
			if(unit[UNIT_ID]==unitIDSelected&&(
				unitIDsMoved.contains(unit[UNIT_ID])==false)){
				//correct unit and not moved yet
				//Unit Move Range
				int tempMove = 0;
				if(unit[UNIT_TYPE]==0)
					tempMove=infantryMove;
				if(unit[UNIT_TYPE]==1)
					tempMove=artilleryMove;
				if(unit[UNIT_TYPE]==2)
					tempMove=tankMove;
				boolean xInMoveRange =(pointX<=(unit[UNIT_X]+(tileSize/2)+
					(tempMove*tileSize))&&
					pointX>=(unit[UNIT_X]-(tileSize/2))-(tempMove*tileSize));
				boolean yInMoveRange =(pointY<=(unit[UNIT_Y]+(tileSize/2)+
					(tempMove*tileSize))&&
					pointY>=(unit[UNIT_Y]-(tileSize/2))-(tempMove*tileSize));
				int newPositionX=centerOnTile(pointX);
				int newPositionY=centerOnTile(pointY);
				if((isEnemyUnitAtPosition(newPositionX,newPositionY)==true)){
					int tempAttackRange = 0;//Unit Attack Range
					if(unit[UNIT_TYPE]==0)
						tempAttackRange=infantryAttackRange;
					if(unit[UNIT_TYPE]==1)
						tempAttackRange=artilleryAttackRange;
					if(unit[UNIT_TYPE]==2)
						tempAttackRange=tankAttackRange;
					boolean xInAttackRange =(pointX<=(unit[UNIT_X]+(tileSize/2)+
						(tempAttackRange*tileSize))&&
						pointX>=(unit[UNIT_X]-(tileSize/2))-(tempAttackRange*tileSize));
					boolean yInAttackRange =(pointY<=(unit[UNIT_Y]+(tileSize/2)+
						(tempAttackRange*tileSize))&&
						pointY>=(unit[UNIT_Y]-(tileSize/2))-(tempAttackRange*tileSize));
					if(xInAttackRange&&yInAttackRange){
						playAttackSound(unit[UNIT_TYPE]);
						attackUnitAtPosition(newPositionX,newPositionY,unit[UNIT_TYPE]);
						actioned=true;
						unitIDsMoved.add(unit[UNIT_ID]);
					}
				}
				if(xInMoveRange&&yInMoveRange&&(
					isFriendlyUnitAtPosition(newPositionX,newPositionY)==false)&&
					(isEnemyUnitAtPosition(newPositionX,newPositionY)==false)){
					playMoveSound(unit[UNIT_TYPE]);
					Integer[] lerpData = {(int)unit[UNIT_ID],
						(int)newPositionX,(int)newPositionY};
					unitsToMove.add(lerpData);
					tryClaimTown(newPositionX,newPositionY,unit[UNIT_FACTION]);
					actioned=true;
					unitIDsMoved.add(unit[UNIT_ID]);//cant move anymore this turn
				}
			}
		}
	}//Town Functions
	public void trySelectTown(int pointX, int pointY){//selects or deselects town
		for(Integer[] town:allTowns){
			if(isPointOnMe(pointX,pointY,town[TOWN_X],town[TOWN_Y])&&
				town[TOWN_FACTION]==playerFaction
				&&(isFriendlyUnitAtPosition(centerOnTile(pointX),
					centerOnTile(pointY))==false)){
				townIDSelected=town[TOWN_ID];
				if(townSelected){
					play(soundSelect);
					townSelected=false;
					showProductionMenu=false;
				}else{
					play(soundSelect);
					townSelected=true;
					showProductionMenu=true;
				}
				return;
			}
		}
		townSelected=false;//deselect when clicking on nothing
		showProductionMenu=false;
	}
	public void tryClaimTown(int x, int y,int faction){
		for(Integer[] town:allTowns){
			if(town[TOWN_X]==x&&town[TOWN_Y]==y){
				townSelected=false;
				unitSelected=false;
				if(town[TOWN_FACTION]==enemyFaction){
				//loot the town during capture
					playerMoney+=town[TOWN_MONEY];
					townIDsThatProduced.add(town[TOWN_ID]);
				}
				if(town[TOWN_FACTION]!=faction)play(soundCapture);
				town[TOWN_FACTION]=faction;
			}
		}
	}
	public void collectTownResources(){
		for(Integer[] town:allTowns){
			if(town[TOWN_FACTION]==playerFaction)
				playerMoney+=town[TOWN_MONEY];
			if(town[TOWN_FACTION]==enemyFaction)
				enemyMoney+=town[TOWN_MONEY];
		}
	}
	public int checkVictory(){
		int playerUnits=0;
		int enemyUnits=0;
		int playerTowns=0;
		int enemyTowns=0;
		for(Integer[] unit:allUnits){
			if(unit[UNIT_FACTION]==playerFaction)
				playerUnits++;
			if(unit[UNIT_FACTION]==enemyFaction)
				enemyUnits++;
		}
		for(Integer[] town:allTowns){
			if(town[TOWN_FACTION]==playerFaction)
				playerTowns++;
			if(town[TOWN_FACTION]==enemyFaction)
				enemyTowns++;
		}
		if(playerUnits==0&&playerTowns==0){
			return ENEMY_VICTORY;
		}
		if(enemyUnits==0&&enemyTowns==0)
			return PLAYER_VICTORY;
		return 42;
	}
	public boolean lerpDataIsValid(int id, int x, int y, int initialX, int initialY){
		if(distanceBetween(initialX,initialY,x,y)>2*tileSize)
			return false;
		return true;
	}
	public void lerpUnitsToMove(){
		for(Integer[] unitData:unitsToMove){
			for(Integer[] unit:allUnits){
				if(unitData[0]==unit[UNIT_ID]){//is the unit to move
					if(lerpDataIsValid(unitData[0],unitData[1],unitData[2],unit[UNIT_X],unit[UNIT_Y])==false)
						if(unit[UNIT_FACTION]==enemyFaction){
							unitsToMove.remove(unitData);//prevent AI going bonkers
							unit[UNIT_X]=centerOnTile(unit[UNIT_X]);
							unit[UNIT_Y]=centerOnTile(unit[UNIT_Y]);
						}
					unit[UNIT_X]=linearInterpolation((int)unit[UNIT_X],
						(int)unitData[1],moveFactor);
					unit[UNIT_Y]=linearInterpolation((int)unit[UNIT_Y],
						(int)unitData[2],moveFactor);
					if(smokeCreateTimer==smokeCreateTimerMax){
						addSmoke(unit[UNIT_X],unit[UNIT_Y]);//Smoke Effect
						smokeCreateTimer=0;
					}else{
						smokeCreateTimer++;
					}
					boolean inRangeX = ((unit[UNIT_X]>=(int)unitData[1]-(tileSize/4))
						&&(unit[UNIT_X]<=(int)unitData[1]+(tileSize/4)));
					boolean inRangeY = ((unit[UNIT_Y]>=(int)unitData[2]-(tileSize/4))
						&&(unit[UNIT_Y]<=(int)unitData[2]+(tileSize/4)));
					if(inRangeX&&inRangeY){
						unit[UNIT_X]=centerOnTile((int)unit[UNIT_X]);
						unit[UNIT_Y]=centerOnTile((int)unit[UNIT_Y]);
						unitData[0]=(-1);//effectively removes it
					}
				}
			}
		}
	}
	public void fixAllOffGrids(){
		for(Integer[] unit:allUnits){
			unit[UNIT_X]=centerOnTile(unit[UNIT_X]);
			unit[UNIT_Y]=centerOnTile(unit[UNIT_Y]);
		}
	}

	//AI-------------------------------------
	public void runAI(){
		boolean producedYet =false;
		for(Integer[] town:allTowns){
			if(town[TOWN_FACTION]==enemyFaction){
				producedYet=false;
				if(townIDsThatProduced.contains(town[TOWN_ID])==false){
					Random random = new Random();
					int randomInteger=random.nextInt(10);
					if(randomInteger>4){
						//dont do anything
					}else{
						if(enemyMoney>=tankPrice&&producedYet==false&&
							isEnemyUnitAtPosition(town[TOWN_X],town[TOWN_Y])==false){
							addTank(enemyFaction,town[TOWN_X],town[TOWN_Y]-menuOffset);
							enemyMoney-=tankPrice;
							townIDsThatProduced.add(town[TOWN_ID]);
							producedYet=true;
						}
						if(enemyMoney>=artilleryPrice&&producedYet==false&&
							isEnemyUnitAtPosition(town[TOWN_X],town[TOWN_Y])==false){
							addArtillery(enemyFaction,town[TOWN_X],town[TOWN_Y]-menuOffset);
							enemyMoney-=artilleryPrice;
							townIDsThatProduced.add(town[TOWN_ID]);
							producedYet=true;
						}
						if(enemyMoney>=infantryPrice&&producedYet==false&&
							isEnemyUnitAtPosition(town[TOWN_X],town[TOWN_Y])==false){
							addInfantry(enemyFaction,town[TOWN_X],town[TOWN_Y]-menuOffset);
							enemyMoney-=infantryPrice;
							townIDsThatProduced.add(town[TOWN_ID]);
							producedYet=true;
						}
					}
				}
			}
		}
		int closestPlayerX = 0;
		int closestPlayerY = 0;
		int currentClosestPlayerDistance = 9999;
		for(Integer[] unit:allUnits){
			if(unit[UNIT_FACTION]==enemyFaction){//All enemy/AI controlled units
				//Calculate distance and store if smallest found yet
				for(Integer[] playerUnit:allUnits){
					if(playerUnit[UNIT_FACTION]==playerFaction){
						int tempDistance=distanceBetween(unit[UNIT_X],unit[UNIT_Y],
							playerUnit[UNIT_X],playerUnit[UNIT_Y]);
						if(tempDistance<currentClosestPlayerDistance){
							currentClosestPlayerDistance=(tempDistance);
							closestPlayerX = playerUnit[UNIT_X];
							closestPlayerY = playerUnit[UNIT_Y];
						}
					}
				}
				//See if a city is closer which is empty, therefore not targeted by previous code
				for(Integer[] playerTown:allTowns){
					if(playerTown[TOWN_FACTION]==playerFaction){
						int tempDistance=distanceBetween(unit[UNIT_X],unit[UNIT_Y],
							playerTown[TOWN_X],playerTown[TOWN_Y]);
						if(tempDistance<currentClosestPlayerDistance){
							currentClosestPlayerDistance=(tempDistance);
							closestPlayerX = playerTown[TOWN_X];
							closestPlayerY = playerTown[TOWN_Y];
						}
					}
				}
				//Search for enemy in attack range
				boolean playerWasInAttackRange = false;
				int tempAttackRange=0;
				if(unit[UNIT_TYPE]==0)
					tempAttackRange = infantryAttackRange;
				if(unit[UNIT_TYPE]==1)
					tempAttackRange = artilleryAttackRange;
				if(unit[UNIT_TYPE]==2)
					tempAttackRange = tankAttackRange;
				for(int i=(-tempAttackRange);i<=tempAttackRange;i++){//search around within attack range
					for(int j=(-tempAttackRange);j<=tempAttackRange;j++){
						int tempPositionX = (unit[UNIT_X])+(i*tileSize);
						int tempPositionY = (unit[UNIT_Y])+(j*tileSize);
						if(isFriendlyUnitAtPosition(tempPositionX,tempPositionY)&&
							(unitIDsMoved.contains(unit[UNIT_ID])==false)){
							attackUnitAtPosition(tempPositionX,tempPositionY,unit[UNIT_TYPE]);
							unitIDsMoved.add(unit[UNIT_ID]);
							playerWasInAttackRange=true;
						}
					}
				}
				//Move towards target player unit
				int tempMove=0;
				if(unit[UNIT_TYPE]==0)
					tempMove = infantryMove;
				if(unit[UNIT_TYPE]==1)
					tempMove = artilleryMove;
				if(unit[UNIT_TYPE]==2)
					tempMove = tankMove;
				tempMove=1;
				int testFarRightX = (-tempMove*tileSize+unit[UNIT_X]);
				int testMiddleX = (0);
				int testFarLeftX = (tempMove*tileSize+unit[UNIT_X]);
				int testFarDownY = (-tempMove*tileSize+unit[UNIT_Y]);
				int testMiddleY = (0);
				int testFarUpY = (tempMove*tileSize+unit[UNIT_Y]);
				int closestDistanceWas = 9999;
				int closestComboWas = (-1);
				//Based off a sketch I made
				if(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=0;
				}
				if(distanceBetween(testFarLeftX,testMiddleY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=1;
				}
				if(distanceBetween(testFarLeftX,testFarUpY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=2;
				}
				if(distanceBetween(testMiddleX,testFarDownY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=3;
				}
				if(distanceBetween(testMiddleX,testFarUpY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarUpY,closestPlayerX,closestPlayerY));
					closestComboWas=4;
				}
				if(distanceBetween(testFarRightX,testFarDownY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=5;
				}
				if(distanceBetween(testFarRightX,testMiddleY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarRightX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=6;
				}
				if(distanceBetween(testFarRightX,testFarUpY,closestPlayerX,closestPlayerY)<closestDistanceWas){
					closestDistanceWas=(distanceBetween(testFarLeftX,testFarDownY,closestPlayerX,closestPlayerY));
					closestComboWas=7;
				}
				int tempNewX = testFarLeftX;
				int tempNewY = testFarDownY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==0&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
					if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testFarLeftX;
				tempNewY=testMiddleY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==1&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testFarLeftX;
				tempNewY=testFarUpY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==2&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testMiddleX;
				tempNewY=testFarDownY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==3&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testMiddleX;
				tempNewY=testFarDownY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==4&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testFarRightX;
				tempNewY=testFarDownY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==5&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testFarRightX;
				tempNewY=testMiddleY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==6&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
				tempNewX=testFarRightX;
				tempNewY=testFarUpY;
				tempNewX=centerOnTile(tempNewX);
				tempNewY=centerOnTile(tempNewY);
				if(unit[UNIT_X]==closestPlayerX)tempNewX=unit[UNIT_X];//Follow Straightaway
				if(unit[UNIT_Y]==closestPlayerY)tempNewY=unit[UNIT_Y];
				if(isUnitMovingToPosition(tempNewX,tempNewY)==false)
				if(closestComboWas==7&&isFriendlyUnitAtPosition(tempNewX,tempNewY)==
					false&&isEnemyUnitAtPosition(tempNewX,tempNewY)==false){
					Integer[] tempier = {unit[UNIT_ID],tempNewX,tempNewY};
					unitsToMove.add(tempier);
					tryClaimTown(tempNewX,tempNewY,enemyFaction);
				}
			}
		}
		playMoveSound(0);
	}

	//EVENT METHODS
	public void mousePressed(int x, int y){
		if(gameIsRunning){
			if(y<menuOffset){
				if(showProductionMenu){//Town - Unit Production
					for(Integer[] town:allTowns){
						if(town[TOWN_ID]==townIDSelected&&(townIDsThatProduced.contains(town[TOWN_ID])==false)){
							if(isUnitButtonClicked(x,y,infantryButtonX,infantryButtonY)&&playerMoney>=infantryPrice){
								addInfantry(playerFaction,town[TOWN_X],town[TOWN_Y]-menuOffset);
								playerMoney-=infantryPrice;
								townIDsThatProduced.add(town[TOWN_ID]);
								townSelected=false;
								showProductionMenu=false;
							}else if(isUnitButtonClicked(x,y,artilleryButtonX,artilleryButtonY)&&playerMoney>=artilleryPrice){
								addArtillery(playerFaction,town[TOWN_X],town[TOWN_Y]-menuOffset);
								playerMoney-=artilleryPrice;
								townIDsThatProduced.add(town[TOWN_ID]);
								townSelected=false;
								showProductionMenu=false;
							}else if(isUnitButtonClicked(x,y,tankButtonX,tankButtonY)&&playerMoney>=tankPrice){
								addTank(playerFaction,town[TOWN_X],town[TOWN_Y]-menuOffset);
								playerMoney-=tankPrice;
								townIDsThatProduced.add(town[TOWN_ID]);
								townSelected=false;
								showProductionMenu=false;
							}
						}
					}
				}else if(isEndTurnButtonClicked(x,y)){
					endTurn();
				}else if(isSwitchFactionButtonClicked(x,y)){
					int temp = enemyFaction;
					enemyFaction=playerFaction;
					playerFaction=temp;
					play(soundSelect);
				}
				return;
			}
			if(unitSelected){
				tryMoveUnit(x,y);
			}
			trySelectUnit(x,y);
			trySelectTown(x,y);
		}
	}
	public void keyPressed(char key){
		if(gameIsRunning){
			if(key=='w'){
				yViewOffset+=viewMoveSpeed;
				if(yViewOffset>((windowHeigth/4)-(tileSize*2))){
					yViewOffset-=viewMoveSpeed;
					return;
				}
				viewMovementController(0,viewMoveSpeed);
			}
			if(key=='a'){
				xViewOffset+=viewMoveSpeed;
				if(xViewOffset>(warMapWidth/2)){
					xViewOffset-=viewMoveSpeed;
					return;
				}
				viewMovementController(viewMoveSpeed,0);
			}
			if(key=='s'){
				yViewOffset-=viewMoveSpeed;
				if(yViewOffset<-(warMapHeight/3*tileSize)){
					yViewOffset+=viewMoveSpeed;
					return;
				}
				viewMovementController(0,-viewMoveSpeed);
			}
			if(key=='d'){
				xViewOffset-=viewMoveSpeed;
				if(xViewOffset<-(warMapWidth/4)-10*tileSize){
					xViewOffset+=viewMoveSpeed;
					return;
				}
				viewMovementController(-viewMoveSpeed,0);
			}
		}else{
			start();
		}
	}
	public void viewMovementController(int x, int y){//Does not include warMap offsetting
		for(Integer[] unit:allUnits){
			unit[UNIT_X]+=x;
			unit[UNIT_Y]+=y;
		}
		for(Integer[] town:allTowns){
			town[TOWN_X]+=x;
			town[TOWN_Y]+=y;
		}
		for(Double[] smoke:allSmoke){
			smoke[0]+=(double)x;
			smoke[1]+=(double)y;
		}
		for(Integer[] explosion:allExplosions){
			explosion[0]+=x;
			explosion[1]+=x;
		}
		for(Integer[] data:unitsToMove){
			data[1]+=x;
			data[2]+=y;
		}
	}

	//INITIALIZATION, TURN, AND TIMER METHODS-------------------------------------------------------------------------------
	public void start(){
		//RESET FROM PREVIOUS RUN
		playerFaction=0;
		enemyFaction=1;
		nextUnitID=1;
		nextTownID=1;
		townIDSelected=0;
		unitIDSelected=0;
		playerHasWon = false;
		enemyHasWon = false;
		playerMoney = 0;
		enemyMoney = 0;
		allUnits.clear();
		allTowns.clear();
		allSmoke.clear();
		xViewOffset = 0;
		yViewOffset = menuOffset;
		myTurn = true;
		unitIDsMoved.clear();
		unitsToMove.clear();
		townIDsThatProduced.clear();
		turnNumber=0;;
		unitSelected=false;
		townSelected=false;
		actioned=false;
		gameIsRunning=true;
		showProductionMenu=false;
		//SETUP GAME SCENARIO AND BOARD
		for(int i=0;i<warMapWidth;i++)
			for(int j=0;j<warMapHeight;j++)
				warMap[i][j]=GRASS_TILE;
		warMap[0][0]=WATER_TILE;warMap[1][0]=WATER_TILE;warMap[2][0]=WATER_TILE;//add water
		warMap[3][0]=WATER_TILE;warMap[4][0]=WATER_TILE;warMap[5][0]=WATER_TILE;
		warMap[6][0]=WATER_TILE;warMap[0][1]=WATER_TILE;warMap[1][1]=WATER_TILE;
		warMap[0][1]=WATER_TILE;warMap[0][2]=WATER_TILE;warMap[1][2]=WATER_TILE;warMap[0][3]=WATER_TILE;
		warMap[34][0]=WATER_TILE;warMap[35][0]=WATER_TILE;warMap[36][0]=WATER_TILE;//east germany
		warMap[37][0]=WATER_TILE;warMap[37][0]=WATER_TILE;
		warMap[13][29]=WATER_TILE;warMap[14][29]=WATER_TILE;warMap[15][29]=WATER_TILE;//south france/north italy
		warMap[16][29]=WATER_TILE;warMap[17][29]=WATER_TILE;warMap[18][29]=WATER_TILE;warMap[19][29]=WATER_TILE;
		warMap[12][29]=WATER_TILE;warMap[11][29]=WATER_TILE;warMap[10][29]=WATER_TILE;warMap[9][29]=WATER_TILE;
		warMap[15][28]=WATER_TILE;warMap[13][28]=WATER_TILE;warMap[14][28]=WATER_TILE;warMap[15][28]=WATER_TILE;
		warMap[16][28]=WATER_TILE;warMap[17][28]=WATER_TILE;warMap[18][28]=WATER_TILE;
		//Spawn Towns - This scenario is the invasion of France by Germany
		//            - It includes german tanks and the Maginot Line
		//French Cities - 7
		addTown(playerFaction,"Paris",onTile(4),onTile(11),40);
		addTown(playerFaction,"Lille",onTile(8),onTile(6),10);
		addTown(playerFaction,"Strasbourg",onTile(11),onTile(10),10);
		addTown(playerFaction,"Lyon",onTile(9),onTile(18),20);
		addTown(playerFaction,"Nice",onTile(12),onTile(26),10);
		addTown(playerFaction,"Marseilles",onTile(8),onTile(28),30);
		addTown(playerFaction,"Toulouse",onTile(2),onTile(22),20);
		//German Cities
		addTown(enemyFaction,"Berlin",onTile(28),onTile(5),40);
		addTown(enemyFaction,"Brussels",onTile(13),onTile(3),20);
		addTown(enemyFaction,"Essen",onTile(20),onTile(6),20);
		addTown(enemyFaction,"Frankfurt",onTile(23),onTile(13),20);
		addTown(enemyFaction,"Munich",onTile(31),onTile(18),30);
		//Spawn Player Units
		addInfantry(playerFaction,onTile(3),onTile(9));
		addTank(playerFaction,onTile(4),onTile(11));
		addArtillery(playerFaction,onTile(9),onTile(9));
		addArtillery(playerFaction,onTile(9),onTile(10));
		addArtillery(playerFaction,onTile(9),onTile(11));
		addArtillery(playerFaction,onTile(9),onTile(12));
		//Spawn Enemy Units
		addTank(enemyFaction,onTile(10),onTile(1));
		addTank(enemyFaction,onTile(10),onTile(2));
		addTank(enemyFaction,onTile(11),onTile(3));
		addTank(enemyFaction,onTile(11),onTile(4));
		addTank(enemyFaction,onTile(12),onTile(4));
		addTank(enemyFaction,onTile(12),onTile(3));
		//Align properly according to menuOffset
		viewMovementController(0,menuOffset);
	}//--------------------------------------------------------------------------------------------------------------------
	public void finishLerps(){
		for(Integer[] lerpData:unitsToMove){
			for(Integer[] unit:allUnits){
				if(lerpData[0]==unit[UNIT_ID]){
					unit[UNIT_X]=lerpData[1];
					unit[UNIT_Y]=lerpData[2];
				}
			}
		}
	}
	public void endTurn(){
		if(gameIsRunning){
			allSmoke.clear();
			unitIDsMoved.clear();
			finishLerps();
			unitsToMove.clear();
			townIDsThatProduced.clear();
			turnNumber++;
			unitSelected=false;
			townSelected=false;
			actioned=false;
			collectTownResources();
			runAI();
			fixAllOffGrids();
		}
	}
	public void timerFired(){
		setTimerDelay(15);
		if(gameIsRunning){
			lerpUnitsToMove();
			if(actioned){
				unitSelected=false;
				actioned=false;
			}
			if(checkVictory()==PLAYER_VICTORY){
				playerHasWon=true;
				gameIsRunning=false;
				finishLerps();
			}
			if(checkVictory()==ENEMY_VICTORY){
				enemyHasWon=true;
				gameIsRunning=false;
				finishLerps();
			}
		}
	}

	////PAINT METHODS/////////////////////////////////////////////////////////////////////////////////////////
	public void paintWarMap(Graphics2D page){
		double scale = ((double)tileSize/(double)unitSize);
		page.setColor(Color.black);
		for(int i=0;i<warMapWidth;i++)
			for(int j=0;j<warMapHeight;j++){
				if(warMap[i][j]==GRASS_TILE){
					drawCenteredImage(page,grassImage,onTile(i)+xViewOffset,onTile(j)+yViewOffset,scale,0);
				}
				if(warMap[i][j]==WATER_TILE){
					drawCenteredImage(page,waterImage,onTile(i)+xViewOffset,onTile(j)+yViewOffset,scale,0);
				}
			}
	}
	public void paintUnits(Graphics2D page){
		int tempMove=0;
		int tempAttackRange=0;
		for(Integer[] unit:allUnits){
			if(unit[UNIT_ID]==unitIDSelected&&unitSelected){
				page.setColor(Color.yellow);
				if(unit[UNIT_TYPE]==0)//Move Range
					tempMove = infantryMove;
				if(unit[UNIT_TYPE]==1)
					tempMove = artilleryMove;
				if(unit[UNIT_TYPE]==2)
					tempMove = tankMove;
				if(unit[UNIT_TYPE]==0)//Attack Range
					tempAttackRange = infantryAttackRange;
				if(unit[UNIT_TYPE]==1)
					tempAttackRange = artilleryAttackRange;
				if(unit[UNIT_TYPE]==2)
					tempAttackRange = tankAttackRange;
				for(int i=(-tempMove);i<=tempMove;i++){//draw move range
					for(int j=(-tempMove);j<=tempMove;j++){
						int tempPositionX = (unit[UNIT_X])+(i*tileSize);
						int tempPositionY = (unit[UNIT_Y])+(j*tileSize);
						page.setColor(Color.yellow);
						page.drawOval(unit[UNIT_X]-(tileSize/2),unit[UNIT_Y]-(tileSize/2),tileSize,tileSize);
						if(isEnemyUnitAtPosition(tempPositionX,tempPositionY)==false&&
							isFriendlyUnitAtPosition(tempPositionX,tempPositionY)==false&&
							unitIDsMoved.contains(unit[UNIT_ID])==false){
							page.drawOval(tempPositionX-(tileSize/2),tempPositionY-(tileSize/2),tileSize,tileSize);
						}
					}
				}
				for(int i=(-tempAttackRange);i<=tempAttackRange;i++){//draw attack range
					for(int j=(-tempAttackRange);j<=tempAttackRange;j++){
						int tempPositionX = (unit[UNIT_X])+(i*tileSize);
						int tempPositionY = (unit[UNIT_Y])+(j*tileSize);
						boolean xInAttackRange =(tempPositionX<=(unit[UNIT_X]+(tileSize/2)+(tempAttackRange*tileSize))&&
							tempPositionX>=(unit[UNIT_X]-(tileSize/2))-(tempAttackRange*tileSize));
						boolean yInAttackRange =(tempPositionY<=(unit[UNIT_Y]+(tileSize/2)+(tempAttackRange*tileSize))&&
							tempPositionY>=(unit[UNIT_Y]-(tileSize/2))-(tempAttackRange*tileSize));
						page.setColor(Color.yellow);
						if(isEnemyUnitAtPosition(tempPositionX,tempPositionY)&&xInAttackRange&&yInAttackRange&&
							unitIDsMoved.contains(unit[UNIT_ID])==false){
							page.setColor(Color.red);
							page.drawRect(tempPositionX-(tileSize/2),tempPositionY-(tileSize/2),tileSize,tileSize);
						}
					}
				}
			}
			double scale = ((double)tileSize/(double)unitSize);
			//draw image
			if(unit[UNIT_TYPE]==0)
				drawCenteredImage(page,infantryImage,unit[UNIT_X],unit[UNIT_Y],scale,0);
			if(unit[UNIT_TYPE]==1)
				drawCenteredImage(page,artilleryImage,unit[UNIT_X],unit[UNIT_Y],scale,0);
			if(unit[UNIT_TYPE]==2)
				drawCenteredImage(page,tankImage,unit[UNIT_X],unit[UNIT_Y],scale,0);
			//draw health
			if(unit[UNIT_FACTION]==playerFaction)
				page.setColor(Color.green);
			if(unit[UNIT_FACTION]==enemyFaction)
				page.setColor(Color.red);
			int tempUnitMaxHealth = 0;//Drawing unit healthbars
			if(unit[UNIT_TYPE]==0)tempUnitMaxHealth=maxInfantryHealth;
			if(unit[UNIT_TYPE]==1)tempUnitMaxHealth=maxArtilleryHealth;
			if(unit[UNIT_TYPE]==2)tempUnitMaxHealth=maxTankHealth;
			page.setFont(new Font(gameFont, Font.PLAIN, 9));
			double healthPixel=((double)unit[UNIT_HEALTH]/(double)tempUnitMaxHealth)*((double)tileSize);
			page.fillRect(unit[UNIT_X]-(tileSize/2)+2,unit[UNIT_Y]+tileSize/2-(tileSize/4)+2,(int)healthPixel-3,tileSize/8);
		}
	}
	public void paintTowns(Graphics2D page){
		double scale = ((double)tileSize/40.0);
		for(Integer[] town:allTowns){
			drawCenteredImage(page,townImage,town[TOWN_X],town[TOWN_Y],scale,0);
			if(town[TOWN_ID]==townIDSelected&&townSelected){
				page.setColor(Color.yellow);
				page.drawRect(town[TOWN_X]-(tileSize/2)-13,town[TOWN_Y]-(tileSize/2)-2,tileSize+25,tileSize+2);
			}
			if(town[TOWN_FACTION]==playerFaction)
				page.setColor(Color.green);
			if(town[TOWN_FACTION]==enemyFaction)
				page.setColor(Color.red);
			page.setFont(new Font(gameFont, Font.PLAIN, tileSize/2));
			drawCenteredString(page,""+townNames.get(town[TOWN_NAME]), centerOnTile(town[TOWN_X])-20,
				centerOnTile(town[TOWN_Y])+(tileSize/2), tileSize, tileSize);
		}
	}
	public void paintSidebar(Graphics2D page){
		page.setColor(menuColor);
		page.fillRect(0,0,windowWidth,menuOffset);
		page.setColor(Color.black);
		page.drawRect(0,0,windowWidth,menuOffset);
		page.setFont(new Font(gameFont, Font.BOLD, 16));
		drawCenteredString(page,"Turn: "+turnNumber, 60,40,20, 20);
		drawCenteredString(page,"Money: "+playerMoney, 60,20,20, 20);
		page.drawRect(endTurnButtonX,endTurnButtonY,endTurnButtonWidth,endTurnButtonHeight);//endTurnButton
		drawCenteredString(page,"End Turn", endTurnButtonX,endTurnButtonY,
			endTurnButtonWidth, endTurnButtonHeight);
		if(showProductionMenu){
			drawCenteredImage(page,infantryImage,infantryButtonX,infantryButtonY,unitButtonImageScale,0);
			drawCenteredString(page,"Cost "+infantryPrice, infantryButtonX-10,infantryButtonY+(tileSize),20, 20);
			drawCenteredImage(page,artilleryImage,artilleryButtonX,artilleryButtonY,unitButtonImageScale,0);
			drawCenteredString(page,"Cost "+artilleryPrice, artilleryButtonX,artilleryButtonY+(tileSize),20, 20);
			drawCenteredImage(page,tankImage,tankButtonX,tankButtonY,unitButtonImageScale,0);
			drawCenteredString(page,"Cost "+tankPrice, tankButtonX,tankButtonY+(tileSize),20, 20);
		}
		//Faction info and Controls Info
		page.setColor(Color.black);
		//switchFactionButton
		page.drawRect(switchFButtonX,switchFButtonY,switchFButtonWidth,switchFButtonHeight);
		drawCenteredString(page,"Switch Factions", switchFButtonX,switchFButtonY,
			switchFButtonWidth, switchFButtonHeight);
		drawCenteredString(page,"Player Faction", switchFButtonX+160,switchFButtonY-40,
			switchFButtonWidth, switchFButtonHeight);
		if(playerFaction==FRENCH_FACTION)
			drawCenteredImage(page,frenchFactionIcon,switchFButtonX+225,switchFButtonY+25,1,0);
		if(playerFaction==GERMAN_FACTION)
			drawCenteredImage(page,germanFactionIcon,switchFButtonX+225,switchFButtonY+25,1,0);
		if(playerFaction==FRENCH_FACTION)
			drawCenteredString(page,"France", switchFButtonX+155,switchFButtonY+35,
			switchFButtonWidth, switchFButtonHeight);
		if(playerFaction==GERMAN_FACTION)
			drawCenteredString(page,"Germany", switchFButtonX+155,switchFButtonY+35,
			switchFButtonWidth, switchFButtonHeight);
	}
	public void paintSmoke(Graphics2D page){
		int x=0;int y=0;int frameFactor=0;
		for(Double[] smoke:allSmoke){
			x = smoke[0].intValue();//indexes
			y = smoke[1].intValue();
			frameFactor = smoke[2].intValue();
			double size = smoke[3];
			double rot = smoke[4];
			if(frameFactor>(smokeMaxFrame*effectFrameInterval))
				allSmoke.remove(smoke);
			if(frameFactor<(1*effectFrameInterval)){
				drawCenteredImage(page,smokeFrame1,x,y,size,rot);
			}
			if(frameFactor<(2*effectFrameInterval)){
				drawCenteredImage(page,smokeFrame2,x,y,size,rot);
			}
			if(frameFactor<(3*effectFrameInterval)){
				drawCenteredImage(page,smokeFrame3,x,y,size,rot);
			}
			if(frameFactor<(4*effectFrameInterval)){
				drawCenteredImage(page,smokeFrame4,x,y,size,rot);
			}
			smoke[2]++;
		}
	}
	public void paintExplosions(Graphics2D page){
		for(Integer[] explosion:allExplosions){
			if(explosion[2]>(6*effectFrameInterval))
				allExplosions.remove(explosion);
			double size = (explosion[3]/artilleryDamage);
			size=0.75;
			int x = explosion[0];
			int y = explosion[1];
			int myFrameInterval = 5;
			if(explosion[2]<(6*myFrameInterval)){
				drawCenteredImage(page,explodeFrame6,x,y,size,0);
			}
			if(explosion[2]<(5*myFrameInterval)){
				drawCenteredImage(page,explodeFrame5,x,y,size,0);
			}
			if(explosion[2]<(4*myFrameInterval)){
				drawCenteredImage(page,explodeFrame4,x,y,size,0);
			}
			if(explosion[2]<(3*myFrameInterval)){
				drawCenteredImage(page,explodeFrame3,x,y,size,0);
			}
			if(explosion[2]<(2*myFrameInterval)){
				drawCenteredImage(page,explodeFrame2,x,y,size,0);
			}
			if(explosion[2]<(1*myFrameInterval)){
				drawCenteredImage(page,explodeFrame1,x,y,size,0);
			}
			explosion[2]++;
		}
	}
	public void paintGameOver(Graphics2D page){
		page.setColor(Color.black);
		page.fillRect(0,(windowHeigth/2)-(endBannerSize/2),windowWidth,endBannerSize);
		page.setFont(new Font(gameFont,Font.BOLD,40));
		if(playerHasWon){
			page.setColor(Color.green);
			drawCenteredString(page,"VICTORY",0,(windowHeigth/2)-(endBannerSize/2),windowWidth,endBannerSize);
		}
		if(enemyHasWon){
			page.setColor(Color.red);
			drawCenteredString(page,"DEFEATED",0,(windowHeigth/2)-(endBannerSize/2),windowWidth,endBannerSize);
		}
		page.drawLine((windowWidth/2)-100,(windowHeigth/2)+30,(windowWidth/2)+100,(windowHeigth/2)+30);
		page.setFont(new Font(gameFont,Font.BOLD,20));
		drawCenteredString(page,"Press Any Key to Restart",0,(windowHeigth/2)-(endBannerSize/2)+60,windowWidth,endBannerSize);
	}
	public void paint(Graphics2D page){
		page.setStroke(new BasicStroke(3));
		paintWarMap(page);
		paintTowns(page);
		paintSmoke(page);
		paintUnits(page);
		paintExplosions(page);
		paintSidebar(page);
		if(gameIsRunning==false){
			paintGameOver(page);
		}
	}
	public static void main(String[] args){launch(windowWidth, windowHeigth);}
}
