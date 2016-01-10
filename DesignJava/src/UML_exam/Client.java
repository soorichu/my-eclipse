package UML_exam;

class Client{
	
	Server server = new Server("Computer");
	void work(){
		server.open();
		server.print();
		server.close();
	}
	
	
}