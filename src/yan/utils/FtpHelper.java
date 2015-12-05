package yan.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 使用org.apache.commons.net.ftp.FTPClient上传文件,FTP.BINARY_FILE_TYPE
 **/
public class FtpHelper {
	private FTPClient ftp = null;
	private String serverUrl;
	private String username;
	private String password;

	public FtpHelper(String serverUrl, String username, String password) {
		super();
		ftp = new FTPClient();
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
	}

	public boolean connectServer() {
		ftp.setControlEncoding("GBK");
		try {
			ftp.connect(serverUrl);
			if (ftp.login(username, password)) {
				int reply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftp.disconnect();
					ftp.logout();
					return false;
				} else {
					ftp.enterLocalPassiveMode();
					ftp.setFileType(FTP.BINARY_FILE_TYPE);
					return true;
				}
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param ftp
	 * @param serverDir
	 *            "ftpxiaoyan/"
	 * @return
	 */
	public boolean makeDirectory(String serverDir) {
		FTPFile[] files = null;
		try {
			files = ftp.listFiles(serverDir);
			if (files == null || files.length == 0) {
				ftp.makeDirectory(serverDir);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private FTPFile existsFile(String serverDir, File file) {
		FTPFile[] remoteFiles = null;
		try {
			remoteFiles = ftp.listFiles(serverDir);
			for (FTPFile remoteFile : remoteFiles) {
				if (file.getName().equals(remoteFile.getName())) {
					return remoteFile;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void uploadFile(String serverDir, String localFilename) {
		try {
			// ftp.initiateListParsing();
			File localFile = new File(localFilename);
			if (localFile.isFile()) {
				String name = localFile.getName();
				// 检查FTP目录
				makeDirectory(serverDir);

				FTPFile remoteFile = existsFile(serverDir, localFile);
				OutputStream os = ftp.storeFileStream(serverDir + name);

				if (os != null) {
					RandomAccessFile raf = new RandomAccessFile(localFile, "rw");
					// 如果远程文件存在,并且小于当前文件大小

					if (remoteFile != null
							&& raf.length() >= remoteFile.getSize()) {
						raf.seek(remoteFile.getSize());
					}

					byte buffer[] = new byte[4 * 1024];
					int len = 0;
					while ((len = raf.read(buffer)) != -1) {
						os.write(buffer, 0, len);
						os.flush();
					}
					os.close();
					raf.close();

					if (ftp.completePendingCommand()) {
						System.out.println("done!");
					} else {
						System.out.println("can't put file:" + name);
					}
				} else {
					System.out.println("can't put file:" + name);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFile(String serverFile, String localDir) {
		try {
			String remoteFileName = serverFile;
			if (serverFile.contains("/")) {
				remoteFileName = serverFile.substring(serverFile
						.lastIndexOf("/") + 1);
			}
			File localFile = new File(localDir + remoteFileName);
			if (!localFile.exists()) {
				localFile.createNewFile();
			} else {
				localFile.delete();
				localFile.createNewFile();
			}
			long pos = localFile.length();
			RandomAccessFile raf = new RandomAccessFile(localFile, "rw");
			raf.seek(pos);
			ftp.setRestartOffset(pos);

			InputStream is = ftp.retrieveFileStream(serverFile);
			if (is == null) {
				System.out.println("no such file:" + serverFile);
			} else {

				byte buffer[] = new byte[4 * 1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					raf.write(buffer, 0, len);
				}
				is.close();
				raf.close();
				if (ftp.completePendingCommand()) {
					System.out.println("done!");
				} else {
					System.out.println("can't get file:" + serverFile);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteFile(String serverDir, String serverFile) {
		try {
			ftp.deleteFile(serverDir + serverFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logout() {
		try {
			if (ftp != null && ftp.isConnected()) {
				ftp.logout();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
