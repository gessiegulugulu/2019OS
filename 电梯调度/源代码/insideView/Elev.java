package insideView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
//import javax.swing.plaf.basic.BasicButtonUI;

import elevatorTools.dataConst;
import elevatorUI.circleButton;
import outsideView.Button;

//电梯内部
class InsideView extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4372949602779682709L;
	Elev elevator;
	
	JTextField showState=new JTextField(); //高两行 宽十五个字符
	JTextField showFloor=new JTextField(); //高两行 宽十五个字符
	
	
	JButton floorButton[];
	
	JButton openButton=new circleButton("◀▶");
	JButton closeButton=new circleButton("▶◀");
	JButton alarm=new circleButton("🔔");

	@SuppressWarnings("unused")
	InsideView(Elev ele)
	{
		setLayout(null);
		elevator=ele;
		
		int high_show=(dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor-
		        dataConst.elevatorButtonHigh*(int)(Math.ceil(dataConst.totalFloor/2)+1);
		
		floorButton=new circleButton[dataConst.totalFloor];
		
		for (int i=0,j=dataConst.totalFloor-1; i<dataConst.totalFloor; i++,j--)
		{
			floorButton[j]=new circleButton(""+(j+1));

		
			floorButton[j].setMargin(new Insets(0,0,0,0));
			floorButton[j].setFont(new Font(floorButton[j].getFont().getFontName(),
					                        floorButton[j].getFont().getStyle(),
					                        17));
			floorButton[j].setBounds( ((dataConst.totalFloor-j)%2)*dataConst.elevatorButtonWide, 
					  high_show+((dataConst.totalFloor-j-1)/2)*dataConst.elevatorButtonHigh, 
					  dataConst.elevatorButtonWide, 
					  dataConst.elevatorButtonHigh);
			
			floorButton[j].setBackground(Color.white);	
			
			floorButton[j].addActionListener(floorButtonListener);
			add(floorButton[j]);
		}
		

		//开门键
		openButton.setBounds(
				           0, 
				           high_show+((dataConst.totalFloor+1)/2)*dataConst.elevatorButtonHigh+5,
						   dataConst.elevatorButtonWide*2/3, 
						   dataConst.elevatorButtonHigh*2/3);//(0,10*h)
		openButton.setMargin(new Insets(0,0,0,0));
		openButton.setFont(new Font(openButton.getFont().getFontName(),
									openButton.getFont().getStyle(),
									11));
		openButton.addActionListener(openButtonListener);
		openButton.setBackground(Color.white);
		
		//关门键
		closeButton.setBounds(
				dataConst.elevatorButtonWide*2/3, 
	            high_show+((dataConst.totalFloor+1)/2)*dataConst.elevatorButtonHigh+5,
				dataConst.elevatorButtonWide*2/3, 
				dataConst.elevatorButtonHigh*2/3);//(w,10*h)
		closeButton.setMargin(new Insets(0,0,0,0));
		closeButton.setFont(new Font(closeButton.getFont().getFontName(),
									 closeButton.getFont().getStyle(),
									 11));
		closeButton.addActionListener(closeButtonListener);
		closeButton.setBackground(Color.white);
		
		
		//alarm
	    alarm.setBounds(
				dataConst.elevatorButtonWide*4/3, 
	            high_show+((dataConst.totalFloor+1)/2)*dataConst.elevatorButtonHigh+5,
				dataConst.elevatorButtonWide*2/3, 
				dataConst.elevatorButtonHigh*2/3);//(w,10*h)
		alarm.setMargin(new Insets(0,0,0,0));
		alarm.setFont(new Font(alarm.getFont().getFontName(),
									 alarm.getFont().getStyle(),
									 19));
		alarm.addActionListener(alarmListener);
		alarm.setBackground(Color.white);
		
		//文本框
		   //上下行状态打印
		showState.setBorder(BorderFactory.createEmptyBorder());
		showState.setEditable(false); //不可编辑
		showState.setBounds(
				0,
				10,
				dataConst.elevatorButtonWide*2 ,
				(high_show-30)/2 );
		
		int wordFSize=45;
		int wordSSize=30;
		if(dataConst.totalFloor>=15)
		{
			wordFSize=45;
			wordSSize=30;
		}
		else if(12<dataConst.totalFloor&&dataConst.totalFloor<=14)
		{
			wordFSize=30;
			wordSSize=20;
		}
		else if(10<dataConst.totalFloor&&dataConst.totalFloor<=12)
		{
			wordFSize=25;
			wordSSize=15;		
		}
		else if(8<dataConst.totalFloor&&dataConst.totalFloor<=10)
		{
			wordFSize=18;
			wordSSize=10;		
		}

		
		showState.setFont(new Font(
				showState.getFont().getFontName(),
				showState.getFont().getStyle(),
				 wordSSize)
				);
		showState.setHorizontalAlignment(JTextField.CENTER);
		showState.setBackground(Color.black);
		showState.setForeground(Color.white);
		showState.setText("");
		//showState.addActionListener(showStateListener);
		
		
		  //所在楼层打印
		showFloor.setBorder(BorderFactory.createEmptyBorder());
		showFloor.setEditable(false); //不可编辑
		showFloor.setBounds(
				0,
				(high_show-30)/2+10,
				dataConst.elevatorButtonWide*2 ,
				(high_show-30)/2 );

		showFloor.setFont(new Font(
				showFloor.getFont().getFontName(),
				showFloor.getFont().getStyle(),
				 wordFSize)
				);
		showFloor.setHorizontalAlignment(JTextField.CENTER);
		showFloor.setBackground(Color.black);
		showFloor.setForeground(Color.white);
		showFloor.setText("1");
		
		add(openButton);
		add(closeButton);
		
		if(dataConst.totalFloor>8)
		{
			add(showState);
		    add(showFloor);
		}
		
		add(alarm);
//		add(showStateFloor);
	}
	
	ActionListener floorButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent	e)
		{
			int floor=Integer.parseInt(( (JButton)e.getSource() ).getText());//本次层数

			if (elevator.getFloor()==floor && elevator.doorOpen) 
			{
				elevator.reopenButton() ;
				return;
			}
			if (elevator.getElevatorState()==0 && elevator.getFloor()==floor)
			{//停在本层没有运行
				elevator.openButton();
				return;
			}

			
			floorButton[floor-1].setBackground(Color.LIGHT_GRAY);
			
			if (floor==dataConst.totalFloor)
			{
				elevator.setArriveFloor(2*dataConst.totalFloor-floor);
				return;
			}
			if (floor==1)
			{
				elevator.setArriveFloor(0);
				return;
			}
			if (elevator.getFloor()<floor) 
			{
				elevator.setArriveFloor(floor-1);
			}
			else if (elevator.getFloor()>floor) 
			{
				elevator.setArriveFloor(2*dataConst.totalFloor-floor);
			}
			else if (elevator.getFloor()==floor)
			{
				if (elevator.getElevatorState()==1 || elevator.getElevatorState()==2)
					 elevator.setArriveFloor(2*dataConst.totalFloor-floor);
				else if (elevator.getElevatorState()==-1 || elevator.getElevatorState()==-2)
					elevator.setArriveFloor(floor-1);
			}
		}
	};
	
	//关门动作
	ActionListener closeButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (!elevator.doorOpen) 
				return;//状态为关  不处理
			elevator.closeButton();
		}
	};
	
	//开门动作
	ActionListener openButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (elevator.doorOpen)	
				elevator.reopenButton();//终止线程
			else if (elevator.getElevatorState()==0) 
				elevator.setArriveFloor(elevator.getFloor()-1);
			else 
				return ;
		}
	};
	
	//alarm
	ActionListener alarmListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			JLabel message = new JLabel("Someone triggered an alert ! ");
			message.setForeground(Color.black);
			message.setFont(new Font(
					message.getFont().getFontName(),
					message.getFont().getStyle(),
					 25));	
			JOptionPane.showMessageDialog(null,message, "Elevator",JOptionPane.WARNING_MESSAGE);  		
		}
	
	};
		
	
}


