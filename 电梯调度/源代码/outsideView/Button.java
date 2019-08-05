package outsideView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

import elevatorTools.dataConst;
import insideView.Elev;

public class Button extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5812529950052200916L;
	public static Queue<Integer> queue = new LinkedList<Integer>();//存产生需求的队列
	public static JButton []callButton=new JButton[dataConst.totalFloor*2];	
	public static boolean []call=new boolean[dataConst.totalFloor*2];
	
	
	public Button()
	{
		//需求全空
		for (int i=0; i<dataConst.totalFloor; i++)
		{
			call[i]=false;			
		}	
		
		//按钮赋值
		for (int i=0; i<dataConst.totalFloor; i++)
		{
			callButton[i]=new JButton((i+1)+"▲");//上 数组中前20
		}
		for (int i=dataConst.totalFloor; i<2*dataConst.totalFloor; i++)
		{
			callButton[i]=new JButton(2*dataConst.totalFloor-i+"▼");//下 数组中后20 callbutton[20] 20↓ 
		}
		/*
		 * callbutton[20] 20↓ 
		 *            21  19↓
		 *            39  1
		 */
		
		
		setLayout(null);//清空布局管理器
		//画出按钮
		for (int i=0; i<dataConst.totalFloor*2-1; i++)//1没有下
		{
			if (i==dataConst.totalFloor-1) 
				continue;//20层没有上  数组19
			int j;
			if (i<dataConst.totalFloor-1) //图标为上
				j=dataConst.totalFloor-i-1;//j为
			else j=i-dataConst.totalFloor;
						
			setButton(callButton[i],i,j);
			add(callButton[i]);
		}
	}
	
	//设置按钮样式
	public void setButton(JButton b,int i,int j)
	{
		
		b.setMargin(new Insets(0,0,0,0));//设置空隙
		b.setVerticalAlignment(SwingConstants.CENTER);//居中对齐
		b.setFont(new Font(b.getFont().getFontName(),
				           b.getFont().getStyle(),
				           15));
		
		b.setBounds(
				i/dataConst.totalFloor*dataConst.elevatorButtonWide, 
				j*(dataConst.floorHigh+dataConst.floorSpace)+dataConst.floorSpace,
				dataConst.floorWide,
				dataConst.floorHigh);//(x y wide high)
		
		b.setBorderPainted(true);
		b.setBackground(Color.white);
		b.setForeground(Color.black);
		
		b.addActionListener(callButtonListener);
	}
	
	//监听
	ActionListener callButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent	e)
		{
			JButton press=(JButton) e.getSource();
			int temp = 0;
			for (int i=0; i<dataConst.totalFloor*2; i++)
			{
				if (press==callButton[i])
				{
					temp=i;
					break;
				}
			}
			
			if (press.getBackground()==Color.LIGHT_GRAY) 
				return ;			
		
			press.setBackground(Color.LIGHT_GRAY);	
			
			call[temp]=true;//需求集对应位置置true

			queue.offer(temp);
			
		}
	};
	
	
	//路程最短的电梯
	public static boolean isNearest(Elev ele, int floor)
	{
		for (int i=0; i<dataConst.totalElevator; i++)
		{
			if (dataConst.elevator[i].getElevatorState()==0 
				&& Math.abs(ele.getFloor()-floor)>Math.abs(dataConst.elevator[i].getFloor()-floor)) 
				return false;
		}
		return true;
	}

	public static int getRequire()
	{
		while (true)
		{		
			if (queue.isEmpty()) 
				return -1;
			int floor=queue.element();
			
			if (call[floor]) //true
				return floor;
			else queue.remove();//弹出	
		
		}
	}
	

}