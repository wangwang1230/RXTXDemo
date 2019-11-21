import java.util.Scanner;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

public class TestRXTX {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) {
			System.out.println("请输入一个整数:0/1-调试开关，8-自动调试,66-退出！");
			Scanner in = new Scanner(System.in);
			int a = in.nextInt();// 输入一个整数
			if (a == 0) {
				NetSwitchUtil.openPrivateNet();
			} else if (a == 1) {
				NetSwitchUtil.openUnitNet();
			} else if(a==3) {
				try {
					CommPortIdentifier linuxPort = CommPortIdentifier.getPortIdentifier("ttyACM0");
					if(linuxPort!=null) {
						System.out.println("端口类型："+linuxPort.getPortType()+"|||名字："+linuxPort.getName());
					}
				} catch (NoSuchPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (a == 8) {
				System.out.println("qingshuruzidongzhixingdecishu:");
				Scanner sc = new Scanner(System.in);
				int c = sc.nextInt();// 输入一个整数
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
