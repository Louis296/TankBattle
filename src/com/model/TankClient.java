package com.model;

import com.utils.GetBlood;
import com.utils.Tools;
import com.music.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class TankClient extends Frame implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final int Fram_width = 800;
	public static final int Fram_length = 600;
	public static boolean printable = true;
	public boolean Player2 = false;
	public static boolean welcome = true;
	public boolean firstrun=true;
	public static int map = 1;
	MenuBar jmb = null;
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null, jm5 = null, jm6 = null;
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null, jmi10 = null,
			jmi11 = null, jmi12 = null, jmi13 = null, jmi14 = null,jmi15=null,jmi16=null;
	Image screenImage = null;

	Tank homeTank = new Tank(300, 560, true, Direction.STOP, this, 1);
	Tank homeTank2 = new Tank(449, 560, true, Direction.STOP, this, 2);
	GetBlood blood = new GetBlood();
	Home home = new Home(373, 557, this);
	Boolean win = false, lose = false;
	List<River> theRiver = new ArrayList<River>();
	List<Tank> tanks = new ArrayList<Tank>();
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	List<Bullets> bullets = new ArrayList<Bullets>();
	List<Tree> trees = new ArrayList<Tree>();
	public static List<CommonWall> homeWall = new ArrayList<CommonWall>();
	List<CommonWall> otherWall = new ArrayList<CommonWall>();
	List<MetalWall> metalWall = new ArrayList<MetalWall>();

	public void update(Graphics g) {
		if (!welcome) {
			screenImage = this.createImage(Fram_width, Fram_length);

			Graphics gps = screenImage.getGraphics();
			Color c = gps.getColor();
			gps.setColor(Color.GRAY);
			gps.fillRect(0, 0, Fram_width, Fram_length);
			gps.setColor(c);
			framePaint(gps);
			g.drawImage(screenImage, 0, 0, null);
		}

	}

	public void framePaint(Graphics g) {

		Color c = g.getColor();
		g.setColor(Color.green);

		Font f1 = g.getFont();
		g.setFont(new Font("Times New Roman", Font.BOLD, 20));
		if (!Player2) g.drawString("Tanks left in the field: ", 200, 70);
		else g.drawString("Tanks left in the field: ", 100, 70);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 30));
		if (!Player2) g.drawString("" + tanks.size(), 400, 70);
		else g.drawString("" + tanks.size(), 300, 70);
		g.setFont(new Font("Times New Roman", Font.BOLD, 20));
		if (!Player2) g.drawString("Health: ", 580, 70);
		else g.drawString("Health: ", 380, 70);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 30));
		if (!Player2) g.drawString("" + homeTank.getLife(), 650, 70);
		else g.drawString("Player1: " + homeTank.getLife() + "    Player2:" + homeTank2.getLife(), 450, 70);
		g.setFont(f1);
		if (!Player2) {
			if (tanks.size() == 0 && home.isLive() && homeTank.isLive() && lose == false) {
				if (map!=5){
					if(!firstrun){
						printable=false;
						map+=1;
						this.dispose();
						new TankClient(map);
					}
				}else{
					Font f = g.getFont();
					g.setFont(new Font("Times New Roman", Font.BOLD, 60));
					this.otherWall.clear();
					g.drawString("Congratulations! ", 200, 300);
					g.setFont(f);
					win = true;
				}

			}

			if (homeTank.isLive() == false && win == false) {
				Font f = g.getFont();
				g.setFont(new Font("Times New Roman", Font.BOLD, 40));
				tanks.clear();
				bullets.clear();
				g.drawString("Sorry. You lose!", 200, 300);
				lose = true;
				g.setFont(f);
			}
		} else {
			if (tanks.size() == 0 && home.isLive() && (homeTank.isLive() || homeTank2.isLive()) && lose == false) {
				if (map!=5) {
					if (!firstrun) {
						printable = false;
						map += 1;
						this.dispose();
						new TankClient(map);
					}
				}else{
					Font f = g.getFont();
					g.setFont(new Font("Times New Roman", Font.BOLD, 60));
					this.otherWall.clear();
					g.drawString("Congratulations! ", 200, 300);
					g.setFont(f);
					win = true;
				}

			}

			if (homeTank.isLive() == false && homeTank2.isLive() == false && win == false) {
				Font f = g.getFont();
				g.setFont(new Font("Times New Roman", Font.BOLD, 40));
				tanks.clear();
				bullets.clear();
				g.drawString("Sorry. You lose!", 200, 300);
				System.out.println("2");
				g.setFont(f);
				lose = true;
			}
		}
		g.setColor(c);

		for (int i = 0; i < theRiver.size(); i++) {
			River r = theRiver.get(i);
			r.draw(g);
		}

		for (int i = 0; i < theRiver.size(); i++) {
			River r = theRiver.get(i);
			homeTank.collideRiver(r);
			if (Player2) homeTank2.collideRiver(r);
			r.draw(g);
		}

		home.draw(g);
		homeTank.draw(g);
		homeTank.eat(blood);
		if (Player2) {
			homeTank2.draw(g);
			homeTank2.eat(blood);
		}

		for (int i = 0; i < bullets.size(); i++) {
			Bullets m = bullets.get(i);
			m.hitTanks(tanks);
			m.hitTank(homeTank);
			m.hitTank(homeTank2);
			m.hitHome();
			for (int j = 0; j < bullets.size(); j++) {
				if (i == j) continue;
				Bullets bts = bullets.get(j);
				m.hitBullet(bts);
			}
			for (int j = 0; j < metalWall.size(); j++) {
				MetalWall mw = metalWall.get(j);
				m.hitWall(mw);
			}

			for (int j = 0; j < otherWall.size(); j++) {
				CommonWall w = otherWall.get(j);
				m.hitWall(w);
			}

			for (int j = 0; j < homeWall.size(); j++) {
				CommonWall cw = homeWall.get(j);
				m.hitWall(cw);
			}
			m.draw(g);
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);

			for (int j = 0; j < homeWall.size(); j++) {
				CommonWall cw = homeWall.get(j);
				t.collideWithWall(cw);
				cw.draw(g);
			}
			for (int j = 0; j < otherWall.size(); j++) {
				CommonWall cw = otherWall.get(j);
				t.collideWithWall(cw);
				cw.draw(g);
			}
			for (int j = 0; j < metalWall.size(); j++) {
				MetalWall mw = metalWall.get(j);
				t.collideWithWall(mw);
				mw.draw(g);
			}
			for (int j = 0; j < theRiver.size(); j++) {
				River r = theRiver.get(j);
				t.collideRiver(r);
				r.draw(g);
				// t.draw(g);
			}

			t.collideWithTanks(tanks);
			t.collideHome(home);

			t.draw(g);
		}

		//blood.draw(g);

		for (int i = 0; i < trees.size(); i++) {
			Tree tr = trees.get(i);
			tr.draw(g);
		}

		for (int i = 0; i < bombTanks.size(); i++) {
			BombTank bt = bombTanks.get(i);
			bt.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {
			CommonWall cw = otherWall.get(i);
			cw.draw(g);
		}

		for (int i = 0; i < metalWall.size(); i++) {
			MetalWall mw = metalWall.get(i);
			mw.draw(g);
		}

		homeTank.collideWithTanks(tanks);
		homeTank.collideHome(home);
		if (Player2) {
			homeTank2.collideWithTanks(tanks);
			homeTank2.collideHome(home);
		}

		for (int i = 0; i < metalWall.size(); i++) {
			MetalWall w = metalWall.get(i);
			homeTank.collideWithWall(w);
			if (Player2) homeTank2.collideWithWall(w);
			w.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {
			CommonWall cw = otherWall.get(i);
			homeTank.collideWithWall(cw);
			if (Player2) homeTank2.collideWithWall(cw);
			cw.draw(g);
		}

		for (int i = 0; i < homeWall.size(); i++) {
			CommonWall w = homeWall.get(i);
			homeTank.collideWithWall(w);
			if (Player2) homeTank2.collideWithWall(w);
			w.draw(g);
		}

	}

	public TankClient(){

		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jm2 = new Menu("暂停/继续");
		jm3 = new Menu("帮助");
		jm4 = new Menu("难度");
		jm5 = new Menu("其它选项");
		jm6 = new Menu("关卡");

		jm1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm2.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm3.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm4.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm5.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm6.setFont(new Font("Times New Roman", Font.BOLD, 15));

		jmi1 = new MenuItem("新的游戏");
		jmi2 = new MenuItem("退出");
		jmi3 = new MenuItem("暂停");
		jmi4 = new MenuItem("继续");
		jmi5 = new MenuItem("帮助");
		jmi6 = new MenuItem("难度1");
		jmi7 = new MenuItem("难度2");
		jmi8 = new MenuItem("难度3");
		jmi9 = new MenuItem("难度4");
		jmi10 = new MenuItem("添加玩家2");
		jmi11 = new MenuItem("加入其他游戏");
		jmi12 = new MenuItem("关卡一");
		jmi13 = new MenuItem("关卡二");
		jmi14 = new MenuItem("关卡三");
		jmi15 = new MenuItem("关卡四");
		jmi16 = new MenuItem("关卡五");
		jmi1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi2.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi3.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi4.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi5.setFont(new Font("Times New Roman", Font.BOLD, 15));

		jm1.add(jmi1);
		jm1.add(jmi2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);
		jm4.add(jmi6);
		jm4.add(jmi7);
		jm4.add(jmi8);
		jm4.add(jmi9);
		jm5.add(jmi10);
		jm5.add(jmi11);
		jm6.add(jmi12);
		jm6.add(jmi13);
		jm6.add(jmi14);
		jm6.add(jmi15);
		jm6.add(jmi16);
		jmb.add(jm1);
		jmb.add(jm2);

		jmb.add(jm4);
		jmb.add(jm5);
		jmb.add(jm6);
		jmb.add(jm3);


		jmi1.addActionListener(this);
		jmi1.setActionCommand("NewGame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("help");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("level1");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("level2");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("level3");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("level4");
		jmi10.addActionListener(this);
		jmi10.setActionCommand("Player2");
		jmi11.addActionListener(this);
		jmi11.setActionCommand("Join");
		jmi12.addActionListener(this);
		jmi12.setActionCommand("关卡一");
		jmi13.addActionListener(this);
		jmi13.setActionCommand("关卡二");
		jmi14.addActionListener(this);
		jmi14.setActionCommand("关卡三");
		jmi15.addActionListener(this);
		jmi15.setActionCommand("关卡四");
		jmi16.addActionListener(this);
		jmi16.setActionCommand("关卡五");

		this.setMenuBar(jmb);
		this.setVisible(true);

		if (!welcome) {
			printable = true;
			switch (map){
				case 1:
					Firstmap();
					break;
				case 2:
					Secondmap();
					break;
				case 3:
					Thirdmap();
					break;
				case 4:
					Fourthmap();
					break;
				case 5:
					Fifthmap();
					break;
			}

			this.setSize(Fram_width, Fram_length);
			this.setLocation(500, 260);
			this.setTitle("TankBattle");

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			this.setResizable(false);
			this.setBackground(Color.GRAY);
			this.setVisible(true);

			this.addKeyListener(new KeyMonitor());
			new Thread(new PaintThread()).start();

		} else {
			printable = false;
			JLabel jLabel = new JLabel();
			Tools.cgJLabelImg(jLabel, TankClient.class.getResource("/Images/welcome.png"));
			this.add(jLabel);
			this.setSize(800, 550);
			this.setLocation(500, 260);
			this.setTitle("TankBattle");

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
		firstrun=false;
	}

	public TankClient(int map) {
		// printable = false;


		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jm2 = new Menu("暂停/继续");
		jm3 = new Menu("帮助");
		jm4 = new Menu("难度");
		jm5 = new Menu("其它选项");
		jm6 = new Menu("关卡");

		jm1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm2.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm3.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm4.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm5.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jm6.setFont(new Font("Times New Roman", Font.BOLD, 15));

		jmi1 = new MenuItem("新的游戏");
		jmi2 = new MenuItem("退出");
		jmi3 = new MenuItem("暂停");
		jmi4 = new MenuItem("继续");
		jmi5 = new MenuItem("帮助");
		jmi6 = new MenuItem("难度1");
		jmi7 = new MenuItem("难度2");
		jmi8 = new MenuItem("难度3");
		jmi9 = new MenuItem("难度4");
		jmi10 = new MenuItem("添加玩家2");
		jmi11 = new MenuItem("加入其他游戏");
		jmi12 = new MenuItem("关卡一");
		jmi13 = new MenuItem("关卡二");
		jmi14 = new MenuItem("关卡三");
		jmi15 = new MenuItem("关卡四");
		jmi16 = new MenuItem("关卡五");

		jmi1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi2.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi3.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi4.setFont(new Font("Times New Roman", Font.BOLD, 15));
		jmi5.setFont(new Font("Times New Roman", Font.BOLD, 15));

		jm1.add(jmi1);
		jm1.add(jmi2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);
		jm4.add(jmi6);
		jm4.add(jmi7);
		jm4.add(jmi8);
		jm4.add(jmi9);
		jm5.add(jmi10);
		jm5.add(jmi11);
		jm6.add(jmi12);
		jm6.add(jmi13);
		jm6.add(jmi14);
		jm6.add(jmi15);
		jm6.add(jmi16);

		jmb.add(jm1);
		jmb.add(jm2);

		jmb.add(jm4);
		jmb.add(jm5);
		jmb.add(jm6);
		jmb.add(jm3);


		jmi1.addActionListener(this);
		jmi1.setActionCommand("NewGame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("help");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("level1");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("level2");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("level3");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("level4");
		jmi10.addActionListener(this);
		jmi10.setActionCommand("Player2");
		jmi11.addActionListener(this);
		jmi11.setActionCommand("Join");
		jmi12.addActionListener(this);
		jmi12.setActionCommand("关卡一");
		jmi13.addActionListener(this);
		jmi13.setActionCommand("关卡二");
		jmi14.addActionListener(this);
		jmi14.setActionCommand("关卡三");
		jmi15.addActionListener(this);
		jmi15.setActionCommand("关卡四");
		jmi16.addActionListener(this);
		jmi16.setActionCommand("关卡五");

		this.setMenuBar(jmb);
		this.setVisible(true);

		if (!welcome) {
			printable = true;
			switch (map){
				case 1:
					Firstmap();
					break;
				case 2:
					Secondmap();
					break;
				case 3:
					Thirdmap();
					break;
				case 4:
					Fourthmap();
					break;
				case 5:
					Fifthmap();
					break;
			}

			this.setSize(Fram_width, Fram_length);
			this.setLocation(500, 260);
			this.setTitle("TankBattle");

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			this.setResizable(false);
			this.setBackground(Color.GRAY);
			this.setVisible(true);

			this.addKeyListener(new KeyMonitor());
			new Thread(new PaintThread()).start();

		} else {
			printable = false;
			JLabel jLabel = new JLabel();
			Tools.cgJLabelImg(jLabel, TankClient.class.getResource("/Images/welcome.png"));
			this.add(jLabel);
			this.setSize(800, 550);
			this.setLocation(500, 260);
			this.setTitle("TankBattle");

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
		firstrun=false;
	}

	private class PaintThread implements Runnable {    //进行重复绘图
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			homeTank.keyReleased(e);
			homeTank2.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			homeTank.keyPressed(e);
			homeTank2.keyPressed(e);

		}

	}



	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("NewGame")) {
			printable = false;

			Object[] options = { "Confirm", "Cancel" };
			int response;
			if (!welcome){
				response = JOptionPane.showOptionDialog(this, "Confirm to start a new game?", "",
						JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						options, options[0]);
			}
			else{
				response = 0;
				welcome = false;
			}


			if (response == 0) {

				printable = true;
				this.dispose();
				new TankClient(map);
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;

		} else if (e.getActionCommand().equals("Continue")) {

			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "Confirm", "Cancel" };
			int response = JOptionPane.showOptionDialog(this, "Confirm to exit?", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("break down");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start();

			}

		} else if(e.getActionCommand().equals("Player2")){
			printable = false;
			Object[] options = { "Confirm", "Cancel" };
			int response = JOptionPane.showOptionDialog(this, "Confirm to add player2?", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				printable = true;
				this.dispose();
				TankClient Player2add=new TankClient();
				Player2add.Player2=true;
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		}
		else if (e.getActionCommand().equals("help")) {
			printable = false;
			JOptionPane.showMessageDialog(null,
					"使用WASD来控制玩家1的移动\n"+
							"按J进行射击，按R重新开始\n用方向键控制玩家2的移动"+
							"按/进行射击\n按下K释放全屏弹幕大招（cd20秒）",
					"Help", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start();
		} else if (e.getActionCommand().equals("level1")) {
			Tank.count = 12;
			Tank.speedX = 6;
			Tank.speedY = 6;
			Bullets.speedX = 10;
			Bullets.speedY = 10;
			this.dispose();
			new TankClient();
		} else if (e.getActionCommand().equals("level2")) {
			Tank.count = 12;
			Tank.speedX = 10;
			Tank.speedY = 10;
			Bullets.speedX = 12;
			Bullets.speedY = 12;
			this.dispose();
			new TankClient();

		} else if (e.getActionCommand().equals("level3")) {
			Tank.count = 20;
			Tank.speedX = 14;
			Tank.speedY = 14;
			Bullets.speedX = 16;
			Bullets.speedY = 16;
			this.dispose();
			new TankClient();
		} else if (e.getActionCommand().equals("level4")) {
			Tank.count = 20;
			Tank.speedX = 16;
			Tank.speedY = 16;
			Bullets.speedX = 18;
			Bullets.speedY = 18;
			this.dispose();
			new TankClient();
		} else if (e.getActionCommand().equals("Join")){
			printable = false;
			String s=JOptionPane.showInputDialog("Please input URL:");
			System.out.println(s);
			printable = true;
			new Thread(new PaintThread()).start();
		}else if (e.getActionCommand().equals("关卡一")){
			map = 1;
			this.dispose();
			new TankClient(map);
		}else if (e.getActionCommand().equals("关卡二")){
			map = 2;
			this.dispose();
			new TankClient(map);
		}else if (e.getActionCommand().equals("关卡三")){
			map = 3;
			this.dispose();
			new TankClient(map);
		}else if(e.getActionCommand().equals("关卡四")){
			map=4;
			this.dispose();
			new TankClient(map);
		}else if(e.getActionCommand().equals("关卡五")){
			map=5;
			this.dispose();
			new TankClient(map);
		}

	}

	public void Firstmap(){
		for (int i = 0; i < 10; i++) {
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				metalWall.add(new MetalWall(200 + 21 * i, 300, this));
				metalWall.add(new MetalWall(500 + 21 * i, 180, this));
				metalWall.add(new MetalWall(200, 400 + 21 * i, this));
				metalWall.add(new MetalWall(500, 400 + 21 * i, this));
			} else if (i < 32) {
				metalWall.add(new MetalWall(200 + 21 * (i - 16), 320, this));
				metalWall.add(new MetalWall(500 + 21 * (i - 16), 220, this));
				metalWall.add(new MetalWall(222, 400 + 21 * (i - 16), this));
				metalWall.add(new MetalWall(522, 400 + 21 * (i - 16), this));
			}
		}

		for (int i = 0; i < 20; i++) {
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));

		}

		for (int i = 0; i < 4; i++) {
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(300 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}

		}

		for (int i = 0; i < 20; i++) {
			if (i < 9)
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this,0));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this,0));
			else
				tanks
						.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this,0));
		}


	}

	public void Secondmap(){
		for (int i = 0; i < 10; i++) {
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(200 + 21 * i, 300, this));
				otherWall.add(new CommonWall(500 + 21 * i, 180, this));
				otherWall.add(new CommonWall(200, 400 + 21 * i, this));
				otherWall.add(new CommonWall(500, 400 + 21 * i, this));
			} else if (i < 32) {
				otherWall.add(new CommonWall(200 + 21 * (i - 16), 320, this));
				otherWall.add(new CommonWall(500 + 21 * (i - 16), 220, this));
				otherWall.add(new CommonWall(222, 400 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
			}
		}

		for (int i = 0; i < 20; i++) {
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));

		}

		for (int i = 0; i < 4; i++) {
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(220 + 30 * i, 360, this));
				trees.add(new Tree(440 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}

		}

		theRiver.add(new River(85, 100, this));

		for (int i = 0; i < 20; i++) {
			if (i < 9)
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this,0));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this,0));
			else
				tanks
						.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this,0));
		}



	}

	public void Thirdmap() {

		for (int i = 0; i < 10; i++) {
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 64; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(0 + 21 * i, 300, this));

				otherWall.add(new CommonWall(200, 400 + 21 * i, this));
				otherWall.add(new CommonWall(500, 400 + 21 * i, this));
			} else if (i < 32) {
				otherWall.add(new CommonWall(0 + 21 * (i - 16), 320, this));

				otherWall.add(new CommonWall(222, 400 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
			}else if (i < 48) {
				otherWall.add(new CommonWall(0 + 21 * (i - 16), 320, this));

				otherWall.add(new CommonWall(222, 400 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
			}else if (i < 64) {
				otherWall.add(new CommonWall(0 + 21 * (i - 16), 320, this));

				otherWall.add(new CommonWall(222, 400 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
			}
		}



			for (int i = 0; i < 4; i++) {
				if (i < 4) {
					trees.add(new Tree(0 + 30 * i, 360, this));
					trees.add(new Tree(220 + 30 * i, 360, this));
					trees.add(new Tree(440 + 30 * i, 360, this));
					trees.add(new Tree(660 + 30 * i, 360, this));
				}

			}
			theRiver.add(new River(85, 100, this));
			theRiver.add(new River(245, 100, this));
			theRiver.add(new River(565, 100, this));
			theRiver.add(new River(405, 100, this));
			theRiver.add(new River(725, 100, this));

			for (int i = 0; i < 20; i++) {
				if (i < 9)
					tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this, 0));
				else if (i < 15)
					tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
							this, 0));
				else
					tanks
							.add(new Tank(10, 50 * (i - 12), false, Direction.D,
									this, 0));
			}


		}

	public void Fourthmap(){
		for (int i = 0; i < 10; i++) {
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(200 + 21 * i, 400, this));

				otherWall.add(new CommonWall(200, 400 + 21 * i, this));
				otherWall.add(new CommonWall(200, 0 + 21 * i, this));
				otherWall.add(new CommonWall(500, 400 + 21 * i, this));
			} else if (i < 32) {

				otherWall.add(new CommonWall(222, 400 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(222, 0 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
			}
		}

		for (int i = 0; i < 20; i++) {
			if (i < 10) {
				metalWall.add(new MetalWall(600 + 20 * i, 300, this));

			} else if (i < 20)
				metalWall.add(new MetalWall(0 + 20 * (i - 10), 300, this));

		}

		for (int i = 0; i < 4; i++) {
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 260, this));
				trees.add(new Tree(220 + 30 * i, 200, this));
				trees.add(new Tree(440 + 30 * i, 200, this));
				trees.add(new Tree(660 + 30 * i, 260, this));
				trees.add(new Tree(0 + 30 * i, 100, this));
				trees.add(new Tree(220 + 30 * i, 100, this));
				trees.add(new Tree(440 + 30 * i, 100, this));
				trees.add(new Tree(660 + 30 * i, 100, this));
				trees.add(new Tree(0 + 30 * i, 400, this));
				trees.add(new Tree(660 + 30 * i, 400, this));
				trees.add(new Tree(0 + 30 * i, 430, this));
				trees.add(new Tree(660 + 30 * i, 430, this));

			}

		}

		theRiver.add(new River(365, 80, this));
		theRiver.add(new River(440, 440, this));

		for (int i = 0; i < 20; i++) {
			if (i < 9)
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this,0));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this,0));
			else
				tanks
						.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this,0));
		}


	}

	public void Fifthmap(){
		for (int i = 0; i < 10; i++) {
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(230, 400 + 21 * i, this));
				otherWall.add(new CommonWall(500, 400 + 21 * i, this));
			} else {
				otherWall.add(new CommonWall(252, 400 + 21 * (i - 16), this));
				otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
			}
		}

		for (int i=0;i<15;i++){
			otherWall.add(new CommonWall(230+21*i,400,this));
			otherWall.add(new CommonWall(230+21*i,378,this));
		}

		for (int i = 0; i < 48; i++) {
			trees.add(new Tree(30 * i, 340, this));
			trees.add(new Tree(30 * i, 290, this));

		}

		for (int i=0;i<7;i++){
			for (int j=0;j<7;j+=3){
				trees.add(new Tree(30*i,400+21*j,this));
				trees.add(new Tree(562+30*i,400+21*j,this));

			}
			trees.add(new Tree(-5,100+21*i,this));
			trees.add(new Tree(35,100+21*i,this));
			trees.add(new Tree(155,100+21*i,this));
			trees.add(new Tree(195,100+21*i,this));
			trees.add(new Tree(315,100+21*i,this));
			trees.add(new Tree(355,100+21*i,this));
			trees.add(new Tree(635,100+21*i,this));
			trees.add(new Tree(675,100+21*i,this));
			trees.add(new Tree(475,100+21*i,this));
			trees.add(new Tree(515,100+21*i,this));
			trees.add(new Tree(795,100+21*i,this));
			trees.add(new Tree(835,100+21*i,this));

		}
		theRiver.add(new River(85, 100, this));
		theRiver.add(new River(245, 100, this));
		theRiver.add(new River(565, 100, this));
		theRiver.add(new River(405, 100, this));
		theRiver.add(new River(725, 100, this));

		for (int i = 0; i < 20; i++) {
			if (i < 9)
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this, 0));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this, 0));
			else
				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this, 0));
		}
	}


}


