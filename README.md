
# Dream-Z: An Analytical Dream Journal

## Background

I have always have vivid dreams that feel like movies. I go through periods of time where I write the dreams down every day. But once they are written, I can never find the time to through them and look for patterns. So I wanted to start a project that would do that for me!

## Goal

I wanted to build an app that would utlize some language analytics tools to show patterns and commonalities over several journal entries. 

## Tech Stack

Java Spring Boot, SQL, Hibernate, Javascript, Thymeleaf, APIs

## Deployment

This site and database were deployed on railway.

## Use 

- On app mount, the user is prompted to login or register.
- Once registered and logged in, the user will see a handful of entries that have been provided as an example. The navbar at the top shows the add, edit and analyze features.
- To add an entry, the user moves the cursor over the Add menu item and clicks on entry, where they will be prompted to enter their journal entry and assign it a topic and mood. These associations will later allow the user to group entries by topic and mood, which can help show common themes and patterns. You can also choose to add topics and moods that are specific to your dreams.
- If the user wants to analyze their dreams, they have several options. Once the cursor is on the analyze part of the dropdown, they can choose to build a wordcloud or use the language analytics tools provided by Stanford's CoreNLP. They can also group the entries by topic or mood, as previously mentioned.
- At the worldcloud enpoint, the user can choose which entries they want to analyze and a WordCloud API will build a wordcloud graphic showing the most used words common to all the entries.
- At the analysis enpoint, the user can use Stanford's CoreNLP to analyize the overall sentiment of a dream (positive, nuetral, negative). They can also analyze the entries for Named Enities (such as locations, proper nouns, etc) and they can break the entry into noun\verb phrases, which can help them think of events in more action-oriented ways that might help them find common threads. 

