package UML_exam;

class Server{
	
	String device = new String();
	
	Server(){
		this.device = "Unknown Device";
	}
	Server(String dev){
		this.device = dev;
	}
	void open(){
		System.out.println(device + " is opening");
		
	}
	void print(){
		System.out.println("The device is "+device);
	}
	void close(){
		System.out.println(device +" is close");
		
	}
	
}
