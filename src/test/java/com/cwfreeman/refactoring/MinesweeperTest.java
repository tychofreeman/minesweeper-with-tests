package com.cwfreeman.refactoring;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MinesweeperTest
{
    Minesweeper minesweeper;
    private int[] before;

    @Before
    public void setup() {
        minesweeper = new Minesweeper() {
            @Override
            protected void paintFace(Graphics g) {}
        };
        AppletStub stub = new AppletStub() {


            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public URL getDocumentBase() {
                return null;
            }

            @Override
            public URL getCodeBase() {
                return null;
            }

            @Override
            public String getParameter(String name) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("COLUMNS", "9");
                params.put("ROWS", "9");
                params.put("PIXELS", "9");
                params.put("MINES", "9");

                return params.get(name);
            }

            @Override
            public AppletContext getAppletContext() {
                return null;
            }

            @Override
            public void appletResize(int width, int height) {

            }
        };
        minesweeper.setStub(stub);
        minesweeper.start();
        minesweeper.run();
        minesweeper.init();

        for (int i = 0; i < 81; i++) {
            minesweeper.adjacent[i] = 0;
        }
        minesweeper.adjacent[0] = Minesweeper.mine;
        minesweeper.adjacent[1] = 1;
        minesweeper.adjacent[10] = 1;
        minesweeper.adjacent[11] = 1;
        before = minesweeper.exposed.clone();

    }

    @After
    public void teardown()
    {
        minesweeper.stop();
    }

    @Test
    public void noExposedMinesOnInit() {
        Assert.assertNotNull(minesweeper.exposed);
        Assert.assertEquals(true, Arrays.stream(minesweeper.exposed).noneMatch((x) -> x != -4));
    }

    @Test
    public void clicksOnMineEndsGame() {
        minesweeper.adjacent[0] = Minesweeper.mine;
        minesweeper.expose(0, 0);
        Assert.assertEquals(Minesweeper.sad, minesweeper.sadness);
    }

    @Test
    public void clicksOnSquareAdjacentToMine() {
        minesweeper.expose(8,8);

        Assert.assertEquals(Minesweeper.bored, minesweeper.sadness);
        for (int i = 0; i < before.length; i++) {
            if (i > 1 && i != 9 && i != 10) {
                Assert.assertTrue(String.format("At index %d - expected %d and got %d", i, before[i], minesweeper.exposed[i]),
                        minesweeper.exposed[i] >= Minesweeper.listEnd);
            }
        }
        Assert.assertEquals(0, minesweeper.exposed[0]);
        Assert.assertEquals(0, minesweeper.exposed[1]);
        Assert.assertEquals(0, minesweeper.exposed[9]);
        Assert.assertEquals(0, minesweeper.exposed[10]);

    }

    @Test
    public void clicksOnEmptySquare() {

    }


}
