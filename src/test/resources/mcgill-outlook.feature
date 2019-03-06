Feature: Outlook

	#basic flow
	Scenario: Adding the image as an attachment to the email and sending it
		Given that I have a new email draft open on the McGill Outlook Email page
		And I fill out the necessary email recipient credentials
		When I click attachments
		And attach an image
		Then the email can be sent along with the image

  	#Error flow
	Scenario: Adding in an image that exceeds the size limit
		Given that I have a new email draft open on the McGill Outlook Email page
		And I fill out the necessary email recipient credentials
		When I click attachments
		And attach a large image
		Then outlook provides me with an image size error
		And I cannot attach the photo to send
