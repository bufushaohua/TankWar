import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	Tank myTank = new Tank(50, 50, true, this);
	Tank enemyTank = new Tank(100, 100, false, this);
	
	Explode e = new Explode(70, 70, this);
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	
	Image offScreenImage = null;
	
	@Override
	public void paint(Graphics g) {
		g.drawString("missiles count: " + missiles.size(), 6, 40);
		g.drawString("explodes count: " + explodes.size(), 6, 50);
		
		for(int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			/*if(!m.isLive()) missiles.remove(m);
			else m.draw(g);*/
			m.hitTank(enemyTank);
			m.draw(g);
		}
		
		for(int i = 0;i<explodes.size();i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		myTank.draw(g);
		enemyTank.draw(g);
	}
	
	public void update(Graphics g){
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreenImage = offScreenImage.getGraphics();
		Color c = gOffScreenImage.getColor();
		gOffScreenImage.setColor(Color.GREEN);
		gOffScreenImage.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreenImage.setColor(c);
		paint(gOffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void lauchFrame() {
		this.setLocation(400, 300);
		this.setBounds(300, 100, GAME_WIDTH, GAME_HEIGHT);
		this.setVisible(true);
		this.setTitle("坦克大战");
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new Thread (new PaintThread()).start();
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
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
