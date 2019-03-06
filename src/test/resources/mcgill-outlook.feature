Scenario: Adding the image as an attachment to the email and sending it
Given I am on the McGill Outlook Email page
And the email contains all the correct credentials
And the email has an image attached
When I click “send”
Then the email should be sent with the photo


Scenario: Adding in an image and attempting to send it when the image exceeds the size limit
Given I am on the McGill Outlook Email page
And the email contains all the correct credentials
And the email has an image attached
When I click “send”
Then the email should be sent with the photo