//电梯动态上下楼视角
class FloorView extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8789945850027246030L;
	//JTextField floor[];
	JButton floor[];
	Elev elevator;
	FloorView(Elev ele)
	{
		elevator=ele;
		setLayout(null);
		//floor=new JTextField[dataConst.totalFloor];
		floor=new JButton[dataConst.totalFloor];
		//floor=new circleButton[dataConst.totalFloor];
		
		/***********************************************************************************************************
		 * 		添加电梯示意图
		 *           
		 **********************************************************************************************************/	
		
		//电梯	
		ele.elevator.setBounds(0, 
				(dataConst.totalFloor-1)*(dataConst.floorHigh+dataConst.floorSpace)+dataConst.floorSpace, 
				dataConst.floorWide, 
				dataConst.floorHigh);
//////		
//		ImageIcon image = new ImageIcon("closeDoor4.png");
//		//根据按钮大小改变图片大小
//	    Image temp = image.getImage().getScaledInstance(ele.elevator.getWidth(), ele.elevator.getHeight(), image.getImage().SCALE_DEFAULT);
//		image = new ImageIcon(temp);
//		ele.elevator.setIcon(image);
//////		
		ele.elevator.setEnabled(false);
		ele.elevator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));//√
		ele.elevator.setBackground(Color.gray);//√		
		add(ele.elevator);
		
		/***********************************************************************************************************
		 * 		添加楼层提示
		 *           
		 **********************************************************************************************************/	
		
		//楼层
		for (int i=0,j=dataConst.totalFloor-1; i<dataConst.totalFloor; i++,j--)
		{
			floor[j]=new JButton(""+(dataConst.totalFloor-i));	

			floor[j].setEnabled(false);
			floor[j].setMargin(new Insets(0,0,0,0));
			floor[j].setFont(new Font(floor[j].getFont().getFontName(),
										floor[j].getFont().getStyle(),
										 12));
			floor[j].setVerticalAlignment(SwingConstants.CENTER);
			floor[j].setBorderPainted(false);
			floor[j].setContentAreaFilled(false);
			floor[j].setForeground(Color.black);
			floor[j].setBounds(0, 
					i*(dataConst.floorHigh+dataConst.floorSpace)+dataConst.floorSpace, 
					dataConst.floorWide,
					dataConst.floorHigh);
			
			add(floor[j]);
		}

	}
}


