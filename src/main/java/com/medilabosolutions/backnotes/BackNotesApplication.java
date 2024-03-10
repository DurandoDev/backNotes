package com.medilabosolutions.backnotes;

import com.medilabosolutions.backnotes.model.Note;
import com.medilabosolutions.backnotes.repository.NoteRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;


@SpringBootApplication
public class BackNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackNotesApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(NoteRepository repository) {
		return args -> {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("notes.csv");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
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

