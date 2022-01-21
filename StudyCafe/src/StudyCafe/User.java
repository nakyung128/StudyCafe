package StudyCafe;

public class User implements java.io.Serializable {
	private String name; // 사용자의 이름 필드
	private String phoneNum; // 사용자의 휴대폰 번호 필드
	
	User() {
		
	}
	
	User (String name, String phoneNum) { // 사용자 생성자
		this.name = name;
		this.phoneNum = phoneNum;
	}
	void setName(String name) { // 이름을 정하는 메소드
		this.name = name;
	}
	String getName() { // 이름을 반환하는 메소드
		return name;
	}
	void setPhoneNum(String phoneNum) { // 휴대폰 번호를 지정하는 메소드
		this.phoneNum = phoneNum;
	}
	String getPhoneNum() { // 휴대폰 번호를 리턴하는 메소드
		return phoneNum;
	}
}
