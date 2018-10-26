package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {

        String cmdType;
        try {
            String[] argSplit = args.trim().split(" ", 2);
            cmdType = argSplit[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

        if (!cmdType.equals("patient") && !cmdType.equals("symptom") && !cmdType.equals("appointment")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

        return new ListCommand(cmdType);
    }
}