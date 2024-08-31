I’m thrilled to introduce NakamaAI, an innovative app I developed that combines the power of Gemini AI with Firebase to create a seamless, engaging user experience. NakamaAI stands out with its dual functionality—offering both text generation and multimodal (text and image) interaction, along with robust chat management features.

Key Features of NakamaAI:

🔊 AI-Powered Conversations:
NakamaAI uses Gemini AI to generate meaningful text responses, enhancing user interactions.
Multimodal capability allows users to interact using both text and images, making conversations more dynamic.

🗂️ Chat Management:
Users can save, delete, and view their conversations, offering control over interaction history.
Powered by Firebase, ensuring secure and scalable data storage.

🔒 Secure Authentication:
Login with Google accounts using Firebase Authentication for a smooth and secure user experience.

🎨 Modern UI with Jetpack Compose:
Built with Jetpack Compose for a responsive and visually appealing user interface.

⚙️ Scalable Architecture:
Developed using MVVM architecture and Hilt for dependency injection, ensuring a maintainable and scalable codebase.
DataStore is used for onboarding, ensuring a personalized and frictionless start for users.


To run NakamaAI, you need to set up API keys for Gemini AI and Firebase. 

1. **Gemini AI**: 
   - Obtain an API key from the Gemini AI dashboard.
   - Add the API key in `local.properties` or the respective configuration file.

2. **Firebase**:
   - Set up a Firebase project and add your Android app.
   - Download the `google-services.json` and place it in the `app/` directory.