//一台电梯为一个线程
public class Elev extends Thread
{
	JButton elevator=new JButton(); //电梯
	
	private InsideView elevatorInsideView=new InsideView(this); //视图
	private FloorView floorView=new FloorView(this); //视图
	private int state;	//电梯现行状态
						//当前上  未来上   1
						//当前上  停       10
						//当前上  未来下   2
						//停               0
	private boolean restart;
	private int floor;  //当前层
	public boolean arrive[]; //电梯需要到达的楼层
	private int aimFloor; //电梯下一步要到的楼层
	boolean doorOpen;
	
	//初始状态 关门停在一楼
	public Elev()
	{
		doorOpen=false;
		state=0;
		floor=1;
		arrive=new boolean[dataConst.totalFloor*2];
		aimFloor=1;
		for (int i=0; i<dataConst.totalFloor*2; i++)
			arrive[i]=false;
	}
	
	//返回状态
	public int getElevatorState()
	{
		return state;
	}
	
	//判断下一步电梯状态,传入state
	private int upORdown()
	{
		if ((state==0) && (arrive[floor-1]))	
			return 100;
		if ((state==0) && (arrive[2*dataConst.totalFloor-floor])) 
			return -100;	//电梯静止但在当前楼层有需求		
		
		if (state==0) //停
		{
			for (int i=0; i<dataConst.totalFloor; i++)
			{
				if (arrive[i])
				{
					aimFloor=i+1;	
					if (i+1>floor)		
						return 1;
					else 	
						return -2;	

				}
				if (arrive[2*dataConst.totalFloor-i-1])
				{
					aimFloor=i+1;
					if (i+1>floor)	
						return 2;
					else 
						return -1;
				}
			}
			search(this);
		}
		
		if (state==1 || state==2)	//电梯当前向上
		{
			for (int i=dataConst.totalFloor-1; i>floor-1; i--)
			{
				if (arrive[i])									 
				{
					//未来向上
					aimFloor=Math.max(i+1,aimFloor);	
					return 1;
				}
				if (arrive[2*dataConst.totalFloor-i-1])
				{
					//未来向下
					aimFloor=Math.max(i+1,aimFloor);	
					return 2;
				}
			}
		}
		
		if (state==-1 || state==-2) //当前向下
		{
			for (int i=0; i<floor-1; i++)
			{
				if (arrive[2*dataConst.totalFloor-i-1])
				{
					//未来向下
					aimFloor=Math.min(i+1,aimFloor);
					return -1;
				}
				if (arrive[i])
				{
					//未来向上
					aimFloor=Math.min(i+1,aimFloor);
					return -2;
				}
			}
			
		}
		return 0;
	} 
	/***********************************************************************************************************
	 * 		
	 *   run()不需要用户调用，当通过start方法启动一个线程之后，自动进入run方法体去执行具体的任务。
	 *    
	 *     重写run方法中定义具体要执行的任务。
	 *           
	 **********************************************************************************************************/
	public void run()
	{
		while (true)
		{
			state=upORdown(); //得到新的状态
			
			//对应upORdown中的状态进行处理
			if(state==0)
				elevatorInsideView.showState.setText("");
			if (state==1 || state==2)	//电梯状态向上
				upFloor();
			else if (state==-1 || state==-2) //电梯状态向下	
				downFloor();
			else if (state==100)
			{
				elevatorInsideView.showState.setText("▲");
				arrive[floor-1]=false;
				Button.call[floor-1]=false;				
				
				elevatorInsideView.floorButton[floor-1].setBackground(Color.white);
				
				Button.callButton[floor-1].setBackground(Color.white);
				openButton();
				state=1;
			}
			else if (state==-100)
			{
				elevatorInsideView.showState.setText("▼");
				arrive[2*dataConst.totalFloor-floor]=false;
				Button.call[2*dataConst.totalFloor-floor]=false;
				
				elevatorInsideView.floorButton[floor].setBackground(Color.white);
				Button.callButton[2*dataConst.totalFloor-floor].setBackground(Color.white);
				openButton();
				state=-1;
			}
		}
	}
	
