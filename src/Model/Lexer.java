package Model;

import java.util.ArrayList;

public class Lexer {

	private enum States {
		START,  WALK, ROTATE, LOOK, DROP, FREE, PICK, GRAB, WALKTO,
		CONDITION, DEFINE, END
	}

	private ArrayList<String> functions;
	private ArrayList<String> variables;
	private States state;

	public Lexer() {
		state = States.START;
	}

	public boolean validateString(String comands) {
		String[] comandsArray = comands.split(" ");
		for (String comand : comandsArray) {
			if (state == States.START) {
				if (comand.charAt(0) != '(') {
					return false;
				}
				if (comand == "(walk") {
					state = States.WALK;
				} else if (comand == "(rotate") {
					state = States.ROTATE;
				} else if (comand == "(look") {
					state = States.LOOK;
				} else if (comand == "(drop") {
					state = States.DROP;
				} else if (comand == "(free") {
					state = States.FREE;
				} else if (comand == "(pick") {
					state = States.PICK;
				} else if (comand == "(grab") {
					state = States.GRAB;
				} else if (comand == "(walkTo") {
					state = States.WALKTO;
				}
			} else if (state == States.WALK) {
			    try {
			        double d = Double.parseDouble(comand);
			    } catch (NumberFormatException nfe) {
			    	if (!variables.contains(comand)) {			    		
			    		return false;
			    	}
			    }
			    
			}
		}
		return true;
	}
}
