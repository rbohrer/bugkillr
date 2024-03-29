package html;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.*;

import redirects.Redirector;
/**
 * 
 * @author Randy Bohrer
 * HTMLWriter contains functions for writing common
 * pieces of HTML.
 */
public class HTMLWriter {
	private HttpServletResponse resp;
	private HttpServletRequest req;

	/**
	 * @param Resp The HttpServletResponse which the HTMLWriter will write to
	 */
	public HTMLWriter(HttpServletRequest Req, HttpServletResponse Resp)
	{
		req = Req;
		resp = Resp;
	}

	/**
	 * @param titleText The page's title
	 * @throws IOException
	 * This creates the beginning of the XHTML page. It writes out the doctype and html tags, and a 
	 * header (which includes the specified title and a meta tag denoting the character encoding)
	 */
	public void writeProlog(String titleText) throws IOException
	{
		resp.getWriter().println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
				"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
				"<head>\n" +
				"<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />" +
				"<title>" + titleText + "</title>\n" +
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"static/main.css\"\n>" +
				"<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js\"></script>\n" +
				"<script type=\"text/javascript\" src=\"static/script.js\"></script>\n" +
				"</head>\n" +
		"<body>");
	}
	/**
	 * @throws IOException
	 * Writes the end of an XHTML page (Closing tags for "body" and "html")
	 */
	public void writeEpilog() throws IOException
	{
		resp.getWriter().println("</div></body>\n" +
		"</html>");
	}

	/**
	 * @param address The address that the link leads to.
	 * @param text The text of the link
	 * @throws IOException
	 * Inserts a hyperlink.
	 */
	public void writeLink(String address, String text) throws IOException
	{
		resp.getWriter().println("<a href=\"" + address + "\">" + text + "</a>");
	}

	/**
	 * @throws IOException 
	 * Inserts a horizontal rule on the page.
	 */
	public void writeRule() throws IOException
	{
		resp.getWriter().println("<hr/>");
	}
	/**
	 * @throws IOException
	 * This writes out the XHTML for a header bar, containing links to the major
	 * pages of the site. Use this function instead of writing out the header 
	 * "by hand" so that when the links change it remains consistent.
	 */
	public void writeHeader() throws IOException
	{
		resp.getWriter().println("<div class=\"gametitle\">" +
				"<div class=\"logo\">&nbsp;</div>");
		//Unimplemented pages are commented out.
		UserService us = UserServiceFactory.getUserService();
		Redirector redir = new Redirector(req,resp);
		resp.getWriter().println("<div class=\"menubar\">");
		writeLink("home", "Home");
		writeLink("problems", "Available Problems");
		writeLink("tools", "Tools");
		writeLink("highscores", "Team Rankings");
		writeLink("addteamform", "Create New Team");
		writeLink("viewteams", "Join a Different Team");
		
		//Use Google Apps API to generate login/logout links.
		if(redir.isLoggedIn())
		{
			writeLink(us.createLogoutURL(req.getRequestURI()), "Log Out");
		}
		else
		{
			writeLink(us.createLoginURL(req.getRequestURI()), "Log In");
		}
		if(us.isUserLoggedIn() && us.isUserAdmin()){
			writeLink("addproblemform","Create New Problem");
		}
		resp.getWriter().println("</div></div>\n<div class=\"content\"");
	}

	//Write a message explaining the GET method is unsupported for a page.
	//This occurs when the player accesses a page that uses POST, then logs
	//out and logs back in.
	public void writeUnsupportedGet() throws IOException
	{
		resp.getWriter().println("You have accessed this page through the HTTP GET method," +
				"which this page does not support. This generally occurs if you log out and" +
				"log in again. This is completely normal. Please navigate to another page and" +
		"continue what you were doing.");
	}
}
