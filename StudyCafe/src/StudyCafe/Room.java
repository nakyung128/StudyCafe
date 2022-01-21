package StudyCafe;

public class Room implements java.io.Serializable {
	private boolean isEmpty; // 방이 비어 있는지 확인하는 필드
	private int chargePerHour; // 1초당 요금 필드
	private String roomNum; // 방 번호 필드 
	private int sizeOfRoom; // 룸 사이즈 필드 (최대 인원 수) 
	private User user = new User();
	private long checkInTime; // 체크인 시간 객체
	private long checkOutTime; // 이용 시간 객체
	
	Room() {
		
	}
	
	Room (int sizeOfRoom, String roomNum, int chargePerHour, boolean isEmpty, long checkInTime) {
		isEmpty = true; // 방 비어 있음을 기본으로 설정
		this.chargePerHour = chargePerHour;
		this.sizeOfRoom = sizeOfRoom;
		this.roomNum = roomNum;
		this.user = new User(null, null);
		this.isEmpty = isEmpty;
		this.checkInTime = checkInTime;
	}
	
	// 룸 생성자, 룸 사이즈, 룸 번호, 가격을 파라미터로 받음.
	Room(int sizeOfRoom, String roomNum, int chargePerHour) {
		isEmpty = true; // 방 비어 있음을 기본으로 설정
		this.chargePerHour = chargePerHour;
		this.sizeOfRoom = sizeOfRoom;
		this.roomNum = roomNum;
		this.user = new User(null, null);
	}
	User getUser() {
		return user;
	}
	boolean isEmpty() { // isEmpty를 반환하는 메소드
		return isEmpty;
	}
	void setIsEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	int getCharge() { // 요금을 반환하는 메소드
		return chargePerHour;
	}
	void setCharge(int chargePerHour) { // 요금 설정하는 메소드
		this.chargePerHour = chargePerHour;
	}
	void setRoomNum(String roomNum) { // 방 번호를 설정하는 메소드
		this.roomNum = roomNum;
	}
	String getRoomNum() { // 방 번호 반환 메소드
		return roomNum;
	}
	int getSizeOfRoom() { // 방 사이즈 반환 메소드
		return sizeOfRoom;
	}
	void setSizeOfRoom(int size) { // 방 사이즈를 설정하는 메소드
		sizeOfRoom = size;
	}
	void usingStart() {
		isEmpty = false;
	}
	void usingEnd() {
		isEmpty = true;
	}
	// equals 함수 오버라이딩
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
		usingStart(); // 비어 있음을 false 처리해 줌.
	}
	void checkOut() {
		// user 객체의 이름과 번호를 null로 초기화해 줌.
		getUser().setName(null);
		getUser().setPhoneNum(null);
		usingEnd(); // 비어 있음을 true로 처리해 줌.
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
