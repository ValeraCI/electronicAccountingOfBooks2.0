/*package com.company.DAO;

import com.company.Models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper bookMapper = new RowMapper<Book>(){

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setAuthor(rs.getString("author"));
            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setYear(rs.getInt("year"));
            book.setFree(0 == rs.getInt("person_id"));
            book.setPersonId(rs.getInt("person_id"));
            return book;
        }
    };


    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAny(){
        return jdbcTemplate.query("SELECT * FROM book", bookMapper);
    }

    public List<Book> getFree(){
        return getAny()
                .stream()
                .filter(book -> book.isFree())
                .collect(Collectors.toList());
    }

    public List<Book> getBusy(){
        return getAny()
                .stream()
                .filter(book -> !book.isFree())
                .collect(Collectors.toList());
    }

    public Book getForBookID(int id){
        return (Book) jdbcTemplate.query("SELECT * FROM book WHERE id = ?",new Object[]{id},
                bookMapper).stream().findAny().orElse(null);
    }

    public List<Book> getForPersonID(int id){
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id = ?",  new Object[]{id},
                bookMapper);
    }

    public void add(Book book){
        jdbcTemplate.update("INSERT INTO Book(title, author,  year) values(?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void edit(Book book, int id){
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?",
                book.getTitle(), book.getAuthor(), book.getYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }

    public void take(int id, int person_id){
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", person_id, id);
    }

    public void getBack(int id){
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", null, id);
    }
}
 */
