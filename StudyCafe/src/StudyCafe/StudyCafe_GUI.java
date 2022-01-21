package StudyCafe;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import java.awt.Component;
import javax.swing.JTextPane;

public class StudyCafe_GUI {

	private JFrame frame;
	private JTextField name;
	private JTextField phoneNum;
	private JTextField idText;
	private JTextField phoneText;
	private JTable checkInTable;
	private JTable allTable;
	private JTextField monthText;
	private JTextField dayText;
	ObjectOutputStream roomInfoOut = null;
	ObjectInputStream roomInfoIn = null;
	ObjectOutputStream incomeOut = null;
	ObjectInputStream incomeIn = null;
	private JTextField searchTextField;
	Object searchData[][] = new Object[1000][1000];
	int searchSize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudyCafe_GUI window = new StudyCafe_GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StudyCafe_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Management mng = new Management("cafemanager");
		File roomInfo = new File("roomInfo.txt");
		File incomeInfo = new File("income.txt");

		try {
			// 방 정보를 읽어올 파일 불러오기
			roomInfoIn = new ObjectInputStream(new FileInputStream(roomInfo));
			incomeIn = new ObjectInputStream(new FileInputStream(incomeInfo));
			try {
				mng.readFile(roomInfoIn);
				mng.readIncome(incomeIn);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FileNotFoundException fnfe) {
			JOptionPane.showMessageDialog(null, "파일을 찾을 수 없습니다.");
		} catch (EOFException eofe) {
			JOptionPane.showMessageDialog(null, "파일이 비어 있습니다.");
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "파일을 읽어올 수 없습니다.");
		} finally {
			try {
				roomInfoIn.close();
				incomeIn.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		frame = new JFrame("STUDY CAFE");
		frame.getContentPane().setBackground(SystemColor.control);
		frame.setBounds(100, 100, 400, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JPanel ManagerMenu = new JPanel();
		JPanel Main = new JPanel();
		JPanel CheckOut = new JPanel();
		JPanel Income = new JPanel();
		JPanel ManagerID = new JPanel();

		// 체크인 할 수 있는 방 테이블
		String colNames[] = { "방 번호", "수용 인원", "초당 가격" };
		DefaultTableModel checkInModel = new DefaultTableModel(colNames, mng.roomCount);

		Object roomData[][] = mng.checkInTable();

		// 현재 방 정보 테이블에 추가하기
		for (int i = 0; i < mng.roomArray.size(); i++) {
			checkInModel.addRow(roomData[i]);
		}
		long today = mng.getTotalIncome();
		JPanel Room = new JPanel();

		// 현재 생성되어 있는 방 전체 목록 테이블
		String allColNames[] = { "호수", "수용 인원", "초당 가격", "사용 중 여부" };
		DefaultTableModel model = new DefaultTableModel(allColNames, mng.roomCount);

		Object allRoomData[][] = mng.allRoomTable();

		// 현재 방 정보 테이블에 추가하기
		for (int i = 0; i < mng.roomArray.size(); i++) {
			model.addRow(allRoomData[i]);
		}
		JPanel CheckIn = new JPanel();

		searchTextField = new JTextField();
		searchTextField.setBounds(78, 48, 28, 21);
		CheckIn.add(searchTextField);
		searchTextField.setColumns(10);

		JLabel searchLabel = new JLabel("\uC0AC\uC6A9 \uC778\uC6D0");
		searchLabel.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchLabel.setBounds(12, 52, 65, 15);
		CheckIn.add(searchLabel);

		JButton searchButton = new JButton("\uAC80\uC0C9");

		// 사용 인원 검색 버튼 눌렀을 때
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 아무것도 입력 안 하고 검색 버튼 눌렀을 때 체크인 가능한 방 모두 보여 주기
				if (searchTextField.getText().equals("")) {
					searchData = mng.checkInTable();
				} else {
					searchSize = Integer.parseInt(searchTextField.getText());
					searchData = mng.searchTable(searchSize);
				}
				checkInModel.setNumRows(0);
				for (int i = 0; i < mng.roomArray.size(); i++) {
					checkInModel.addRow(searchData[i]);
				}
			}
		});

