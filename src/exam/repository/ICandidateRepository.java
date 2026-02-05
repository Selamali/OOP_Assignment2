package exam.repository;

import exam.model.Candidate;
import java.util.List;

public interface ICandidateRepository {
    void addCandidate(Candidate candidate);
    List<Candidate> getAllCandidates();
    Candidate getCandidateByName(String name);
    void updateCandidate(String name, Candidate updatedCandidate);

    // НОВЫЙ МЕТОД
    void deleteCandidate(String name);
}