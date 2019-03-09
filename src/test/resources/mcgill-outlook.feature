Feature: Outlook

	#basic flow
	Scenario Outline: Sending an email with an attachment
		Given a user with username <username> and password <password> has an email draft open in the McGill Outlook Email page
		And image at <imagePath> is attached
		When they send an email to <address>
		Then an email with recipient address <address> with an attachment will appear in the "sent" section
		
	Examples:
		| 			username 			| password  | address 			 						 | imagePath 											|
		| ecse428@hotmail.com | 1234Test  | ecse428receive@hotmail.com | /SEP-Assignment-B/src/test/resources/cat.jpg |
		
  #Error flow
	Scenario Outline: Adding in an image that exceeds the size limit
		Given a user with username <username> and password <password> has an email draft open in the McGill Outlook Email page
		And a large image at <imagePath> is attached
		When they send an email to <address>
		Then outlook throws a size error




