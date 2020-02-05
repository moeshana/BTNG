package edu.mccc.cos210.ds.fp.bugattitng;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import edu.mccc.cos210.ds.fp.bugattitng.Map;


public class Network implements Runnable {
	private String server_address = "localhost";
	private InetAddress inet_address;
	private int server_port = 0;
	private boolean isServer = false;
//	private JPanel jp;

	private CarInfoInNet myCarInfo; 
	private int max = 1;
	private SocketAddress[] clientList;
	private DatagramSocket sock;
	private int networkProcess = -1;
//	public Car[] getCars = new Car[max];
	private String tmpMessage;
	private int test = 0;
	
	private int pos = 0;
	private Map maps;
	private MyCar myCar;
	private String BUG = "";
	private int initPos;
	private String[] CarInfo;
	private String BacktoClient;
	
	public String getBacktoClient() {
		return this.BacktoClient;
	}
	
	public int getInitPos() {
		return this.initPos;
	}
	public void setMyCar(MyCar a) {
		this.myCar = a;
	}
	public void setBug(String bug) {
		this.BUG = bug;
	}
	public String getBug() {
		return this.BUG;
	}
//	public static void main(String... args) {
//		Network a = new Network("192.168.1.141",8888,true,4);
//		Network a = new Network("10.10.14.143",8888,false,4);
//	}
	public CarInfoInNet getCarInfo() {
		return this.myCarInfo;
	}
	public MyCar getMyCar() {
		return this.myCar;
	}
	public Map getMap() {
		return this.maps;
	}
	public int getProcess () {
		return this.networkProcess;
	}
	public Network (String ip, int port, boolean doServer,int max) { //,Car myCar) {
		this.server_address = ip;
		this.server_port = port;
		this.isServer = doServer;	
		this.max = max;
//		this.jp = jp;
		clientList = new SocketAddress[max];
		CarInfo = new String[max + 1];
		for (int i = 0; i < max; i++) {
			clientList[i] = null;
		}
		for (int n = 0; n < max + 1; n++) {
			CarInfo[n] = null;
		}
		

		
	}

	private void startNetwork() {
		if (isServer) {
			startServer();
		} else {
			startClient();
		}
	}

