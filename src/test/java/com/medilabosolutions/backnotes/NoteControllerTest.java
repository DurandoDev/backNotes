package com.medilabosolutions.backnotes;

import com.medilabosolutions.backnotes.model.Note;
import com.medilabosolutions.backnotes.repository.NoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteControllerTest {

	@Autowired
	NoteRepository noteRepository;

	@Autowired
	MockMvc mockMvc;

	Note noteTest;

	String noteId;

	private List<String> createdNoteIds = new ArrayList<>();

	@BeforeEach
	public void setup() throws Exception {
		noteTest = new Note();
		noteTest.setNote("testNote");
		noteTest.setPatient("testPatient");
		noteTest.setPatId("testId");

		MvcResult result = mockMvc.perform(post("/notes")
				.content("{\"patId\":\"" + noteTest.getPatId() + "\"," +
						"\"patient\":\"" + noteTest.getPatient() + "\"," +
						"\"note\":\"" + noteTest.getNote() + "\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		String location = result.getResponse().getHeader("Location");
		noteId = location.substring(location.lastIndexOf('/') + 1);
		createdNoteIds.add(noteId);
	}

	@AfterEach
	public void deleteSetup() {
		List<Note> notesToDelete = noteRepository.findByPatId(noteTest.getPatId());
		for (Note note : notesToDelete) {
			noteRepository.deleteById(note.getId());
		}
	}

	@AfterAll
	public void cleanUp() {
		// Supprimer toutes les notes créées pendant les tests
		for (String id : createdNoteIds) {
			noteRepository.deleteById(id);
		}
	}

	@Test
	public void getAllNotesTest() throws Exception {
		mockMvc.perform(get("/notes")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[*].note", hasItem("testNote")));
	}

	@Test
	public void addNoteTest() throws Exception {
		Note newNote = new Note();
		newNote.setPatId("newTestId");
		newNote.setPatient("newTestPatient");
		newNote.setNote("newTestNote");

		String newNoteJson = "{\"patId\":\"" + newNote.getPatId() + "\"," +
				"\"patient\":\"" + newNote.getPatient() + "\"," +
				"\"note\":\"" + newNote.getNote() + "\"}";

		MvcResult result = mockMvc.perform(post("/notes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newNoteJson))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(header().string("Location", containsString("/notes/")))
				.andReturn();

		String location = result.getResponse().getHeader("Location");
		String createdNoteId = location.substring(location.lastIndexOf('/') + 1);
		this.createdNoteIds.add(createdNoteId);
	}


	@Test
	public void deleteNoteTest() throws Exception {
		Note newNote = new Note();
		newNote.setPatId("testIdForDelete");
		newNote.setPatient("testPatientForDelete");
		newNote.setNote("This is a test note for deletion");

		Note savedNote = noteRepository.save(newNote);
		String idToDelete = savedNote.getId();

		mockMvc.perform(delete("/notes/{id}", idToDelete))
				.andExpect(status().isOk());
	}

	@Test
	public void updateNoteTest() throws Exception {
		mockMvc.perform(put("/notes/{id}",noteId)
						.content("{\"patId\":\"patIdUpdated\"," +
								"\"patient\":\"patientUpdated\"," +
								"\"note\":\"noteUpdated\"}")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
