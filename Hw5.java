import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract class Block{
	double blockWidth;
	double  blockHeight;
	double blockX;
	double blockY;
	Color blockColor;
	
	public abstract void draw(Graphics g);
}
class SpecialBlock extends Block implements Runnable{
	SpecialBlock(double w, double h, double x, double y){
		blockWidth=w;
		blockHeight=h;
		blockX=x;
		blockY=y;
		blockColor=new Color(224,112,180);
		
		Thread tt=new Thread(this);
		tt.start();
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(blockColor);
		g.fillRect((int)blockX, (int)blockY, (int)blockWidth, (int)blockHeight);
		g.setColor(Color.black);
		g.drawRect((int)blockX, (int)blockY, (int)blockWidth, (int)blockHeight);
	}
	@Override
	public void run() {
		boolean n=true;
		while(true) {
			if(n) {
				this.blockColor=new Color(230,152,204);
			}
			else {
				this.blockColor=new Color(224,112,180);
			}
			
			if(n) {
				n=false;
			}
			else {
				n=true;
			}
			
			try {
				Thread.sleep(1000);
			}
			catch(Exception e) {
				return;
			}
		}
	}
	
}
class NormalBlock extends Block{
	NormalBlock(double w, double h, double x, double y){
		blockWidth=w;
		blockHeight=h;
		blockX=x;
		blockY=y;
		blockColor=new Color(181,147,236);
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(blockColor);
		g.fillRect((int)blockX, (int)blockY, (int)blockWidth, (int)blockHeight);
		g.setColor(Color.black);
		g.drawRect((int)blockX, (int)blockY, (int)blockWidth, (int)blockHeight);
	}
	
}

class FlickeringLabel extends JLabel implements Runnable{
	FlickeringLabel(String text){
		super(text);
		this.setOpaque(true);
		Thread th=new Thread(this);
		th.start();
	}
	@Override
	public void run() {
		boolean n=true;
		while(true) {
			if(n) {
				this.setForeground(Color.pink);
			}
			else {
				this.setForeground(new Color(242,13,116));
			}
			
			if(n) {
				n=false;
			}
			else {
				n=true;
			}
			
			try {
				Thread.sleep(200);
			}
			catch(Exception e) {
				return;
			}
		}
		
	}
	
}

class MyBar{
	double pxBar, pyBar;
	double wBar, hBar;
	Color colorBar;
	
	MyBar(){
		pxBar=400;
		pyBar=680;
		wBar=150;
		hBar=25;
		
		colorBar = new Color(182,73,103);
	}
	
	public void draw(Graphics g) {
		g.setColor(colorBar);
		g.fillRect((int)(pxBar), (int)(pyBar), (int)wBar, (int)hBar);
		g.setColor(Color.black);
		g.drawRect((int)(pxBar), (int)(pyBar), (int)wBar, (int)hBar);
	}
	
}

