package Program;

import java.util.Date;

import com.hao.keylogger.controllers.ClientLogController;
import com.hao.keylogger.models.Log;
import com.hao.keylogger.views.ClientView;

public class ClientKeyLoggerProgram {

	public ClientKeyLoggerProgram() {
		
	}
	
	public static void main(String[] args) {
		ClientView clientView =   new ClientView();
		Log log = new Log("localhost", 17, new Date(), "L O G C O N T E N T [ENTER]");
		
		ClientLogController controller = new ClientLogController(clientView, log);
		
		controller.loadView();
	}

}
