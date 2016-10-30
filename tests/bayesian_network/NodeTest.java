package bayesian_network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/20/16.
 */
public class NodeTest {


    private Bayesian_network bn_;

    @Before
    public void setUp() throws Exception {
        bn_ = new Bayesian_network();

        bn_.addNode("nodeA");
        bn_.addNode("nodeH");
        bn_.addNode("nodeO");
        bn_.addNode("nodeU");
    }

    @After
    public void tearDown() throws Exception {
        bn_ = null;
    }

    @Test
    public void getName() throws Exception {
        assertEquals("nodeA", bn_.getNodeByIndex(0).getName());
        assertEquals("nodeH", bn_.getNodeByIndex(1).getName());
        assertEquals("nodeO", bn_.getNodeByIndex(2).getName());
        assertEquals("nodeU", bn_.getNodeByIndex(3).getName());
    }

    @Test
    public void getIndex() throws Exception {
        assertEquals(0, bn_.getNodeByName("nodeA").getIndex());
        assertEquals(1, bn_.getNodeByName("nodeH").getIndex());
        assertEquals(2, bn_.getNodeByName("nodeO").getIndex());
        assertEquals(3, bn_.getNodeByName("nodeU").getIndex());
    }


    @Test
    public void getNumberOfStates() throws Exception {
        Node nodeA = bn_.getNodeByName("nodeA");
        assertEquals(0, nodeA.getNumberOfStates());

        nodeA.addState("high");
        assertEquals(1, nodeA.getNumberOfStates());
        assertEquals("high", nodeA.getStateLabel(0));
        assertEquals(0, nodeA.getStateIndex("high"));
        nodeA.addState("middle");
        nodeA.addState("low");

        assertEquals(3, nodeA.getNumberOfStates());
        assertEquals("middle", nodeA.getStateLabel(1));
        assertEquals(1, nodeA.getStateIndex("middle"));
        assertEquals("low", nodeA.getStateLabel(2));
        assertEquals(2, nodeA.getStateIndex("low"));
    }

    @Test
    public void getStateIndex() throws Exception {
        Node nodeA = bn_.getNodeByName("nodeA");
        assertEquals(0, nodeA.getNumberOfStates());

        nodeA.addState("high");
        nodeA.addState("middle");
        nodeA.addState("low");
        assertEquals(3, nodeA.getNumberOfStates());

        assertEquals(0, nodeA.getStateIndex("high"));
        assertEquals(1, nodeA.getStateIndex("middle"));
        assertEquals(2, nodeA.getStateIndex("low"));
    }

    @Test
    public void getStateLabel() throws Exception {
        Node nodeA = bn_.getNodeByName("nodeA");
        assertEquals(0, nodeA.getNumberOfStates());

        nodeA.addState("high");
        nodeA.addState("middle");
        nodeA.addState("low");
        assertEquals(3, nodeA.getNumberOfStates());
        assertEquals("high", nodeA.getStateLabel(0));
        assertEquals("middle", nodeA.getStateLabel(1));
        assertEquals("low", nodeA.getStateLabel(2));
    }

    @Test
    public void addState() throws Exception {
        Node nodeA = bn_.getNodeByIndex(0);
        assertEquals(0, nodeA.getNumberOfStates());

        nodeA.addState("high");
        assertEquals(1, nodeA.getNumberOfStates());
        assertEquals(0, nodeA.getStateIndex("high"));
        nodeA.addState("middle");
        nodeA.addState("low");

        assertEquals(3, nodeA.getNumberOfStates());
        assertEquals(1, nodeA.getStateIndex("middle"));
        assertEquals(2, nodeA.getStateIndex("low"));
    }
}