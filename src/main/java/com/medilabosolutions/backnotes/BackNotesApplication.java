package com.medilabosolutions.backnotes;

import com.medilabosolutions.backnotes.model.Note;
import com.medilabosolutions.backnotes.repository.NoteRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@SpringBootApplication
public class BackNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackNotesApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(NoteRepository repository, MongoTemplate mongoTemplate) {
		return args -> {
			String path = "src/main/resources/notes.csv";
			try (BufferedReader br = new BufferedReader(new FileReader(path))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] fields = line.split(";");
					if (fields.length > 2 && !line.contains("patId")) {
						String noteContent = fields[2];
						if (!repository.existsByNote(noteContent)) {
							Note note = new Note();
							note.setPatId(fields[0]);
							note.setPatient(fields[1]);
							note.setNote(noteContent);
							repository.save(note);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}
}

