package com.youngkeun.realEstate;

import java.util.Scanner;

import com.youngkeun.realEstate.controller.AuthController;
import com.youngkeun.realEstate.controller.implement.AuthControllerImplement;
import com.youngkeun.realEstate.dto.reponse.CheckIdResponseDto;
import com.youngkeun.realEstate.dto.reponse.SendCodeResponseDto;
import com.youngkeun.realEstate.dto.request.CheckIdRequestDto;
import com.youngkeun.realEstate.dto.request.SendCodeRequestDto;
import com.youngkeun.realEstate.interfaces.Code;
import com.youngkeun.realEstate.repository.EmailAuthenticationRepository;
import com.youngkeun.realEstate.repository.UserRepository;
import com.youngkeun.realEstate.repository.implement.EmailAuthenticationRepositoryImplement;
import com.youngkeun.realEstate.repository.implement.UserRepositoryImplement;
import com.youngkeun.realEstate.service.AuthService;
import com.youngkeun.realEstate.service.implement.AuthServiceImplement;

// Controller (package / interface) : 입력과 출력을 담당하는 요소
// Service (package / interface) : 실제 비지니스 로직 (연산)
// Repository (package / interface) : 데이터베이스 연결과 관련된 작업

// DTO (Data Transfer Object) : 데이터 전송을 위한 객체
// Entity : 데이터베이스에 있는 테이블을 자바 클래스로 표현한 객체

public class App {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		UserRepository userRepository = new UserRepositoryImplement();
		EmailAuthenticationRepository emailAuthenticationRepository = new EmailAuthenticationRepositoryImplement();
		AuthService authService = new AuthServiceImplement(userRepository, emailAuthenticationRepository);
		AuthController authcontroller = new AuthControllerImplement(authService);
		
		while(true) {
			System.out.print("1. 회원가입 / 2. 로그인 / 3. 게시물 작성 / 4. 게시물 보기 / 5. 종료 : ");
			String input = scanner.nextLine();
			
			if (input.equals("1")) {
//				1. 아이디 중복 확인
				CheckIdRequestDto CheckIdrequest = new CheckIdRequestDto();
				System.out.print("아이디 : ");
				String id = scanner.nextLine();
				CheckIdrequest.setId(id);
				
				CheckIdResponseDto checkIdResponseDto = authcontroller.checkId(CheckIdrequest);
				Code code = checkIdResponseDto.getCode();
				
				if (code == Code.DI) {
					System.out.println("중복된 아이디입니다.");
					continue;
				}
				
				if (code == Code.DBE) {
					System.out.println("데이터베이스 에러입니다.");
					continue;
				}
				
				if (code == Code.VF) {
					System.out.println("잘못된 입력입니다.");
					continue;
				}
				
				System.out.println("사용 가능한 아이디입니다.");
				
				System.out.println("비밀번호 : ");
				String password = scanner.nextLine();
				System.out.println("비빌번호 확인 : ");
				String passwordCheck = scanner.nextLine();
//				2. 이메일 인증코드
				System.out.println("이메일 : ");
				String eamil = scanner.nextLine();
				
				SendCodeRequestDto sendCodeRequest = new SendCodeRequestDto();
				sendCodeRequest.setEmail(eamil);
				
				
				SendCodeResponseDto sendCodeResponse = authcontroller.sendCode(sendCodeRequest);
				code = sendCodeResponse.getCode();
				
				if (code == Code.VF) {
					System.out.println("잘못된 입력입니다.");
					continue;
				}
				
				if (code == Code.DE) {
					System.out.println("중복된 이메일 입니다.");
					continue;
				}
				
				if (code == Code.DBE) {
					System.out.println("데이터베이스 에러입니다.");
					continue;
				}
				
				System.out.println(sendCodeResponse.getData());
					
					
				
//				3. 이메일 인증 처리
//				4. 회원가입 처리
			}
			if (input.equals("2")) System.out.println("로그인");
			if (input.equals("3")) System.out.println("게시물 작성");
			if (input.equals("4")) System.out.println("게시물 보기");
			if (input.equals("5")) break;

			 
		}
		
		System.out.println("프로그램 종료");
		


		
	}

}