	private void startServer() {
		byte[] buffer = new byte[1024];
		DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
		byte[] bufferReply = new byte[1024];
		DatagramPacket pktReply = new DatagramPacket(bufferReply, bufferReply.length);
		try {
			sock = new DatagramSocket(this.server_port);
			System.out.println("Now server is running on " + this.server_address + " : " + this.server_port);
			while (true) {
//				System.out.println("Lisening....");
				sock.receive(pkt);
				String reply = checkMessage(buffer,pkt);
				bufferReply = reply.getBytes();
				pktReply = new DatagramPacket(bufferReply, bufferReply.length,pkt.getSocketAddress());
				sock.send(pktReply);


				pkt.setLength(buffer.length);
				pktReply.setLength(bufferReply.length);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			sock.close();
		}
	}

	private void getInterAddress() {
		
	}
	
	private String checkMessage(byte[] buffer, DatagramPacket pkt) {
		String message = new String(buffer, 0, pkt.getLength());
		String[] sa = message.split(":");

		int tmp = Integer.valueOf(sa[0]);
		String reply = null;
		switch (tmp) {
			case 0:
				System.out.println("in0");
				if (this.networkProcess < 50) {
					for (int i = 0; i < clientList.length; i++) {
						if (clientList[i] == null) {
							clientList[i] = pkt.getSocketAddress();
							reply = "15:00";
							this.networkProcess = 39;
							if (i == clientList.length - 1) {
								this.networkProcess = 59;  // loop for find out the map.
							}
							return reply;
						}
						if (clientList[i].equals(pkt.getSocketAddress())) {
							reply = "15:00";
							return reply;
						}
					}	
				}
				reply = "11:00";
				break;
			case 11:   // connect failed...
				System.out.println("in11");
				reply ="11:00";
				this.networkProcess = 11;
				//在timer 里面加上一个 如果这个数值等于11， 打开某个开关，然后在paint中画一个Connect failed.
				break;
			case 15:
				System.out.println("in15");
				//map request
				reply = "50:00";
				this.networkProcess = 50;
				break;
			case 50:
				System.out.println("in50");
				reply = "51:00"; // add map information
				//send map
				this.networkProcess = 51;
				break;
			case 51:
				System.out.println("in51");
				reply = "60:00:" + BUG; //发送已选车辆信息
//				for (int i = 0; i < clientList.length; i++) {
//					System.out.println(clientList[i]);
//				}
				maps = new Map(false);
				this.networkProcess = 55;
				//receive map
				break;
			case 60:
				System.out.println("in60");
				//然后初始化车辆信息
//				CarInfoInNet a = new CarInfoInNet(pos,"M",0,0,BUG,0,0);
				reply = "61:00:" + this.pos; //61
//				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + sa[1]);
				this.networkProcess = 61;
				this.pos += 1;
				break;

		
			case 61:
				System.out.println("in61");
				this.networkProcess = 80;
				this.initPos = Integer.valueOf(sa[2]);		
				
				while (myCar == null) {
//					System.out.println("haha");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
//				System.out.println(myCar.getID());
//				System.out.println(myCar.getX());
//				System.out.println(myCar.getCurrentSpeed());
//				System.out.println(myCar.getBugName());				
//				System.out.println(myCar.getHeadingInRadius());
			
				String d = myCar.getID() + "++" + myCar.getX() +  "++" + myCar.getY() + "++" +  10 + "++"
							+ myCar.getBugName() + "++" +myCar.getHeadingInRadius() ; 				
				reply = "80:00:" + d;
				System.out.println("d : " + d + "==================================");
				break;
				
			case 80:
				
				System.out.println("in80");
				for (int n = 0; n < max; n++) {
					if (CarInfo[n] != null) {
						String[] testString = sa[2].split("\\+\\+");
						String[] oldString = CarInfo[n].split("\\+\\+");
						if (testString[0].equals(oldString[0])) {
							CarInfo[n] = sa[2];
						} 
					} else {
						CarInfo[n] = sa[2];
					}
				}
				while (myCar == null) {
//					System.out.println("haha");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				String mine = myCar.getID() + "++" + myCar.getX() +  "++" + myCar.getY() + "++" +  10 + "++"
						+ myCar.getBugName() + "++" +myCar.getHeadingInRadius() ;
				String toClient = "";
				for (int m = 0; m < max; m++) {
					toClient += CarInfo[m] + "=";
				}
				toClient = toClient + mine;
//				toClient = toClient.substring(0, toClient.length()-1);
				reply = "81:00:" + toClient;
				this.networkProcess = 80;
				break;
			case 81:
				System.out.println("in81");
				if (this.networkProcess == 80) {
					this.BacktoClient = sa[2];
					String e = myCar.getID() + "++" + myCar.getX() +  "++" + myCar.getY() + "++" +  myCar.getCurrentSpeed() 
							+ "++"	+ myCar.getBugName() + "++" +myCar.getHeadingInRadius() ; 				
					reply = "80:00:" + e;					
					System.out.println("e : " + e + "==================================");
					if (this.test>= 10) {
						reply = "90:00";
						this.networkProcess = 90;
					}
				}
				break;
			case 90:
				System.out.println("in90");
				reply = "91:00";
				this.networkProcess = 91;
				break;
			case 91:
				System.out.println("in91");
				reply = "92:00";
				this.networkProcess = 92;
				break;
			case 92:
				System.out.println("in92");
				reply = "93:00";
				this.networkProcess = 93;
				break;
			case 93:
				reply = "94:00";
				this.networkProcess = 94;
				break;
			case 94:
				reply = "94:00";
				break;
		}
		return reply;
	}


		
		
	private void startClient() {
		try {
			inet_address = InetAddress.getByName(server_address);
			sendToinClient("00:00");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void sendToinClient(String message) {
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket pkt =  new DatagramPacket(buffer, buffer.length);
			byte[] buffer2 = new byte[1024];
			DatagramPacket pkt2 = new DatagramPacket(buffer2, buffer2.length);
			DatagramSocket sock = new DatagramSocket();
			buffer = message.getBytes();
			pkt = new DatagramPacket(buffer, buffer.length,this.inet_address,this.server_port);
			while (!message.equals("11:00")) {
				
				sock.send(pkt);
				sock.receive(pkt2);

				buffer = checkMessage(buffer2,pkt2).getBytes();
//				System.out.println(buffer.length);
				
				pkt = new DatagramPacket(buffer, buffer.length,this.inet_address,this.server_port);
				message = new String(buffer, 0, pkt.getLength());
			}
			sock.close();
			System.out.println("End");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		startNetwork();
	}
}
