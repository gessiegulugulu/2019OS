package elevatorUI;

import java.awt.*;
import javax.swing.*;

 
public class roundcornerButton extends JButton  
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Shape shape = null;
	 private Color quit = new Color(0, 0, 228);// �뿪ʱ��ɫ
	 public roundcornerButton(String s) 
	 {
	  super(s);
	  //addMouseListener(this);
	  setContentAreaFilled(false);// �Ƿ���ʾ��Χ�������� ѡ��
	 }
	 public void paintComponent(Graphics g) 
	 {
	   g.setColor(quit);
	  //���Բ�Ǿ������� Ҳ����Ϊ������ͼ��
	  g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1,
	      20, 20);
	  //���������� ���򻭲�����
	  super.paintComponent(g);
	 }
	 public void paintBorder(Graphics g) 
	 {
	  //���߽�����
	  g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1,
	      20, 20);
	 }
}


