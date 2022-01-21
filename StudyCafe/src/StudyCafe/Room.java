package StudyCafe;

public class Room implements java.io.Serializable {
	private boolean isEmpty; // ���� ��� �ִ��� Ȯ���ϴ� �ʵ�
	private int chargePerHour; // 1�ʴ� ��� �ʵ�
	private String roomNum; // �� ��ȣ �ʵ� 
	private int sizeOfRoom; // �� ������ �ʵ� (�ִ� �ο� ��) 
	private User user = new User();
	private long checkInTime; // üũ�� �ð� ��ü
	private long checkOutTime; // �̿� �ð� ��ü
	
	Room() {
		
	}
	
	Room (int sizeOfRoom, String roomNum, int chargePerHour, boolean isEmpty, long checkInTime) {
		isEmpty = true; // �� ��� ������ �⺻���� ����
		this.chargePerHour = chargePerHour;
		this.sizeOfRoom = sizeOfRoom;
		this.roomNum = roomNum;
		this.user = new User(null, null);
		this.isEmpty = isEmpty;
		this.checkInTime = checkInTime;
	}
	
	// �� ������, �� ������, �� ��ȣ, ������ �Ķ���ͷ� ����.
	Room(int sizeOfRoom, String roomNum, int chargePerHour) {
		isEmpty = true; // �� ��� ������ �⺻���� ����
		this.chargePerHour = chargePerHour;
		this.sizeOfRoom = sizeOfRoom;
		this.roomNum = roomNum;
		this.user = new User(null, null);
	}
	User getUser() {
		return user;
	}
	boolean isEmpty() { // isEmpty�� ��ȯ�ϴ� �޼ҵ�
		return isEmpty;
	}
	void setIsEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	int getCharge() { // ����� ��ȯ�ϴ� �޼ҵ�
		return chargePerHour;
	}
	void setCharge(int chargePerHour) { // ��� �����ϴ� �޼ҵ�
		this.chargePerHour = chargePerHour;
	}
	void setRoomNum(String roomNum) { // �� ��ȣ�� �����ϴ� �޼ҵ�
		this.roomNum = roomNum;
	}
	String getRoomNum() { // �� ��ȣ ��ȯ �޼ҵ�
		return roomNum;
	}
	int getSizeOfRoom() { // �� ������ ��ȯ �޼ҵ�
		return sizeOfRoom;
	}
	void setSizeOfRoom(int size) { // �� ����� �����ϴ� �޼ҵ�
		sizeOfRoom = size;
	}
	void usingStart() {
		isEmpty = false;
	}
	void usingEnd() {
		isEmpty = true;
	}
	// equals �Լ� �������̵�
	public boolean equals(Object obj) {
		Room room = (Room) obj;
		if (roomNum.equals(room.getRoomNum()))
		{
			return true;
		}
		else return false;
	}
	void checkIn(String name, String phoneNum) {
		getUser().setName(name);
		getUser().setPhoneNum(phoneNum);
		usingStart(); // ��� ������ false ó���� ��.
	}
	void checkOut() {
		// user ��ü�� �̸��� ��ȣ�� null�� �ʱ�ȭ�� ��.
		getUser().setName(null);
		getUser().setPhoneNum(null);
		usingEnd(); // ��� ������ true�� ó���� ��.
	}
	long getCheckInTime() {
		return checkInTime;
	}
	void setCheckInTime(String roomNum, long checkInTime) {
		this.checkInTime = checkInTime;
	}
	long getCheckOutTime() {
		return checkOutTime;
	}
	void setCheckOutTime(String roomNum, long checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	
}