class MyBall{
	double px, py, vx, vy, ax, ay;
	double r;
	Color color;
	MyBar b;
	Clip barClip;
	ArrayList<Block> blocks;
	ArrayList<MyBall> balls;
	MyBall(GamePanel game){
		px = 300;	py = 600;
		vx = 300;	vy = -300;
		r = 6;
		
		
		try {
			URL url5 = getClass().getResource("bar.wav");
			AudioInputStream sound2 =  AudioSystem.getAudioInputStream(url5);
			barClip=AudioSystem.getClip();
			barClip.open(sound2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		b=game.bar;
		blocks=game.blocks;
		balls=game.balls;
		color = new Color(242,62,188);
	}
	public void draw(Graphics g){
		g.setColor(color);
		g.fillOval((int)(px-r), (int)(py-r), (int)(2*r), (int)(2*r));
		g.setColor(Color.black);
		g.drawOval((int)(px-r), (int)(py-r), (int)(2*r), (int)(2*r));
	}
	void update(double dt) {
		px = px + vx*dt;	py = py + vy*dt;
	}
	void collisionHandling(Dimension d) {
		if(px+r>d.width) {
			px = d.width - r;
			vx = -vx;
		}
		if(py+r<r) {
			py=r;
			vy=-vy;
		}
		if(px-r<0) {
			px = r;
			vx = -vx;
		}	
		if((px>=b.pxBar) && (px<=(b.pxBar+b.wBar)) && ((py>=b.pyBar)&&(py<=b.pyBar+b.hBar))) {
			py=680-r-1;
			barClip.setFramePosition(0);
			barClip.start();
			vy=-vy;
		}
		
		
		
	}
}

class EndPanel extends JPanel{
	JLabel endLabel;
	JLabel high;
	JLabel score;
	Hw5 fr;
	FlickeringLabel toNew;
	EndPanel(Integer a, Integer b){
		endLabel=new JLabel("Game Over!!");
		high=new JLabel("High Score : "+a);
		score=new JLabel("Your Score : "+b);
		toNew=new FlickeringLabel("Press Spacebar!");
		
		endLabel.setFont(new Font("궁서",Font.BOLD,45));
		high.setFont(new Font("궁서",Font.BOLD,20));
		score.setFont(new Font("궁서",Font.BOLD,20));
		toNew.setFont(new Font("궁서",Font.BOLD,20));
		
		setLayout(null);
		
		toNew.setOpaque(false);
		toNew.setForeground(new Color(242,13,116));
		
		
		endLabel.setBounds(250,200,800,100);
		high.setBounds(300,400,800,50);
		score.setBounds(300,450,800,50);
		toNew.setBounds(300,600,800,100);
		
		setBackground(new Color(230,230,230));
		
		add(endLabel);
		add(high);
		add(score);
		add(toNew);
		
	}
	
}

class GamePanel extends JPanel implements Runnable{
	
	ArrayList<MyBall> balls;
	MyBar bar;
	MyBall copyBall;
	
	
	Integer stage=0;
	Hw5 fr;
	ArrayList<Block> blocks;
	
	Clip blockClip;
	Clip specialClip;
	Clip overClip;
	
	ImageIcon icon2;
	
	boolean bricksOver = false;
	boolean ball3 = false;
	boolean left=false;
	boolean right=false;
	
	boolean ballFlag=false;
	boolean stageFlag=false;
	
	int count = 0;
	String status;
	
	Hw5 win;
	
	GamePanel(Hw5 a){
		win=a;
		balls = new ArrayList<MyBall>();
		blocks = new ArrayList<Block>();
		bar = new MyBar();
		Dimension di = getSize();
		fr=a;
		Thread t = new Thread(this);
		t.start();
		
		icon2 = new ImageIcon(getClass().getClassLoader().getResource("gameback.jpg"));
		
		try {
			URL url1 = getClass().getResource("special.wav");
			AudioInputStream sound3 =  AudioSystem.getAudioInputStream(url1);
			specialClip=AudioSystem.getClip();
			specialClip.open(sound3);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			URL url2 = getClass().getResource("brick.wav");
			AudioInputStream sound4 =  AudioSystem.getAudioInputStream(url2);
			blockClip=AudioSystem.getClip();
			blockClip.open(sound4);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			URL url6 = getClass().getResource("over.wav");
			AudioInputStream sound5 =  AudioSystem.getAudioInputStream(url6);
			overClip=AudioSystem.getClip();
			overClip.open(sound5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setBackground(Color.gray);
		
		setFocusable(true);
		requestFocus();
	}
	
	public void paintComponent(Graphics g)
	{
		
		
		Dimension d = getSize();
		
		g.drawImage(icon2.getImage(), 0, 0, null);
	       
	    g.drawImage(icon2.getImage(), 0, 0, d.width, d.height, null);
	       
	    setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	  
		super.paintComponent(g);
		if(balls.size()<1) {
			overClip.setFramePosition(0);
			overClip.start();
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			win.gameOver();
		}
		if(blocks.size()<1) {
			if(stage!=0) {
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stage++;
			
			for(int i=0;i<(3*stage)*(3*stage);i++) {
				if((int)(Math.random()*10)<=2) {
					blocks.add(new SpecialBlock(800/(3*stage), 400/(3*stage), (i%(3*stage))*(800/(3*stage)),(double)((double)(400/(3*stage))*(int)(i/(3*stage)))));
				}
				else {
				
					blocks.add(new NormalBlock(800/(3*stage), 400/(3*stage), (i%(3*stage))*(800/(3*stage)),(double)((double)(400/(3*stage))*(int)(i/(3*stage)))));
				}
			}
		
			
			bar.pxBar=300;
			stageFlag=true;
		}
		bar.draw(g);
		
		for(MyBall b : balls)
			b.draw(g);
		
		for(MyBall b : balls) {
			if(b.py+b.r>d.height) {
				balls.remove(b);
			}
		} 
		
		
		
		for(Block bl : blocks) {
			if(bl instanceof SpecialBlock) {
				SpecialBlock sb=(SpecialBlock)bl;
				sb.draw(g);
			}
			else {
				NormalBlock nb=(NormalBlock)bl;
				nb.draw(g);
			}
			
		}
		
		if(stageFlag) {
			stageFlag=false;
			initializeBall();
		}
		
		
	}
	void initializeBall() {
		balls.clear();
		balls.add(new MyBall(this));
	}
	

	@Override
	public void run() {
		while(true) {
			try {
				Dimension d = getSize();
				
				if (left == true) {
				    bar.pxBar -= 10;
				    right = false;
				}
				if (right == true) {
				    bar.pxBar += 10;
				    left = false;
				}
				if (bar.pxBar <= 0) {
				    bar.pxBar = 0;
				}
				else if (bar.pxBar+bar.wBar >= d.width-2) {
				    bar.pxBar = d.width-bar.wBar-2;
				}
				
				for(MyBall b : balls)
				{
					b.update(0.033);
					b.collisionHandling(d);
				}
				outerloop :
				for(Iterator<MyBall> itb = balls.iterator(); itb.hasNext();) {
					MyBall b=itb.next();
					for(Iterator<Block> it = blocks.iterator(); it.hasNext();) {
						Block bl=it.next();
						if(((b.py+b.r>=bl.blockY && b.py-b.r <=bl.blockY+bl.blockHeight)&&((b.px+b.r)>=bl.blockX && (b.px-b.r)<=(bl.blockX+bl.blockWidth)))
								||((b.px+b.r >= bl.blockX && b.px-b.r <=bl.blockX+bl.blockWidth)&&((b.py-b.r)>=(bl.blockY+bl.blockHeight) && (b.py+b.r)<=bl.blockY))) 
						{
							b.px=b.px+b.r;
							b.py=b.py+b.r;
							b.vx=-b.vx;
							b.vy=-b.vy;
								if(bl instanceof SpecialBlock) {
									copyBall=b;
									it.remove();
								
									specialClip.setFramePosition(0);
									specialClip.start();
									fr.your_score+=30;
									
									ballFlag=true;
									break outerloop;
								}
								else {
									it.remove();
									blockClip.setFramePosition(0);
									blockClip.start();
									fr.your_score+=10;
									
								}
						}
					}
				}
				
				if(ballFlag) {
					ballFlag=false;
					addBall();
					
				}
				
				repaint();
				Thread.sleep(33);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	void addBall() {
		ballFlag=false;
		
		MyBall b1 = new MyBall(this);
		MyBall b2 = new MyBall(this);
		
		b1.px=copyBall.px;
		b1.py=copyBall.py;
		b1.vx=400;
		b1.vy=copyBall.vy+10;
		
		b2.px=copyBall.px;
		b2.py=copyBall.py;
		b2.vx=200;
		b2.vy=copyBall.vy+10;
		
		balls.add(b1);
		balls.add(b2);
	}
	
}

class OpenPanel extends JPanel{
	FlickeringLabel label;
	JLabel l1;
	JLabel l2;
	JLabel name;
	ImageIcon icon;
	
	OpenPanel(){
		
		l1=new JLabel("Java Programming");
		l2=new JLabel("Homework #5");
		name=new JLabel("벽돌깨기");
		label=new FlickeringLabel("Press SpaceBar!");
		
		l1.setFont(new Font("궁서",Font.BOLD,30));
		l2.setFont(new Font("궁서",Font.BOLD,30));
		name.setFont(new Font("궁서",Font.BOLD,50));
		label.setFont(new Font("궁서",Font.BOLD,20));
		
		icon = new ImageIcon(getClass().getClassLoader().getResource("back.png"));
		
		setLayout(null);
		
		label.setOpaque(false);
		
		label.setForeground(new Color(242,13,116));
		
		l1.setBounds(240,100,800,100);
		l2.setBounds(270,200,800,100);
		name.setBounds(280,350,800,100);
		label.setBounds(300,600,800,100);
		
		add(l1);
		add(l2);
		add(name);
		add(label);
		
		setBackground(Color.pink);
	}
	public void paintComponent(Graphics g) {
       
        g.drawImage(icon.getImage(), 0, 0, null);
        Dimension d = getSize();
        g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
       
        setOpaque(false); //그림을 표시하게 설정,투명하게 조절
        super.paintComponent(g);
    }
	
}

public class Hw5 extends JFrame implements KeyListener{

	Container c;
	CardLayout card;
	GamePanel game;
	OpenPanel open;
	EndPanel end;
	
	Integer high_score=0;
	Integer your_score=0;

	boolean flag1=true;
	boolean flag2=true;
	
	Clip clip;
	Clip endClip;
	
	public static void main(String[] args) {
		new Hw5();
	}
	
	Hw5(){
		setSize(800,800);
		setTitle("Java Homework5");
		
		setResizable(false);
		

		try {
			URL url3 = getClass().getResource("loopEDM.wav");
			AudioInputStream sound =  AudioSystem.getAudioInputStream(url3);
			clip=AudioSystem.getClip();
			clip.open(sound);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			URL url4 = getClass().getResource("end.wav");
			AudioInputStream sound1 =  AudioSystem.getAudioInputStream(url4);
			endClip=AudioSystem.getClip();
			endClip.open(sound1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		
		game = new GamePanel(this);
		open = new OpenPanel();
		end = new EndPanel(high_score, your_score);
		
		add(open);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			game.left = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			game.right = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE && flag1==true && flag2==true) {
			remove(open);
			game.balls.add(new MyBall(game));
			clip.stop();
			add(game);
			revalidate();
			repaint();
			flag1=false;
		}
		else if(flag1==false && flag2==true) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				game.left = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				game.right = false;
			}
		}
		 
		else if(e.getKeyCode() == KeyEvent.VK_SPACE && flag1==false && flag2==true) {
			flag2=false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE && flag1==false && flag2==false) {
			flag1=flag2=true;
			your_score=0;
			endClip.stop();
			remove(end);
			game=new GamePanel(this);
			add(open);
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			revalidate();
			repaint();
		}
		
	}
	void gameOver() {
		remove(game);
		if(high_score < your_score)
			high_score=your_score;
		end.high.setText("High Score : "+high_score);
		end.score.setText("Your Score : "+your_score);
		
		add(end);
		endClip.setFramePosition(0);
		endClip.loop(Clip.LOOP_CONTINUOUSLY);
		flag2=false;
		revalidate();
		repaint();
	}
}
