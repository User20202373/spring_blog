package com.tenco.blog.board;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //IoC +DI
@RequiredArgsConstructor // 생성자 쓰기 귀찮다면 자동
//  생성해주는 @RequiredArgsConstructor사용

public class BoardNativeRepository {
    // EntityManager: JPA 핵심 인터페이스
    // 데이터베이스와 모든 작업을 담당
    private final EntityManager em;

    //DI - 생성자 의존 주입
//    public BoardNativeRepository(EntityManager em) {
//        this.em = em;
//    }

    //트랜잭션 처리
    @Transactional
    public void save(String title, String content, String username) {
        Query query = em.createNativeQuery("insert " +
                "into board_tb(title,content,username,created_at) values (?, ?, ?, now())");

        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, username);

        query.executeUpdate();

    }

    // 게시글 목록 조회 메서드
    public List<Board> findAll() {
        String sql = """
                select * from board_tb order by id desc
                """;
        //rs.getString("title") ---> new Board() doard.title ="rs.getString("title")
        //while(rs.next) {Board board = new Board(); board.setTitle(rs.getString("title"))}
        Query query = em.createNativeQuery(sql, Board.class);
        return query.getResultList();
    }


    // 게시글 상세 보기 (특정 ID로 조회)
    public Board findById(Integer id) {
        String strQuery = """
                SELECT * FROM BOARD_TB where id = ?
                """;
        try {
            Query query = em.createNativeQuery(strQuery, Board.class);
            query.setParameter(1, id);
            return (Board) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    //특정 게시글 삭제 요청
    @Transactional
    public void deleteById(Integer id) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }

    //게시글 수정하기

    @Transactional
    public boolean updateById(String username, String title, String content, Integer id) {
        String queryStr = """
                update board_tb set username = ? ,title = ?, content = ? where id = ?
                """;
        Query query = em.createNativeQuery(queryStr);
        query.setParameter(1, username);
        query.setParameter(2, title);
        query.setParameter(3, content);
        query.setParameter(4, id);
        int rows = query.executeUpdate();
        if (rows > 0){
            return true;
        }else {
            return false;
        }
    }
}
