package Iteraters;

public class BookShelf implements Aggregate{
	Book[] books;
	private int last;
	
	public BookShelf(int maxsize){
		this.books = new Book[maxsize];
	}
	public Book getBookAt(int index){
		return books[index];
	}
	public void appendBook(Book book){
		this.books[last] = book;
		last++;
	}
	public int getLength(){
		return last;
	}
	public Iterator iterator(){
		return new BookShelfIterator(this);
	}
}
