package service;

import java.sql.SQLException;

import DAO.UsersDAO;
import domin.Users;
import exception.NameException;
import exception.PassException;
import util.ConnUtil;

public class UsersService {
	//����¼��service ������DAO
		private UsersDAO userDAO = new UsersDAO();
		/**
		 * ��¼��֤����
		 * @param name �˺�
		 * @param pass ����
		 * @return ��¼�ɹ�ʱ�����û�,���ݿ�����쳣ʱ�����ؿ�ֵ
		 * @throws NameException 
		 */
			
		public Users isLogin(String name, String pass) throws NameException,PassException{
			try {
				//2 ��֤�˺�
				Users user = this.userDAO.selectByName(name);
				if(user == null){
					//�ֶ��׳��쳣
					throw new NameException("�˺Ų����ڣ�");
				}
						
				//3 ��֤����
				if(user.getUser_pass().equals(pass)){
					//��¼�ɹ�
					return user;
				}else{
					//��¼ʧ��
					throw new PassException("�������");
				}	
				
									
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
				//throw new RuntimeException(e);
			} finally{
				ConnUtil.closeConn();//һ��ҵ����ɣ��ر����ݿ�����
			}
			
			
		}
		
		public Users register(String name, String pass) throws NameException,PassException{
			try {
				//2 ��֤�˺�
				Users user = this.userDAO.insert(name,pass);
				
				if(user == null){
					//�ֶ��׳��쳣
					throw new NameException("ע��ʧ�ܣ�");
				}
						
				//3 ��֤����
				if(user.getUser_pass().equals(pass)){
					//ע��ɹ�
					return user;
				}else{
					//ע��ʧ��
					throw new PassException("ע��ʧ�ܣ�");
				}	
				
									
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
				//throw new RuntimeException(e);
			} finally{
				ConnUtil.closeConn();//һ��ҵ����ɣ��ر����ݿ�����
			}
			
			
		}


}