	//上行
	public void upFloor()
	{
		elevatorInsideView.showState.setText("▲");
		for (int i=0; i<dataConst.floorHigh+dataConst.floorSpace; i++)
		{
			try	
			{
				Thread.sleep(40);
			}
			catch (InterruptedException e) {}
			
			elevator.setLocation(elevator.getLocation().x, 
					             elevator.getLocation().y-1);//电梯示意图上行一个单元
		}
		
		floor++; //当前楼层上移一层
		elevatorInsideView.showFloor.setText(""+floor);
		
		if (aimFloor==floor && //到达目标楼层
			state==2 &&          //当前上未来下
			!arrive[floor-1] &&  //无向上需求
			!Button.call[floor-1])
		{			
			elevatorInsideView.floorButton[floor-1].setBackground(Color.white);
			arrive[2*dataConst.totalFloor-floor]=false;
			Button.call[2*dataConst.totalFloor-floor]=false;
			
			
			Button.callButton[2*dataConst.totalFloor-floor].setBackground(Color.white);			
			//Button.callButton[2*dataConst.totalFloor-floor].setBorderPainted(true);		
			
			openButton();			
		}
		//向上需求
		else if (arrive[floor-1] || Button.call[floor-1] )
		{
			elevatorInsideView.floorButton[floor-1].setBackground(Color.white);
			arrive[floor-1]=false;
			Button.call[floor-1]=false;
			
			Button.callButton[floor-1].setBackground(Color.white);
			
			openButton();			
		}
	}
	
	public void downFloor()
	{
		elevatorInsideView.showState.setText("▼");
		for (int i=0; i<dataConst.floorHigh+dataConst.floorSpace; i++)
		{
			try	
			{
				this.sleep(40);
			}
			catch (InterruptedException e) {}
			
			elevator.setLocation(elevator.getLocation().x, 
					elevator.getLocation().y+1);
		}
		floor--;
		elevatorInsideView.showFloor.setText(""+floor);
		
		//将上行
		if (aimFloor==floor &&
			state==-2 && 
			!arrive[2*dataConst.totalFloor-floor] &&
			!Button.call[2*dataConst.totalFloor-floor])
		{
			arrive[floor-1]=false;
			Button.call[floor-1]=false;
			elevatorInsideView.floorButton[floor-1].setBackground(Color.white);
			
			Button.callButton[floor-1].setBackground(Color.white);
			
			
			openButton();
		}
		//将下行
		else if (arrive[2*dataConst.totalFloor-floor] ||
				Button.call[2*dataConst.totalFloor-floor])
		{
				arrive[2*dataConst.totalFloor-floor]=false;				
				Button.call[2*dataConst.totalFloor-floor]=false;
				
				elevatorInsideView.floorButton[floor-1].setBackground(Color.white);				
				Button.callButton[2*dataConst.totalFloor-floor].setBackground(Color.white);
				
				openButton();
		}

	}
	
	//寻找目标
	static public synchronized void search(Elev ele)	
	{
		int i=Button.getRequire();
		
		if (i==-1) 
			return ;
		int floor=i+1;
		if (floor>dataConst.totalFloor)
			floor=2*dataConst.totalFloor-floor;
		if (Button.call[i]&& Button.isNearest(ele, floor))//Button.require[i] 
		{
			ele.arrive[i]=true;
			Button.call[i]=false;
			Button.queue.remove();
			return ;
		}
	}
	
	public void setFloor(int i)
	{
		floor=i;
	}
	
	public void setArriveFloor(int i)
	{
		arrive[i]=true;
	}
	
	public int getFloor()
	{
		return floor;
	}
	
	public void openButton()
	{	
		elevator.setContentAreaFilled(false); //√	
		doorOpen=true;		

		restart=false;
		try	
		{
			this.sleep(2000);
		}
		catch (InterruptedException e)
		{
			if (restart)	
				openButton();
		}
		closeButton();
	}
	
	public void closeButton()
	{
		if (doorOpen && state!=-100 && state!=100)
			this.interrupt();

		elevator.setBackground(Color.gray);
		elevator.setContentAreaFilled(true);   

		doorOpen=false;
	}
	
	public void reopenButton()
	{
		restart=true;
		this.interrupt();
	}
	
	public void add(JPanel frame,int i)
	{	
		frame.setLayout(null);
				
		elevatorInsideView.setBounds(
				i*200+30, 
				20,
				dataConst.elevatorButtonWide*2, 
				(dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor
				);
		
		floorView.setBounds(
				200*(i+1)-dataConst.elevatorButtonWide*2+10,
				20, 
				dataConst.elevatorButtonWide*2,
				(dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor);

		
		frame.add(floorView);
		frame.add(elevatorInsideView);
		
	}
}