package com.example.houseagent.agents.api;

public interface Agent<I, O> {
    O handle(I input);
}
