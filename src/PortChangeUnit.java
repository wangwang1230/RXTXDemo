import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class PortChangeUnit {
	// 检测系统中可用的通讯端口类
		private CommPortIdentifier portId;
		private Enumeration<CommPortIdentifier> portList;
		// RS-232的串行口
		private SerialPort serialPort;
		private CommPort commPort;

		// 初始化串口
		@SuppressWarnings("unchecked")
		public void init(String portName) {
			portList = CommPortIdentifier.getPortIdentifiers();
			System.out.println("1-portList："+portList);
			while (portList.hasMoreElements()) {
				System.out.println("2-portList.hasMoreElements()：true");
				portId = (CommPortIdentifier) portList.nextElement();
				System.out.println("3-portId:" + portId);
				System.out.println("portId.getPortType():"+portId.getPortType() + ">>>><<<<<<CommPortIdentifier.PORT_SERIAL:"+CommPortIdentifier.PORT_SERIAL);
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					System.out.println("portId.getName():"+portId.getName());
					if (portId.getName().equals(portName)) {
						try {
							//打开端口，并给端口名字和一个timeout（打开操作的超时时间）
							commPort = portId.open(this.getClass().getName(), 2000);
							System.out.println("打开端口成功："+commPort);
							serialPort = (SerialPort) commPort;
							System.out.println("打开端口成功serialPort："+serialPort);
							// 设置串口通讯参数
							serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
						} catch (PortInUseException e) {					
//							 ("串口被占用：" + e.getMessage(), e);
							 closeSerialPort();
						} catch (UnsupportedCommOperationException e) {
//							 System.out.println("设置串口参数失败：" + e.getMessage(), e);
							closeSerialPort();
						}
					}
				}
			}
		}

		/**
		 * 发送请求
		 * 
		 * @param msg
		 * @throws IOException
		 */
		public void sendMsg(String msg) {
			OutputStream outputStream = null;
			try {
				if (serialPort != null) {
					outputStream = serialPort.getOutputStream();// 获得串口的输出流
					outputStream.write(msg.getBytes());
					outputStream.flush();
				} else {
					 System.out.println("串口为空，请检查网闸设备是否连接");
				}
			} catch (IOException e) {
				 System.out.println("写入串口指令错误：" + e.getMessage());
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						 System.out.println("关闭输入流失败：" + e.getMessage());
					}
				}
			}
		}

		/**
		 * 接收请求
		 * 
		 * @return 返回网口当前状态 1,2,N,E
		 */
		public byte[] getMsg() {
			InputStream inputStream = null;
			byte[] bytes = null;
			try {
				if (serialPort != null) {
					inputStream = serialPort.getInputStream();// 获得串口的输入流
					int bufflenth = inputStream.available();// 获得数据长度
					while (bufflenth != 0) {
						bytes = new byte[bufflenth];// 初始化byte数组
						inputStream.read(bytes);
						bufflenth = inputStream.available();
					}
				}else {
					 System.out.println("串口为空，请检查网闸设备是否连接");
				}
			} catch (IOException e) {
				 System.out.println("读取串口返回数据错误：" + e.getMessage());
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
						inputStream = null;
					}
				} catch (IOException e) {
					 System.out.println("关闭输出流失败：" + e.getMessage());
				}
			}
			return bytes;
		}

		/**
		 * 关闭串口
		 */
		public void closeSerialPort() {
			if (serialPort != null) {
				serialPort.close();
				serialPort = null;
			}
			if(commPort!=null){
				commPort.close();
				commPort = null;
			}	
		}
}
