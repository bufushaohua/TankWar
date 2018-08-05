import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.LongAccumulator;

import javax.swing.*;

public class Tank {
	private static final int XSPEED = 5, YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	TankClient tc;
	int x,y;
	
	private boolean bL=false, bR=false, bU=false, bD=false;
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	public Tank(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public Tank(int x, int y, TankClient tc){
		this(x,y);
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		switch(ptDir){
			case L:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
				break;
			case LU:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
				break;
			case U:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
				break;
			case RU:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
				break;
			case R:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
				break;
			case RD:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
				break;
			case D:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
				break;
			case LD:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
				break;
		}
		
		move();
	}
	
	void move(){
		switch(dir){
			case L:
				x -= XSPEED;
				break;
			case LU:
				x -= XSPEED;
				y -= YSPEED;
				break;
			case U:
				y -= YSPEED;
				break;
			case RU:
				x += XSPEED;
				y -= YSPEED;
				break;
			case R:
				x += XSPEED;
				break;
			case RD:
				x += XSPEED;
				y += YSPEED;
				break;
			case D:
				y += YSPEED;
				break;
			case LD:
				x -= XSPEED;
				y += YSPEED;
				break;
			case STOP:
				break;
		}
		
		if(this.dir != Direction.STOP){
			this.ptDir = this.dir;
		}
		
	}
	
	public void keyPressed(KeyEvent e){
		int  key = e.getKeyCode();		
		switch(key){
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			bR = true;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			bL = true;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			bD = true;
			break;
		default :
			break;
		}
		locateDirection();
	}

	public void KeyReleased(KeyEvent e) {
		int  key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			bR = false;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			bL = false;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			bD = false;
			break;
		default :
			break;
		}
		locateDirection();
	}
	
	void locateDirection(){
		if( bL && !bR && !bU && !bD) dir = Direction.L;
		else if( bL && !bR &&  bU && !bD) dir = Direction.LU;
		else if(!bL && !bR &&  bU && !bD) dir = Direction.U;
		else if(!bL &&  bR &&  bU && !bD) dir = Direction.RU;
		else if(!bL &&  bR && !bU && !bD) dir = Direction.R;
		else if(!bL &&  bR && !bU &&  bD) dir = Direction.RD;
		else if(!bL && !bR && !bU &&  bD) dir = Direction.D;
		else if( bL && !bR && !bU &&  bD) dir = Direction.LD;
		else if(!bL && !bR && !bU && !bD) dir = Direction.STOP;
	}

	public Missile fire(){
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir);
		tc.missiles.add(m);
		return m;
	}

}
