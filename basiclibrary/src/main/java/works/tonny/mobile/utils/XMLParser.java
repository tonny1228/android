package works.tonny.mobile.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import works.tonny.mobile.widget.IDLinkedHashMap;

/**
 * Created by tonny on 2015/7/6.
 */
public class XMLParser {
    private Map<String, Object> datas = new IDLinkedHashMap();
    private Node node;


    /**
     * 解析
     *
     * @param xml
     * @throws Exception
     */
    public void parse(String xml) throws Exception {
        try {
            Log.debug(xml.substring(xml.length()/10*9));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            Element root = document.getDocumentElement();
            node = new Node();
            node.name = root.getTagName();
            node.depth = 0;
            listChildren(root, node);
            rebuild(node);
            datas.put(node.getPath(), node.mapChild);
            mappping(node);
            Set<String> strings = datas.keySet();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 将所有数据添加到map中
     *
     * @param node
     */
    private void mappping(Node node) {
        for (String key : node.mapChild.keySet()) {
            Object o = node.mapChild.get(key);
            if (o instanceof Node) {
                Node o1 = (Node) o;
                putInDatas(o1);
                mappping(o1);
            } else if (o instanceof List) {
                List l = (List) o;
                for (int i = 0; i < l.size(); i++) {

                    Object o1 = l.get(i);
                    if (o1 instanceof Node) {
                        String substring = ((Node) o1).getPath().substring(0, ((Node) o1).getPath().lastIndexOf("["));
                        if (i == 0) {
                            List list = new ArrayList();
                            list.add(((Node) o1).mapChild);
                            datas.put(substring, list);
                        } else {
                            List list = (List) datas.get(substring);
                            list.add(((Node) o1).mapChild);
                        }

                        putInDatas((Node) o1);

                    } else {
                        datas.put(node.getPath(), o1);
                    }
                }
            } else if (o instanceof IDLinkedHashMap) {
//                datas.put(node.getPath() + "." + key, o);
                IDLinkedHashMap map = (IDLinkedHashMap) o;
                Set<String> strings = ((IDLinkedHashMap) o).keySet();
                for (String string : strings) {
                    datas.put(node.getPath() + "." + key + "." + string, map.get(string));
                }
            } else {
//                datas.put(node.getPath() + "." + key, o);
                datas.put(node.getPath() + "." + key, o);
            }
        }

    }


    /**
     * 添加node到data
     *
     * @param o1
     */
    void putInDatas(Node o1) {
        for (String aname : o1.attributes.keySet()) {
            datas.put(o1.getPath() + "[" + aname + "]", o1.attributes.get(aname));
        }
        datas.put(o1.getPath(), o1.mapChild);
    }


    /**
     * 重构数据
     *
     * @param n
     */
    private void rebuild(Node n) {
        if (n.children != null) {
            for (int i = 0; i < n.children.size(); i++) {
                rebuild(n.children.get(i));
            }
        }
        n.rebuild();

    }


    /**
     * @param element
     * @param parent
     */
    private void listChildren(Element element, Node parent) {
        NodeList childNodes = element.getChildNodes();
        boolean hasChildren = false;
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (!(childNodes.item(i) instanceof Element)) {
                continue;
            }
            hasChildren = true;
            Element item = (Element) childNodes.item(i);
            Node node = new Node();
            node.name = item.getTagName();
            node.depth = parent.depth + 1;
            node.setParent(parent);
            NamedNodeMap attributes = item.getAttributes();
            Log.info(node.name);
            for (int j = 0; j < attributes.getLength(); j++) {
                Log.info("attr:" + node.name + " " + attributes.item(j).getNodeName());
                node.attributes.put(attributes.item(j).getNodeName(), attributes.item(j).getTextContent().trim());
            }
            listChildren(item, node);
        }
        if (!hasChildren) {
            parent.text = element.getTextContent().trim();
        }
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public Node getNode() {
        return node;
    }

    public class Node implements Serializable {
        Node parent;
        List<Node> children;
        String name;
        String text;
        int depth;
        int index;
        Node child;
        IDLinkedHashMap mapChild = new IDLinkedHashMap();
        Map<String, String> attributes = new HashMap<String, String>();

        public void rebuild() {

            if (!attributes.isEmpty()) {
                Set<String> keySet = attributes.keySet();
                for (String s : keySet) {
                    mapChild.put(s, attributes.get(s));
                }
                Log.info("xxx:" + name + " " + mapChild);
            }

            if (!mapChild.isEmpty() && parent != null) {
                index = parent.put(name, this.mapChild);
                Log.info("child:" + getPath() + " " + this.mapChild);
                datas.put(getPath(), this.mapChild);
                this.children = null;
            }
            if (children == null && mapChild.isEmpty()) {
                index = parent.put(name, text);
//                Log.info("put in text " + name);
                datas.put(getPath(), text);
                return;
            }

        }

        public int put(String name, Object o) {
            Log.info("rrrr " + this.getPath() + " " + mapChild + " " + name + " " + o);
            if (!mapChild.containsKey(name)) {
                mapChild.put(name, o);
                if (name.equals("id"))
                    mapChild.setIdField("id");
                return -1;
            } else {
                Object e = mapChild.get(name);
                if (e instanceof List) {
                    ((List) e).add(o);
                    return ((List) e).size() - 1;
                } else {
                    ArrayList list = new ArrayList();
                    list.add(e);
                    if (e instanceof Node) {
                        ((Node) e).index = 0;
                    }
                    list.add(o);
                    mapChild.put(name, list);
                    return list.size() - 1;
                }
            }

        }

        public void setParent(Node parent) {
            this.parent = parent;
            if (parent.children == null) {
                parent.children = new ArrayList<Node>();
            }
            this.index = parent.children.size();
            parent.children.add(this);
        }

        String getPath() {
            if (parent == null)
                return name;
            if (index > -1)
                return parent.getPath() + "." + name + "[" + index + "]";
            else
                return parent.getPath() + "." + name;
        }

        @Override
        public String toString() {
            return getPath() + "=" + (mapChild != null ? "(m)" + mapChild : (children != null ? "(c)" + children : text));
        }
    }


    public static void main(String[] args) {

    }
}
