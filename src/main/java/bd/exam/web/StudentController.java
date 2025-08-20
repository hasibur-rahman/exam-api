package bd.exam.web;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bd.exam.model.StudentResult;
import bd.exam.repo.StudentRepo;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepo repo;
    public StudentController(StudentRepo repo) { this.repo = repo; }

    // WRITE endpoints
    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentResult s) {
        repo.upsert(s);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roll}")
    public ResponseEntity<?> update(@PathVariable("roll") long roll, @RequestBody StudentResult s) {
        s.setRollNumber(roll);
        repo.upsert(s);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roll}")
    public ResponseEntity<?> delete(@PathVariable("roll") long roll) {
        return repo.delete(roll) > 0 ? ResponseEntity.noContent().build()
                                     : ResponseEntity.notFound().build();
    }

    // READ endpoints
    @GetMapping("/{roll}")
    public ResponseEntity<StudentResult> get(@PathVariable("roll") long roll) {
        return repo.findByRoll(roll).map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<StudentResult> listByYear(@RequestParam("year") int year,
                                          @RequestParam(value = "limit", defaultValue = "100") int limit,
                                          @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return repo.findByYear(year, Math.min(limit, 1000), offset);
    }
}
