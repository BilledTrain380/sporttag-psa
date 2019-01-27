# User Guide

Welcome to PSA User Guide. You'll find here pretty much everything that can be
done by PSA.

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) Some sort of extra information or tips and tricks.

![arrow]({{ site.baseurl }}/img/user-guide/arrow-graphic.png) Points to a referencing action in PSA (they only appear in images).

![warning]({{ site.baseurl }}/img/user-guide/warning-graphic.png) Warns for some sort of special cases

## Manage Participants

Participants can be be managed with several operations, but first of all
you need to understand how a data set for a participant is composited.

A participant consists of three different parts:
* The participant data itself (last name, first name, gender, etc.)
* A group where the participant belongs to.
* A group coach which is responsible for the group.

Each group must be unique and each group coach belongs to exactly one group.

### Create a group

In order to create a group with a group coach and its participants, you have
to import a CSV file with a specific format.

![import]({{ site.baseurl }}/img/user-guide/group-import.png){: .img-fluid}

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) Currently a group can only be created with the CSV import.

### Add a participant after the import

Usually all your participants should be added with the import. But maybe
you have to add a participant after that.

You can just add a new participant in the detail view of a group.

![add participant]({{ site.baseurl }}/img/user-guide/add-participant.png){: .img-fluid}

This action will open a dialog to enter a new participant that belongs to the current group.

### Edit a participant

Maybe some data of a participant are wrong. You can just edit them any time.

TODO: Insert img which shows edit participant

This action will open a dialog to edit the participant data (like first name, last name, etc.).

### Delete a participant

Maybe you have a wrong record of a participant and you have to delete it.

TODO: Insert img which shows delete participant

### Select a sport for a participant

When you import your groups, all your new participants will not have any kind of sport,
where they participate.

Simple select a kind of sport for each participant.

TODO: Isert img which shows select sport

### Mark a participant as absent

Maybe a participant is sick and can not be present during the sport event.

Simple mark them as absent.

TODO: Insert img which shows absent participant

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) You can mark a participant as absent at any time. Any absent marked participant will not be visible in any kind of ranking.

## Manage the competition

Once you selected a kind of sport for each participant. You're ready to
start the competition.

![note]({{ site.baseurl }}/img/user-guide/note-graphic.png) Every participant which participates in the sport 'Athletics' will become a competitor for the competition

First of all you need to close the participation.

![close participation]({{ site.baseurl }}/img/user-guide/close-participation.png){: .img-fluid}

This action will create a start number for each competitor.

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) In case you forgot to select a kind of sport for a participant, this overview will tell you which group(s) they belong to.

![warning]({{ site.baseurl }}/img/user-guide/warning-graphic.png) Do only close the participation if you're sure every participant has select the correct kind of sport.
This action will disable the sport selection in the group detail page

### Re-participate after the participation is closed

Once you close the participation, the sport selection in the group detail page will be disabled.
However, you still can change the kind of sport with a special action.

![re-participate]({{ site.baseurl }}/img/user-guide/re-participate.png){: .img-fluid}

This action will enable the sport selection for exactly the selected participant.

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) You can add a new participant at any time. It does not matter if the participation is open or already closed.

### Reset the sport event

PSA is design to only manage one sport event at the same time. If you want to make a new sport event
you have to reset the sport event as a whole. That means that all participant data
will be lost.

![reset-participation]({{ site.baseurl }}/img/user-guide/reset-participation.png){: .img-fluid}

![note]({{ site.baseurl }}/img/user-guide/note-graphic.jpg) PSA can not store more than one sport event at the same time.

![warning]({{ site.baseurl }}/img/user-guide/warning-graphic.png) All participant data will be lost in the process.

## Record competitor results

Once the participation is finished




