
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetSwitchUtil {
	
	public static boolean openPrivateNet() {
		String msg = "2";
			String result = ControllerCOM.ChangeCOM(msg);
			if(!"0".equals(result)){
				Constants.isUnitNet = true;
			}else {
				return false;
			}
		return true;
	}
	
	public static boolean openUnitNet() {
		String msg = "1";
			String result = ControllerCOM.ChangeCOM(msg);
			if(!"0".equals(result)){
				Constants.isUnitNet = true;
			}else {
				return false;
			}
		return true;
	}
	
	/**
	 * �����������״̬
	 */
	public static boolean isConnect() {
		boolean connect = false;
		Runtime runtime = Runtime.getRuntime();
		Process process;
		String ip1 = "";
		String system = System.getProperty("os.name");
		try {
			// �жϵ�ǰ����ϵͳΪWindow��Linux
			// ÿ��ִֻ��һ��ping����������˷�ʱ��
			if (system.toLowerCase().startsWith("win")) {
				// window��ִ��һ��ping
				process = runtime.exec("ping -n 1 " + ip1);
			} else {
				// LINUX��ִ��һ��ping
				process = runtime.exec("ping -c 1 " + ip1);
			}
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			isr.close();
			br.close();

			if (null != sb && !sb.toString().equals("")) {
				if (system.toLowerCase().startsWith("win")) {
					if (sb.toString().indexOf("TTL=") > 0) {
						// ���糩ͨ
						connect = true;
					} else {
						// ���粻��ͨ
						connect = false;
					}
				} else {
					if (sb.toString().indexOf("ttl=") > 0) {
						// ���糩ͨ
						connect = true;
					} else {
						// ���粻��ͨ
						connect = false;
					}
				}
			}
		} catch (IOException e) {
//			LOG.error("���ӱ���Ƶ�λ����ʧ�ܣ�{}", e.getMessage(), e);
		}
		return connect;
	}
	
	public static boolean testNetIsOk(){
		boolean result = false;
		for (int i = 5; i > 0; i--) {
			result = isConnect();
			if (result) {
				break;				
			}
		}
		return result;
	}
}