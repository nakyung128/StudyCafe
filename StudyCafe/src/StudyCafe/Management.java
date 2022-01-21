package StudyCafe;
import java.util.*;
import java.io.*;


public class Management implements java.io.Serializable {

	ArrayList<Room> roomArray = new ArrayList<Room>(); // �� ��ü ����Ʈ ����
	Room room = new Room(); // �� ��ü ����
	int roomCount; // roomArray�� ũ��
	private int income[][] = new int[12][31]; // �� �� ������ ������ ��� 2���� �迭.
	private long totalIncome;
	private String managerID;
	
	Management(String ManagerID) {
		this.managerID = ManagerID;
	}
	
	// income �����ϱ�
	void writeIncome(ObjectOutputStream out) throws IOException {
		out.writeObject(income);
	}
	//  roomArray �����ϱ�
	void writeFile(ObjectOutputStream out) throws Exception {
		// �� ����
		out.writeObject(roomArray);
	}
	
	// income �ҷ�����
	void readIncome(ObjectInputStream in) throws Exception {
		income = (int[][]) in.readObject();
	}

	// roomArray �ҷ�����
	@SuppressWarnings("unchecked")
	void readFile(ObjectInputStream in) throws Exception {
		roomArray = (ArrayList<Room>) in.readObject();
	}

	// manager ���̵� ���� �޼ҵ�
	void setManagerID(String ID) {
		this.managerID = ID;
	}
	
	// manager ���̵� ��ġ Ȯ�� �޼ҵ�
	boolean correctManager(String managerID) {
		return (this.managerID.equals(managerID));
	}
	
	// ������ ��� ���̺��� ����� ���� �Լ�
	Object[][] allRoomTable() {
		int roomCount = roomArray.size();
		Object roomData[][] = new Object[1000][1000];
		
		if (roomCount > 0) {
			for (int i = 0; i < roomCount; i++) {
					roomData[i][0] = roomArray.get(i).getRoomNum();
					roomData[i][1] = roomArray.get(i).getSizeOfRoom();
					roomData[i][2] = roomArray.get(i).getCharge();
					if (roomArray.get(i).isEmpty()) {
						roomData[i][3] = "X";
					} else {
						roomData[i][3] = "O";
					}
			}
		}
		return roomData;
	}
	
	// üũ�� ���̺� ����� ���� �Լ�
	Object[][] checkInTable() {
		int roomCount = roomArray.size();
		Object roomData[][] = new Object[1000][1000];
		if (roomCount > 0) {
			for (int i = 0; i < roomCount; i++) {
				if (roomArray.get(i).isEmpty()) {
					roomData[i][0] = roomArray.get(i).getRoomNum();
					roomData[i][1] = roomArray.get(i).getSizeOfRoom();
					roomData[i][2] = roomArray.get(i).getCharge();
				}
			}
		}
		return roomData;
	}
	
	// ���� �ο� ���̺� �����
	Object[][] searchTable(int size) {
		Object roomData[][] = new Object[1000][1000];
		ArrayList<Room> emptyArray = searchEmptyRoom(size);
		int roomCount = emptyArray.size();
		if (roomCount > 0) {
			for (int i = 0; i < roomCount; i++) {
				Room room = emptyArray.get(i);
				roomData[i][0] = room.getRoomNum();
				roomData[i][1] = room.getSizeOfRoom();
				roomData[i][2] = room.getCharge();
			}
		}
		return roomData;
	}
	
	// ��, ������ �Ķ���ͷ� ��� ���� ���� �޼ҵ�
	void setIncome(long totalIncome) {
		Calendar today = Calendar.getInstance();
		int month = today.get(Calendar.MONTH) + 1;
		int day = today.get(Calendar.DATE);
		income[month][day] += (int) totalIncome;
	}
	
	// ���� �Ķ���ͷ� �޴� ���� ��ȯ �޼ҵ�, 31 �̻� ���� �Է½� ����ó��
	Integer getIncome(int month, int day) throws Exception {
		if (day <= 31) return income[month][day];
		else throw new Exception("31�� �̳��� �Է��� �ּ���.");
	}
	
	// �Ϸ� �� ������ ��ȯ�ϴ� �޼ҵ�
	long getTotalIncome() {
		return totalIncome;
	}
	
	void setTotalIncome(int income) {
		this.totalIncome = income;
	}
	
	Room findRoom(String roomNum) {
		room.setRoomNum(roomNum);
		int index = roomArray.indexOf(room);
		return roomArray.get(index);
	}
	
	// �ش� �� �ѹ��� ���� ���� index�� ��ȯ�� �ִ� �Լ�
	int searchRoomIndex(String roomNum) {
		room.setRoomNum(roomNum);
		return roomArray.indexOf(room);
	}
	
	// roomArray���� �ش� �� �ѹ��� ���� ���� �ִ��� �˻��ϴ� �Լ�
	boolean searchRoom(String roomNum) {
		room.setRoomNum(roomNum);
		return roomArray.contains(room);
	}

	int getRoomCount() {
		roomCount = roomArray.size();
		return roomCount;
	}
	
	// �� ���� ���
	void editRoom(Room room, String roomNum, int roomSize, int price) {
		room.setRoomNum(roomNum);
		room.setSizeOfRoom(roomSize);
		room.setCharge(price);
	}
	
