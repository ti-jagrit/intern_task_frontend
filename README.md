# Book Borrowing Android App

This is a Book Borrowing Management App built using Android (Java) for the frontend and Spring Boot for the backend. It allows users to manage books, borrowers, and borrow records with JWT-based authentication.

## What it Contains

- JWT-based login system
- Book CRUD (Create, Read, Update, Delete)
- Borrower CRUD
- Borrow functionality (assign books to borrowers)
- View borrows by individual borrower or all
- Dialog-based UI for adding/editing books, borrowers, and borrow records
- Retrofit integration with Bearer token
## Screenshots

- Login Screen
  <img width="548" height="1099" alt="image" src="https://github.com/user-attachments/assets/4a1f8839-406d-4b8f-994e-5a94e5afb2d7" />

- Dashboard
  <img width="578" height="1080" alt="image" src="https://github.com/user-attachments/assets/63c45d95-4ba0-4e83-998e-6cabfb85476d" />

- Book
  <img width="530" height="1066" alt="image" src="https://github.com/user-attachments/assets/e15b706a-f3e6-4ae7-a340-e4747cd61742" />

- Borrow Books
  <img width="539" height="1103" alt="image" src="https://github.com/user-attachments/assets/1a7788ce-57d5-4308-894e-4607060f9c19" />


## Technologies Used

- Android (Java)
- RecyclerView
- Custom Dialogs
- Spring Boot (backend)
- MySQL
- JWT (JSON Web Token) Authentication

## Setup Instructions

1. Clone the repository:
   git clone https://github.com/yourusername/book-borrow-app.git

2. Open the project in Android Studio

3. Make sure the backend Spring Boot API is running on your machine or server.

4. Run the app on emulator or real device.

## Folder Structure

```
app/
├── api/
├── models/
├── ui/
├── adapters/
└── utils/
```

