package File;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;


public class FolderToolBar
{
	JTextField addressField;
	JTextField searchField;

	JPanel addressPanel;
	JPanel searchPanel;

	JButton enterSearch;
	JButton enterAddress;

	JPanel panel;
	static FolderToolBar folderToolBar;

	static public FolderToolBar getToolBar()
	{
		return folderToolBar;
	}

	public FolderToolBar()
	{

		folderToolBar=this;

		panel=new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.BLACK);
		panel.setForeground(Color.WHITE);

		JButton back=new JButton("⇐");
		back.setFont(new Font(back.getFont().getFontName(),
		           back.getFont().getStyle(),
		           21));
		Color backgroundColor=new Color(230,230,230);
		back.setBackground(backgroundColor);
		Color foregroundColor=new Color(104,118,138);
		back.setForeground(foregroundColor);
		back.setBorderPainted(false);
		back.setFocusPainted(false);
		back.addActionListener(backButtonListener);
		panel.add(back,BorderLayout.WEST);


		searchPanel=new JPanel();
		searchPanel.setLayout(new BorderLayout());

		enterSearch=new JButton("🔍");
		enterSearch.setFont(new Font(enterSearch.getFont().getFontName(),
				   enterSearch.getFont().getStyle(),
		           21));
		enterSearch.setBackground(backgroundColor);
		enterSearch.setForeground(foregroundColor);
		//enterSearch=new JButton(new ImageIcon(FolderToolBar.class.getResource("/img/search.png")));
		enterSearch.setBorderPainted(false);
		enterSearch.setFocusPainted(false);
		enterSearch.addActionListener(searchButtonListener);
		searchPanel.add(enterSearch,BorderLayout.EAST);


		searchField=new JTextField();
		searchField.setPreferredSize(new Dimension(150,30));
		MatteBorder border = new MatteBorder(2, 2, 2, 2, new Color(217, 217, 217));
		searchField.setBorder(border);
		searchField.setFont(new Font(searchField.getFont().getFontName(),searchField.getFont().getStyle(),20));
		searchPanel.add(searchField,BorderLayout.CENTER);


		panel.add(searchPanel,BorderLayout.EAST);

		addressPanel=new JPanel();
		addressPanel.setLayout(new BorderLayout());
		addressField=new JTextField();
		addressField.setFont(new Font(addressField.getFont().getFontName(),addressField.getFont().getStyle(),20));
		addressField.setBorder(border);

		enterAddress=new JButton("⇑");
		enterAddress.setFont(new Font(enterAddress.getFont().getFontName(),
		           enterAddress.getFont().getStyle(),
		           21));
		enterAddress.setBackground(backgroundColor);
		enterAddress.setForeground(foregroundColor);

		enterAddress.setFocusPainted(false);
		enterAddress.setBorderPainted(false);
		enterAddress.addActionListener(addressButtonListener);
		addressPanel.add(enterAddress,BorderLayout.EAST);
		addressPanel.add(addressField,BorderLayout.CENTER);
		panel.add(addressPanel,BorderLayout.CENTER);
	}

	void setAddress(String str)
	{
		addressField.setText(str);
	}

	ActionListener backButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (ContentPanel.getShowingPanel()==null || ContentPanel.getShowingPanel().fatherContentPanel==null)	//在磁盘下或者在磁盘的第一级目录
			{
				setAddress("Disk");
				ContentPanel.switchPanel(Disk.getDiskPanel());
				return ;
			}
			else
			{
				ContentPanel.switchPanel(ContentPanel.getShowingPanel().fatherContentPanel);
			}
		}
	};

	ActionListener searchButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			JPanel panel=new JPanel();
			String str=searchField.getText();
			while (str.length()!=0 && str.charAt(str.length()-1)==' ') str=str.substring(0, str.length()-1);

			if (str.length()==0) return ;

			//清空面板
			Disk.getMainPanel().removeAll();
			Disk.getMainPanel().repaint();
			Disk.getMainPanel().updateUI();


			ContentPanel contentPanel=ContentPanel.getShowingPanel();
			if (contentPanel==null) contentPanel=Disk.contentPanel;
			contentPanel.addKeyStringDocument(str,panel);

			panel.setBackground(Color.white);
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));

			Disk.getMainPanel().add(panel);
		}
	};

	ActionListener addressButtonListener=new ActionListener()
	{
		void warningErrorAddress()
		{
			JOptionPane.showMessageDialog(null, "地址输入错误", "waring", JOptionPane.WARNING_MESSAGE, null);
		}

		public void actionPerformed(ActionEvent e)
		{
			String str=addressField.getText();
			while (!str.isEmpty() && str.charAt(str.length()-1)==' ') str=str.substring(0, str.length()-1);
			while (!str.isEmpty() && str.charAt(0)==' ') str=str.substring(1);
			if (str.length()<=4)
			{
				if (str.equals("Disk"))
				{
					setAddress("Disk");
					ContentPanel.switchPanel(Disk.getDiskPanel());
				}
				else
				{
					warningErrorAddress();
				}
				return ;
			}

			if (!str.substring(0, 5).equals("Disk/"))
			{
				warningErrorAddress();
				return ;
			}
			str=str.substring(5);
			ContentPanel contentPanel=Disk.contentPanel;

			boolean isFind;
			while (!str.isEmpty())
			{
				int next=str.indexOf('/');
				if (next==-1) break;

				isFind=false;

				String nextFolder=str.substring(0,next);
				str=str.substring(next+1, str.length());

				for (int i=0; i<contentPanel.folderList.size(); i++)
				{
					if (contentPanel.folderList.get(i).name.equals(nextFolder))
					{
						isFind=true;
						contentPanel=contentPanel.folderList.get(i).contentPanel;
					}
				}

				if (!isFind)
				{
					warningErrorAddress();
					return ;
				}
			}
			ContentPanel.switchPanel(contentPanel);
			if (!str.isEmpty())
			{
				isFind=false;
				for (int i=0; i<contentPanel.fileList.size(); i++)
				{
					if (contentPanel.fileList.get(i).name.equals(str))
					{
						contentPanel.fileList.get(i).open();
						isFind=true;
					}
				}
				if (!isFind)
				{
					JOptionPane.showMessageDialog(null, "无法找到该文件", "地址错误", JOptionPane.ERROR_MESSAGE, null);
				}
			}
		}
	};
}