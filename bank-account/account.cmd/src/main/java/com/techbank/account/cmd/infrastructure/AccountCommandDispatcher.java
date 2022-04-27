package com.techbank.account.cmd.infrastructure;

import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        routes.computeIfAbsent(type, c -> new ArrayList<>()).add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        List<CommandHandlerMethod> handlers = routes.get(command.getClass());

        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No handler is registered");
        }

        if (handlers.size() > 1) {
            throw new RuntimeException("Cannot send to more than 1");
        }

        handlers.get(0).handle(command);
    }
}
