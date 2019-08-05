import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import elevatorTools.dataConst;
import insideView.Elev;
import outsideView.Button;

class View extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	View()
	{		
//		int frameWide=Math.min(1160, dataConst.totalElevator*200+160);
//		int frameHigh=Math.min(680, (dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor+55);
		
//		this.setSize(frameWide,frameHigh);
				
		/***********************************************************************************************************
		 * 		�����ⲿ�ӽǵĿؼ��Ͳ���
		 **********************************************************************************************************/		
			
		//outside view ��ǩ
		JTextField outsideView=new JTextField("Outside View");
		outsideView.setBorder(BorderFactory.createEmptyBorder());
		outsideView.setEditable(false);
		outsideView.setBounds(dataConst.totalElevator*200+20,
			      10, 
				  100,
				 20
				 );
		outsideView.setFont(new Font(
				outsideView.getFont().getFontName(),
				outsideView.getFont().getStyle(),
				 17)
				);
		outsideView.setForeground(Color.black);

		
		
		//�ⲿ��ť
		Button insideViewButton=new Button();
		insideViewButton.setBounds(dataConst.totalElevator*200+30,
					      30, 
						  dataConst.elevatorButtonWide*2,
						  (dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor);
	
		
		 
		/***********************************************************************************************************
		 * 		����Panel�������ⲿ�ӽǣ���ť+label��+ �ڲ��ӽ� + �߿��߲�����panel��
		 **********************************************************************************************************/	
		JPanel container=new JPanel(){			
			private static final long serialVersionUID = 1L;
			
			public void paint(Graphics g) {
				super.paint(g);
				int i=0;
				//((Graphics2D)g).setStroke(new BasicStroke(2f)); //���ڱ߿��߿�
				for(i=0;i<dataConst.totalElevator;i++) 
				{           
			        g.drawRoundRect(
							i*200+15,
							15,
							dataConst.elevatorButtonWide*4,
							(dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor+20,
							20, 20);
			        
			        g.drawLine(
			        		200*(i+1)-dataConst.elevatorButtonWide*2+5,
			        		15,
			        		200*(i+1)-dataConst.elevatorButtonWide*2+5,
			        		(dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor+20+15);
				}
			}
			
		};
			
		container.setPreferredSize(new Dimension(
					dataConst.totalElevator*200+140,
					(dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor+35+10
				));
			

	    container.add(outsideView);
	    container.add(insideViewButton);	
		
		
		dataConst.elevator=new Elev[dataConst.totalElevator];
		for(int i=0;i<dataConst.totalElevator;i++) 
		{
			dataConst.elevator[i]=new Elev();
			dataConst.elevator[i].add(container,i);
		}
		
		for (int i=0; i<dataConst.totalElevator	; i++)
		{
			dataConst.elevator[i].start();
		}
		
		
		/***********************************************************************************************************
		 * 		����JScrollPane����JPanel����JScrollPane�ϣ��ٽ�JScrollPane����frame��      ,����frame
		 *           
		 **********************************************************************************************************/		
		JScrollPane scrollPane = new JScrollPane(container);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	   // scrollPane.setBounds(0, 0, 100,100);
	    
		int frameWide=Math.min(1180, dataConst.totalElevator*200+150+30);//+160
		int frameHigh=Math.min(695, (dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor+45+50);		
		this.setSize(frameWide,frameHigh);
		
	    this.getContentPane().add(scrollPane);
	    this.setVisible(true);
	    this.setResizable(true);//!!!!
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	 
}

public class main 
{	
	public static void main(String[] args) 
	{// TODO �Զ����ɵķ������
		View frame=new View();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}


