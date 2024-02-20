package com.medilabosolutions.backnotes.repository;

import com.medilabosolutions.backnotes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
	public List<Note> findByPatient(String patient);
	List<Note> findByPatId(String patId);
}
