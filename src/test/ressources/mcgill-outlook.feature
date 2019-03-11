Feature: Outlook

	#basic flow
	Scenario Outline: Creating a new email and sending it with an attachment
		Given a user with username <username> and password <password> has a new email open in the Outlook Email page
		And image at <imagePath> is attached
		When they send an email to <address> with subject <subject>
		Then an email with an attachment sent to recipient address <address> with subject <subject> will appear in the "sent" section

		Examples:
			| username            | password | address                    | subject            | imagePath                   |
			| ecse428@hotmail.com | 1234Test | ecse428receive@hotmail.com | testSubjectSent4281 | src/test/ressources/SuccessImages/cat.jpg |
			| ecse428@hotmail.com | 1234Test | alexander.lam@mail.mcgill.ca | testSubjectSent4282 | src/test/ressources/SuccessImages/apple.jpg |
			| ecse428@hotmail.com | 1234Test | tyrone.wong@mail.mcgill.ca | testSubjectSent4283 | src/test/ressources/SuccessImages/checkmark.jpg |
			| ecse428@hotmail.com | 1234Test | ecse428receive@hotmail.com | testSubjectSent4284 | src/test/ressources/SuccessImages/owl.jpg |
			| ecse428@hotmail.com | 1234Test | ecse428receive@hotmail.com | testSubjectSent4285 | src/test/ressources/SuccessImages/rabbit.jpg |


	#Alternative flow
	Scenario Outline: Continuing an existing email draft and sending it with an attachment
		Given a user with username <username> and password <password> has an existing email draft with image <imagePath>
		When they send an email to <address> with subject <subject>
		Then an email with an attachment sent to recipient address <address> with subject <subject> will appear in the "sent" section
		Examples:
			| username            | password | address                    | subject                 | imagePath                   |
			| ecse428@hotmail.com | 1234Test | ecse428receive@hotmail.com | testSubjectSent4281 | src/test/ressources/SuccessImages/cat.jpg |
			| ecse428@hotmail.com | 1234Test | alexander.lam@mail.mcgill.ca | testSubjectSent4282 | src/test/ressources/SuccessImages/apple.jpg |
			| ecse428@hotmail.com | 1234Test | tyrone.wong@mail.mcgill.ca | testSubjectSent4283 | src/test/ressources/SuccessImages/checkmark.jpg |
			| ecse428@hotmail.com | 1234Test | ecse428receive@hotmail.com | testSubjectSent4284 | src/test/ressources/SuccessImages/owl.jpg |
			| ecse428@hotmail.com | 1234Test | ecse428receive@hotmail.com | testSubjectSent4285 | src/test/ressources/SuccessImages/rabbit.jpg |
	#Error flow
	Scenario Outline: Adding in an image that exceeds the size limit
		Given a user with username <username> and password <password> has a new email open in the Outlook Email page
		When user attempts to attach a large image from <imagePath>
		Then outlook throws a size error


		Examples:
			| username            | password | imagePath                            |
			| ecse428@hotmail.com | 1234Test | src/test/ressources/FailureImages/sky.jpg |
			| ecse428@hotmail.com | 1234Test | src/test/ressources/FailureImages/rocks.jpg |
			| ecse428@hotmail.com | 1234Test | src/test/ressources/FailureImages/sky.jpg |
			| ecse428@hotmail.com | 1234Test | src/test/ressources/FailureImages/rocks.jpg |
			| ecse428@hotmail.com | 1234Test | src/test/ressources/FailureImages/sky.jpg |


