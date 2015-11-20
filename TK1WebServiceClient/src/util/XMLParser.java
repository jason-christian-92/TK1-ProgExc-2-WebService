package util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import objects.ItemObject;

public class XMLParser {

	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	private static NodeList xmlToNodeList(String xml) throws 
				ParserConfigurationException, SAXException, IOException{
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		return doc.getDocumentElement().getChildNodes();
	}
	
	public static ArrayList<ItemObject> parseToListItemObject(String xml) throws ParserConfigurationException, 
				SAXException, IOException{
		NodeList nodeList = xmlToNodeList(xml);
		ArrayList<ItemObject> items = new ArrayList<ItemObject>();
		for (int i = 0 ; i < nodeList.getLength() ; i++){
			NodeList nodeChild = nodeList.item(i).getChildNodes();
			items.add(nodeChildToItemObject(nodeChild));
		}
		return items;
	}
	
	public static ItemObject parseToItemObject(String xml) throws ParserConfigurationException, 
				SAXException, IOException{
		NodeList nodeList = xmlToNodeList(xml);
		NodeList nodeChild = nodeList.item(0).getChildNodes();
		return nodeChildToItemObject(nodeChild);
	}
	
	private static ItemObject nodeChildToItemObject(NodeList node){
		ItemObject obj = new ItemObject();
		for (int i = 0 ; i < node.getLength() ; i++){
			Node nd = node.item(i);
			String content = nd.getLastChild().getTextContent().trim();
			
			switch(nd.getNodeName()){
				case "id":
					obj.setID(Integer.parseInt(content));
					break;
				case "name":
					obj.setName(content);
					break;
				case "amount":
					obj.setAmount(Integer.parseInt(content));
					break;
				case "price":
					obj.setPrice(Double.parseDouble(content));
					break;
			}
		}
		return obj;
	}
}
