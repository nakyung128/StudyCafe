package StudyCafe;
import java.util.*;
import java.io.*;


public class Management implements java.io.Serializable {

	ArrayList<Room> roomArray = new ArrayList<Room>(); // 룸 객체 리스트 생성
	Room room = new Room(); // 룸 객체 생성
	int roomCount; // roomArray의 크기
	private int income[][] = new int[12][31]; // 일 년 동안의 수입을 담는 2차원 배열.
	private long totalIncome;
	private String managerID;
	
	Management(String ManagerID) {
		this.managerID = ManagerID;
	}
	
	// income 저장하기
	void writeIncome(ObjectOutputStream out) throws IOException {
		out.writeObject(income);
	}
	//  roomArray 저장하기
	void writeFile(ObjectOutputStream out) throws Exception {
		// 룸 저장
		out.writeObject(roomArray);
	}
	
	// income 불러오기
	void readIncome(ObjectInputStream in) throws Exception {
		income = (int[][]) in.readObject();
	}

	// roomArray 불러오기
	@SuppressWarnings("unchecked")
	void readFile(ObjectInputStream in) throws Exception {
		roomArray = (ArrayList<Room>) in.readObject();
	}

	// manager 아이디 설정 메소드
	void setManagerID(String ID) {
		this.managerID = ID;
	}
	
	// manager 아이디 일치 확인 메소드
	boolean correctManager(String managerID) {
		return (this.managerID.equals(managerID));
	}
	
	// 관리자 모드 테이블을 만들기 위한 함수
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
	
	// 체크인 테이블 만들기 위한 함수
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
	
	// 수용 인원 테이블 만들기
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
	
	// 일, 수익을 파라미터로 담는 수입 지정 메소드
	void setIncome(long totalIncome) {
		Calendar today = Calendar.getInstance();
		int month = today.get(Calendar.MONTH) + 1;
		int day = today.get(Calendar.DATE);
		income[month][day] += (int) totalIncome;
	}
	
	// 일을 파라미터로 받는 수입 반환 메소드, 31 이상 숫자 입력시 예외처리
	Integer getIncome(int month, int day) throws Exception {
		if (day <= 31) return income[month][day];
		else throw new Exception("31일 이내로 입력해 주세요.");
	}
	
	// 하루 총 수입을 반환하는 메소드
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
	
	// 해당 룸 넘버를 가진 방의 index를 반환해 주는 함수
	int searchRoomIndex(String roomNum) {
		room.setRoomNum(roomNum);
		return roomArray.indexOf(room);
	}
	
	// roomArray에서 해당 룸 넘버를 가진 방이 있는지 검사하는 함수
	boolean searchRoom(String roomNum) {
		room.setRoomNum(roomNum);
		return roomArray.contains(room);
	}

	int getRoomCount() {
		roomCount = roomArray.size();
		return roomCount;
	}
	
	// 방 수정 기능
	void editRoom(Room room, String roomNum, int roomSize, int price) {
		room.setRoomNum(roomNum);
		room.setSizeOfRoom(roomSize);
		room.setCharge(price);
	}
	
	// 빈 방을 찾는 메소드
	ArrayList<Room> searchEmptyRoom(int roomSize) {
	      ArrayList<Room> emptyArray = new ArrayList<Room>();
	      for (int i = 0; i < roomArray.size(); i++) { // 방의 개수만큼 for문을 돌려준다.
	    	  Room room = roomArray.get(i);
	    	  if (room.isEmpty() == true && room.getSizeOfRoom() >= roomSize) { // 방이 비어 있고 입력한 인원 이상을 수용 가능하면
	    			  emptyArray.add(room);
	    	  }
	      }
	      return emptyArray; // 빈 방 배열 반환
	}
	
	// 체크인, try catch로 없는 호수 입력했을시 예외처리 구현하기
	void checkIn(String roomNum, String name, String phoneNum) throws Exception { // 방 번호, 사용자 이름, 전화번호를 파라미터로 하는 체크인 메소드
		Room room = findRoom(roomNum);
		if (!room.isEmpty()) return;
		else {
			room.checkIn(name, phoneNum);
		}
	}
	
	// 방 번호를 파라미터로 하는 체크아웃 메소드
	void checkOut(String roomNum) throws Exception {
		Room room = findRoom(roomNum);
		if (room.isEmpty()) return;
		else {
			room.checkOut();
		}
	}
	
	// 방 크기와 번호, 가격을 파라미터로 받는 방 생성 메소드.
	void plusRoom(int roomSize, String roomNum, int charge) {
		roomArray.add(new Room(roomSize, roomNum, charge)); 
		
	}
	
	// 방 번호를 메소드로 받는 방 삭제 메소드, 존재하지 않는 방일 경우 exception 처리
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
		if (!foundRoom) // 방을 찾지 못 했을 때
			throw new Exception(roomNum + "호가 존재하지 않습니다.");
		else if (using) // 해당 방은 사용 중일 때
			throw new Exception(roomNum + "은 현재 사용 중입니다.");
		return null;
	}
	
	// 사용자가 사용한 방 번호를 리턴하는 메소드. 사용자의 번호를 찾지 못했을 때 예외처리.
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
			throw new Exception("등록되지 않은 번호입니다.");
		return null;
	}
	
	// 현재 시간을 출력해 주는 메소드. (시간, 분)
	String printTime() {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		int amPm = time.get(Calendar.AM_PM);
		String sAmPm = amPm == Calendar.AM ? "오전" : "오후";
		int hour = time.get(Calendar.HOUR);
		int minute = time.get(Calendar.MINUTE);
		int second = time.get(Calendar.SECOND);
		if (hour == 0) hour = 12;
		return (sAmPm + " " + hour + "시 " + minute + "분 " + second + "초");
	}
	
	// 체크인 시간을 해당 방에 설정해 주는 메소드.
	void checkInTime(String roomNum) throws Exception {
		Room room = findRoom(roomNum);
		long checkInTime = System.currentTimeMillis();
		room.setCheckInTime(roomNum, checkInTime);
	}
	
	// 체크아웃 시간을 해당 방에 설정해 주는 메소드.
	void checkOutTime(String roomNum) throws Exception {
		Room room = findRoom(roomNum);
		long checkOutTime = System.currentTimeMillis();
		room.setCheckOutTime(roomNum, checkOutTime); // setCheckOutTime 메소드로 설정함.
	}
	
	// 요금 지불 메소드 (초 단위)
	long pay(String roomNum) throws Exception {
		long amountOfPay = 0;
		Room room = findRoom(roomNum);
		// 사용한 시간을 계산해 1시간당 요금 할당해 이용 요금 계산.
		long usingTime = (room.getCheckOutTime() - room.getCheckInTime()) / 1000;
		amountOfPay = usingTime * room.getCharge(); // 시간에 방의 가격을 곱해 줌.
		// 가격 계산 끝나고 방 체크인 시간 초기화해 주기
		room.setCheckInTime(roomNum, 0);
		
		return amountOfPay;
	}
	
	// 하루 총 수입을 담는 변수에 사용자의 요금 더해 주는 메소드.
	void saveIncome(long income) {
		this.totalIncome += income;
	}
}
	
