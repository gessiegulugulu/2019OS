package elevatorUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class circleButton extends JButton 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7567311421767468779L;

	public circleButton(String label) {
		super(label);

		// ��ȡ��ť����Ѵ�С
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);

		setContentAreaFilled(false);
	}

	// ��Բ�İ�ť�ı����ͱ�ǩ
	protected void paintComponent(Graphics g) {

		if (getModel().isArmed()) {
			g.setColor(Color.lightGray); // ���ʱ����
		} else {
			g.setColor(getBackground());
		}
		// fillOval������һ�����ε�������Բ��������������Բ��
		// ������Ϊ������ʱ����������Բ����Բ
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

		super.paintComponent(g);
	}

	// �ü򵥵Ļ�����ť�ı߽硣
	protected void paintBorder(Graphics g) {
		//((Graphics2D) g).setStroke(new BasicStroke(2.0f));
		//g.setColor(Color.black);
		// drawOval���������ε�������Բ��������䡣ֻ����һ���߽�
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

	// shape�������ڱ��水ť����״�����������������ť�¼�
	Shape shape;

	public boolean contains(int x, int y) {

		if ((shape == null) || (!shape.getBounds().equals(getBounds()))) {
			// ����һ����Բ�ζ���
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		// �ж�����x��y�����Ƿ����ڰ�ť��״�ڡ�
		return shape.contains(x, y);
	}
}


