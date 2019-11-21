
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class ControllerCOM {


	private static Properties properties = null;

	// 读取配置文件
	static {
		properties = new Properties();
		try (InputStream is = ControllerCOM.class.getClassLoader().getResourceAsStream("dc.properties");
				BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));) {
			properties.load(bf);
		} catch (IOException e) {
		}
	}

	/**
	 * 调用网闸开关
	 */
	public static String ChangeCOM(String msg) {
		PortChangeUnit portChangeUnit = new PortChangeUnit();
		String portName = null;
		String result = "0";
		String system = System.getProperty("os.name");
		System.out.println("—》》》os.name:" + system);
		// 判断当前操作系统为Window或Linux
		if (system.toLowerCase().startsWith("win")) {
			// window下执行切换
			portName = properties.getProperty("com.port");
		} else {
			// LINUX下切换
			portName = properties.getProperty("tty.port");
		}
		try {
			// 初始化端口
			portChangeUnit.init(portName);
			Thread.sleep(50);
			// 发送请求
			portChangeUnit.sendMsg(msg);
			Thread.sleep(50);
			// 获取返回值
			byte[] byteResult = portChangeUnit.getMsg();
			if (byteResult != null) {
				result = new String(byteResult);
			}
			Thread.sleep(50);
			// 关闭端口
			portChangeUnit.closeSerialPort();
		} catch (InterruptedException e) {
//			LOG.error("切换过程中出错" + e.getMessage(), e);
		}
		return result;
	}
}
