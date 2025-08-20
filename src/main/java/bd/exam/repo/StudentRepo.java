package bd.exam.repo;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import bd.exam.model.StudentResult;

@Repository
public class StudentRepo {

    private final JdbcTemplate readJdbc;
    private final JdbcTemplate writeJdbc;

    public StudentRepo(@Qualifier("readJdbc") JdbcTemplate readJdbc,
                       @Qualifier("writeJdbc") JdbcTemplate writeJdbc) {
        this.readJdbc = readJdbc;
        this.writeJdbc = writeJdbc;
    }

    private StudentResult map(ResultSet rs, int row) throws java.sql.SQLException {
        StudentResult s = new StudentResult();
        s.setRollNumber(rs.getLong("roll_number"));
        s.setMarks(rs.getInt("marks"));
        s.setExamYear(rs.getInt("exam_year"));
        return s;
    }

    // WRITE
    public int upsert(StudentResult s) {
        return writeJdbc.update(
            "INSERT INTO student_results (roll_number, marks, exam_year) " +
            "VALUES (?, ?, ?) " +
            "ON CONFLICT (roll_number) DO UPDATE " +
            "SET marks = EXCLUDED.marks, exam_year = EXCLUDED.exam_year",
            s.getRollNumber(), s.getMarks(), s.getExamYear()
        );
    }

    public int delete(long roll) {
        return writeJdbc.update("DELETE FROM student_results WHERE roll_number = ?", roll);
    }

    // READ
    public Optional<StudentResult> findByRoll(long roll) {
        List<StudentResult> list = readJdbc.query(
            "SELECT roll_number, marks, exam_year FROM student_results WHERE roll_number = ?",
            this::map, roll);
        return list.stream().findFirst();
    }

    public List<StudentResult> findByYear(int year, int limit, int offset) {
        return readJdbc.query(
            "SELECT roll_number, marks, exam_year FROM student_results " +
            "WHERE exam_year = ? ORDER BY roll_number LIMIT ? OFFSET ?",
            this::map, year, limit, offset
        );
    }
}
