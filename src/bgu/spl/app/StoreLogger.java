package bgu.spl.app;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



// this custom formatter formats parts of a log record to a single line
class MyHtmlFormatter extends Formatter {
  // this method is called for every log records
  public String format(LogRecord rec) {
    StringBuffer buf = new StringBuffer(1000);
    buf.append("<tr>\n");

    
    if (rec.getLevel().intValue() == Level.INFO.intValue()) {
      buf.append("\t<td style=\"color:blue\">");
      buf.append("<b>");
      buf.append(rec.getLevel());
      buf.append("</b>");
  
    }
    if (rec.getLevel().intValue() == Level.CONFIG.intValue()) {
        buf.append("\t<td style=\"color:green\">");
        buf.append("<b>");
        buf.append(rec.getLevel());
        buf.append("</b>");

      }
    if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
        buf.append("\t<td style=\"color:red\">");
        buf.append("<b>");
        buf.append(rec.getLevel());
        buf.append("</b>");
      } 

    buf.append("</td>\n");
    buf.append("\t<td>");
    buf.append(calcDate(rec.getMillis()));
    buf.append("</td>\n");
    if (rec.getLevel().intValue() == Level.INFO.intValue()) 
        buf.append("\t<td style=\"color:blue\">");
    else buf.append("\t<td>");
    buf.append(formatMessage(rec));
    buf.append("</td>\n");
    buf.append("</tr>\n");

    return buf.toString();
  }

  private String calcDate(long millisecs) {
    SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    Date resultdate = new Date(millisecs);
    return date_format.format(resultdate);
  }

  // this method is called just after the handler using this
  // formatter is created
  public String getHead(Handler h) {
      return "<!DOCTYPE html>\n<head>\n<style>\n"
          + "table { width: 100% }\n"
          + "th { font:bold 10pt Tahoma; }\n"
          + "td { font:normal 10pt Tahoma; }\n"
          + "h1 {font:normal 11pt Tahoma;}\n"
          + "</style>\n"
          + "</head>\n"
          + "<body>\n"
          + "<h1>" + (new Date()) + "</h1>\n"
          + "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n"
          + "<tr align=\"left\">\n"
          + "\t<th style=\"width:10%\">Loglevel</th>\n"
          + "\t<th style=\"width:15%\">Time</th>\n"
          + "\t<th style=\"width:75%\">Log Message</th>\n"
          + "</tr>\n";
    }

  // this method is called just after the handler using this
  // formatter is closed
  public String getTail(Handler h) {
    return "</table>\n</body>\n</html>";
  }
} 
public class StoreLogger {

	 static private FileHandler fileTxt;
	  static private SimpleFormatter formatterTxt;
	  static private FileHandler fileHTML;
	  static private Formatter formatterHTML;

	  static public void setup() throws IOException {

	    // get the global logger to configure it
	    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	    // suppress the logging output to the console
	//    Logger rootLogger = Logger.*getLogger*("");
	 //   Handler[] handlers = rootLogger.getHandlers();
	   // if (handlers[0] instanceof ConsoleHandler) {
	   //   rootLogger.removeHandler(handlers[0]);
	   // }

	    logger.setLevel(Level.CONFIG);
	    //fileTxt = new FileHandler("Logging.txt");
	    fileHTML = new FileHandler("Logging.html");

	    // create a TXT formatter
	  //  formatterTxt = new SimpleFormatter();
	 //   fileTxt.setFormatter(formatterTxt);
	 //   logger.addHandler(fileTxt);

	    // create an HTML formatter
	    formatterHTML = new MyHtmlFormatter();
	    fileHTML.setFormatter(formatterHTML);
	    logger.addHandler(fileHTML);
	  }

}
