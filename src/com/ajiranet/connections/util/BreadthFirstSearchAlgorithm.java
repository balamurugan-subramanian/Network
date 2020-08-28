package com.ajiranet.connections.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.Node;

import java.util.*;

public class BreadthFirstSearchAlgorithm {

    private static final Logger LOGGER = LoggerFactory.getLogger(BreadthFirstSearchAlgorithm.class);

    public static <T> Optional<Node<T>> search(T value, Node<T> start) {
        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(start);

        Node<T> currentNode;
        Set<Node<T>> alreadyVisited = new HashSet<>();

        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            LOGGER.info("Visited node with value: {}", currentNode.getValue());

            if (currentNode.getValue().equals(value)) {
                return Optional.of(currentNode);
            } else {
                alreadyVisited.add(currentNode);
                queue.addAll(currentNode.getNeighbors());
                queue.removeAll(alreadyVisited);
            }
        }

        return Optional.empty();
    }
    
//    public  void dfsUsingStack(Node node)
//	{
//		Stack<Node> stack=new  Stack<Node>();
//		stack.add(node);
//		while (!stack.isEmpty())
//		{
//			Node element=stack.pop();
//			if(!element.hasVisited())
//			{
//				System.out.print(element.data + " ");
//				element.setVisited(true);
//			}
//			
//			Set<Node<Device>> neighbours=element.getNeighbors();
//			List<String> name = new ArrayList<String>();
//			for (int i = 0; i < neighbours.size(); i++) {
//				Node n=neighbours.get(i);
//				if(n!=null && !n.visited)
//				{
//					stack.add(n);
//				}
//			}
//		}
//	}

}
