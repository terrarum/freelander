package com.closedorbit.freelander.gameStates;

import com.closedorbit.freelander.GameStateManager;

public class Play extends GameState {

    public Play(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(float dt) {
        System.out.println("update!");
    }

    @Override
    public void render() {
        System.out.println("render!");
    }

    @Override
    public void dispose() {

    }
}
