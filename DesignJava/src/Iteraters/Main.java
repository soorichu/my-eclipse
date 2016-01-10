package Iteraters;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BookShelf bookShelf = new BookShelf(100);
		bookShelf.appendBook(new Book("Twilight"));
		bookShelf.appendBook(new Book("Eclipse"));
		bookShelf.appendBook(new Book("Superman"));
		bookShelf.appendBook(new Book("SnowWhite"));
		Iterator it = bookShelf.iterator();
		while(it.hasNext()){
			Book book = (Book) it.next();
			System.out.println(book.getName());
		}
	}

}
