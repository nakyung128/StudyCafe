package StudyCafe;

public class User implements java.io.Serializable {
	private String name; // ������� �̸� �ʵ�
	private String phoneNum; // ������� �޴��� ��ȣ �ʵ�
	
	User() {
		
	}
	
	User (String name, String phoneNum) { // ����� ������
		this.name = name;
		this.phoneNum = phoneNum;
	}
	void setName(String name) { // �̸��� ���ϴ� �޼ҵ�
		this.name = name;
	}
	String getName() { // �̸��� ��ȯ�ϴ� �޼ҵ�
		return name;
	}
	void setPhoneNum(String phoneNum) { // �޴��� ��ȣ�� �����ϴ� �޼ҵ�
		this.phoneNum = phoneNum;
	}
	String getPhoneNum() { // �޴��� ��ȣ�� �����ϴ� �޼ҵ�
		return phoneNum;
	}
}
