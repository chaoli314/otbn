package io;

import bayesian_network.BayesianNetwork;
import bayesian_network.Node;
import inference.CPT;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chaoli on 10/22/16.
 */
public class HuginNetFormat {
    public static BayesianNetwork read(String netFileName) {
        BayesianNetwork bn = new BayesianNetwork();

        // 1. Read file
        StringBuilder hugin_string = new StringBuilder();
        try {
            List<String> stringList = Files.readAllLines(Paths.get(netFileName), StandardCharsets.US_ASCII);
            stringList.forEach((str) -> hugin_string.append(str));
        } catch (IOException e) {
            System.err.println("IOException in HuginNetFormat.read!");
        }

        // 2. Parse Nodes
        // ############################## node asia                {   states    =      ( "yes" "no" );}
        // p_node: ###################### node (group_1)           {   states    =      ( group_2    );}
        Pattern p_node = Pattern.compile("node\\s+([^\\s{]+)[^{]*\\{.*?states\\s*=\\s*\\((.+?)\\);.*?}");
        // ############################## "yes" "no"
        // p_state: ##################### "(group_1)"
        Pattern p_state = Pattern.compile("\"(.+?)\"");
        Matcher m_node = p_node.matcher(hugin_string);
        // ~ Find all nodes ~
        for (int node_ID = 0; m_node.find(); ++node_ID) {
            String nodeName = m_node.group(1);
            Node this_node = bn.addNode(nodeName);
            String stateLabels = m_node.group(2)/* stateLabels of the node */;
            Matcher m_state = p_state.matcher(stateLabels);
            while (m_state.find()) {
                this_node.addState(m_state.group(1));
            }
        }

        // 3. Parse potentials
        //  ################################    potential ( smoke ) {data = ( 0.5 0.5 );}
        //  ################################    potential ( smoke | ) {data = ( 0.5 0.5 );}
        //  ################################    potential ( either | lung tub ) {data = (((1.0 0.0)(1.0 0.0))((1.0 0.0)(0.0 1.0))) ;}
        Pattern p_potential = Pattern.compile("potential\\s*\\(\\s*([^\\|\\)\\s]+)(\\s*[|]*\\s*([^)]*))?\\s*\\)\\s*" +
                "\\{.*?data\\s*=\\s*(\\([^\\}]+\\))[^\\}]*\\}");
        /// potential_(childGROUP1 | parentsGROUP3){*data = (probabilitiesGROUP4)*}

        Pattern p_nodeName = Pattern.compile("\\S+");
        Pattern p_probability = Pattern.compile("(?<=\\()[^\\(\\)]+(?=\\))");

        Matcher m_potential = p_potential.matcher(hugin_string);

        while (m_potential.find()) {

            String childNodeName = m_potential.group(1);
            Node child = bn.getNodeByName(childNodeName);

            Stack<String> parentNodeNames = new Stack<>();

            String parentsString = null;

            if (null != (parentsString = m_potential.group(3))) {
                Matcher m_parent = p_nodeName.matcher(parentsString);
                while (m_parent.find()) {
                    parentNodeNames.add(m_parent.group());
                }
            }

            for (String nodeName : parentNodeNames) {
                Node parentNode = bn.getNodeByName(nodeName);
                child.addParent(parentNode);
            }

            CPT cpt = child.generateTable();

            String data = m_potential.group(4);
            Matcher m_probability = p_probability.matcher(data);
            int index = 0;
            while (m_probability.find()) {
                Scanner in = new Scanner(m_probability.group());
                while (in.hasNextDouble()) {
                    double dataItem = in.nextDouble();
                    cpt.set(index, dataItem);
                    index++;
                }
                in.close();
            }
        }


        return bn;
    }

    public static void saveAsNet(BayesianNetwork bn, String fileName) {
        // TODO
    }
}