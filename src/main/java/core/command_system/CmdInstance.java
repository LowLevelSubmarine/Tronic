package core.command_system;

import core.command_system.arguments.CmdArgument;

public interface CmdInstance {

    void run(CmdParser parser) throws CmdArgument.InvalidArgumentException;

}
