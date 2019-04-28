package ru.ancevt.net.httprequest;

public class Test {
    public static void main(String[] args) throws HTTPException {
    	final HTTPRequest req = new HTTPRequest();
    	
    	System.out.println("*** Request headers ***");
    	System.out.println(req.getRequestHeaders());
    	System.out.println("*** Request body: ");
    	System.out.println(req.data().toString());
    	
    	req.send("http://ancevt.ru", HTTPMethod.GET, new HTTPHeaders(new String[][] {
    		{"My-Header", "My header value"}
    	}));

    	System.out.println("Status: " + req.getStatus());
    	System.out.println("*** Response headers: ***");
    	System.out.println(req.getResponseHeaders().toString());
    	System.out.println("*** Response body: ***");
    	System.out.println(req.readString());
    	
    }
}
