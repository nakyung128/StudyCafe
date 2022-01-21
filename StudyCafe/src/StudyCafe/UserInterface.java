package StudyCafe;

import java.util.*;
import java.util.InputMismatchException; 
import java.util.Scanner;
import java.io.*;

public class UserInterface {
	
	@SuppressWarnings("null")
	public static void main(String args[]) throws Exception {
		// ���̵� <cafemanager>�� �Ŵ��� ��ü ����.
		Management Manager = new Management("cafemanager");
		boolean run = true;    // while���� ����ϱ� ���� boolean ����. true�� ������ �ش�.
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
				System.out.println("������ ã�� �� �����ϴ�.");
			}
		}

		try {
			// �� ������ �о�� ���� �ҷ�����
			roomInfoIn = new ObjectInputStream(new FileInputStream(roomInfo));
			incomeIn = new ObjectInputStream(new FileInputStream(incomeInfo));
			Manager.readFile(roomInfoIn);
			Manager.readIncome(incomeIn);
			
			int roomCount = Manager.getRoomCount();
			System.out.println("���� " + roomCount + "���� ���� �ֽ��ϴ�.");
			System.out.println();
		} catch (FileNotFoundException fnfe) {
			System.out.println("������ ã�� �� �����ϴ�.");
			System.out.println();
		} catch (EOFException eofe) {
			System.out.println("������ ��� �ֽ��ϴ�.");
			System.out.println();
		} catch (IOException ioe) {
			System.out.println("������ �о�� �� �����ϴ�.");
		} finally {
			try {
				roomInfoIn.close();
				incomeIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		while (run) {
			boolean isUser = false, isManager = false; // �������� �Ŵ������� �����ϱ� ���� boolean.
			
			// ��������� ���������� �����ϱ�
			System.out.println("\n-----MODE CHOICE----");
			System.out.println("[ 1. USER ]");
			System.out.println("[ 2. MANAGER ]");
			System.out.println("[ 3. EXIT ]");
			System.out.println("--------------------");
			
			// ����� ������ �޾Ҵ��� Ȯ���ϴ� boolean �Լ�
			boolean mode = false;
			while (!mode) {
				try {
					System.out.print("\n���ϴ� ����� ��ȣ�� �Է��ϼ���: ");
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
						System.out.println("�߸��� �Է��Դϴ�. �ٽ� �õ��� �ּ���.");
					}
				} catch (InputMismatchException e) { // ���ڰ� �ƴ� �ٸ� ���� �Է� �� ����ϴ� try, catch��
					scan = new Scanner(System.in);	
					System.out.println("���ڸ� �Է� �����մϴ�. �ٽ� �õ��� �ּ���.");
				}
			}
			
			// �� ��忡 ���� �� ������ goToMain�� false ó���� �ش�.
			
			boolean goToMain = false;	
			// USER MODE
			if (isUser) {
				// USER MENU
				while (!goToMain) {
					try {  // ���� �̿��� ���ڸ� �Է����� �� ����ó��
						System.out.println("\n------ MENU ------");
						System.out.println("1. �� �� ã�� �� üũ��");
						System.out.println("2. üũ�ƿ�");
						System.out.println("3. ���� ȭ������ ���ư���");
						System.out.println("-------------------");

						System.out.print("\n���ϴ� ����� ��ȣ�� �Է��ϼ���: ");
						int num = scan.nextInt();
						
						switch(num) {
						case 1:
							System.out.print("\n�� ��� �ο����� �Է��� �ּ���: ");
							int roomSize = scan.nextInt();
							
							// �� ���� ������� RoomArray ����Ʈ ����.
							ArrayList<Room> RoomArray = Manager.searchEmptyRoom(roomSize);
							
							if (RoomArray.size() == 0) {  // roomArray �迭 ���̰� 0�� �� = ��� �ִ� ���� �������� ���� ��
								System.out.println("��� �ִ� ���� �����ϴ�. \n");
								break;
							}
							else { // 0 ���� ��
								System.out.println("<��� �ִ� �� ���>");
								for (int i = 0; i < RoomArray.size(); i++) {
									Room room = RoomArray.get(i);
									if (room == null) break;
									else {
										System.out.println((i + 1) + "��°: [" + room.getRoomNum() + "]ȣ, " + room.getSizeOfRoom() + "�ν�, " + room.getCharge() + "��.");
									}
								}
								System.out.print("üũ�� �Ͻðھ��? (1. yes | 2. no) => ");
								int check = scan.nextInt();
								try {
									if (check == 1) {
										String roomNum;
										String phoneNum;
										String name;
										System.out.print("������� �̸��� �Է��� �ּ���: ");
										name = scan.next();
										System.out.print("��ȭ��ȣ �� �� �ڸ��� �Է��� �ּ���: ");
										phoneNum = scan.next();
										if (phoneNum.length() != 4) { // �Է��� ��ȣ�� �� �ڸ� ���ڰ� �ƴ� ���
											System.out.println("�� �ڸ� ���ڰ� �ƴմϴ�. �ٽ� �õ��� �ּ���.\n");
											continue;
										}
										System.out.print("üũ�� �� �� ȣ���� �Է��� �ּ���: ");
										roomNum = scan.next();
										Manager.checkIn(roomNum, name, phoneNum);
										System.out.println("[" + roomNum + "]ȣ�� üũ�� �Ǿ����ϴ�.");
										Manager.checkInTime(roomNum); // �� �濡 üũ���� �ð� ����
										System.out.println(Manager.printTime());
										}
										else break;
								} catch (Exception e) { // �������� �ʴ� ���� �� ����ó��
									System.out.println(e.getMessage());
								}
							}
							break;
						case 2:
							System.out.print("��ȭ��ȣ �� �� �ڸ��� �Է��� �ּ���: ");
							String phoneNum = scan.next();
							try {
								String usingRoomNum = Manager.usingRoom(phoneNum);
								System.out.print("[" + usingRoomNum + "]ȣ���� üũ�ƿ� �Ͻðڽ��ϱ�? (1. yes | 2. no) => ");	
								int answer = scan.nextInt();
								switch(answer) {
								case 1:
									Manager.checkOut(usingRoomNum);	
									Manager.checkOutTime(usingRoomNum); // checkOutTime ����
									System.out.println("[" + usingRoomNum + "]ȣ���� üũ�ƿ� �Ǿ����ϴ�.\n");
									System.out.println(Manager.printTime());
									long amount =  Manager.pay(usingRoomNum);
									System.out.println("�����Ͻ� �ݾ��� " + amount + "���Դϴ�.");
									Manager.saveIncome(amount); // ������ �ݾ��� totalIncome ������ ������.
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
							goToMain = true; // ���� ȭ������ ���ư��� ���ؼ� true�� ������ ��.
							break;
						default:
							System.out.println("�߸��� �޴��Դϴ�. �ٽ� �õ��� �ּ���.\n");
							break;	
						}
					} catch (InputMismatchException e) {
						scan = new Scanner(System.in);	
						System.out.println("���ڸ� �Է� �����մϴ�. �ٽ� �õ��� �ּ���.");
					}
				}
			}
			
			// MANAGER MODE
			if (isManager && !goToMain) { // �Ŵ����� ��, goToMain�� false��
				System.out.print("�Ŵ��� ���̵� �Է��� �ּ���: ");
				String id = scan.next();
				
				boolean correct = false; // ���̵� ��ġ�ϴ��� �Ǵ��ϴ� boolean ����.
				while (!correct && !goToMain) { // �Ŵ��� ���̵� ��ġ�ϰ� �������� ���ư��� ������
					if (Manager.correctManager(id)) {
						System.out.println("�α��� �Ϸ�.");
						correct = true; // ��ġ�ϹǷ� true�� �ٲ��ش�.
					} else {
						System.out.print("�α��� ����. �ٽ� �õ��� �ּ���. (������: exit �Է�) => ");
						id = scan.next();
						if (id.equals(new String("exit"))) // exit �Է����� �� �ݺ��� ����. ���� ȭ������ ���ư�.
							goToMain = true;
						
					}	
				}
				// MANAGER MENU
				while (!goToMain) {
					System.out.println("\n------ MENU -------");
					System.out.println("1. �� ����");
					System.out.println("2. �� ����");
					System.out.println("3. �� ����");
					System.out.println("4. ���� ����");
					System.out.println("5. ���� ȭ������ ���ư���");
					System.out.println("-------------------");
					
					try { // ���� �̿��� ���ڸ� �Է����� �� ����ó��
						System.out.print("\n���ϴ� ����� ��ȣ�� �Է��ϼ���: ");
						int num = scan.nextInt();
						switch(num) {
						case 1:
							System.out.print("\n���ο� ���� �����Ͻðڽ��ϱ�? (1. 1�ο� | 2. 2�ο� | 4. 4�ο�) => ");
							int size = scan.nextInt();
							
							if (size != 1 && size != 2 && size !=4) // 1, 2, 4 �̿��� ��ȣ�� ���ڸ� �Է����� �� ����ó��
								throw new InputMismatchException();
							else {
								System.out.print("\n�� ��ȣ�� �Է��� �ּ���: ");
								String roomNum = scan.next(); // �Է��� ��ȣ�� ��� ���� number
								System.out.print("�� ������ �Է��� �ּ���: ");
								int charge = scan.nextInt();
								if (Manager.roomArray.size() == 0) {  // ������ ���� �ϳ��� ���� ��
									Manager.plusRoom(size, roomNum, charge);
									System.out.println("[" + roomNum + "]ȣ�� �����߽��ϴ�.");		
								}
								else { // ������ ���� �� �� �̻��� ��
									for (int i = 0; i < Manager.roomArray.size(); i++) { // for���� �̿��� �Է��� �� ��ȣ�� �̹� �ִ��� �˻�
										if (roomNum.equals(Manager.roomArray.get(i).getRoomNum()) == true) {
											System.out.println("�̹� �����ϴ� ���Դϴ�. �ٽ� �õ��� �ּ���.\n");
											break;
										}
										else if (roomNum.equals(Manager.roomArray.get(i).getRoomNum()) == false) { // ���� ��ȣ��� ����
											Manager.plusRoom(size, roomNum, charge);
											System.out.println("[" + roomNum + "]ȣ�� �����߽��ϴ�.");	
											break;
										}
									}	
								}
							}
							break;
						case 2:
							System.out.print("������ �� ȣ���� �Է��� �ּ���: ");
							String editNum = scan.next();
							
							int price;
							int roomSize;
							String roomNum;
							
							System.out.print("���� �� �� ȣ�� �Է�: ");
							roomNum = scan.next();
							System.out.print("���� �� �� ���� �Է�: ");
							price = scan.nextInt();
							System.out.print("���� �� ���� �ο� �Է�: ");
							roomSize = scan.nextInt();
							
							Room edit = Manager.findRoom(editNum);
							Manager.editRoom(edit, roomNum, roomSize, price);
							
							System.out.println();
							System.out.println("�����Ǿ����ϴ�.");
							
							break;
						case 3:
							if (Manager.roomArray.size() == 0) { // ���� ������ 0�� ��
								System.out.println("���� ���� �� ���� �����ϴ�.");
							}
							else {
								System.out.print("������ ȣ���� �Է��ϼ���: ");
								roomNum = scan.next();
								try {
									Manager.deleteRoom(roomNum);	
									System.out.println("[" + roomNum + "]ȣ�� �����Ǿ����ϴ�.\n");	
								}
								catch (Exception e) { // �������� �ʴ� ȣ���� �� ����ó��
									System.out.println(e.getMessage());
								}
							}
							break;
						case 4:
							System.out.println("\n---MENU---");
							System.out.println("1. ���� Ȯ��");
							System.out.println("2. ���� ����");
							System.out.println("----------");
							System.out.print("���ϴ� ��ȣ�� �Է��� �ּ���: ");
							int choice = scan.nextInt();
							switch(choice) {
							case 1:
								System.out.print("���ϴ� ���� �Է��� �ּ���: ");
								int month = scan.nextInt();
								System.out.print("���ϴ� ���� �Է��� �ּ���: ");
								int day = scan.nextInt();
								// try, catch���� �̿��� 31 �̻��� ���� �Է� �� ����ó��
								try {
									System.out.println(month + "�� " + day + "���� ������ " + Manager.getIncome(month, day) + "���Դϴ�.");	
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
								break;
							case 2:
								// ������ �� ���� ����ϰ� income �迭�� �ֱ�.
								long total = Manager.getTotalIncome();
								System.out.println("\n���ݱ��� ������ ������ " + total + "���Դϴ�.");
								System.out.print("�����Ͻðھ��? (1. yes | 2. no) => ");
								choice = scan.nextInt();
								switch(choice) {
								case 1:
									Manager.setIncome(total);
									System.out.println("����Ǿ����ϴ�.");
									// ���Ͽ� ����
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
							System.out.println("�߸��� �޴��Դϴ�. �ٽ� �õ��� �ּ���.\n");
							break;	
						}
						
					} catch (InputMismatchException e ) {
						scan = new Scanner(System.in);
						System.out.println("���ڸ� �Է� �����մϴ�. �ٽ� �õ��� �ּ���.");
					}
				}	
			}
		}
		
		// UI ����
		System.out.println();
		System.out.println("�̿��� �ּż� �����մϴ�. �ȳ��� ������. :-)");
		scan.close();
		
		// �����ϰ� ������ ���ش�.
		try {
			roomInfoOut = new ObjectOutputStream(new FileOutputStream(roomInfo));
			Manager.writeFile(roomInfoOut);
		} catch (IOException ioe) {
			System.out.println("����� �� �����ϴ�.");
		} finally {
			try {
				roomInfoOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}