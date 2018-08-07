import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.List;
import javax.swing.*;

public class Tank {
	private static final int XSPEED = 5, YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	TankClient tc;
	
	private boolean good;
	
	private int life = 100;
	
	private boolean live = true;
	
	private BloodBar bb = new BloodBar();
	
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	
	private int x,y;
	//上一步的位置
	private int oldX,oldY;
	
	private boolean bL=false, bR=false, bU=false, bD=false;
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isLive() {
		return live;
	}

	public boolean isGood() {
		return good;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int x,int y, boolean good){
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY = y;
	}
	
	public Tank(int x, int y, boolean good, TankClient tc){
		this(x, y, good);
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){ 
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if(good) g.setColor(Color.RED);
		else  g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		//画血条
		if(this.isGood()) bb.draw(g);
		else {
			this.setLife(20);
			bb.draw(g);
		}
		
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
		
		//记录上一步的x和 y的值 
		this.oldX = x; 
		this.oldY = y;
		
		
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
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT >TankClient.GAME_HEIGHT) y =TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good){
			if(step == 0){
				step = r.nextInt(12) + 3;
				
				Direction[] dirs = Direction.values();
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			
			step--;
			
			if(r.nextInt(40) > 38) this.fire();
		}
		
	}
	
	public void stay(){
		x = oldX;
		y = oldY;
	}
	
	public void keyPressed(KeyEvent e){
		int  key = e.getKeyCode();		
		switch(key){
		//判断我军坦克是否死亡 死亡按~键复活
		case KeyEvent.VK_F1:
			if(!this.isLive()){
				this.setLive(true);
				this.setLife(100);
			}
			break;
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
		case KeyEvent.VK_NUMPAD0:
			superFire();
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
		if(!live) return null;
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, ptDir, this.tc);
		tc.missiles.add(m);
		return m;
	}

	public Missile fire(Direction dir){
		if(!live) return null;
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean tankHitWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return false;
		}
		return false;
	}
	
	public boolean tankWithTank(List<Tank> tanks){
		for(int i=0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return false;
				}
			}
		}
		return false;
	}
	
	//发射超级炮弹
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i=0; i<8; i++){
			fire(dirs[i]);
		}
	}
	
	//血条类
	public class BloodBar{
		public void draw(Graphics g){
			if(life == 100) g.setColor(new Color(25,25,112));
			else if(life == 80) g.setColor(new Color(184,134,11));
			else if(life == 60) g.setColor(new Color(0,0,0));
			else if(life == 40) g.setColor(new Color(50,205,50));
			else if(life == 20) g.setColor(new Color(255,0,0));
			g.drawRect(x, y-7, WIDTH, 5);
			g.fillRect(x, y-7, WIDTH, 5);
			
		}
	}
	
}
