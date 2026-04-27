package com.tenco.blog.board;

import lombok.Builder;
import lombok.Data;

// 요청 데이터를 담는 DTO 클래스
//컨트롤러.. 비즈니스 .. 데이터 계층 사이에서 데이터 전송 역할 객체
public class BoardRequest {
    @Data
    @Builder
    public static class SaveDTO {
        private String username;
        private String title;
        private String content;

        // 편의 기능 설계 가능
        //DTO에서 Entity로 변환해주는 편의 메서드
        public Board toEntity() {
            return Board.builder()
                    .username(username)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    // 내부 정적 클래스 게시글 수정 DTO 설계
    @Data
    public static class UpdateDTO {
        private String username;
        private String title;
        private String content;

//        public Board toEntity() {
//            return Board.builder()
//                    .username(username)
//                    .title(title)
//                    .content(content)
//                    .build();
//        }

        //게시글 수정시 유효성 검사 편의 메서드
        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("작성자 이름은필수입니다");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 필수입니다");
            }
            if (content == null || content.length() < 3) {
                throw new IllegalArgumentException("내용은 3글자 이상 작성해야합니다");
            }
        }
    }

}
