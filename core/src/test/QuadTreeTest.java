package test;


import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.utils.QuadTree.QuadTreeV1.QuadTree;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuadTreeTest {

    private Rectangle r1,r2,r3,r4,r5,r6;
    private QuadTree<Rectangle> quadTree;

    @Before
    public void sepup(){
        quadTree = new QuadTree<>(new Rectangle(0, 0, 10, 10), 0);
        QuadTree.maxItemByNode = 1;
        QuadTree.maxLevel = 2;

        r1 = new Rectangle(1, 1, 1, 1);
        r2 = new Rectangle(2, 2, 1, 1);
        r3 = new Rectangle(4, 4, 1, 1);
        r4 = new Rectangle(6, 6, 1, 1);
        r5 = new Rectangle(4, 4, 2, 2);
        r6 = new Rectangle(0.5f, 6.5f, 0.5f, 0.5f);
    }

    @Test
    public void testInsertElements1() {

        quadTree.insert(r1, r1);
        quadTree.insert(r2, r2);
        quadTree.insert(r3, r3);
        quadTree.insert(r4, r4);
        quadTree.insert(r5, r5);
        quadTree.insert(r6, r6);

        ArrayList<Rectangle> list = new ArrayList<>();
        quadTree.getElements(list, new Rectangle(2, 2, 1, 1));

        ArrayList<Rectangle> expected = new ArrayList<>();
        expected.add(r1);
        expected.add(r3);
        expected.add(r5);
        expected.add(r2);

        assertEquals(expected, list);
    }

    @Test
    public void testInsertElements2() {

        quadTree.insert(r1, r1);
        quadTree.insert(r2, r2);
        quadTree.insert(r3, r3);
        quadTree.insert(r4, r4);
        quadTree.insert(r5, r5);

        ArrayList<Rectangle> zoneList = new ArrayList<>();
        quadTree.getAllZones(zoneList);

        assertEquals(zoneList.size(), 5);
    }

    @Test
    public void testIntersectElementsAreInserted() {

        quadTree.insert(r1, r1);
        quadTree.insert(r2, r2);

        ArrayList<Rectangle> list = new ArrayList<>();
        quadTree.getElements(list, new Rectangle(2, 2, 1, 1));

        assertTrue(list.size() == 2);

        list = new ArrayList<>();
        quadTree.getElements(list, new Rectangle(2, 2, 1, 1));
    }

}