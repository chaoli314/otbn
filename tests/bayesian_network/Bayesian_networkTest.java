package bayesian_network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/20/16.
 */
public class Bayesian_networkTest {
    private BayesianNetwork bn_;
    @Before
    public void setUp() throws Exception {
        bn_ = new BayesianNetwork();
    }

    @After
    public void tearDown() throws Exception {
        bn_ = null;
    }

    @Test
    public void getNodeByName() throws Exception {
        bn_.addNode("nodeA");
        bn_.addNode("nodeH");
        bn_.addNode("nodeO");
        bn_.addNode("nodeU");

        assertEquals(0, bn_.getNodeByName("nodeA").getIndex());
        assertEquals(1, bn_.getNodeByName("nodeH").getIndex());
        assertEquals(2, bn_.getNodeByName("nodeO").getIndex());
        assertEquals(3, bn_.getNodeByName("nodeU").getIndex());
    }

    @Test
    public void getNodeByIndex() throws Exception {
        bn_.addNode("nodeA");
        bn_.addNode("nodeH");
        bn_.addNode("nodeC");
        bn_.addNode("nodeU");

        assertEquals("nodeA", bn_.getNodeByIndex(0).getName());
        assertEquals("nodeH", bn_.getNodeByIndex(1).getName());
        assertEquals("nodeC", bn_.getNodeByIndex(2).getName());
        assertEquals("nodeU", bn_.getNodeByIndex(3).getName());
    }

    @Test
    public void getNodeIndex() throws Exception {
        bn_.addNode("nodeA");
        bn_.addNode("nodeH");
        bn_.addNode("nodeC");
        bn_.addNode("nodeU");

        assertEquals(0, bn_.getNodeIndex("nodeA"));
        assertEquals(1, bn_.getNodeIndex("nodeH"));
        assertEquals(2, bn_.getNodeIndex("nodeC"));
        assertEquals(3, bn_.getNodeIndex("nodeU"));
    }

    @Test
    public void getNodeName() throws Exception {
        bn_.addNode("nodeA");
        bn_.addNode("nodeH");
        bn_.addNode("nodeC");
        bn_.addNode("nodeU");

        assertEquals("nodeA", bn_.getNodeName(0));
        assertEquals("nodeH", bn_.getNodeName(1));
        assertEquals("nodeC", bn_.getNodeName(2));
        assertEquals("nodeU", bn_.getNodeName(3));
    }

    @Test
    public void addNode() throws Exception {
        bn_.addNode("nodeA");
        bn_.addNode("nodeH");
        bn_.addNode("nodeO");
        bn_.addNode("nodeU");

        assertEquals("nodeA", bn_.getNodeByIndex(0).getName());
        assertEquals("nodeH", bn_.getNodeByIndex(1).getName());
        assertEquals("nodeO", bn_.getNodeByIndex(2).getName());
        assertEquals("nodeU", bn_.getNodeByIndex(3).getName());
    }

    @Test
    public void getNumberOfNodes() throws Exception {
        assertEquals(0, bn_.getNumberOfNodes());
        bn_.addNode("nodeA");
        assertEquals(1, bn_.getNumberOfNodes());
        bn_.addNode("nodeH");
        assertEquals(2, bn_.getNumberOfNodes());
        bn_.addNode("nodeO");
        assertEquals(3, bn_.getNumberOfNodes());
        bn_.addNode("nodeU");
        assertEquals(4, bn_.getNumberOfNodes());
    }
}