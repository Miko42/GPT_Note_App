# GPT Note App

**GPT Note App** is a Kotlin-based mobile application that serves as a standard note-taking app with an added feature allowing users to send prompts to OpenAI's ChatGPT API directly from their notes. Users can highlight text in their notes or write a prompt in the title, click a button, and receive a response from ChatGPT.

Quick overview:


![overview](https://github.com/user-attachments/assets/a5f19521-bf04-48a2-9593-476b610eacc1)


## Key Features

- **Classic Note-Taking**: Users can create, edit, and delete notes, which are stored locally in a database using Room.

- **ChatGPT Integration**: Users can type a prompt either in the title or the body of the note, select the text, and send the request to the GPT API to get AI-generated responses.

- **Offline Mode**: Notes can be accessed and managed offline. Only when sending prompts to the API does the app require an internet connection.

- **UI/UX**: The app is built with Jetpack Compose, ensuring a smooth, modern user interface and seamless animations.

- **Kotlin**: The entire application is written in Kotlin, leveraging the full power of the language's features for Android development.

- **Room Database**: Used for storing and managing notes locally, ensuring efficient and persistent storage.

- **Ktor**: Networking is handled by Ktor, specifically for interacting with the ChatGPT API. This involves making HTTP requests, handling JSON serialization, and managing API timeouts.

- **Dagger Hilt**: Used for dependency injection to make the app modular, maintainable, and easy to test.

- **Kotlin Coroutines**: Provides efficient background threading for executing tasks like network requests and database operations without blocking the main UI thread.

- **ViewModel & State Management**: Leverages ViewModel and MutableStateFlow/StateFlow for managing UI state and surviving configuration changes.
