package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

	private enum States {
		START, COMMAND, WALK, ROTATE, LOOK, DROP, FREE, PICK, GRAB, WALKTO, CONDITION, CONDITION_VALUE, FACING, CLOSE,
		CAN, DEFINE, DEF_VAULE, FUNCTION_PARAMS, END
	}

	private ArrayList<String> functions;
	private HashMap<String, String> variables;
	private States state;

	public Lexer() {
		state = States.START;
		functions = new ArrayList<String>();
		variables = new HashMap<String, String>();
	}

	/**
	 * @param commands
	 * @return
	 */
	public boolean validateString(String commands) {
		System.out.println(commands);
		String[] commandsArray = commands.split("\\s+");
		int blockClosing = 0;
		int defineClosing = 0;
		int closing = 0;
		int conditionalCommands = 0;
		boolean inCondition = false;
		String VarName = "";
		for (String command : commandsArray) {
			command.toLowerCase();

			if (command.length() == 0 || command == null) {
				continue;
			}
			if (state == States.START && closing > 0 && (conditionalCommands == 0 || inCondition)) {
				state = States.CLOSE;
			} else if (closing == 0 && blockClosing > 0) {
				state = States.CLOSE;
			}
			System.out.println("command: " + command + " " + state + " " + closing + " " + conditionalCommands + " "
					+ inCondition + " " + blockClosing);
			if (state == States.START) {
				conditionalCommands = (conditionalCommands > 0) ? conditionalCommands - 1 : 0;
				if (!command.startsWith("(") && blockClosing > 0) {
					System.out.println("Command doesnt start with '('");
					return false;
				}
				if (command.startsWith("(") && command.length() == 1) {
					closing++;
					state = States.COMMAND;
				}
				if (command.startsWith("(walk")) {
					state = States.WALK;
				} else if (command.startsWith("(rotate")) {
					state = States.ROTATE;
				} else if (command.startsWith("(look")) {
					state = States.LOOK;
				} else if (command.startsWith("(drop")) {
					state = States.DROP;
				} else if (command.startsWith("(free")) {
					state = States.FREE;
				} else if (command.startsWith("(pick")) {
					state = States.PICK;
				} else if (command.startsWith("(grab")) {
					state = States.GRAB;
				} else if (command.startsWith("(walkTo")) {
					state = States.WALKTO;
				} else if (command.startsWith("(block")) {
					blockClosing++;
					state = States.START;
				} else if (command.startsWith("(if")) {
					conditionalCommands += 2;
					closing++;
					state = States.CONDITION;
				} else if (command.startsWith("(define")) {
					state = States.DEFINE;
				} else if (command.startsWith("(nop")) {
					if (command.endsWith(")")) {
						state = States.START;
					} else {
						state = States.CLOSE;
					}
				} else if (functions.contains("(" +command)) {
					closing++;
					state = States.FUNCTION_PARAMS;
				}
			} else if (state == States.CLOSE) {
				inCondition = false;
				if (!command.startsWith(")") && blockClosing == 0 && defineClosing == 0) {
					System.out.println("Expected ')' at the end of a command");
					return false;
				} else if (closing > 0){
					closing--;
					state = States.START;
				} else if (blockClosing > 0){
					blockClosing--;
					state = States.START;
				} else if (defineClosing > 0){
					blockClosing--;
					state = States.START;
				}
			} else if (state == States.COMMAND) {
				if (command.startsWith("walk")) {
					state = States.WALK;
				} else if (command.startsWith("rotate")) {
					state = States.ROTATE;
				} else if (command.startsWith("look")) {
					state = States.LOOK;
				} else if (command.startsWith("drop")) {
					state = States.DROP;
				} else if (command.startsWith("free")) {
					state = States.FREE;
				} else if (command.startsWith("pick")) {
					state = States.PICK;
				} else if (command.startsWith("grab")) {
					state = States.GRAB;
				} else if (command.startsWith("walkTo")) {
					state = States.WALKTO;
				} else if (command.startsWith("block")) {
					closing--;
					blockClosing++;
					state = States.START;
				} else if (command.startsWith("if")) {
					conditionalCommands += 2;
					state = States.CONDITION;
				} else if (command.startsWith("define")) {
					state = States.DEFINE;
				} else if (command.startsWith("nop")) {
					if (command.endsWith(")")) {
						state = States.START;
					} else {
						state = States.CLOSE;
					}
				} else if (functions.contains(command)) {
					state = States.FUNCTION_PARAMS;
				}
			} else if (state == States.WALK || state == States.DROP || state == States.FREE || state == States.PICK
					|| state == States.GRAB) {
				if (!validateVariable(command)) {
					System.out.println("Invalid command for " + state);
					return false;
				}
				if (command.endsWith(")")) {
					state = States.START;
				} else {
					state = States.CLOSE;
				}

			} else if (state == States.ROTATE) {
				if (!command.startsWith("left") && !command.startsWith("right") && !command.startsWith("back")) {
					System.out.println(command + " is not a valid argument for " + state);
					return false;
				}
				if (command.endsWith(")")) {
					state = States.START;
				} else {
					state = States.CLOSE;
				}
			} else if (state == States.LOOK || state == States.FACING) {
				if (!command.startsWith("n") && !command.startsWith("e") && !command.startsWith("w")
						&& !command.startsWith("s")) {
					System.out.println(command + " is not a valid argument for " + state);
					return false;
				}
				if (command.endsWith(")")) {
					state = States.START;
				} else {
					state = States.CLOSE;
				}
			} else if (state == States.CONDITION) {
				inCondition = true;
				if (command.startsWith("(") && command.length() == 1) {
					state = States.CONDITION_VALUE;
				} else if (command.startsWith("(blocked?")) {
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						state = States.START;
					}
				} else if (command.startsWith("(facing?")) {
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						state = States.START;
					}
				} else if (command.startsWith("(can?")) {
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						state = States.START;
					}
				} else if (command.startsWith("(not")) {
					state = States.CONDITION;
					closing++;
				}
			} else if (state == States.CONDITION_VALUE) {
				if (command.startsWith("blocked?")) {
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						state = States.START;
					}
				} else if (command.startsWith("facing?")) {
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						state = States.START;
					}
				} else if (command.startsWith("can?")) {
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						state = States.START;
					}
				} else if (command.startsWith("not")) {
					state = States.CONDITION;
				}
			} else if (state == States.CAN) {
				if (!command.startsWith("grab") && !command.startsWith("drop") && !command.startsWith("free")
						&& !command.startsWith("pick")) {
					System.out.println(command + " is not a valid argument for " + state);
					return false;
				}
			} else if (state == States.DEFINE) {
				VarName = command;
				state = States.DEF_VAULE;
			} else if (state == States.DEF_VAULE) {
				if (command.startsWith("(") && command.length() == 1) {
					closing++;
					state = States.FUNCTION_PARAMS;
				} else {
					variables.put(VarName, command);
					VarName = "";
					if (!command.endsWith(")")) {
						state = States.CLOSE;
					} else if (command.endsWith(")")) {
						closing--;
						state = States.START;
					}
				}
			} else if (state == States.FUNCTION_PARAMS) {
				if (command.endsWith(")")) {
					closing--;
					state = States.START;
				}
			} 
		}
		if (closing != 0 || blockClosing != 0) {
			System.out.println("Unmatching number of opening and closing parenthesis");
			return false;
		}
		return true;
	}

	private boolean validateVariable(String command) {
		String value = command;
		if (command.endsWith(")")) {
			value = command.substring(0, command.length() - 1);
		}
		try {
			double d = Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			if (!variables.containsKey(value)) {
				System.out.println(value + " is not a valid argument for walk");
				return false;
			}
		}
		return true;
	}
}
