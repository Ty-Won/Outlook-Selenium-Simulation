Feature: Outlook

	#basic flow
	Scenario: Sending an email with an attachment
		Given a valid user with an email draft open in the McGill Outlook Email page
		When they send an email to <address>
		And an <image> is attached
		Then the email can be viewed in the sent section to <address> with <image> attached

  	#Error flow
	Scenario: Adding in an image that exceeds the size limit
		Given a valid user with an email draft open in the McGill Outlook Email page
		When they send an email to <address>
		And an <image> is attached
		Then outlook throws a size error for <image>


