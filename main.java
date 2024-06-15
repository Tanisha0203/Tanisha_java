import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

// Book class represents a book in the library with title, author, ISBN, and available copies.
class Book {
    private String title;
    private String author;
    private String ISBN;
    private int availableCopies;

    // Constructor to initialize a Book object.
    public Book(String title, String author, String ISBN, int availableCopies) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.availableCopies = availableCopies;
    }

    // Getters for the book attributes.
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    // Setter for available copies.
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    // Override toString method to display book details.
    @Override
    public String toString() {
        return "Book{" + "title='" + title + '\'' + ", author='" + author + '\'' +
               ", ISBN='" + ISBN + '\'' + ", availableCopies=" + availableCopies + '}';
    }
}

// Member class represents a library member with name, memberId, email, and borrowed books.
class Member {
    private String name;
    private String memberId;
    private String email;
    private List<Book> borrowedBooks;

    // Constructor to initialize a Member object.
    public Member(String name, String memberId, String email) {
        this.name = name;
        this.memberId = memberId;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters for the member attributes.
    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    // Method to borrow a book.
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    // Method to return a borrowed book.
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    // Override toString method to display member details.
    @Override
    public String toString() {
        return "Member{" + "name='" + name + '\'' + ", memberId='" + memberId + '\'' +
               ", email='" + email + '\'' + '}';
    }
}

// Library class manages the collection of books and members.
class Library {
    private List<Book> books;
    private List<Member> members;

    // Constructor to initialize the Library.
    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
    }

    // Method to add a new book to the library.
    public void addBook(Book book) {
        books.add(book);
    }

    // Method to remove a book from the library using its ISBN.
    public void removeBook(String ISBN) {
        books.removeIf(book -> book.getISBN().equals(ISBN));
    }

    // Method to search for a book by its title.
    public Optional<Book> searchBookByTitle(String title) {
        return books.stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    // Method to search for a book by its author.
    public Optional<Book> searchBookByAuthor(String author) {
        return books.stream().filter(book -> book.getAuthor().equalsIgnoreCase(author)).findFirst();
    }

    // Method to list all books in the library.
    public List<Book> listAllBooks() {
        return new ArrayList<>(books);
    }

    // Method to add a new member to the library.
    public void addMember(Member member) {
        members.add(member);
    }

    // Method to remove a member from the library using their member ID.
    public void removeMember(String memberId) {
        members.removeIf(member -> member.getMemberId().equals(memberId));
    }

    // Method to search for a member by their name.
    public Optional<Member> searchMemberByName(String name) {
        return members.stream().filter(member -> member.getName().equalsIgnoreCase(name)).findFirst();
    }

    // Method to search for a member by their member ID.
    public Optional<Member> searchMemberById(String memberId) {
        return members.stream().filter(member -> member.getMemberId().equals(memberId)).findFirst();
    }

    // Method to list all members in the library.
    public List<Member> listAllMembers() {
        return new ArrayList<>(members);
    }

    // Method to issue a book to a member.
    public void issueBook(String ISBN, String memberId) {
        Optional<Book> bookOpt = books.stream().filter(book -> book.getISBN().equals(ISBN) && book.getAvailableCopies() > 0).findFirst();
        Optional<Member> memberOpt = members.stream().filter(member -> member.getMemberId().equals(memberId)).findFirst();

        if (bookOpt.isPresent() && memberOpt.isPresent()) {
            Book book = bookOpt.get();
            Member member = memberOpt.get();
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            member.borrowBook(book);
        } else {
            throw new IllegalArgumentException("Book or Member not found, or no available copies of the book.");
        }
    }

    // Method to return a borrowed book to the library.
    public void returnBook(String ISBN, String memberId) {
        Optional<Member> memberOpt = members.stream().filter(member -> member.getMemberId().equals(memberId)).findFirst();

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            Optional<Book> bookOpt = member.getBorrowedBooks().stream().filter(book -> book.getISBN().equals(ISBN)).findFirst();

            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                member.returnBook(book);
            } else {
                throw new IllegalArgumentException("Book not borrowed by this member.");
            }
        } else {
            throw new IllegalArgumentException("Member not found.");
        }
    }

    // Method to list all borrowed books along with the details of the members who borrowed them.
    public List<String> listAllBorrowedBooks() {
        List<String> borrowedBooksDetails = new ArrayList<>();
        for (Member member : members) {
            for (Book book : member.getBorrowedBooks()) {
                borrowedBooksDetails.add("Book: " + book.getTitle() + ", Borrowed by: " + member.getName());
            }
        }
        return borrowedBooksDetails;
    }
}

