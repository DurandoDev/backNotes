package com.medilabosolutions.backnotes;

import com.medilabosolutions.backnotes.model.Note;
import com.medilabosolutions.backnotes.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class BackNotesApplication {

	private final Logger logger = LoggerFactory.getLogger(BackNotesApplication.class);

	@Autowired
	private NoteRepository noteRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackNotesApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		List<Note> result = noteRepository.findByPatient("TestInDanger");
//		result.stream().forEach((note -> logger.info(note.getNote())));
//	}
}