		searchButton.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		searchButton.setBounds(118, 48, 65, 23);
		CheckIn.add(searchButton);

		checkInTable = new JTable(checkInModel);
		checkInTable.setBounds(201, 315, 1, 1);

		checkInTable.setBounds(201, 315, 1, 1);
		CheckIn.add(checkInTable);

		// 스크롤 판 만든 뒤 테이블 올려놓기
		JScrollPane scrollPane = new JScrollPane(checkInTable);
		scrollPane.setBounds(12, 77, 362, 192);
		CheckIn.add(scrollPane, BorderLayout.CENTER);

		CheckIn.setBounds(0, 0, 386, 563);
		frame.getContentPane().add(CheckIn);

		CheckIn.setVisible(false);
		CheckIn.setLayout(null);

		// 체크인
		JLabel lblNewLabel_2 = new JLabel("\uCCB4\uD06C\uC778");
		lblNewLabel_2.setBounds(147, 10, 68, 28);
		lblNewLabel_2.setFont(new Font("a시월구일굴림2", Font.PLAIN, 23));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		CheckIn.add(lblNewLabel_2);

		JLabel nameLabel = new JLabel("\uC774\uB984");
		nameLabel.setBounds(58, 298, 35, 22);
		nameLabel.setFont(new Font("a시월구일굴림1", Font.PLAIN, 18));
		CheckIn.add(nameLabel);

		// 체크인 화면에서 이름을 담는 텍스트 필드
		name = new JTextField();
		name.setBounds(173, 299, 162, 21);
		CheckIn.add(name);

		JLabel phoneLabel = new JLabel("\uC804\uD654\uBC88\uD638 \uB4B7\uC790\uB9AC");
		phoneLabel.setBounds(12, 329, 129, 22);
		phoneLabel.setFont(new Font("a시월구일굴림1", Font.PLAIN, 18));
		CheckIn.add(phoneLabel);

		// 체크인 화면에서 번호를 담는 텍스트 필드
		phoneNum = new JTextField();
		phoneNum.setBounds(173, 330, 162, 21);
		phoneNum.setColumns(10);
		CheckIn.add(phoneNum);

		// 메인화면으로 돌아가는 버튼
		JButton checkIn_mainbtn = new JButton("\uBA54\uC778\uD654\uBA74");
		checkIn_mainbtn.setBounds(276, 530, 105, 23);
		checkIn_mainbtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		CheckIn.add(checkIn_mainbtn);

