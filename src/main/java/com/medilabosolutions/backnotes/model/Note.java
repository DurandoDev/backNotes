package com.medilabosolutions.backnotes.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
@Data
public class Note {

	private String patId;

	private String patient;

	private String note;
}
