
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class ControllerCOM {


	private static Properties properties = null;

	// ��ȡ�����ļ�
	static {
		properties = new Properties();
		try (InputStream is = ControllerCOM.class.getClassLoader().getResourceAsStream("dc.properties");
				BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));) {
			properties.load(bf);
		} catch (IOException e) {
		}
	}

	/**
	 * ������բ����
	 */
	public static String ChangeCOM(String msg) {
		PortChangeUnit portChangeUnit = new PortChangeUnit();
		String portName = null;
		String result = "0";
		String system = System.getProperty("os.name");
		System.out.println("��������os.name:" + system);
		// �жϵ�ǰ����ϵͳΪWindow��Linux
		if (system.toLowerCase().startsWith("win")) {
			// window��ִ���л�
			portName = properties.getProperty("com.port");
		} else {
			// LINUX���л�
			portName = properties.getProperty("tty.port");
		}
		try {
			// ��ʼ���˿�
			portChangeUnit.init(portName);
			Thread.sleep(50);
			// ��������
			portChangeUnit.sendMsg(msg);
			Thread.sleep(50);
			// ��ȡ����ֵ
			byte[] byteResult = portChangeUnit.getMsg();
			if (byteResult != null) {
				result = new String(byteResult);
			}
			Thread.sleep(50);
			// �رն˿�
			portChangeUnit.closeSerialPort();
		} catch (InterruptedException e) {
//			LOG.error("�л������г���" + e.getMessage(), e);
		}
		return result;
	}
}
