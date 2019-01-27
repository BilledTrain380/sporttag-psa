# Manage Participants

Participants can be be managed with several operations, but first of all
you need to understand how a data set for a participant is composited.

A participant consists of three different parts:
* The participant data itself (last name, first name, gender, etc.)
* A group where the participant belongs to.
* A group coach which is responsible for the group.

Each group must be unique and each group coach belongs to exactly one group.

## Create a group

In order to create a group with a group coach and its participants, you have
to import a CSV file with a specific format.

![import]({{ site.baseurl }}/img/user-guide/group-import.png){: .img-fluid}

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) Currently a group can only be created with the CSV import.

## Add a participant after the import

Usually all your participants should be added with the import. But maybe
you have to add a participant after that.

You can just add a new participant in the detail view of a group.

![add participant]({{ site.baseurl }}/img/user-guide/add-participant.png){: .img-fluid}

This action will open a dialog to enter a new participant that belongs to the current group.

## Edit a participant

Maybe some data of a participant are wrong. You can just edit them any time.

TODO: Insert img which shows edit participant

This action will open a dialog to edit the participant data (like first name, last name, etc.).

## Delete a participant

Maybe you have a wrong record of a participant and you have to delete it.

TODO: Insert img which shows delete participant

## Select a sport for a participant

When you import your groups, all your new participants will not have any kind of sport,
where they participate.

Simple select a kind of sport for each participant.

TODO: Isert img which shows select sport

## Mark a participant as absent

Maybe a participant is sick and can not be present during the sport event.

Simple mark them as absent.

TODO: Insert img which shows absent participant

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) You can mark a participant as absent at any time. Any absent marked participant will not be visible in any kind of ranking.
