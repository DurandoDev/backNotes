package com.medilabosolutions.backnotes.controller;

import com.medilabosolutions.backnotes.model.Note;
import com.medilabosolutions.backnotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteRepository noteRepository;

	// Récupérer toutes les notes
	@GetMapping
	public List<Note> getAllNotes() {
		return noteRepository.findAll();
	}

	// Récupérer une note par son ID
	@GetMapping("/{patId}")
	public List<Note> getNotesByPatientId(@PathVariable String patId) {
		List<Note> notes = noteRepository.findByPatId(patId);
		if(notes.isEmpty()) {
			throw new RuntimeException("No notes found for patient with id " + patId);
		}
		return notes;
	}

	//Récupérer une note par le nom du patient
	@GetMapping("/byPatientName/{patientName}")
	public List<Note> findByPatient(@PathVariable String patientName) {
		return noteRepository.findByPatient(patientName);
	}

	// Ajouter une nouvelle note
	@PostMapping
	public ResponseEntity<Note> addNote(@RequestBody Note newNote) {
		Note note = noteRepository.save(newNote);
		if (note == null) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(note.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}


	// Mettre à jour une note
	@PutMapping("/{id}")
	public Note updateNote(@PathVariable String id, @RequestBody Note updatedNote) {
		return noteRepository.findById(id)
				.map(note -> {
					 note.setPatient(updatedNote.getPatient());
					 note.setNote(updatedNote.getNote());
					return noteRepository.save(note);
				})
				.orElseGet(() -> {
					updatedNote.setPatId(id);
					return noteRepository.save(updatedNote);
				});
	}

	// Supprimer une note
	@DeleteMapping("/{id}")
	public void deleteNote(@PathVariable String id) {
		noteRepository.deleteById(id);
	}
}
