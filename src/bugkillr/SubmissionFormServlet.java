package bugkillr;
import java.io.IOException;

import javax.servlet.http.*;

import html.HTMLWriter;
import redirects.Redirector;

@SuppressWarnings("serial")
/**
 * The beginning of the problem submission process. This page
 * creates an HTML form that the player uses to submit their problem.
 * From here, the player's code is sent to another page, which uses an
 * HTTP POST to send the data to a CGI program and determine whether it's
 * solved correctly. Then it produces an error message if appropriate or
 * tells the user they succeeded and updates the database.
 */
public class SubmissionFormServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HTMLWriter hw = new HTMLWriter(req, resp);
		Redirector redir = new Redirector(req,resp);
		hw.writeProlog("Bugkiller - Submit a Solution");
		hw.writeHeader();
		
		//Make sure the player is logged in, is in the database and has a team.
		redir.loginRedirect();
		try {
			redir.userRedirect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Display a message to the user
		resp.getWriter().println("Copy and paste your source code into the area below.<br/>\n");
		//Submission form
		resp.getWriter().println("<form name=\"input\" action=\"DBprocess_submission\" method=\"post\" >\n" +
				"<textarea name=\"inputText\" rows=\"25\" cols=\"120\">Test Text</textarea>\n" +
				"<input type=\"hidden\" name=\"pid\" value=\""+req.getParameter("pid")+"\"/><br/>\n" +
				"<input type=\"submit\" value=\"Submit\"/>\n" +
				"</form>");
		hw.writeEpilog();
	}
}