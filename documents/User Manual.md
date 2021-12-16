# User manual

## Introduction

This is a brief user manual for the Bookcase application, which was a mandatory miniproject during the Software Engineering course at Helsinki University. It is intended for personal use to store reading tips (books, blogs, podcasts, etc) that relate to hobbies or studies. The user is able to add/remove, edit and search reading tips. The application is written in Java and it uses a SQlite database. The GUI is implemented using JavaFX.

## Installation

Please refer to the instructions in the [README.md](https://github.com/nothros/ohtu-miniprojekti2021/blob/main/README.md).

## Using the application

The application has four screen parts as seen below. The main display, see figure 1, lists five columns (title, author, type, tags, courses) of all reading tip items in the database in a table view. The sixth column contains a **"show"** button that gives further detailed information of the item in that row, as seen in figure 3. Above the table view there are two comboboxes. The top combobox lists the different types of library items (books, blogs and podcasts) available with an **"Add"** button on its right. The user can select an item type and press the **"Add"** button to proceed to a textfield form, see figure 4, where they can add items to their reading tips. The lower combobox enables the user to select a column (title, author, type) and search the table view for items with a keyword that is entered into the textfield.

<p align="center">
  <img width="600" src="https://github.com/Ozath/dummy/blob/main/img/1.PNG" alt="Bookcase">
</p>
<p align="center"> <strong>Fig.1 - main display</strong></p>

&nbsp;

In figure 2 the user has entered the word **space** into the search textfield. This displays all reading tip items with a matching title. 
<p align="center">
  <img width="400" src="https://github.com/Ozath/dummy/blob/main/img/5.PNG" alt="Bookcase">
</p>
<p align="center"> <strong>Fig.2 - search bar in main display</strong></p>

&nbsp;

The detailed info display lists all the data columns that pertain to a reading tip item. These fields vary slightly depending on the type of item. For example, only books have a unique ISBN number field while blogposts and podcasts have a URL field which the former does not have. The URL link can be clicked and it will open the relevant webpage into a browser. Below the comments field there are two buttons. The **"Remove"** button deletes the current item entry from the database and returns the user to the main display above. The item is not actually permanently removed from the database, but rather the entry is hidden from the user. The **"Edit"** button takes the user to a similar prefilled textfield form, see figure 5, as the **"Add"** button. There is also a **"Back"** button in the top right corner, which takes the user back to the previous display; in this case the main screen.

<p align="center">
  <img width="600" src="https://github.com/Ozath/dummy/blob/main/img/2.PNG" alt="Bookcase">
</p>
<p align="center"> <strong>Fig.3 - detailed info display</strong></p>

&nbsp;

The add and edit displays, as seen in figures 4 and 5, are similar in functionality. The user can freely add or edit items information with a few constraints. These are: 1) book items must have a title, author and valid unique ISBN number, 2) blogposts and podcasts must have a title and url specified.

<p align="center">
  <img width="600" src="https://github.com/Ozath/dummy/blob/main/img/3.PNG" alt="Bookcase">
</p>
<p align="center"> <strong>Fig.4 - add item display</strong></p>

&nbsp;

<p align="center">
  <img width="600" src="https://github.com/Ozath/dummy/blob/main/img/4.PNG" alt="Bookcase">
</p>
<p align="center"> <strong>Fig.5 - edit item display</strong></p>

&nbsp;

The application informs the user when any constraints are being broken. In figure 6 below, the user tried to enter a duplicate ISBN value that is already assigned to the book titled **Prime Numbers** from figure 1.

<p align="center">
  <img width="400" src="https://github.com/Ozath/dummy/blob/main/img/6.PNG" alt="Bookcase">
</p>
<p align="center"> <strong>Fig.6 - a non-unique ISBN value given</strong></p>
