package client;


import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;

import domin.Users;
import exception.NameException;
import exception.PassException;
import service.UsersService;


/**
*  ����ͻ��˵Ķ���д���Լ���¼�ͷ��͵ļ��� 
*  ֮���԰ѵ�¼�ͷ��͵ļ��������������ΪҪ����һЩ���ݣ�����mySocket,textArea
*/
public class ClientReadAndPrint extends Thread{
	static Socket mySocket = null;  // һ��Ҫ����static�������½��߳�ʱ�����
	static JTextField textInput;
	static JTextArea textShow;
	static JFrame chatViewJFrame;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static String userName;
	
	// ���ڽ��մӷ���˷���������Ϣ
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // ������
			while (true) {
				String str = in.readLine();  // ��ȡ����˷��͵���Ϣ
				textShow.append(str + '\n');  // ��ӽ�����ͻ��˵��ı�����
				textShow.setCaretPosition(textShow.getDocument().getLength());  // ���ù�������������
			}
		} catch (Exception e) {}
	}
	
	/**********************��¼����(�ڲ���)**********************/
	
     class LoginListen implements ActionListener{
		JTextField textField;
		JPasswordField pwdField;
		JFrame loginJFrame;  // ��¼���ڱ���
		
		
		public void setJTextField(JTextField textField) {
			this.textField = textField;
		}
		public void setJPasswordField(JPasswordField pwdField) {
			this.pwdField = pwdField;
		}
		public void setJFrame(JFrame jFrame) {
			this.loginJFrame = jFrame;
		}
		
		ClientView chatView = null;
		
		public void actionPerformed(ActionEvent event) {
			int action = Integer.parseInt(event.getActionCommand());
			
			if(action==1){
				userName = textField.getText();
				String userPwd = String.valueOf(pwdField.getPassword());  // getPassword�������char����
				
				UsersService user1 = new UsersService();
				Users user = null;
				try {
					user = user1.isLogin(userName, userPwd);
				} catch (NameException | PassException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				if(user != null) {  
					chatView = new ClientView(userName);  // �½����촰��,�������촰�ڵ��û�������̬��
					// �����ͷ���������ϵ
					try {
						InetAddress addr = InetAddress.getByName(null);  // ��ȡ������ַ
						mySocket = new Socket(addr,8081);  // �ͻ����׽���
						loginJFrame.setVisible(false);  // ���ص�¼����
						out = new PrintWriter(mySocket.getOutputStream());  // �����
						out.println("�û���" + userName + "�����������ң�");  // �����û�����������
						out.flush();  // ��ջ�����out�е�����
					} catch (IOException e) {
						e.printStackTrace();
					}
					// �½���ͨ��д�̲߳�����
					ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
					readAndPrint.start();
					// �½��ļ���д�̲߳�����
					ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
					fileThread.start();
				}
				else {
					JOptionPane.showMessageDialog(loginJFrame, "�˺Ż�����������������룡", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
				}		
			
		 	else{				
				loginJFrame.setVisible(false);  // ���ص�¼����
				new Register();
			}
			}
		}
	
    
     /**********************ע�����(�ڲ���)**********************/
     class RegisterListen implements ActionListener{
    	 JTextField textField;
 		JPasswordField pwdField;
 		JPasswordField pwdField2;
 		JFrame registerJFrame;  // ��¼���ڱ���
 		
 		
 		public void setJTextField(JTextField textField) {
 			this.textField = textField;
 		}
 		public void setJPasswordField(JPasswordField pwdField,JPasswordField pwdField2 ) {
 			this.pwdField = pwdField;
 			this.pwdField2 = pwdField2;
 		}
 		
 		public void setJFrame(JFrame jFrame) {
 			this.registerJFrame = jFrame;
 		}
 		ClientView chatView = null;
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			userName = textField.getText();
			String userPwd = String.valueOf(pwdField.getPassword());  // getPassword�������char����
			String userPwd2 = String.valueOf(pwdField2.getPassword());
			UsersService user1 = new UsersService();
			Users user = null;
			
			if(userPwd.equals(userPwd2)){
				try {
					user = user1.register(userName, userPwd);
					
				} catch (NameException | PassException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				JOptionPane.showMessageDialog(registerJFrame, "����������������룡", "��ʾ", JOptionPane.WARNING_MESSAGE);
			}
			
			if(user!=null) {  
				
				chatView = new ClientView(userName);  // �½����촰��,�������촰�ڵ��û�������̬��
				// �����ͷ���������ϵ
				try {
					InetAddress addr = InetAddress.getByName(null);  // ��ȡ������ַ
					mySocket = new Socket(addr,8081);  // �ͻ����׽���
					registerJFrame.setVisible(false);  // ����ע�ᴰ��
					out = new PrintWriter(mySocket.getOutputStream());  // �����
					out.println("�û���" + userName + "�����������ң�");  // �����û�����������
					out.flush();  // ��ջ�����out�е�����
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// �½���ͨ��д�̲߳�����
				ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
				readAndPrint.start();
				// �½��ļ���д�̲߳�����
				ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
				fileThread.start();
			}
			else {
				JOptionPane.showMessageDialog(registerJFrame, "ע��ʧ�ܣ����������룡", "��ʾ", JOptionPane.WARNING_MESSAGE);
			}
			}		
     
		}
    	 
     
	
	/**********************����������(�ڲ���)**********************/
	class ChatViewListen implements ActionListener{
		public void setJTextField(JTextField text) {
			textInput = text;  // �����ⲿ�࣬��Ϊ�����ط�ҲҪ�õ�
		}
		public void setJTextArea(JTextArea textArea) {
			textShow = textArea;  // �����ⲿ�࣬��Ϊ�����ط�ҲҪ�õ�
		}
		public void setChatViewJf(JFrame jFrame) {
			chatViewJFrame = jFrame;  // �����ⲿ�࣬��Ϊ�����ط�ҲҪ�õ�
			// ���ùر��������ļ���
			chatViewJFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					out.println("�û���" + userName + "���뿪�����ң�");
					out.flush();
					System.exit(0);
				}
			});
		}
		// ����ִ�к���
		public void actionPerformed(ActionEvent event) {
			try {
				String str = textInput.getText();
				// �ı�������Ϊ��
				if("".equals(str)) {
					textInput.grabFocus();  // ���ý��㣨���У�
					// ������Ϣ�Ի��򣨾�����Ϣ��
					JOptionPane.showMessageDialog(chatViewJFrame, "����Ϊ�գ����������룡", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				out.println(userName + "˵��" + str);  // ����������
				out.flush();  // ��ջ�����out�е�����
				
				textInput.setText("");  // ����ı���
				textInput.grabFocus();  // ���ý��㣨���У�
//				textInput.requestFocus(true);  // ���ý��㣨���У�
			} catch (Exception e) {}
		}
	}
}

