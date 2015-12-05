package yan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import sun.net.ftp.FtpClient;

public class FtpUploadUtils {
	/**
	 * ʹ��sun.net.ftp.FtpClient�ϴ��ļ�
	 * 
	 * @param localfile
	 * @param server
	 * @param username
	 * @param password
	 * @param remotedir
	 * @param remotename
	 * @return
	 */
	public static boolean uploadFileBySun(String localfile, String server,
			String username, String password, String remotedir,
			String remotename) {
		FtpClient ftpClient = new FtpClient();
		try {
			// 1.��FTP������
			ftpClient.openServer(server);
			// 2.ʹ��ָ���û���¼
			ftpClient.login(username, password);
			// 3.��ת��������ָ��·��
			ftpClient.cd(remotedir);
			ftpClient.binary();
			
			InputStream is = new FileInputStream(new File(localfile));
			OutputStream os = ftpClient.put(remotename);
			byte buffer[] = new byte[4 * 1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				os.flush();
			}
			os.close();
			is.close();

		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			try {
				ftpClient.closeServer();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		return true;
	}

	/**
	 * ʹ��org.apache.commons.net.ftp.FTPClient�ϴ��ļ�
	 * 
	 * @param localfile
	 * @param server
	 * @param username
	 * @param password
	 * @param remotedir
	 * @param remotefile
	 * @return
	 */
	public static boolean uploadFileByApache(String localfile, String server,
			String username, String password, String remotedir,
			String remotefile) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server);
			ftpClient.login(username, password);
			ftpClient.changeWorkingDirectory(remotedir);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			InputStream is = new FileInputStream(new File(localfile));
			// ftpClient.storeFile(new
			// String(remotename.getBytes("GBK"),"ISO-8859-1"), is);
			OutputStream os = ftpClient.storeFileStream(new String(remotefile
					.getBytes("GBK"), "ISO-8859-1"));
			byte buffer[] = new byte[4 * 1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				os.flush();
			}
			os.close();
			is.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		return true;
	}



}