// Main class for the console-based menu system to interact with the library.
public class main {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    // Main method to start the program.
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();  // Print the menu options.
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline.
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 5:
                    addMember();
                    break;
                case 6:
                    removeMember();
                    break;
                case 7:
                    searchMember();
                    break;
                case 8:
                    listAllMembers();
                    break;
                case 9:
                    issueBook();
                    break;
                case 10:
                    returnBook();
                    break;
                case 11:
                    listAllBorrowedBooks();
                    break;
                case 12:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to print the menu options.
    private static void printMenu() {
        System.out.println("\nLibrary Management System");
        System.out.println("1. Add a new book");
        System.out.println("2. Remove a book");
        System.out.println("3. Search for a book");
        System.out.println("4. List all books");
        System.out.println("5. Add a new member");
        System.out.println("6. Remove a member");
        System.out.println("7. Search for a member");
        System.out.println("8. List all members");
        System.out.println("9. Issue a book");
        System.out.println("10. Return a book");
        System.out.println("11. List all borrowed books");
        System.out.println("12. Exit");
        System.out.print("Enter your choice: ");
    }

    // Method to add a new book to the library.
    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String ISBN = scanner.nextLine();
        System.out.print("Enter number of available copies: ");
        int availableCopies = scanner.nextInt();
        scanner.nextLine();  // Consume newline.

        Book book = new Book(title, author, ISBN, availableCopies);
        library.addBook(book);
        System.out.println("Book added successfully.");
    }

    // Method to remove a book from the library.
    private static void removeBook() {
        System.out.print("Enter book ISBN: ");
        String ISBN = scanner.nextLine();
        library.removeBook(ISBN);
        System.out.println("Book removed successfully.");
    }

    // Method to search for a book by title or author.
    private static void searchBook() {
        System.out.print("Enter book title or author: ");
        String searchQuery = scanner.nextLine();
        Optional<Book> bookByTitle = library.searchBookByTitle(searchQuery);
        Optional<Book> bookByAuthor = library.searchBookByAuthor(searchQuery);

        if (bookByTitle.isPresent()) {
            System.out.println("Book found: " + bookByTitle.get());
        } else if (bookByAuthor.isPresent()) {
            System.out.println("Book found: " + bookByAuthor.get());
        } else {
            System.out.println("Book not found.");
        }
    }

    // Method to list all books in the library.
    private static void listAllBooks() {
        library.listAllBooks().forEach(System.out::println);
    }

    // Method to add a new member to the library.
    private static void addMember() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();

        Member member = new Member(name, memberId, email);
        library.addMember(member);
        System.out.println("Member added successfully.");
    }

    // Method to remove a member from the library.
    private static void removeMember() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        library.removeMember(memberId);
        System.out.println("Member removed successfully.");
    }

    // Method to search for a member by name or ID.
    private static void searchMember() {
        System.out.print("Enter member name or ID: ");
        String searchQuery = scanner.nextLine();
        Optional<Member> memberByName = library.searchMemberByName(searchQuery);
        Optional<Member> memberById = library.searchMemberById(searchQuery);

        if (memberByName.isPresent()) {
            System.out.println("Member found: " + memberByName.get());
        } else if (memberById.isPresent()) {
            System.out.println("Member found: " + memberById.get());
        } else {
            System.out.println("Member not found.");
        }
    }

    // Method to list all members in the library.
    private static void listAllMembers() {
        library.listAllMembers().forEach(System.out::println);
    }

    // Method to issue a book to a member.
    private static void issueBook() {
        System.out.print("Enter book ISBN: ");
        String ISBN = scanner.nextLine();
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();

        try {
            library.issueBook(ISBN, memberId);
            System.out.println("Book issued successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to return a borrowed book to the library.
    private static void returnBook() {
        System.out.print("Enter book ISBN: ");
        String ISBN = scanner.nextLine();
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();

        try {
            library.returnBook(ISBN, memberId);
            System.out.println("Book returned successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to list all borrowed books with member details.
    private static void listAllBorrowedBooks() {
        library.listAllBorrowedBooks().forEach(System.out::println);
    }
}
