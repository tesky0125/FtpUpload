package yan.continueftp;

public enum DownloadStatus {
	Remote_File_Noexist, // Զ���ļ�������
	Local_Bigger_Remote, // �����ļ�����Զ���ļ�
	Download_From_Break_Success, // �ϵ������ļ��ɹ�
	Download_From_Break_Failed, // �ϵ������ļ�ʧ��
	Download_New_Success, // ȫ�������ļ��ɹ�
	Download_New_Failed; // ȫ�������ļ�ʧ��
}
