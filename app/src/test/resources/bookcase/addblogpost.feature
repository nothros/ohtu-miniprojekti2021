Feature: As a user I want to be able to add a blogpost to the application
  which has a title, author and unique isbn

    Scenario Outline: Blogpost can be added when all necessary information is filled in
      Given application has opened
      When  Choose "Blogpost" from list
      When  add item button is clicked
      When  form is filled with "Title" as name and "Author" as author and "wwwtestcom"
      When  create item is clicked
      Then  error label has text "New Blogpost added"

    Scenario Outline: Blogpost cant be added when there is no website
      Given application has opened
      When  Choose "Blogpost" from list
      When  add item button is clicked
      When  form is filled with "Title" as name and "Author" as author and "" 
      When  create item is clicked
      Then  error label has text "Something went wrong while adding new Blogpost"

    Scenario Outline: Book cant be added when there is no Author or Title
      Given application has opened
      When  Choose "Blogpost" from list
      When  add item button is clicked
      When  create item is clicked
      Then  error label has text "Something went wrong while adding new Blogpost"