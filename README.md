# GPSTasks

## List of Regrets (Bugs & Incomplete Features)

* You can click on an existing task and it will reload the creation activity with the existing steps. This allows you to edit the task and save it again. However, a bug exists where the response is not properly sent back to the Main Activity. Instead, it gets sent back to the conext of the viewholder. Although the code exists in main to update the task, it will never run due to no response.
* Preview of a step is incomplete
* Deletion of a step was not implemented
* Task toggling has no effect as it doesn't restart the service
* Service won't stop until you kill the app