		// 체크인 확인 버튼
		JButton CheckInOkBtn = new JButton("\uCCB4\uD06C\uC778");
		CheckInOkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 클릭한 방에 체크인
				int row = checkInTable.getSelectedRow();
				String selectRoom;
				if (searchSize > 0) {
					selectRoom = (String) searchData[row][0];
				} else {
					selectRoom = (String) roomData[row][0];
				}
				String userName = name.getText();
				String userPhone = phoneNum.getText();
				try {
					mng.checkIn(selectRoom, userName, userPhone);
					mng.checkInTime(selectRoom);
					// 사용 중으로 변환
					model.setValueAt("O", row, 3);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					roomInfoOut = new ObjectOutputStream(new FileOutputStream(roomInfo));
					mng.writeFile(roomInfoOut);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// 체크인 시각을 알려준다
				String checkMsg = mng.printTime() + " " + selectRoom + "호에 체크인 되었습니다.";
				JOptionPane.showMessageDialog(null, checkMsg);
			}
		});
		CheckInOkBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		CheckInOkBtn.setBounds(147, 385, 81, 23);
		CheckIn.add(CheckInOkBtn);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(157, 164, 2, 2);
		CheckIn.add(scrollPane_1);

		checkIn_mainbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(false);
				ManagerMenu.setVisible(false);
				CheckIn.setVisible(false);
				Income.setVisible(false);
				CheckOut.setVisible(false);
				ManagerID.setVisible(false);
				Main.setVisible(true);
			}
		});

		// 오늘의 수입 라벨
		JLabel todayIncome = new JLabel();
		todayIncome.setText(today + "원");

		Income.add(todayIncome);

		Income.setBounds(0, 0, 386, 563);
		frame.getContentPane().add(Income);
		Income.setLayout(null);
		Income.setVisible(false);

		JLabel incomeLabel = new JLabel("\uC218\uC785 \uAD00\uB9AC");
		incomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		incomeLabel.setFont(new Font("a시월구일굴림2", Font.PLAIN, 23));
		incomeLabel.setBounds(96, 10, 187, 46);
		Income.add(incomeLabel);

		JButton incomeMainBtn = new JButton("\uBA54\uC778\uD654\uBA74");

		incomeMainBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		incomeMainBtn.setBounds(271, 528, 103, 25);
		Income.add(incomeMainBtn);

		// 이전 버튼
		JButton prevBtn = new JButton("\uC774\uC804");
		// 이전 버튼 클릭했을 때
		prevBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerMenu.setVisible(true);
				Room.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				ManagerID.setVisible(false);
				Income.setVisible(false);
				Main.setVisible(false);
			}
		});
		prevBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		prevBtn.setBounds(12, 529, 103, 25);
		Income.add(prevBtn);

		JLabel todayIncomeLb = new JLabel("\uC9C0\uAE08\uAE4C\uC9C0\uC758 \uC218\uC785");
		todayIncomeLb.setFont(new Font("a시월구일굴림1", Font.PLAIN, 15));
		todayIncomeLb.setHorizontalAlignment(SwingConstants.CENTER);
		todayIncomeLb.setBounds(126, 115, 125, 30);
		Income.add(todayIncomeLb);

		JLabel showIncomeLb = new JLabel("\uB0A0\uC9DC\uBCC4 \uC218\uC785 \uD655\uC778");
		showIncomeLb.setHorizontalAlignment(SwingConstants.CENTER);
		showIncomeLb.setFont(new Font("a시월구일굴림1", Font.PLAIN, 15));
		showIncomeLb.setBounds(126, 263, 125, 30);
		Income.add(showIncomeLb);

		JLabel monthLb = new JLabel("\uC6D4");
		monthLb.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		monthLb.setHorizontalAlignment(SwingConstants.CENTER);
		monthLb.setBounds(63, 324, 52, 15);
		Income.add(monthLb);

		JLabel dayLb = new JLabel("\uC77C");
		dayLb.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		dayLb.setHorizontalAlignment(SwingConstants.CENTER);
		dayLb.setBounds(63, 364, 52, 15);
		Income.add(dayLb);

		monthText = new JTextField();
		monthText.setBounds(126, 321, 106, 21);
		Income.add(monthText);
		monthText.setColumns(10);

		dayText = new JTextField();
		dayText.setColumns(10);
		dayText.setBounds(126, 361, 106, 21);
		Income.add(dayText);

		JButton incomeCheckBtn = new JButton("\uC218\uC785 \uD655\uC778");
		incomeCheckBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int month = Integer.parseInt(monthText.getText());
				int day = Integer.parseInt(dayText.getText());
				// try, catch문을 이용해 31 이상의 숫자 입력 시 예외처리
				try {
					// 해당 날짜 수입 출력
					JOptionPane.showMessageDialog(null,
							month + "월 " + day + "일의 수입은 " + mng.getIncome(month, day) + "원입니다.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		incomeCheckBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		incomeCheckBtn.setBounds(258, 336, 88, 25);
		Income.add(incomeCheckBtn);

		todayIncome.setHorizontalAlignment(SwingConstants.CENTER);
		todayIncome.setFont(new Font("a시월구일굴림1", Font.PLAIN, 16));
		todayIncome.setBounds(22, 155, 219, 25);

		JButton incomeCheckBtn_1 = new JButton("\uC218\uC785 \uC800\uC7A5");
		incomeCheckBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 오늘 날짜에 수입 저장
				long income = mng.getTotalIncome();
				// 저장 후엔 지금까지 수입 0으로 초기화해 줌.
				mng.setTotalIncome(0);
				mng.setIncome(income); // 오늘 수입을 배열에 저장함.
				JOptionPane.showMessageDialog(null, "저장되었습니다.");
				todayIncome.setText("0원");

				// 오늘의 수입 income 파일에 쓰기
				try {
					incomeOut = new ObjectOutputStream(new FileOutputStream(incomeInfo));
					mng.writeIncome(incomeOut);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		incomeCheckBtn_1.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		incomeCheckBtn_1.setBounds(258, 156, 88, 25);
		Income.add(incomeCheckBtn_1);

		incomeMainBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				ManagerMenu.setVisible(false);
				ManagerID.setVisible(false);
				Income.setVisible(false);
				Main.setVisible(true);
			}
		});

		// ====== 체크아웃 ======
		CheckOut.setBounds(0, 0, 386, 563);

		frame.getContentPane().add(CheckOut);
		CheckOut.setLayout(null);

		JLabel CheckOutLabel = new JLabel("\uCCB4\uD06C\uC544\uC6C3");
		CheckOutLabel.setFont(new Font("a시월구일굴림2", Font.PLAIN, 23));
		CheckOutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		CheckOutLabel.setBounds(121, 10, 137, 42);
		CheckOut.add(CheckOutLabel);

		JLabel numLabel = new JLabel("\uC804\uD654\uBC88\uD638 \uB4B7\uC790\uB9AC");
		numLabel.setFont(new Font("a시월구일굴림1", Font.PLAIN, 18));
		numLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numLabel.setBounds(12, 240, 151, 42);
		CheckOut.add(numLabel);

		// 체크아웃 휴대폰 번호 텍스트 필드
		phoneText = new JTextField();
		phoneText.setBounds(164, 246, 197, 31);
		CheckOut.add(phoneText);
		phoneText.setColumns(10);

		JButton mainBtn_2 = new JButton("\uBA54\uC778\uD654\uBA74");
		mainBtn_2.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		mainBtn_2.setBounds(279, 530, 95, 23);
		CheckOut.add(mainBtn_2);

		JButton checkOutOkBtn = new JButton("\uCCB4\uD06C\uC544\uC6C3");

		// 체크아웃 버튼을 눌렀을 때
		checkOutOkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// 받은 휴대폰 번호로 체크아웃
					String usingRoomNum = mng.usingRoom(phoneText.getText());
					mng.checkOut(usingRoomNum);
					mng.checkOutTime(usingRoomNum); // checkOutTime 설정
					// 체크아웃 시각 알려줌
					String checkOutMsg = mng.printTime() + "에 " + usingRoomNum + "호에서 체크아웃 되었습니다.";
					JOptionPane.showMessageDialog(null, checkOutMsg);
					long amount = mng.pay(usingRoomNum);
					mng.saveIncome(amount); // 지불한 금액을 totalIncome 변수에 저장함.
					JOptionPane.showMessageDialog(null, "지불하실 금액은 " + amount + "원입니다.");

					// 오늘 수입 라벨 갱신
					long today = mng.getTotalIncome();
					todayIncome.setText(today + "원");

					// roomArray 파일 쓰기
					try {
						roomInfoOut = new ObjectOutputStream(new FileOutputStream(roomInfo));
						mng.writeFile(roomInfoOut);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					// income 파일에 쓰기
					try {
						incomeOut = new ObjectOutputStream(new FileOutputStream(incomeInfo));
						mng.writeIncome(incomeOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					Room.setVisible(false);
					Income.setVisible(false);
					CheckIn.setVisible(false);
					CheckOut.setVisible(false);
					ManagerMenu.setVisible(false);
					ManagerID.setVisible(false);
					Main.setVisible(true);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		checkOutOkBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		checkOutOkBtn.setBounds(143, 304, 81, 23);
		CheckOut.add(checkOutOkBtn);
		CheckOut.setVisible(false);

		mainBtn_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(false);
				Income.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				ManagerMenu.setVisible(false);
				ManagerID.setVisible(false);
				Main.setVisible(true);
			}
		});

		ManagerID.setBounds(0, 0, 386, 563);
		frame.getContentPane().add(ManagerID);
		ManagerID.setLayout(null);

		JLabel idLabel = new JLabel(
				"\uAD00\uB9AC\uC790 \uC544\uC774\uB514\uB97C \uC785\uB825\uD574 \uC8FC\uC138\uC694");
		idLabel.setFont(new Font("a시월구일굴림1", Font.PLAIN, 20));
		idLabel.setBounds(54, 151, 278, 46);
		ManagerID.add(idLabel);

		JLabel managerLabel = new JLabel("\uAD00\uB9AC\uC790 \uBAA8\uB4DC");
		managerLabel.setFont(new Font("a시월구일굴림2", Font.PLAIN, 23));
		managerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		managerLabel.setBounds(98, 20, 187, 46);
		ManagerID.add(managerLabel);

		idText = new JTextField();
		idText.setBounds(109, 212, 237, 32);
		ManagerID.add(idText);
		idText.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("ID");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("a시월구일굴림1", Font.PLAIN, 20));
		lblNewLabel_4.setBounds(34, 207, 63, 40);
		ManagerID.add(lblNewLabel_4);
		ManagerID.setVisible(false);

		JButton managerMainBtn = new JButton("\uBA54\uC778\uD654\uBA74");
		managerMainBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		managerMainBtn.setBounds(271, 528, 103, 25);
		ManagerID.add(managerMainBtn);

		// 아이디 확인 버튼
		JButton idCheckBtn = new JButton("\uD655\uC778");
		idCheckBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		idCheckBtn.setBounds(283, 254, 63, 25);
		ManagerID.add(idCheckBtn);

		// 매니저에서 메인 버튼
		managerMainBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.setVisible(true);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				ManagerID.setVisible(false);
				ManagerMenu.setVisible(false);
				Room.setVisible(false);
				Income.setVisible(false);
			}
		});

		// 확인 버튼을 눌렀을 때
		idCheckBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = idText.getText();
				if (mng.correctManager(id)) {
					JOptionPane.showMessageDialog(null, "로그인 완료.");
					ManagerMenu.setVisible(true);
					ManagerID.setVisible(false);
					Main.setVisible(false);
					CheckIn.setVisible(false);
					CheckOut.setVisible(false);
					Room.setVisible(false);
					Income.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "ID가 일치하지 않습니다.");
				}
			}
		});

		allTable = new JTable(model);
		allTable.setBounds(201, 315, 1, 1);
		Room.add(allTable);

		// ======= 방 관리 =======
		Room.setBounds(0, 0, 386, 563);
		frame.getContentPane().add(Room);
		Room.setLayout(null);
		Room.setVisible(false);

		JLabel managerLabel_2 = new JLabel("\uBC29 \uAD00\uB9AC");
		managerLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		managerLabel_2.setFont(new Font("a시월구일굴림2", Font.PLAIN, 23));
		managerLabel_2.setBounds(96, 10, 187, 46);
		Room.add(managerLabel_2);

		// 스크롤 판 만든 뒤 테이블 올려놓기
		JScrollPane scrollPane1 = new JScrollPane(allTable);
		scrollPane1.setBounds(12, 105, 362, 179);
		Room.add(scrollPane1, BorderLayout.CENTER);

		JLabel label = new JLabel("호수");
		label.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(22, 298, 34, 15);
		Room.add(label);
		JTextField roomNum = new JTextField();
		roomNum.setBounds(68, 294, 48, 21);
		Room.add(roomNum);
		JLabel label_1 = new JLabel("수용 인원");
		label_1.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		label_1.setBounds(135, 298, 57, 15);
		Room.add(label_1);
		JTextField size = new JTextField();
		size.setBounds(204, 294, 34, 21);
		Room.add(size);
		JLabel label_2 = new JLabel("가격");
		label_2.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		label_2.setBounds(250, 298, 34, 15);
		Room.add(label_2);
		JTextField price = new JTextField();
		price.setBounds(286, 294, 63, 21);
		Room.add(price);

		JButton makeRoom = new JButton("생성");
		// 행 추가
		makeRoom.addActionListener(new AddActionListener(allTable, roomNum, size, price));
		// roomArray에 추가
		makeRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int roomSize = Integer.parseInt(size.getText());
				String number = roomNum.getText();
				int charge = Integer.parseInt(price.getText());
				mng.plusRoom(roomSize, number, charge);
				JOptionPane.showMessageDialog(null, number + "호가 생성되었습니다.");
			}
		});

		makeRoom.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		makeRoom.setBounds(49, 343, 77, 23);
		Room.add(makeRoom);

		// 방 삭제 버튼
		JButton deleteRoom = new JButton("삭제");
		deleteRoom.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		deleteRoom.setBounds(250, 343, 77, 23);
		Room.add(deleteRoom);
		// 버튼 클릭했을 때 (테이블에서 삭제)
		deleteRoom.addActionListener(new RemoveActionListener(allTable));
		// roomArray에서 삭제
		deleteRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = allTable.getSelectedRow();
				String number = (String) allRoomData[row][0];
				JOptionPane.showMessageDialog(null, number + "호가 삭제되었습니다.");
				try {
					mng.deleteRoom(number);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		JButton editRoom = new JButton("\uC218\uC815");
		editRoom.addActionListener(new EditActionListener(allTable, roomNum, size, price));
		// roomArray에서 수정
		editRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = allTable.getSelectedRow();
				String selectNum = (String) allRoomData[row][0];
				int roomSize = Integer.parseInt(size.getText());
				String number = roomNum.getText();
				int charge = Integer.parseInt(price.getText());
				Room room = mng.findRoom(selectNum);
				mng.editRoom(room, number, roomSize, charge);
				JOptionPane.showMessageDialog(null, "수정되었습니다.");
			}
		});
		editRoom.setFont(new Font("a시월구일굴림1", Font.PLAIN, 13));
		editRoom.setBounds(152, 343, 77, 23);
		Room.add(editRoom);

		JButton managerMainBtn_2 = new JButton("\uBA54\uC778\uD654\uBA74");

		managerMainBtn_2.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		managerMainBtn_2.setBounds(271, 528, 103, 25);
		Room.add(managerMainBtn_2);

		// 파일 저장 버튼 구현
		JButton saveBtn = new JButton("\uC800\uC7A5");

		saveBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		saveBtn.setBounds(303, 72, 63, 23);
		Room.add(saveBtn);

		// 저장 버튼
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					roomInfoOut = new ObjectOutputStream(new FileOutputStream(roomInfo));
					JOptionPane.showMessageDialog(null, "저장되었습니다.");
					// 방 정보 쓰기
					try {
						mng.writeFile(roomInfoOut);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (FileNotFoundException fnfe) {
					System.out.println("파일을 찾을 수 없습니다.");
				} catch (IOException ioe) {
					System.out.println("파일을 출력할 수 없습니다.");
				} finally {
					try {
						roomInfoOut.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		JButton prevBtn_2 = new JButton("\uC774\uC804");
		prevBtn_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(false);
				Main.setVisible(false);
				ManagerMenu.setVisible(true);
				ManagerID.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				Income.setVisible(false);
			}
		});
		prevBtn_2.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		prevBtn_2.setBounds(13, 529, 103, 25);
		Room.add(prevBtn_2);

		managerMainBtn_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(false);
				Main.setVisible(true);
				ManagerMenu.setVisible(false);
				ManagerID.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				Income.setVisible(false);
			}
		});

		Main.setBounds(0, 0, 386, 563);
		frame.getContentPane().add(Main);
		Main.setLayout(null);

		JButton checkInBtn = new JButton("\uCCB4\uD06C\uC778\r\n");

		checkInBtn.setFont(new Font("a시월구일굴림2", Font.PLAIN, 18));
		checkInBtn.setBounds(105, 222, 169, 43);
		Main.add(checkInBtn);

		JButton managerBtn = new JButton("\uAD00\uB9AC\uC790 \uBAA8\uB4DC");

		managerBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		managerBtn.setBounds(12, 530, 114, 23);
		Main.add(managerBtn);

		JButton mainBtn = new JButton("\uBA54\uC778\uD654\uBA74");
		mainBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 12));
		mainBtn.setBounds(266, 530, 114, 23);
		Main.add(mainBtn);

		JButton checkOutBtn = new JButton("\uCCB4\uD06C\uC544\uC6C3");
		checkOutBtn.setFont(new Font("a시월구일굴림2", Font.PLAIN, 18));
		checkOutBtn.setBounds(105, 282, 169, 43);
		Main.add(checkOutBtn);

		JLabel lblStudyCafe = new JLabel("STUDY CAFE\r\n");
		lblStudyCafe.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudyCafe.setFont(new Font("a시월구일굴림2", Font.BOLD, 30));
		lblStudyCafe.setBounds(83, 104, 212, 50);
		Main.add(lblStudyCafe);

		checkOutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckOut.setVisible(true);
				ManagerMenu.setVisible(false);
				CheckIn.setVisible(false);
				Main.setVisible(false);
				ManagerID.setVisible(false);
				Room.setVisible(false);
				Income.setVisible(false);
			}
		});

		checkInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerMenu.setVisible(false);
				CheckIn.setVisible(true);
				Main.setVisible(false);
				CheckOut.setVisible(false);
				ManagerID.setVisible(false);
				Room.setVisible(false);
				Income.setVisible(false);
			}
		});

		managerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerMenu.setVisible(false);
				Main.setVisible(false);
				ManagerID.setVisible(true);
				CheckIn.setVisible(false);
				Income.setVisible(false);
				CheckOut.setVisible(false);
				Room.setVisible(false);
			}
		});

		ManagerMenu.setBounds(0, 0, 386, 563);
		frame.getContentPane().add(ManagerMenu);
		ManagerMenu.setLayout(null);
		ManagerMenu.setVisible(false);

		JButton roomBtn = new JButton("\uBC29 \uAD00\uB9AC");

		roomBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 16));
		roomBtn.setBounds(117, 220, 151, 36);
		ManagerMenu.add(roomBtn);

		JButton incomBtn = new JButton("\uC218\uC785 \uAD00\uB9AC");

		incomBtn.setFont(new Font("a시월구일굴림1", Font.PLAIN, 16));
		incomBtn.setBounds(117, 276, 151, 36);
		ManagerMenu.add(incomBtn);

		// 매니저 메뉴 선택 메인 버튼
		JButton managerMainBtn_1 = new JButton("\uBA54\uC778\uD654\uBA74");

		managerMainBtn_1.setFont(new Font("a시월구일굴림1", Font.PLAIN, 14));
		managerMainBtn_1.setBounds(271, 528, 103, 25);
		ManagerMenu.add(managerMainBtn_1);

		JLabel managerLabel_1 = new JLabel("\uAD00\uB9AC\uC790 \uBAA8\uB4DC");
		managerLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		managerLabel_1.setFont(new Font("a시월구일굴림2", Font.PLAIN, 23));
		managerLabel_1.setBounds(98, 20, 187, 46);
		ManagerMenu.add(managerLabel_1);

		// 수입 관리 버튼 클릭했을 때
		incomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				ManagerMenu.setVisible(false);
				ManagerID.setVisible(false);
				Income.setVisible(true);
				Main.setVisible(false);
			}
		});

		// 매니저 메뉴 선택 화면에서의 메인 버튼
		managerMainBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.setVisible(true);
				ManagerMenu.setVisible(false);
				ManagerID.setVisible(false);
				CheckIn.setVisible(false);
				CheckOut.setVisible(false);
				Room.setVisible(false);
				Income.setVisible(false);
			}
		});

		// 방 생성 및 삭제 메뉴 클릭 시
		roomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room.setVisible(true);
				Main.setVisible(false);
				ManagerMenu.setVisible(false);
				ManagerID.setVisible(false);
				CheckIn.setVisible(false);
				Income.setVisible(false);
				CheckOut.setVisible(false);
			}
		});
	}
}
