package io;

import bayesian_network.BayesianNetwork;
import bayesian_network.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by chaoli on 10/22/16.
 */
public class HuginNetFormatTest {

    BayesianNetwork bn_;
    String file_name_;

    @Before
    public void setUp() throws Exception {
        bn_ = new BayesianNetwork();
    }

    @After
    public void tearDown() throws Exception {
        bn_ = null;
        file_name_ = null;
    }

    @Test
    public void read() throws Exception {
        file_name_ = "bnr/bnlearn/asia.net";
        bn_ = HuginNetFormat.read(file_name_);

        /// parse nodes
        assertEquals(8, bn_.getNumberOfNodes());
        int broncIndex = bn_.getNodeIndex("bronc");
        assertEquals(4, broncIndex);
        Node broncNode = bn_.getNodeByIndex(4);
        assertEquals("yes", broncNode.getStateLabel(0));
        assertEquals(1, broncNode.getStateIndex("no"));
        assertEquals(2, broncNode.getCard());

        /// parse CPTs
        //  potential ( asia ){data = ( 0.01 0.99 );}
        assertEquals(0, bn_.getNodeByName("asia").getParents().size());
        assertEquals(2, bn_.getNodeByName("asia").getTable().getSize());
        assertEquals(0.01, bn_.getNodeByName("asia").getTable().getDataItem(0), 0.01);
        assertEquals(0.99, bn_.getNodeByName("asia").getTable().getDataItem(1), 0.01);

    //  potential ( either | lung tub ){data = (((1.0 0.0)(1.0 0.0))((1.0 0.0)(0.0 1.0))) ;}
        assertEquals(2, bn_.getNodeByName("either").getParents().size());
        assertEquals(8, bn_.getNodeByName("either").getTable().getSize());
        assertEquals(1.0, bn_.getNodeByName("either").getTable().getDataItem(0), 0.01);
        assertEquals(0.0, bn_.getNodeByName("either").getTable().getDataItem(1), 0.01);
        assertEquals(1.0, bn_.getNodeByName("either").getTable().getDataItem(2), 0.01);
        assertEquals(0.0, bn_.getNodeByName("either").getTable().getDataItem(3), 0.01);
        assertEquals(1.0, bn_.getNodeByName("either").getTable().getDataItem(4), 0.01);
        assertEquals(0.0, bn_.getNodeByName("either").getTable().getDataItem(5), 0.01);
        assertEquals(0.0, bn_.getNodeByName("either").getTable().getDataItem(6), 0.01);
        assertEquals(1.0, bn_.getNodeByName("either").getTable().getDataItem(7), 0.01);
    }

    @Test
    public void saveAsNet() throws Exception {
    }
}