	// �� ���� ã�� �޼ҵ�
	ArrayList<Room> searchEmptyRoom(int roomSize) {
	      ArrayList<Room> emptyArray = new ArrayList<Room>();
	      for (int i = 0; i < roomArray.size(); i++) { // ���� ������ŭ for���� �����ش�.
	    	  Room room = roomArray.get(i);
	    	  if (room.isEmpty() == true && room.getSizeOfRoom() >= roomSize) { // ���� ��� �ְ� �Է��� �ο� �̻��� ���� �����ϸ�
	    			  emptyArray.add(room);
	    	  }
	      }
	      return emptyArray; // �� �� �迭 ��ȯ
	}
	
	// üũ��, try catch�� ���� ȣ�� �Է������� ����ó�� �����ϱ�
	void checkIn(String roomNum, String name, String phoneNum) throws Exception { // �� ��ȣ, ����� �̸�, ��ȭ��ȣ�� �Ķ���ͷ� �ϴ� üũ�� �޼ҵ�
		Room room = findRoom(roomNum);
		if (!room.isEmpty()) return;
		else {
			room.checkIn(name, phoneNum);
		}
	}
	
	// �� ��ȣ�� �Ķ���ͷ� �ϴ� üũ�ƿ� �޼ҵ�
	void checkOut(String roomNum) throws Exception {
		Room room = findRoom(roomNum);
		if (room.isEmpty()) return;
		else {
			room.checkOut();
		}
	}
	
	// �� ũ��� ��ȣ, ������ �Ķ���ͷ� �޴� �� ���� �޼ҵ�.
	void plusRoom(int roomSize, String roomNum, int charge) {
		roomArray.add(new Room(roomSize, roomNum, charge)); 
		
	}
	
	// �� ��ȣ�� �޼ҵ�� �޴� �� ���� �޼ҵ�, �������� �ʴ� ���� ��� exception ó��
	Room deleteRoom(String roomNum) throws Exception {
		boolean foundRoom = false;
		boolean using = false;
		Room room = findRoom(roomNum);
		if (!room.isEmpty()) {
			using = true;
		}
		else if (room.isEmpty()) {
			roomArray.remove(room);
			return room;
		}
		if (!foundRoom) // ���� ã�� �� ���� ��
			throw new Exception(roomNum + "ȣ�� �������� �ʽ��ϴ�.");
		else if (using) // �ش� ���� ��� ���� ��
			throw new Exception(roomNum + "�� ���� ��� ���Դϴ�.");
		return null;
	}
	
	// ����ڰ� ����� �� ��ȣ�� �����ϴ� �޼ҵ�. ������� ��ȣ�� ã�� ������ �� ����ó��.
	String usingRoom(String phoneNum) throws Exception {
		boolean foundPhoneNum = false;
		for (int i = 0; i < roomArray.size(); i++) {
			Room room = roomArray.get(i);
			if (room.getUser().getPhoneNum() != null && room.getUser().getPhoneNum().equals(phoneNum)) {
				foundPhoneNum = true;
				return room.getRoomNum();
			}
		}
		if (!foundPhoneNum)
			throw new Exception("��ϵ��� ���� ��ȣ�Դϴ�.");
		return null;
	}
	
	// ���� �ð��� ����� �ִ� �޼ҵ�. (�ð�, ��)
	String printTime() {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		int amPm = time.get(Calendar.AM_PM);
		String sAmPm = amPm == Calendar.AM ? "����" : "����";
		int hour = time.get(Calendar.HOUR);
		int minute = time.get(Calendar.MINUTE);
		int second = time.get(Calendar.SECOND);
		if (hour == 0) hour = 12;
		return (sAmPm + " " + hour + "�� " + minute + "�� " + second + "��");
	}
	
	// üũ�� �ð��� �ش� �濡 ������ �ִ� �޼ҵ�.
	void checkInTime(String roomNum) throws Exception {
		Room room = findRoom(roomNum);
		long checkInTime = System.currentTimeMillis();
		room.setCheckInTime(roomNum, checkInTime);
	}
	
	// üũ�ƿ� �ð��� �ش� �濡 ������ �ִ� �޼ҵ�.
	void checkOutTime(String roomNum) throws Exception {
		Room room = findRoom(roomNum);
		long checkOutTime = System.currentTimeMillis();
		room.setCheckOutTime(roomNum, checkOutTime); // setCheckOutTime �޼ҵ�� ������.
	}
	
	// ��� ���� �޼ҵ� (�� ����)
	long pay(String roomNum) throws Exception {
		long amountOfPay = 0;
		Room room = findRoom(roomNum);
		// ����� �ð��� ����� 1�ð��� ��� �Ҵ��� �̿� ��� ���.
		long usingTime = (room.getCheckOutTime() - room.getCheckInTime()) / 1000;
		amountOfPay = usingTime * room.getCharge(); // �ð��� ���� ������ ���� ��.
		// ���� ��� ������ �� üũ�� �ð� �ʱ�ȭ�� �ֱ�
		room.setCheckInTime(roomNum, 0);
		
		return amountOfPay;
	}
	
	// �Ϸ� �� ������ ��� ������ ������� ��� ���� �ִ� �޼ҵ�.
	void saveIncome(long income) {
		this.totalIncome += income;
	}
}
	
