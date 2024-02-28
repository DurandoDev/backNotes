package com.medilabosolutions.backnotes.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
@Data
public class Note {

	@Id
	private String id;

	private String patId;

	private String patient;

	private String note;
}
