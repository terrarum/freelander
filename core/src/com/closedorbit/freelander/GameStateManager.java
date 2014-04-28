package com.closedorbit.freelander;

import com.closedorbit.freelander.gameStates.GameState;
import com.closedorbit.freelander.gameStates.Play;
import com.closedorbit.freelander.screens.GameScreen;

import java.util.Stack;

/**
 * Created by terrarum on 28/04/14.
 */
public class GameStateManager {

    private GameScreen game;
    private Stack<GameState> gameStates;

    private static final int PLAY = 3425;

    public GameStateManager(GameScreen game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public GameScreen game() { return game; }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    private GameState getState(int state) {
        if(state == PLAY) return new Play(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }
}
