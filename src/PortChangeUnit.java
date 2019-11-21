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
	// ���ϵͳ�п��õ�ͨѶ�˿���
		private CommPortIdentifier portId;
		private Enumeration<CommPortIdentifier> portList;
		// RS-232�Ĵ��п�
		private SerialPort serialPort;
		private CommPort commPort;

		// ��ʼ������
		@SuppressWarnings("unchecked")
		public void init(String portName) {
			portList = CommPortIdentifier.getPortIdentifiers();
			System.out.println("1-portList��"+portList);
			while (portList.hasMoreElements()) {
				System.out.println("2-portList.hasMoreElements()��true");
				portId = (CommPortIdentifier) portList.nextElement();
				System.out.println("3-portId:" + portId);
				System.out.println("portId.getPortType():"+portId.getPortType() + ">>>><<<<<<CommPortIdentifier.PORT_SERIAL:"+CommPortIdentifier.PORT_SERIAL);
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					System.out.println("portId.getName():"+portId.getName());
					if (portId.getName().equals(portName)) {
						try {
							//�򿪶˿ڣ������˿����ֺ�һ��timeout���򿪲����ĳ�ʱʱ�䣩
							commPort = portId.open(this.getClass().getName(), 2000);
							System.out.println("�򿪶˿ڳɹ���"+commPort);
							serialPort = (SerialPort) commPort;
							System.out.println("�򿪶˿ڳɹ�serialPort��"+serialPort);
							// ���ô���ͨѶ����
							serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
						} catch (PortInUseException e) {					
//							 ("���ڱ�ռ�ã�" + e.getMessage(), e);
							 closeSerialPort();
						} catch (UnsupportedCommOperationException e) {
//							 System.out.println("���ô��ڲ���ʧ�ܣ�" + e.getMessage(), e);
							closeSerialPort();
						}
					}
				}
			}
		}

		/**
		 * ��������
		 * 
		 * @param msg
		 * @throws IOException
		 */
		public void sendMsg(String msg) {
			OutputStream outputStream = null;
			try {
				if (serialPort != null) {
					outputStream = serialPort.getOutputStream();// ��ô��ڵ������
					outputStream.write(msg.getBytes());
					outputStream.flush();
				} else {
					 System.out.println("����Ϊ�գ�������բ�豸�Ƿ�����");
				}
			} catch (IOException e) {
				 System.out.println("д�봮��ָ�����" + e.getMessage());
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						 System.out.println("�ر�������ʧ�ܣ�" + e.getMessage());
					}
				}
			}
		}

		/**
		 * ��������
		 * 
		 * @return �������ڵ�ǰ״̬ 1,2,N,E
		 */
		public byte[] getMsg() {
			InputStream inputStream = null;
			byte[] bytes = null;
			try {
				if (serialPort != null) {
					inputStream = serialPort.getInputStream();// ��ô��ڵ�������
					int bufflenth = inputStream.available();// ������ݳ���
					while (bufflenth != 0) {
						bytes = new byte[bufflenth];// ��ʼ��byte����
						inputStream.read(bytes);
						bufflenth = inputStream.available();
					}
				}else {
					 System.out.println("����Ϊ�գ�������բ�豸�Ƿ�����");
				}
			} catch (IOException e) {
				 System.out.println("��ȡ���ڷ������ݴ���" + e.getMessage());
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
						inputStream = null;
					}
				} catch (IOException e) {
					 System.out.println("�ر������ʧ�ܣ�" + e.getMessage());
				}
			}
			return bytes;
		}

		/**
		 * �رմ���
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
