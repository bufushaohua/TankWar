import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	private Random rd = new Random();
	
	Tank myTank = new Tank(50, 400, true, this);
	//Tank enemyTank = new Tank(100, 100, false, this);
	
	//两堵墙
	Wall w1 = new Wall(150, 200, 20, 150, this),
		 w2 = new Wall(300, 180, 300, 20, this),
		 w3 = new Wall(210, 450, 300, 20, this),
		 w4 = new Wall(650, 220, 20, 150, this);
	
	Explode e = new Explode(70, 70, this);
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	
	@Override
	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 6, 40);
		g.drawString("explodes count: " + explodes.size(), 6, 50);
		g.drawString("tanks count: " + tanks.size(), 6, 60);
		g.drawString("mytank life: " + myTank.getLife(), 6, 70);
		
		//敌方坦克死光后重新生成
		if(tanks.size() <= 0) {
			for(int i=0; i<5; i++){
				int r = rd.nextInt(10); 
				tanks.add(new Tank(50 + 40*(i+1),45 + r,false,this));
				tanks.add(new Tank(60 + 40*(i+1), 495 + r, false, this));
			}
		}
		//子弹击打坦克和墙的方法
		for(int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			/*if(!m.isLive()) missiles.remove(m);
			else m.draw(g);*/
			//m.hitTank(enemyTank);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitWall(w3);
			m.hitWall(w4);
			m.draw(g);
		}
		
		for(int i = 0;i<explodes.size();i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			t.tankHitWall(w1);
			t.tankHitWall(w2);
			t.tankHitWall(w3);
			t.tankHitWall(w4);
			t.tankWithTank(tanks);
			t.draw(g);
		}
		
		myTank.draw(g);
		//enemyTank.draw(g);
		myTank.tankHitWall(w1);
		myTank.tankHitWall(w2);
		myTank.tankHitWall(w3);
		myTank.tankHitWall(w4);
		
		//画墙
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		w4.draw(g);
	}
	
	public void update(Graphics g){
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreenImage = offScreenImage.getGraphics();
		Color c = gOffScreenImage.getColor();
		gOffScreenImage.setColor(new Color(216,191,216));
		gOffScreenImage.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreenImage.setColor(c);
		paint(gOffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void lauchFrame() throws Exception {
		/*
		//为游戏添加背景音乐 运用了JavaMusic包
		File file=new File("E:\\DownLoad Files\\CloudMusic DownLoad\\逃跑计划 - 夜空中最亮的星.mp3");
        FileInputStream fis=new FileInputStream(file);
        BufferedInputStream stream=new BufferedInputStream(fis);
        Player player=new Player(stream);
        player.play();*/
		//游戏背景音乐
		GameMusic gm = new GameMusic(new File("E:\\DownLoad Files\\"
							+ "CloudMusic DownLoad\\逃跑计划 - 夜空中最亮的星.mp3"));
		gm.start();
		
		//生成10辆敌军坦克
		for(int i=0; i<5; i++){
			tanks.add(new Tank(50 + 40*(i+1),50,false,this));
			tanks.add(new Tank(60 + 40*(i+1), 500, false, this));
		}
		
		this.setLocation(400, 300);
		this.setBounds(300, 100, GAME_WIDTH, GAME_HEIGHT);
		this.setVisible(true);
		this.setTitle("坦克大战");
		this.setResizable(false);
		this.setBackground(new Color(216,191,216));
		
		this.addKeyListener(new KeyMonitor());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new Thread (new PaintThread()).start();
	}

	//游戏主方法
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		try {
			tc.lauchFrame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//增加键盘监听
	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.KeyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	} 
}
