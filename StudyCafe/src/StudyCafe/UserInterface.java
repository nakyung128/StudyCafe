package StudyCafe;

import java.util.*;
import java.util.InputMismatchException; 
import java.util.Scanner;
import java.io.*;

public class UserInterface {
	
	@SuppressWarnings("null")
	public static void main(String args[]) throws Exception {
		// 아이디가 <cafemanager>인 매니저 객체 생성.
		Management Manager = new Management("cafemanager");
		boolean run = true;    // while문에 사용하기 위한 boolean 변수. true로 설정해 준다.
		Scanner scan = new Scanner(System.in);
		File roomInfo = new File("roomInfo.txt");
		File incomeInfo = new File("income.txt");
		ObjectOutputStream roomInfoOut = null;
		ObjectInputStream roomInfoIn = null;
		ObjectOutputStream incomeOut = null;
		ObjectInputStream incomeIn = null;
		
		if (!roomInfo.exists()) {
			try {
				roomInfoOut = new ObjectOutputStream(new FileOutputStream(roomInfo));
			} catch (FileNotFoundException fnfe) {
				System.out.println("파일을 찾을 수 없습니다.");
			}
		}

		try {
			// 방 정보를 읽어올 파일 불러오기
			roomInfoIn = new ObjectInputStream(new FileInputStream(roomInfo));
			incomeIn = new ObjectInputStream(new FileInputStream(incomeInfo));
			Manager.readFile(roomInfoIn);
			Manager.readIncome(incomeIn);
			
			int roomCount = Manager.getRoomCount();
			System.out.println("현재 " + roomCount + "개의 방이 있습니다.");
			System.out.println();
		} catch (FileNotFoundException fnfe) {
			System.out.println("파일을 찾을 수 없습니다.");
			System.out.println();
		} catch (EOFException eofe) {
			System.out.println("파일이 비어 있습니다.");
			System.out.println();
		} catch (IOException ioe) {
			System.out.println("파일을 읽어올 수 없습니다.");
		} finally {
			try {
				roomInfoIn.close();
				incomeIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		while (run) {
			boolean isUser = false, isManager = false; // 유저인지 매니저인지 구분하기 위한 boolean.
			
			// 사용자인지 관리자인지 선택하기
			System.out.println("\n-----MODE CHOICE----");
			System.out.println("[ 1. USER ]");
			System.out.println("[ 2. MANAGER ]");
			System.out.println("[ 3. EXIT ]");
			System.out.println("--------------------");
			
			// 제대로 선택을 받았는지 확인하는 boolean 함수
			boolean mode = false;
			while (!mode) {
				try {
					System.out.print("\n원하는 모드의 번호를 입력하세요: ");
					int choiceNum = scan.nextInt();
					
					switch(choiceNum) {
					case 1:
						isUser = true;
						mode = true;
						break;
					case 2:
						isManager = true;
						mode = true;
						break;
					case 3:
						run = false;
						mode = true;
						break;
					default:
						System.out.println("잘못된 입력입니다. 다시 시도해 주세요.");
					}
				} catch (InputMismatchException e) { // 숫자가 아닌 다른 문자 입력 시 출력하는 try, catch문
					scan = new Scanner(System.in);	
					System.out.println("숫자만 입력 가능합니다. 다시 시도해 주세요.");
				}
			}
			
			// 각 모드에 입장 할 때마다 goToMain을 false 처리해 준다.
			
			boolean goToMain = false;	
			// USER MODE
			if (isUser) {
				// USER MENU
				while (!goToMain) {
					try {  // 숫자 이외의 문자를 입력했을 때 예외처리
						System.out.println("\n------ MENU ------");
						System.out.println("1. 빈 방 찾기 및 체크인");
						System.out.println("2. 체크아웃");
						System.out.println("3. 메인 화면으로 돌아가기");
						System.out.println("-------------------");

						System.out.print("\n원하는 모드의 번호를 입력하세요: ");
						int num = scan.nextInt();
						
						switch(num) {
						case 1:
							System.out.print("\n방 사용 인원수를 입력해 주세요: ");
							int roomSize = scan.nextInt();
							
							// 빈 방을 집어넣을 RoomArray 리스트 생성.
							ArrayList<Room> RoomArray = Manager.searchEmptyRoom(roomSize);
							
							if (RoomArray.size() == 0) {  // roomArray 배열 길이가 0일 때 = 비어 있는 방이 존재하지 않을 때
								System.out.println("비어 있는 방이 없습니다. \n");
								break;
							}
							else { // 0 넘을 때
								System.out.println("<비어 있는 방 목록>");
								for (int i = 0; i < RoomArray.size(); i++) {
									Room room = RoomArray.get(i);
									if (room == null) break;
									else {
										System.out.println((i + 1) + "번째: [" + room.getRoomNum() + "]호, " + room.getSizeOfRoom() + "인실, " + room.getCharge() + "원.");
									}
								}
								System.out.print("체크인 하시겠어요? (1. yes | 2. no) => ");
								int check = scan.nextInt();
								try {
									if (check == 1) {
										String roomNum;
										String phoneNum;
										String name;
										System.out.print("사용자의 이름을 입력해 주세요: ");
										name = scan.next();
										System.out.print("전화번호 끝 네 자리를 입력해 주세요: ");
										phoneNum = scan.next();
										if (phoneNum.length() != 4) { // 입력한 번호가 네 자리 숫자가 아닌 경우
											System.out.println("네 자리 숫자가 아닙니다. 다시 시도해 주세요.\n");
											continue;
										}
										System.out.print("체크인 할 방 호수를 입력해 주세요: ");
										roomNum = scan.next();
										Manager.checkIn(roomNum, name, phoneNum);
										System.out.println("[" + roomNum + "]호에 체크인 되었습니다.");
										Manager.checkInTime(roomNum); // 그 방에 체크인한 시간 측정
										System.out.println(Manager.printTime());
										}
										else break;
								} catch (Exception e) { // 존재하지 않는 방일 때 예외처리
									System.out.println(e.getMessage());
								}
							}
							break;
						case 2:
							System.out.print("전화번호 끝 네 자리를 입력해 주세요: ");
							String phoneNum = scan.next();
							try {
								String usingRoomNum = Manager.usingRoom(phoneNum);
								System.out.print("[" + usingRoomNum + "]호에서 체크아웃 하시겠습니까? (1. yes | 2. no) => ");	
								int answer = scan.nextInt();
								switch(answer) {
								case 1:
									Manager.checkOut(usingRoomNum);	
									Manager.checkOutTime(usingRoomNum); // checkOutTime 설정
									System.out.println("[" + usingRoomNum + "]호에서 체크아웃 되었습니다.\n");
									System.out.println(Manager.printTime());
									long amount =  Manager.pay(usingRoomNum);
									System.out.println("지불하실 금액은 " + amount + "원입니다.");
									Manager.saveIncome(amount); // 지불한 금액을 totalIncome 변수에 저장함.
									break;
								case 2:
									break;
								}
							}
							catch (Exception e) {
								System.out.println(e.getMessage());
							}
							break;
						case 3:
							goToMain = true; // 메인 화면으로 돌아가기 위해서 true로 설정해 줌.
							break;
						default:
							System.out.println("잘못된 메뉴입니다. 다시 시도해 주세요.\n");
							break;	
						}
					} catch (InputMismatchException e) {
						scan = new Scanner(System.in);	
						System.out.println("숫자만 입력 가능합니다. 다시 시도해 주세요.");
					}
				}
			}
			
			// MANAGER MODE
			if (isManager && !goToMain) { // 매니저일 때, goToMain이 false면
				System.out.print("매니저 아이디를 입력해 주세요: ");
				String id = scan.next();
				
				boolean correct = false; // 아이디가 일치하는지 판단하는 boolean 변수.
				while (!correct && !goToMain) { // 매니저 아이디가 일치하고 메인으로 돌아가기 전까지
					if (Manager.correctManager(id)) {
						System.out.println("로그인 완료.");
						correct = true; // 일치하므로 true로 바꿔준다.
					} else {
						System.out.print("로그인 실패. 다시 시도해 주세요. (나가기: exit 입력) => ");
						id = scan.next();
						if (id.equals(new String("exit"))) // exit 입력했을 시 반복문 종료. 메인 화면으로 돌아감.
							goToMain = true;
						
					}	
				}
				// MANAGER MENU
				while (!goToMain) {
					System.out.println("\n------ MENU -------");
					System.out.println("1. 방 개설");
					System.out.println("2. 방 수정");
					System.out.println("3. 방 삭제");
					System.out.println("4. 수입 관리");
					System.out.println("5. 메인 화면으로 돌아가기");
					System.out.println("-------------------");
					
					try { // 숫자 이외의 문자를 입력했을 때 예외처리
						System.out.print("\n원하는 모드의 번호를 입력하세요: ");
						int num = scan.nextInt();
						switch(num) {
						case 1:
							System.out.print("\n몇인용 방을 개설하시겠습니까? (1. 1인용 | 2. 2인용 | 4. 4인용) => ");
							int size = scan.nextInt();
							
							if (size != 1 && size != 2 && size !=4) // 1, 2, 4 이외의 번호나 문자를 입력했을 시 예외처리
								throw new InputMismatchException();
							else {
								System.out.print("\n방 번호를 입력해 주세요: ");
								String roomNum = scan.next(); // 입력한 번호를 담는 변수 number
								System.out.print("방 가격을 입력해 주세요: ");
								int charge = scan.nextInt();
								if (Manager.roomArray.size() == 0) {  // 생성된 방이 하나도 없을 때
									Manager.plusRoom(size, roomNum, charge);
									System.out.println("[" + roomNum + "]호를 생성했습니다.");		
								}
								else { // 생성된 방이 한 개 이상일 때
									for (int i = 0; i < Manager.roomArray.size(); i++) { // for문을 이용해 입력한 방 번호가 이미 있는지 검사
										if (roomNum.equals(Manager.roomArray.get(i).getRoomNum()) == true) {
											System.out.println("이미 존재하는 방입니다. 다시 시도해 주세요.\n");
											break;
										}
										else if (roomNum.equals(Manager.roomArray.get(i).getRoomNum()) == false) { // 없는 번호라면 생성
											Manager.plusRoom(size, roomNum, charge);
											System.out.println("[" + roomNum + "]호를 생성했습니다.");	
											break;
										}
									}	
								}
							}
							break;
						case 2:
							System.out.print("수정할 방 호수를 입력해 주세요: ");
							String editNum = scan.next();
							
							int price;
							int roomSize;
							String roomNum;
							
							System.out.print("수정 후 방 호수 입력: ");
							roomNum = scan.next();
							System.out.print("수정 후 방 가격 입력: ");
							price = scan.nextInt();
							System.out.print("수정 후 수용 인원 입력: ");
							roomSize = scan.nextInt();
							
							Room edit = Manager.findRoom(editNum);
							Manager.editRoom(edit, roomNum, roomSize, price);
							
							System.out.println();
							System.out.println("수정되었습니다.");
							
							break;
						case 3:
							if (Manager.roomArray.size() == 0) { // 방의 개수가 0일 때
								System.out.println("아직 방이 한 개도 없습니다.");
							}
							else {
								System.out.print("삭제할 호수를 입력하세요: ");
								roomNum = scan.next();
								try {
									Manager.deleteRoom(roomNum);	
									System.out.println("[" + roomNum + "]호가 삭제되었습니다.\n");	
								}
								catch (Exception e) { // 존재하지 않는 호수일 때 예외처리
									System.out.println(e.getMessage());
								}
							}
							break;
						case 4:
							System.out.println("\n---MENU---");
							System.out.println("1. 수입 확인");
							System.out.println("2. 수입 저장");
							System.out.println("----------");
							System.out.print("원하는 번호를 입력해 주세요: ");
							int choice = scan.nextInt();
							switch(choice) {
							case 1:
								System.out.print("원하는 달을 입력해 주세요: ");
								int month = scan.nextInt();
								System.out.print("원하는 일을 입력해 주세요: ");
								int day = scan.nextInt();
								// try, catch문을 이용해 31 이상의 숫자 입력 시 예외처리
								try {
									System.out.println(month + "월 " + day + "일의 수입은 " + Manager.getIncome(month, day) + "원입니다.");	
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
								break;
							case 2:
								// 오늘의 총 수입 출력하고 income 배열에 넣기.
								long total = Manager.getTotalIncome();
								System.out.println("\n지금까지 오늘의 수입은 " + total + "원입니다.");
								System.out.print("저장하시겠어요? (1. yes | 2. no) => ");
								choice = scan.nextInt();
								switch(choice) {
								case 1:
									Manager.setIncome(total);
									System.out.println("저장되었습니다.");
									// 파일에 저장
									incomeOut = new ObjectOutputStream(new FileOutputStream(incomeInfo));
									Manager.writeIncome(incomeOut);
									break;
								case 2:
									break;
								}
								break;
							}
							break;
						case 5:
							goToMain = true;
							break;
						default:
							System.out.println("잘못된 메뉴입니다. 다시 시도해 주세요.\n");
							break;	
						}
						
					} catch (InputMismatchException e ) {
						scan = new Scanner(System.in);
						System.out.println("숫자만 입력 가능합니다. 다시 시도해 주세요.");
					}
				}	
			}
		}
		
		// UI 종료
		System.out.println();
		System.out.println("이용해 주셔서 감사합니다. 안녕히 가세요. :-)");
		scan.close();
		
		// 종료하고 파일을 써준다.
		try {
			roomInfoOut = new ObjectOutputStream(new FileOutputStream(roomInfo));
			Manager.writeFile(roomInfoOut);
		} catch (IOException ioe) {
			System.out.println("출력할 수 없습니다.");
		} finally {
			try {
				roomInfoOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}