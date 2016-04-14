DELETE FROM workflow_task WHERE id_task=303;
INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order) 
	VALUES 	(303,'taskTicketingCreateCustomer',301,3);
			