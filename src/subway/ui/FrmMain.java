package subway.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import subway.control.lineprocess;
import subway.control.readtxt;
import subway.model.Beanstation;
import subway.ui.FrmMain;

public class FrmMain extends JFrame implements ActionListener{
	private JPanel statusBar = new JPanel();
	private JPanel formBar=new JPanel();//��·-վ��
	private JPanel formBar1=new JPanel();//���

	//��ѯ��̻���·��
    private JPanel searchbar=new JPanel();//��
    private JPanel resultbar=new JPanel();//���
    private JLabel from=new JLabel("���");
    private JLabel to=new JLabel("�յ�");
    private JTextField sfrom=new JTextField(10);
    private JTextField sto=new JTextField(10);
    private JButton searchButton=new JButton("��ѯ");
    private JTextArea output=new JTextArea();//�����
    
    //��·��
	private Object tblMenuTitle[]= {"��·"};
	private Object tblMenuData[][];
	DefaultTableModel tabMenuModel=new DefaultTableModel();
	private JTable dataTableMenu=new JTable(tabMenuModel);
	//վ����
	private Object tblMenuStepTitle[]= {"վ��"};
	private Object tblMenuStepData[][];
	DefaultTableModel tabMenuStepModel=new DefaultTableModel();
	private JTable dataTableMenuStep=new JTable(tabMenuStepModel);
	
	List<Beanstation> lines=null;//��·��
	List stations=null;//վ���
	private static lineprocess<String> graph;
	//��ʾ��·ģ��
	private void loadline(){
		tblMenuData =  new Object[lines.size()][1];
		for(int i=0;i<lines.size();i++){
			for(int j=0;j<1;j++)
				tblMenuData[i][j]=lines.get(i).getStationname();
		}
		tabMenuModel.setDataVector(tblMenuData,tblMenuTitle);
		this.dataTableMenu.validate();
		this.dataTableMenu.repaint();
	}
	//��ʾվ��ģ��
	private void loadstation(int index){
		stations=lines.get(index).getStations();
		tblMenuStepData =  new Object[stations.size()][1];
		for(int i=0;i<stations.size();i++){
			for(int j=0;j<1;j++)
				tblMenuStepData[i][j]=stations.get(i);
		}
		tabMenuStepModel.setDataVector(tblMenuStepData,tblMenuStepTitle);
		this.dataTableMenuStep.validate();
		this.dataTableMenuStep.repaint();
	}

	public FrmMain() throws IOException{
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setTitle("������·��ѯϵͳ");
		String data="C:\\Users\\ushop\\Desktop\\data.txt";
		lines=new readtxt().txt(data);
		List<String> vexnum = new ArrayList<String>();//վ�㣨���㣩����
		for(Beanstation station:lines) {
			String[] s=station.getStations().toArray(new String[station.getStations().size()]);
			for (String t : s) {
				if (!vexnum.contains(t)) {
					vexnum.add(t);
				}
			}
		}

		graph = new lineprocess<String>(vexnum);
		for(Beanstation station:lines) {
			String[] s=station.getStations().toArray(new String[station.getStations().size()]);
			for (int i = 0; i < s.length - 1; i++)
				graph.edg(s[i], s[i + 1], 1);
			
		}
		//���������·��ʾվ��
	    this.dataTableMenu.addMouseListener(new MouseAdapter (){
			@Override
			public void mouseClicked(MouseEvent e) {
				int i=FrmMain.this.dataTableMenu.getSelectedRow();
				if(i<0) {
					return;
				}
				FrmMain.this.loadstation(i);
				System.out.println(lines.get(i).getStations());
			}
	    });
	    this.loadline();
	    this.loadstation(0);//��ʾ1����
	    
	    //formBar����
	    formBar.setLayout(new BorderLayout());
	    formBar.add(new JScrollPane(this.dataTableMenu), BorderLayout.WEST); 
	    formBar.add(new JScrollPane(this.dataTableMenuStep), BorderLayout.CENTER);
	    
	    //formBar1����
	    formBar1.setLayout(new BorderLayout());
	    searchButton.addActionListener(this);//����
	    searchbar.setLayout(new FlowLayout());
	    searchbar.add(from);
	    searchbar.add(sfrom);
	    searchbar.add(to);
	    searchbar.add(sto);
	    searchbar.add(searchButton);
	    formBar1.add(searchbar,BorderLayout.NORTH);
	    
	    resultbar.setLayout(new BorderLayout());
	    resultbar.add(output);
	    formBar1.add(resultbar,BorderLayout.CENTER);
	    
	    this.getContentPane().add(formBar,BorderLayout.NORTH);//��·����վ��
	    this.getContentPane().add(formBar1,BorderLayout.CENTER);//��̲�ѯ
	    
	    //״̬��
	    statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
	    JLabel label=new JLabel("hi");
	    statusBar.add(label);
	    this.getContentPane().add(statusBar,BorderLayout.SOUTH);
	    this.addWindowListener(new WindowAdapter(){   
	    	public void windowClosing(WindowEvent e){ 
	    		System.exit(0);
             }
        });
	    this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String from=sfrom.getText();
		String to=sto.getText();
		if(e.getSource()==searchButton) {
			if(from.equals("")||to.equals("")) {
				try {
					throw new Exception();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "���������վ�㣡","����",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if(from.equals(to)) {
				try {
					throw new Exception();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "���������ʼվ���յ�վ��ͬ��","����",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if (!graph.vertex.contains(from) || !graph.vertex.contains(to)) {
				try {
					throw new Exception();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "û�и�վ�㣡","����",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			StringBuffer result = graph.output(from,to,graph.getMatirx(),lines);
			output.setText(result.toString());
		}
		}
	
}
