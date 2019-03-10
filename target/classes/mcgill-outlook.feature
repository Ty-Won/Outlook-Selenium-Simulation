Feature: Outlook

	#basic flow
	Scenario Outline: Sending an email with an attachment
		Given a user with username <username> and password <password> has an email draft open in the McGill Outlook Email page
		And image at <imagePath> is attached
		When they send an email to <address> with subject <subject>
		Then an email with an attachment sent to recipient address <address> with subject <subject> will appear in the "sent" section
		
	Examples:
		| 			username 			| password  | address |  subject		 						 | imagePath 											|
		| ecse428@hotmail.com | 1234Test  | ecse428receive@hotmail.com |  testSubjectSent428  |  src/test/ressources/cat.jpg |
		
  	#Error flow
	Scenario Outline: Adding in an image that exceeds the size limit
		Given a user with username <username> and password <password> has an email draft open in the McGill Outlook Email page
		And a large image at <imagePath> is attached
		When they send an email to <address> with subject <subject>
		Then outlook throws a size error
	Examples:
		| 			username 			| password  | address |  subject		 						 | imagePath 											|
		| ecse428@hotmail.com | 1234Test  | ecse428receive@hotmail.com |  testSubjectFailureSent428  | src/test/ressources/failurePhoto.jpg |



