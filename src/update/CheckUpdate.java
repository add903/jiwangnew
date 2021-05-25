package update;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
 
public class CheckUpdate extends JFrame {
    JFrame c = this;
 
    public CheckUpdate() {
        //���ô�������
        setAttb();
 
        JLabel title = new JLabel("���ڼ�������ϵĸ�����Դ");
        this.add(title, BorderLayout.NORTH);
        JTextArea msg = new JTextArea();
        this.add(msg, BorderLayout.CENTER);
        JLabel process = new JLabel();
        this.add(process, BorderLayout.SOUTH);
        
        //���������߳�
        new Check(msg, process).start();
    }
 
    private class Check extends Thread {
        //��ʶ,�Ƿ�����µĸ����ļ�
        private boolean isUpdated = false;
        //�������µİ汾
        String netVersion;
        //���ذ汾�ļ���
        String LocalVerFileName = "ver.txt";
 
        //��ʾ��Ϣ
        private JTextArea msg;
        private JLabel process;
 
        public Check(JTextArea msg, JLabel process) {
            this.msg = msg;
            this.process = process;
        }
 
        public void run() {
            //�����ļ��汾��ʶURL
            String versionUrl = "http://XXX.XXX.XXX/AutoUpdate/ver";
 
            /**//*
			������ͨ��HTTP����һ��ҳ��,��ȡ�������ϵİ汾��
			����������������ҳ��ֱ�Ӵ�ӡ�� 6.19.1.1
			Ȼ�������汾�űȶԱ��صİ汾��,����汾�Ų�ͬ�Ļ�,�ʹ������������µĳ��򲢸������г���
            */
 
            URL url = null;
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader netVer = null;
 
            //��ȡ�����ϵİ汾��
            try {
                url = new URL(versionUrl);
                is = url.openStream();
                isr = new InputStreamReader(is);
 
                netVer = new BufferedReader(isr);
                String netVerStr = netVer.readLine();
                String localVerStr = getNowVer();
 
                if (netVerStr.equals(localVerStr)) {
                    msg.append("��ǰ�ļ������°汾\n");
                    isUpdated = false;
                } else {
                    msg.append("���ڸ����ļ�,���ڿ�ʼ����\n");
                    isUpdated = true;
                    netVersion = netVerStr;
                }
 
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {
            } finally {
                //�ͷ���Դ
                try {
                    netVer.close();
                    isr.close();
                    is.close();
                } catch (IOException ex1) {
                }
            }
 
            //����汾��ͬ,���������ϵ��ļ�,���±����ļ�
            if (isUpdated) {
                //������Ҫ�����µ��ļ�
                File oldFile = new File("client.exe");
                //�������������ص��ļ�
                File newFile = new File("temp.exe");
                
                //�����ϵ��ļ�λ��
                String updateUrl =
                        "http://XXX.XXX.XXX/downloads/simpkle.exe";
 
                HttpURLConnection httpUrl = null;
                BufferedInputStream bis = null;
                FileOutputStream fos = null;
 
                try {
                    //��URLͨ��
                    url = new URL(updateUrl);
                    httpUrl = (HttpURLConnection) url.openConnection();
 
                    httpUrl.connect();
 
                    byte[] buffer = new byte[1024];
 
                    int size = 0;
 
                    is = httpUrl.getInputStream();
                    bis = new BufferedInputStream(is);
                    fos = new FileOutputStream(newFile);
 
                    msg.append("���ڴ������������µĸ����ļ�\n");
 
                    //�����ļ�
                    try {
                        int flag = 0;
                        int flag2 = 0;
                        while ((size = bis.read(buffer)) != -1) {
                            //��ȡ��ˢ����ʱ�����ļ�
                            fos.write(buffer, 0, size);
                            fos.flush();
 
                            //ģ��һ���򵥵Ľ�����
                            if (flag2 == 99) {
                                flag2 = 0;
                                process.setText(process.getText() + ".");
                            }
                            flag2++;
                            flag++;
                            if (flag > 99 * 50) {
                                flag = 0;
                                process.setText("");
                            }
                        }
                    } catch (Exception ex4) {
                        System.out.println(ex4.getMessage());
                    }
 
                    msg.append("\n�ļ��������\n");
 
                    //�����ص���ʱ�ļ��滻ԭ���ļ�
                    CopyFile(oldFile,newFile);
                    
                    //�ѱ��ذ汾�ļ�����Ϊ����ͬ��
                    UpdateLocalVerFile();
 
                } catch (MalformedURLException ex2) {
                } catch (IOException ex) {
                    msg.append("�ļ���ȡ����\n");
                } finally {
                    try {
                        fos.close();
                        bis.close();
                        is.close();
                        httpUrl.disconnect();
                    } catch (IOException ex3) {
                    }
                }
            }
 
            //����Ӧ�ó���
            try {
                msg.append("����Ӧ�ó���");
                Thread.sleep(500);
                Process p = Runtime.getRuntime().exec("client.exe");
            } catch (IOException ex5) {
            } catch (InterruptedException ex) {
            }
            
            //�˳����³���
            System.exit(0);
        }
//�����ļ�
        private void CopyFile(File oldFile, File newFile) {
            FileInputStream in = null;
            FileOutputStream out = null;
            
            try {
                if(oldFile.exists()){
                    oldFile.delete();
                }
                in = new FileInputStream(newFile);
                out = new FileOutputStream(oldFile);
 
                byte[] buffer = new byte[1024 * 5];
                int size;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                    out.flush();
                }
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } finally {
                try {
                    out.close();
                    in.close();
                } catch (IOException ex1) {
                }
            }
 
        }
 
        private void UpdateLocalVerFile() {
            //�ѱ��ذ汾�ļ�����Ϊ����ͬ��
            FileWriter verOS = null;
            BufferedWriter bw = null;
            try {
                verOS = new FileWriter(LocalVerFileName);
 
                bw = new BufferedWriter(verOS);
                bw.write(netVersion);
                bw.flush();
 
            } catch (IOException ex) {
            } finally {
                try {
                    bw.close();
                    verOS.close();
                } catch (IOException ex1) {
                }
            }
        }
 
        private String getNowVer() {
            //���ذ汾�ļ�
            File verFile = new File(LocalVerFileName);
 
            FileReader is = null;
            BufferedReader br = null;
 
            //��ȡ���ذ汾
            try {
                is = new FileReader(verFile);
 
                br = new BufferedReader(is);
                String ver = br.readLine();
 
                return ver;
            } catch (FileNotFoundException ex) {
                msg.append("���ذ汾�ļ�δ�ҵ�\n");
            } catch (IOException ex) {
                msg.append("���ذ汾�ļ���ȡ����\n");
            } finally {
                //�ͷ���Դ
                try {
                    br.close();
                    is.close();
                } catch (IOException ex1) {
                }
            }
            return "";
        }
    }
 
 
    private void setAttb() {
        //��������
        this.setTitle("Auto Update");
        this.setSize(200, 150);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
 
        // �������
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2,
                         (screenSize.height - frameSize.height) / 2);
    }
 
    public static void main(String[] args) {
        CheckUpdate checkupdate = new CheckUpdate();
        checkupdate.setVisible(true);
    }
}