Feature: As a user I want to be able to add a podcast to the application
  which has a title, author

    Scenario Outline: Podcast can be added when all necessary information is filled in
      Given application has opened
      When  choose "Podcast" from droplist
      When  add item button is clicked
      When  form is filled with "Title" as name and "Author" as author and "wwwtestcom" as isbn
      When  create item is clicked
      Then  error label has text "New podcast added"
    
    Scenario Outline: Podcast cant be added when Title is empty
      Given application has opened
      When  choose "Podcast" from droplist
      When  add item button is clicked
      When  form is filled with "" as name and "Author" as author and "wwwtestcom" as isbn
      When  create item is clicked
      Then  error label has text "Title field can not be empty."
