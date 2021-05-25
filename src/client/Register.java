package client;

import java.awt.*;
import javax.swing.*;


public class Register {

	JTextField textField = null;
	JPasswordField pwdField = null;
	JPasswordField pwdField2 = null;
	ClientReadAndPrint.RegisterListen listener=null;

	
	// ���캯��
	public Register() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("ע��"); //��¼���� ��
		jf.setBounds(500,100,300,300);//setBounds(int x, int y, int width, int height)
		jf.setResizable(true);  // �����Ƿ�����
		
		JPanel jp1 = new JPanel();
		JLabel headJLabel = new JLabel("ע�����");
		headJLabel.setFont(new Font(null, 0, 35));  // �����ı����������͡���ʽ �� ��С
		jp1.add(headJLabel); 
		
		JPanel jp2 = new JPanel();
		JLabel nameJLabel = new JLabel("�û�����");
		textField = new JTextField(20);
		JLabel pwdJLabel = new JLabel("���룺    ");
		pwdField = new JPasswordField(20);
		JLabel pwdJLabel2 = new JLabel("�ظ����룺    ");
		pwdField2 = new JPasswordField(20);
		
		JButton registerButton = new JButton("ע��"); 
		//  //����ť��ӵ�JPanel
		jp2.add(nameJLabel);
		jp2.add(textField);
		jp2.add(pwdJLabel);
		jp2.add(pwdField);
		jp2.add(pwdJLabel2);
		jp2.add(pwdField2);
		jp2.add(registerButton);
		
		JPanel jp = new JPanel(new BorderLayout());  // BorderLayout����
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(jp2, BorderLayout.CENTER);
		
		
		// ���ü��
		listener = new ClientReadAndPrint().new RegisterListen();  // �½�������
		listener.setJTextField(textField);  // ����PoliceListen��ķ���
		listener.setJPasswordField(pwdField,pwdField2);
		listener.setJFrame(jf);
		pwdField.addActionListener(listener);  // �������Ӽ���
		pwdField2.addActionListener(listener);  // �������Ӽ���
		registerButton.addActionListener(listener);  // ��ť��Ӽ���
		
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ���ùر�ͼ������
		jf.setVisible(true);  // ���ÿɼ�
	}
}

