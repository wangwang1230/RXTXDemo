import java.util.Scanner;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

public class TestRXTX {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) {
			System.out.println("������һ������:0/1-���Կ��أ�8-�Զ�����,66-�˳���");
			Scanner in = new Scanner(System.in);
			int a = in.nextInt();// ����һ������
			if (a == 0) {
				NetSwitchUtil.openPrivateNet();
			} else if (a == 1) {
				NetSwitchUtil.openUnitNet();
			} else if(a==3) {
				try {
					CommPortIdentifier linuxPort = CommPortIdentifier.getPortIdentifier("ttyACM0");
					if(linuxPort!=null) {
						System.out.println("�˿����ͣ�"+linuxPort.getPortType()+"|||���֣�"+linuxPort.getName());
					}
				} catch (NoSuchPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (a == 8) {
				System.out.println("qingshuruzidongzhixingdecishu:");
				Scanner sc = new Scanner(System.in);
				int c = sc.nextInt();// ����һ������
				try {
					Thread.sleep(5000L);
					for (int i = 0; i < c; i++) {
						if (i % 2 == 0) {
							NetSwitchUtil.openPrivateNet();
							System.out.println(i);
							Thread.sleep(3000L);
						} else {
							NetSwitchUtil.openUnitNet();
							System.out.println(i);
							Thread.sleep(3000L);
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (a == 66) {
				break;
			}
		}
	}

}
