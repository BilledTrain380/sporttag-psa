# Manage Participants

Participants can be be managed with several operations, but first of all
you need to understand how a data set for a participant is composited.

A participant consists of three different parts:
* The participant data itself (last name, first name, gender, etc.)
* A group where the participant belongs to.
* A group coach which is responsible for the group.

Each group must be unique and each group coach belongs to exactly one group.

## Create groups

In order to create a group with a group coach and its participants, you have
to import a CSV file with a specific format.

![import]({{ site.baseurl_root }}/img/user-guide/participants/group-import.png){: .img-fluid}

![note]({{ site.baseurl_root }}/img/user-guide/note-graphic.png) Currently a group can only be created with the CSV import.

## Add a participant after the import

Usually all your participants should be added with the import. But maybe
you have to add a participant after that.

You can just add a new participant in the detail view of a group.

![add participant]({{ site.baseurl_root }}/img/user-guide/participants/add-participant.png){: .img-fluid}

This action will open a dialog to enter a new participant that belongs to the current group.

## Edit a participant

Maybe some data of a participant are wrong. You can just edit them any time.

![edit participant]({{ site.baseurl_root }}/img/user-guide/participants/edit-participant.png){: .img-fluid}

This action will open a dialog to edit the participant data (like first name, last name, etc.).

## Delete a participant

Maybe you have a wrong record of a participant and you have to delete it.

![delete participant]({{ site.baseurl_root }}/img/user-guide/participants/delete-participant.png){: .img-fluid}

## Select a sport for a participant

When you import your groups, all your new participants will not have any kind of sport,
where they participate.

Simple select a kind of sport for each participant.

![select sport]({{ site.baseurl_root }}/img/user-guide/participants/select-sport.png){: .img-fluid}

## Mark a participant as absent

Maybe a participant is sick and can not be present during the sport event.

Simple mark them as absent.

![mark absent]({{ site.baseurl_root }}/img/user-guide/participants/mark-absent.png){: .img-fluid}

![note]({{ site.baseurl_root }}/img/user-guide/note-graphic.png) You can mark a participant as absent at any time. Any absent marked participant will not be visible in any kind of ranking.
