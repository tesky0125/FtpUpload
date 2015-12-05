package yan.utils;

import java.io.File;
import java.io.IOException;

public class FtpDemo {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String localfile = "G:\\Downloads\\AdbeRdr90_zh_CN.exe";
//		File file = new File(localfile);
//		System.out.println(file.getName());
//		FtpUploadUtils.uploadFileBySun(localfile,
//				"127.0.0.1","ftpxiaoyan", "880125",
//				"/ftpxiaoyan/", "小燕ftp1.pdf");
//		FtpUploadUtils.uploadFileByApache(localfile,
//				"127.0.0.1","ftpxiaoyan", "880125",
//				"/ftpxiaoyan/", "小燕ftp2.pdf");
		//
		FtpHelper ftpHelper = new FtpHelper("202.114.171.38", "ftpxiaoyan", "880125");
		if(ftpHelper.connectServer()){
			ftpHelper.uploadFile("/ftpxiaoyan/", localfile);
//			ftpHelper.downloadFile("/ftpxiaoyan/使用说明书.pdf", "G:\\Downloads\\");
		}
	}

}
