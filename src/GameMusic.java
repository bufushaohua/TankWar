import javazoom.jl.player.Player;
import java.io.*;

public class GameMusic extends Thread {
	//重写run方法
	@Override
	public void run() {
		super.run();
		try {
			play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private File music;
	Player player;
	
	//构造方法
	public GameMusic(File file){
		this.music = file;
	}
	//播放音乐方法
	public void play() throws Exception{
		FileInputStream buffer = new FileInputStream(music);
		player = new Player(buffer);
		player.play();
	}
}
